package com.example.android.bakingtime.db.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.android.bakingtime.model.Ingredient;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.model.Step;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Recipe recipe);

    @Query("SELECT * FROM recipe_table ORDER BY mId ASC")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM recipe_table WHERE mId = :id")
    LiveData<Recipe> getRecipeByIDLiveData(int id);

    @Query("SELECT * FROM recipe_table WHERE mId = :id")
    Recipe getRecipeByID(int id);
}
