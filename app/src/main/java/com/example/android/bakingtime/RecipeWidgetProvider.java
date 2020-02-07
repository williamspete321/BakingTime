package com.example.android.bakingtime;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import androidx.lifecycle.ViewModelProviders;

import com.example.android.bakingtime.db.AppExecutors;
import com.example.android.bakingtime.db.local.RecipeDatabase;
import com.example.android.bakingtime.model.Ingredient;
import com.example.android.bakingtime.model.Recipe;
import com.example.android.bakingtime.ui.MainActivity;
import com.example.android.bakingtime.ui.RecipeDetailActivity;
import com.example.android.bakingtime.ui.RecipeDetailFragment;
import com.example.android.bakingtime.ui.RecipeViewModel;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPreferences
                = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int recipeID = sharedPreferences.getInt(context.getString(R.string.last_viewed_recipe), 1);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Recipe recipe = RecipeDatabase.getInstance(context).recipeDao().getRecipeByID(recipeID);

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
                views.setTextViewText(R.id.recipe_name_widget_textView, recipe.getName());
                views.setTextViewText(R.id.recipe_ingredient_list_widget_textView, buildIngredientList(recipe));

                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra(RecipeDetailFragment.ARG_RECIPE_ID, recipeID);

                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
                taskStackBuilder.addNextIntentWithParentStack(intent);

                PendingIntent pendingIntent =
                        taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                views.setOnClickPendingIntent(R.id.recipe_name_widget_textView, pendingIntent);
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        });

    }

    public static void updateRecipeWidget(Context context, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds) {
        for(int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static String buildIngredientList(Recipe recipe) {
        ArrayList<Ingredient> ingredients = recipe.getIngredients();

        if(ingredients != null) {
            StringBuilder allIngredients = new StringBuilder();
            for(Ingredient ingredient : ingredients) {
                double quantity = ingredient.getQuantity();
                Ingredient.Measurement measurement = ingredient.getMeasurementType();
                String content = ingredient.getContent();

                allIngredients.append("" + quantity + "\t" + measurement.toString() + "\t\t" + content + "\n");
            }
            return allIngredients.toString();
        }
        return null;
    }
}

