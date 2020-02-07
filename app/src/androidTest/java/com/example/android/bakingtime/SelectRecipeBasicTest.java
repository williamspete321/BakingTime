package com.example.android.bakingtime;

import android.content.Intent;

import androidx.fragment.app.FragmentManager;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.android.bakingtime.ui.MainActivity;
import com.example.android.bakingtime.ui.MainListFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SelectRecipeBasicTest {

    @Rule public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setupActivity() {
        mActivityTestRule.launchActivity(new Intent(mActivityTestRule.getActivity(), MainActivity.class));
    }

    @Before
    public void setupFragment() {
        MainListFragment mainListFragment = new MainListFragment();
        FragmentManager fragmentManager = mActivityTestRule.getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_main_list_container, mainListFragment)
                .commit();

    }

    @Test
    public void selectRecipeItemFromRecyclerView_openDetailActivity() {
        onView(withId(R.id.recipe_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.tv_ingredient_title))
                .check(matches(withText("INGREDIENTS")));
    }

}
