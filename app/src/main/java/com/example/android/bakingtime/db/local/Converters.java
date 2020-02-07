package com.example.android.bakingtime.db.local;

import androidx.room.TypeConverter;

import com.example.android.bakingtime.model.Ingredient;
import com.example.android.bakingtime.model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {

    @TypeConverter
    public static ArrayList<Ingredient> fromStringToIngredientsArrayList(String value) {
        Type listType = new TypeToken<ArrayList<Ingredient>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayListToIngredientsString(ArrayList<Ingredient> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static ArrayList<Step> fromStringToStepsArrayList(String value) {
        Type listType = new TypeToken<ArrayList<Step>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayListToStepString(ArrayList<Step> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
