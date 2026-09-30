package com.example.smartpantrymanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ADD = 1001;
    private static final int REQUEST_EDIT = 1002;

    private DbHelper dbHelper;
    private RecyclerView pantryRecyclerView;
    private IngredientAdapter adapter;
    private TextView emptyPantryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);
        pantryRecyclerView = findViewById(R.id.pantryRecyclerView);
        emptyPantryText = findViewById(R.id.emptyPantryText);

        pantryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IngredientAdapter(new IngredientAdapter.OnIngredientClickListener() {
            @Override
            public void onIngredientClick(Ingredient ingredient) {
                openEditor(ingredient);
            }

            @Override
            public void onIngredientLongClick(Ingredient ingredient) {
                confirmDelete(ingredient);
            }
        });
        pantryRecyclerView.setAdapter(adapter);

        Button addButton = findViewById(R.id.addIngredientButton);
        Button suggestedButton = findViewById(R.id.suggestedRecipesButton);
        Button settingsButton = findViewById(R.id.settingsButton);

        addButton.setOnClickListener(v -> openAddScreen());
        suggestedButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SuggestedRecipesActivity.class)));
        settingsButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SettingsActivity.class)));

        loadIngredients();
    }

    private void loadIngredients() {
        List<Ingredient> ingredients = dbHelper.getAllIngredients();
        adapter.setIngredients(ingredients);
        emptyPantryText.setVisibility(ingredients.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void openAddScreen() {
        Intent intent = new Intent(this, AddEditIngredientActivity.class);
        intent.putExtra("mode", "add");
        startActivityForResult(intent, REQUEST_ADD);
    }

    private void openEditor(Ingredient ingredient) {
        Intent intent = new Intent(this, AddEditIngredientActivity.class);
        intent.putExtra("mode", "edit");
        intent.putExtra("ingredient_id", ingredient.getId());
        startActivityForResult(intent, REQUEST_EDIT);
    }

    private void confirmDelete(final Ingredient ingredient) {
        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient")
                .setMessage("Delete " + DbHelper.formatIngredientName(ingredient.getName()) + " from pantry?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dbHelper.deleteIngredient(ingredient.getId());
                    loadIngredients();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadIngredients();
        }
    }
}
