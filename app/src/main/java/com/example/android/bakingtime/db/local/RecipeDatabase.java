package com.example.android.bakingtime.db.local;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.android.bakingtime.model.Recipe;

@Database(entities = {Recipe.class}, version = 5)
@TypeConverters({Converters.class})
public abstract class RecipeDatabase extends RoomDatabase {
    private static final String TAG = RecipeDatabase.class.getSimpleName();

    private static RecipeDatabase instance;

    public abstract RecipeDao recipeDao();

    public static synchronized RecipeDatabase getInstance(Context context) {
        if(instance == null) {
            Log.d(TAG, "database is being created");
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    RecipeDatabase.class, "recipe_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
