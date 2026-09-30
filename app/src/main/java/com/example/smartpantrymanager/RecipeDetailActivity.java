package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        String recipeName = getIntent().getStringExtra("recipe_name");
        String ingredients = getIntent().getStringExtra("recipe_ingredients");
        String steps = getIntent().getStringExtra("recipe_steps");

        TextView title = findViewById(R.id.recipeDetailTitle);
        TextView ingredientsText = findViewById(R.id.recipeIngredientsText);
        TextView stepsText = findViewById(R.id.recipeStepsText);

        title.setText(recipeName);
        ingredientsText.setText(ingredients.replace(",", "\n• "));
        stepsText.setText(steps.replace(". ", ".\n\n"));
    }
}
