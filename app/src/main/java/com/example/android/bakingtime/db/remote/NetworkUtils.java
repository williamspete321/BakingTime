package com.example.android.bakingtime.db.remote;

public class NetworkUtils {

    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";

    public static RecipeService getRecipeService() {
        return RetrofitClient.getClient(BASE_URL).create(RecipeService.class);
    }

}
