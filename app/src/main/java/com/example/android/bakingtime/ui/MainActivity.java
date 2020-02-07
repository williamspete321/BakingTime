package com.example.android.bakingtime.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.Recipe;

public class MainActivity extends AppCompatActivity
        implements MainListFragment.OnRecipeListFragmentInteractionListener
{
    private boolean isTablet;
    private int tabletColumnCount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isTablet = getResources().getBoolean(R.bool.isTablet);

        if(savedInstanceState == null) {

            MainListFragment mainListFragment = new MainListFragment();
            if(isTablet) mainListFragment.setColumnCount(tabletColumnCount);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_main_list_container, mainListFragment)
                    .commit();

        }

    }

    @Override
    public void onRecipeListFragmentInteraction(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailFragment.ARG_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }

}
