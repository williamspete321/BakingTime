package com.example.android.bakingtime.db;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.android.bakingtime.db.local.RecipeDao;
import com.example.android.bakingtime.db.local.RecipeDatabase;
import com.example.android.bakingtime.db.remote.NetworkUtils;
import com.example.android.bakingtime.db.remote.RecipeService;
import com.example.android.bakingtime.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepository {
    private static final String LOG_TAG = RecipeRepository.class.getSimpleName();

    private static RecipeRepository sInstance;
    private final RecipeDatabase mRecipeDatabase;
    private final RecipeService mRecipeService;
    private final RecipeDao mRecipeDao;
    private LiveData<List<Recipe>> mAllRecipes;
    private MediatorLiveData<List<Recipe>> mediatorLiveData;

    private RecipeRepository(Application application) {
        mRecipeDatabase = RecipeDatabase.getInstance(application);
        mRecipeService = NetworkUtils.getRecipeService();
        mRecipeDao = mRecipeDatabase.recipeDao();
        mAllRecipes = loadRecipes();
    }

    public static RecipeRepository getInstance(Application application) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            sInstance = new RecipeRepository(application);
            Log.d(LOG_TAG, "Made new repository");
        }
        return sInstance;
    }

    public LiveData<Recipe> getRecipeById(int id) {
        return mRecipeDao.getRecipeByIDLiveData(id);
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return mAllRecipes;
    }

    private LiveData<List<Recipe>> loadRecipes() {
        //If this is the first time the app is accessing data, a network call will need to be made
        //otherwise, access the data offline from Room database

        mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.addSource(mRecipeDao.getAllRecipes(), recipes -> {
            if(recipes == null || recipes.isEmpty()) {
                Log.d(LOG_TAG, "recipes == null");
                loadRecipesFromNetwork();
            } else {
                Log.d(LOG_TAG, "recipes is not null");
                mediatorLiveData.postValue(recipes);
            }
            mediatorLiveData.removeSource(mRecipeDao.getAllRecipes());
        });
        return mediatorLiveData;
    }

    private void loadRecipesFromNetwork() {

        Log.d(LOG_TAG, "Making network call");
        mRecipeService.getRecipelist().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.body() != null) {

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            for (Recipe recipe : response.body()) {
                                mRecipeDao.insert(recipe);
                            }
                            mediatorLiveData.postValue(response.body());
                            Log.d(LOG_TAG, "New data has been inserted");
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(LOG_TAG, "Could not successfully connect to network");
            }
        });
    }

}
