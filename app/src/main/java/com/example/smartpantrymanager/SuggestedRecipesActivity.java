package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private DbHelper dbHelper;
    private RecyclerView recyclerView;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        dbHelper = new DbHelper(this);
        recyclerView = findViewById(R.id.suggestedRecipesRecyclerView);
        emptyText = findViewById(R.id.suggestedEmptyText);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {
        List<Ingredient> pantryItems = dbHelper.getAllIngredients();
        List<Recipe> recipes = dbHelper.getAllRecipes();
        List<Recipe> matchedRecipes = MatchingUtil.getMatchingRecipes(pantryItems, recipes);

        RecipeAdapter adapter = new RecipeAdapter(recipe -> {
            Intent intent = new Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
            intent.putExtra("recipe_name", recipe.getName());
            intent.putExtra("recipe_ingredients", recipe.getIngredients());
            intent.putExtra("recipe_steps", recipe.getSteps());
            startActivity(intent);
        });

        adapter.setRecipes(matchedRecipes);
        recyclerView.setAdapter(adapter);

        if (matchedRecipes.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
        }
    }
}
