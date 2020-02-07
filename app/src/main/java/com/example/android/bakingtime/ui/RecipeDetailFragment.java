package com.example.android.bakingtime.ui;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.Ingredient;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.model.Step;
import com.example.android.bakingtime.db.remote.NetworkUtils;
import com.example.android.bakingtime.db.remote.RecipeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment {

    public static final String TAG = RecipeDetailFragment.class.getSimpleName();

    public static final String ARG_RECIPE_ID = "recipe-id";
    private int mRecipeID = -1;
    private OnStepListFragmentInteractionListener mListener;
    private RecipeDetailStepListAdapter mRecipeDetailStepListAdapter;
    private RecyclerView mRecyclerView;
    private TextView mIngredientTitleTextView;
    private TextView mIngredientListTextView;
    private ArrayList<Step> mSteps;
    private RecipeViewModel mRecipeViewModel;


    public RecipeDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView has been called");

        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_RECIPE_ID)) {
            mRecipeID = savedInstanceState.getInt(ARG_RECIPE_ID);
        }
        Log.d(TAG, "Recipe ID = " + mRecipeID);

        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        mIngredientTitleTextView = view.findViewById(R.id.tv_ingredient_title);
        mIngredientListTextView = view.findViewById(R.id.tv_ingredient_list);

        mRecyclerView = view.findViewById(R.id.step_list_recycler_view);
        mRecipeDetailStepListAdapter = new RecipeDetailStepListAdapter(mListener);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRecipeDetailStepListAdapter);

        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        mRecipeViewModel.getRecipeByID(mRecipeID).observe(getViewLifecycleOwner(), new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                Log.d(TAG, "onChanged has been called from RecipeViewModel");
                mIngredientListTextView.setText(buildIngredientList(recipe));
                mRecipeDetailStepListAdapter.inputData(recipe.getSteps());
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnStepListFragmentInteractionListener) {
            mListener = (OnStepListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStepListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnStepListFragmentInteractionListener {
        void onStepListFragmentInteraction(Step step);
    }

    public void setRecipeID(int recipeID) {
        mRecipeID = recipeID;
    }

    private String buildIngredientList(Recipe recipe) {
        ArrayList<Ingredient> ingredients = recipe.getIngredients();

        if(ingredients != null) {
            StringBuilder allIngredients = new StringBuilder();
            for(Ingredient ingredient : ingredients) {
                double quantity = ingredient.getQuantity();
                Ingredient.Measurement measurement = ingredient.getMeasurementType();
                String content = ingredient.getContent();

                allIngredients.append("" + quantity + "\t" + measurement.toString() + "\t\t" + content + "\n");
            }
            return allIngredients.toString();
        }
        return null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_RECIPE_ID, mRecipeID);
    }
}
