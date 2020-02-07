package com.example.android.bakingtime.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.android.bakingtime.R;

public class StepDetailActivity extends AppCompatActivity
        implements StepDetailFragment.OnStepDetailFragmentInteractionListener{

    private int mRecipeID = -1;
    private int mStepID = -1;
    //make another network call
    //store list of steps for that recipe

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (getIntent().hasExtra(StepDetailFragment.ARG_STEP_ID)) {
            mStepID = getIntent().getIntExtra(StepDetailFragment.ARG_STEP_ID, -1);
        }

        if (getIntent().hasExtra(RecipeDetailFragment.ARG_RECIPE_ID)) {
            mRecipeID = getIntent().getIntExtra(RecipeDetailFragment.ARG_RECIPE_ID, -1);
        }

        if(savedInstanceState == null) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepID(mStepID);
            stepDetailFragment.setRecipeID(mRecipeID);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_step_detail_container, stepDetailFragment)
                    .commit();
        }

    }

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
    public void onBackPressed() {
        super.onBackPressed();

    }
}
