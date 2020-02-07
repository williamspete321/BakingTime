package com.example.android.bakingtime.db.remote;

import com.example.android.bakingtime.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call <List<Recipe> > getRecipelist();

}
