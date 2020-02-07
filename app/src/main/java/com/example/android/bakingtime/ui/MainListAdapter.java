package com.example.android.bakingtime.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.ui.MainListFragment.OnRecipeListFragmentInteractionListener;
import com.example.android.bakingtime.model.Recipe;

import java.util.List;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {

    private final OnRecipeListFragmentInteractionListener mListener;
    private List<Recipe> mRecipes;

    public MainListAdapter(OnRecipeListFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mRecipe = mRecipes.get(position);
        holder.mRecipeName.setText(mRecipes.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onRecipeListFragmentInteraction(holder.mRecipe);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(null == mRecipes) return 0;
        return mRecipes.size();
    }

    public void inputData(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mRecipeName;
        public Recipe mRecipe;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mRecipeName = view.findViewById(R.id.recipe_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mRecipeName.getText() + "'";
        }
    }
}
