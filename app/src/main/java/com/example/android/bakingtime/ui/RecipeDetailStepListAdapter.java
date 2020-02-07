package com.example.android.bakingtime.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.ui.RecipeDetailFragment.OnStepListFragmentInteractionListener;
import com.example.android.bakingtime.model.Step;

import java.util.List;

public class RecipeDetailStepListAdapter extends RecyclerView.Adapter<RecipeDetailStepListAdapter.ViewHolder> {

    private final OnStepListFragmentInteractionListener mListener;
    private List<Step> mSteps;

    public RecipeDetailStepListAdapter(OnStepListFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_step_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mStep = mSteps.get(position);
        holder.mStepTitleView.setText(mSteps.get(position).getShortDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mListener) {
                    mListener.onStepListFragmentInteraction(holder.mStep);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(null == mSteps) return 0;
        return mSteps.size();
    }

    public void inputData(List<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mStepTitleView;
        public Step mStep;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mStepTitleView = view.findViewById(R.id.tv_step_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mStepTitleView.getText() + "'";
        }
    }


}
