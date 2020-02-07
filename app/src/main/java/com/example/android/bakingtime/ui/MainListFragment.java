package com.example.android.bakingtime.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.db.remote.RecipeService;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

public class MainListFragment extends Fragment {
    public static final String TAG = MainListFragment.class.getSimpleName();

    private int mColumnCount = 1;
    private OnRecipeListFragmentInteractionListener mListener;
    private MainListAdapter mMainListAdapter;
    private RecyclerView mRecyclerView;
    private RecipeViewModel mRecipeViewModel;

    public MainListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mMainListAdapter = new MainListAdapter(mListener);

            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mRecyclerView.setAdapter(mMainListAdapter);
        }

        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        mRecipeViewModel.getAllRecipes().observe(getViewLifecycleOwner(), new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                mMainListAdapter.inputData(recipes);
                Log.d(TAG, "onChanged has been called from RecipeViewModel");
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeListFragmentInteractionListener) {
            mListener = (OnRecipeListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnRecipeListFragmentInteractionListener {
        void onRecipeListFragmentInteraction(Recipe recipe);
    }

    public void setColumnCount(int columnCount) {
        mColumnCount = columnCount;
    }
}
