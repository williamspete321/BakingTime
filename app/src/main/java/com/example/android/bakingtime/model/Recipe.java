package com.example.android.bakingtime.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity(tableName = "recipe_table")
public class Recipe {

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    private Integer mId;
    @SerializedName("name")
    @Expose
    private String mName;
    @SerializedName("ingredients")
    @Expose
    private ArrayList<Ingredient> mIngredients;
    @SerializedName("steps")
    @Expose
    private ArrayList<Step> mSteps;
    @SerializedName("servings")
    @Expose
    private Integer mServings;

    public Recipe(int id, String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, int servings) {
        mId = id;
        mName = name;
        mIngredients = ingredients;
        mSteps = steps;
        mServings = servings;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return mSteps;
    }

    public void setSteps(ArrayList<Step> steps) {
        mSteps = steps;
    }

    public Integer getServings() {
        return mServings;
    }

    public void setServings(Integer servings) {
        mServings = servings;
    }

}
