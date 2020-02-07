package com.example.android.bakingtime.ui;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.model.Step;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class StepDetailFragment extends Fragment {

    public static final String TAG = StepDetailActivity.class.getSimpleName();

    public static final String ARG_STEP_ID = "step-id";
    private int mRecipeID = -1;
    private int mStepID = -1;
    private ArrayList<Step> mSteps;
    private OnStepDetailFragmentInteractionListener mListener;
    private RecipeViewModel mRecipeViewModel;

    private SimpleExoPlayer mPlayer;
    private PlayerView mPlayerView;
    private boolean hasVideo = false;

    private TextView mNoVideoTextView;
    private TextView mStepTitleTextView;
    private TextView mStepContentTextView;
    private Button mPreviousButton;
    private Button mNextButton;

    public StepDetailFragment() {
    }

    public interface OnStepDetailFragmentInteractionListener {
        void onStepDetailFragmentInteractionPreviousClick(int stepID, int recipeID);
        void onStepDetailFragmentInteractionNextClick(int stepID, int recipeID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(savedInstanceState != null
                && savedInstanceState.containsKey(RecipeDetailFragment.ARG_RECIPE_ID)
                && savedInstanceState.containsKey(ARG_STEP_ID)) {

            mRecipeID = savedInstanceState.getInt(RecipeDetailFragment.ARG_RECIPE_ID);
            mStepID = savedInstanceState.getInt(ARG_STEP_ID);

        }

        View view = getLayoutInflater().inflate(R.layout.fragment_step_detail, container, false);

        mNoVideoTextView = view.findViewById(R.id.no_video_textView);
        mPlayerView = view.findViewById(R.id.playerView);
        mStepTitleTextView = view.findViewById(R.id.step_title_textView);
        mStepContentTextView = view.findViewById(R.id.step_description_textView);
        mPreviousButton = view.findViewById(R.id.previous_button);
        mNextButton = view.findViewById(R.id.next_button);

        setupViewModel();
        setOnClickForPreviousAndNextButtons();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof StepDetailFragment.OnStepDetailFragmentInteractionListener) {
            mListener = (StepDetailFragment.OnStepDetailFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStepDetailFragmentInteractionListener");
        }
    }

    public void setStepID(int stepID) {
        mStepID = stepID;
    }

    public void setRecipeID(int recipeID) {
        mRecipeID = recipeID;
    }

    private void insertTextAndVideo() {
        mStepTitleTextView.setText(mSteps.get(mStepID).getShortDescription());
        mStepContentTextView.setText(mSteps.get(mStepID).getDescription());
        initializePlayer(getVideoUri());
    }

    private Uri getVideoUri() {
        Uri mediaUri = null;
        if (mSteps != null) {
            mediaUri = Uri.parse(mSteps.get(mStepID).getVideoURL());
        }
        return mediaUri;
    }

    private void setupViewModel() {
        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        mRecipeViewModel.getRecipeByID(mRecipeID).observe(getViewLifecycleOwner(), new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                Log.d(TAG, "onChanged called from recipeViewModel");
                Log.d(TAG, "Current recipe ID = " + mRecipeID);
                mSteps = recipe.getSteps();
                insertTextAndVideo();
                displayButtonsIfNeeded();
            }
        });
    }

    private void setOnClickForPreviousAndNextButtons() {
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mListener) {
                    mListener.onStepDetailFragmentInteractionPreviousClick(mStepID, mRecipeID);
                }
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mListener) {
                    mListener.onStepDetailFragmentInteractionNextClick(mStepID, mRecipeID);
                }
            }
        });
    }

    private void displayButtonsIfNeeded() {
        if(mStepID > 0) mPreviousButton.setVisibility(View.VISIBLE);
        if(mStepID < mSteps.size()-1) mNextButton.setVisibility(View.VISIBLE);
    }

    private void initializePlayer(Uri mediaUri) {

        if(mediaUri != null && !mediaUri.toString().isEmpty()) {
            hasVideo = true;

            if(mPlayer == null) {
                mPlayer = new SimpleExoPlayer.Builder(getActivity()).build();
                mPlayerView.setPlayer(mPlayer);

                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                        Util.getUserAgent(getActivity(), "BakingTime"));

                MediaSource videoSource =
                        new ProgressiveMediaSource.Factory(dataSourceFactory)
                                .createMediaSource(mediaUri);

                mPlayer.prepare(videoSource);
            }

        } else {
            mPlayerView.setVisibility(View.INVISIBLE);
            mNoVideoTextView.setVisibility(View.VISIBLE);
        }
    }

    private void releasePlayer() {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(hasVideo) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RecipeDetailFragment.ARG_RECIPE_ID, mRecipeID);
        outState.putInt(ARG_STEP_ID, mStepID);
    }

}
