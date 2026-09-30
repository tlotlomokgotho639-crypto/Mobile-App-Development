package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText nameEdit;
    private EditText quantityEdit;
    private Spinner unitSpinner;
    private EditText expiryEdit;
    private TextView formTitle;
    private DbHelper dbHelper;
    private long ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        dbHelper = new DbHelper(this);

        nameEdit = findViewById(R.id.ingredientNameEditText);
        quantityEdit = findViewById(R.id.ingredientQuantityEditText);
        unitSpinner = findViewById(R.id.ingredientUnitSpinner);
        expiryEdit = findViewById(R.id.expiryDateEditText);
        formTitle = findViewById(R.id.formTitleText);
        Button saveButton = findViewById(R.id.saveIngredientButton);

        String[] units = {"items", "cups", "grams", "ml", "tbsp", "whole"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);

        String mode = getIntent().getStringExtra("mode");
        if ("edit".equals(mode)) {
            ingredientId = getIntent().getLongExtra("ingredient_id", -1);
            populateForm(ingredientId);
            formTitle.setText("Edit Ingredient");
        } else {
            formTitle.setText("Add Ingredient");
        }

        saveButton.setOnClickListener(v -> saveIngredient());
    }

    private void populateForm(long ingredientId) {
        Ingredient ingredient = dbHelper.getIngredientById(ingredientId);
        if (ingredient == null) {
            Toast.makeText(this, "Ingredient not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        nameEdit.setText(ingredient.getName());
        quantityEdit.setText(String.valueOf(ingredient.getQuantity()));
        expiryEdit.setText(ingredient.getExpiryDate() == null ? "" : ingredient.getExpiryDate());

        String[] units = {"items", "cups", "grams", "ml", "tbsp", "whole"};
        for (int i = 0; i < units.length; i++) {
            if (units[i].equalsIgnoreCase(ingredient.getUnit())) {
                unitSpinner.setSelection(i);
                break;
            }
        }
    }

    private void saveIngredient() {
        String name = nameEdit.getText().toString().trim();
        String quantityText = quantityEdit.getText().toString().trim();
        String expiry = expiryEdit.getText().toString().trim();
        String unit = unitSpinner.getSelectedItem().toString();

        if (name.isEmpty()) {
            nameEdit.setError("Ingredient name is required");
            return;
        }

        if (quantityText.isEmpty()) {
            quantityEdit.setError("Quantity is required");
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            quantityEdit.setError("Enter a valid quantity");
            return;
        }

        if (quantity <= 0) {
            quantityEdit.setError("Quantity must be greater than zero");
            return;
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setQuantity(quantity);
        ingredient.setUnit(unit);
        ingredient.setExpiryDate(expiry);

        if (ingredientId > 0) {
            ingredient.setId(ingredientId);
            dbHelper.updateIngredient(ingredient);
        } else {
            dbHelper.addIngredient(ingredient);
        }

        Intent result = new Intent();
        setResult(RESULT_OK, result);
        finish();
    }
}
