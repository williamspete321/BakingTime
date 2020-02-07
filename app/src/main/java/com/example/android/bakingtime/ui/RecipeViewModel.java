package com.example.android.bakingtime.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.bakingtime.db.RecipeRepository;
import com.example.android.bakingtime.model.Recipe;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {
    private RecipeRepository mRecipeRepository;
    private LiveData<List<Recipe>> mAllRecipes;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        mRecipeRepository = RecipeRepository.getInstance(application);
        mAllRecipes = mRecipeRepository.getAllRecipes();
    }

    public LiveData<Recipe> getRecipeByID(int id) {
        return mRecipeRepository.getRecipeById(id);
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return mRecipeRepository.getAllRecipes();
    }
}
