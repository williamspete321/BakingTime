package com.example.android.bakingtime.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.RecipeWidgetProvider;
import com.example.android.bakingtime.model.Step;

public class RecipeDetailActivity extends AppCompatActivity implements
        RecipeDetailFragment.OnStepListFragmentInteractionListener,
StepDetailFragment.OnStepDetailFragmentInteractionListener {

    private boolean isTablet;
    private int mRecipeID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        isTablet = getResources().getBoolean(R.bool.isTablet);

        if(getIntent().hasExtra(RecipeDetailFragment.ARG_RECIPE_ID)) {
            mRecipeID = getIntent().getIntExtra(RecipeDetailFragment.ARG_RECIPE_ID, -1);
        }

        if (savedInstanceState == null) {

            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setRecipeID(mRecipeID);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_recipe_detail_container, recipeDetailFragment)
                    .commit();

            // Open the StepDetailFragment beside the Steps list if app is running on a tablet
            if (isTablet) {
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.setStepID(0);
                stepDetailFragment.setRecipeID(mRecipeID);
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_step_detail_container, stepDetailFragment)
                        .commit();
            }
        }
    }

    @Override
    public void onStepListFragmentInteraction(Step step) {

        if(isTablet) {

            replaceStepDetailFragment(step.getId(), mRecipeID);

        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);

            int stepID = step.getId();
            int recipeID = mRecipeID;

            intent.putExtra(StepDetailFragment.ARG_STEP_ID, stepID);
            intent.putExtra(RecipeDetailFragment.ARG_RECIPE_ID, recipeID);

            startActivity(intent);
        }

    }

    // Used if the application is being run on a tablet
    @Override
    public void onStepDetailFragmentInteractionPreviousClick(int stepID, int recipeID) {
        replaceStepDetailFragment(stepID-1, recipeID);
    }

    @Override
    public void onStepDetailFragmentInteractionNextClick(int stepID, int recipeID) {
        replaceStepDetailFragment(stepID+1, recipeID);
    }

    private void replaceStepDetailFragment(int stepID, int recipeID) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setStepID(stepID);
        stepDetailFragment.setRecipeID(recipeID);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_step_detail_container, stepDetailFragment)
                .commit();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences
                = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.last_viewed_recipe), mRecipeID);
        editor.apply();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds
                = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateRecipeWidget(this, appWidgetManager, appWidgetIds);

    }
}
