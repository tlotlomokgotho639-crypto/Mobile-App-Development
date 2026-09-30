package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchingUtil {

    public static List<Recipe> getMatchingRecipes(List<Ingredient> pantryItems, List<Recipe> recipes) {
        Map<String, Double> pantryMap = new HashMap<>();

        for (Ingredient ingredient : pantryItems) {
            if (ingredient == null || ingredient.getName() == null || ingredient.getName().trim().isEmpty()) {
                continue;
            }
            String key = DbHelper.normaliseName(ingredient.getName());
            pantryMap.put(key, pantryMap.getOrDefault(key, 0.0) + ingredient.getQuantity());
        }

        List<Recipe> matches = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe == null || recipe.getIngredients() == null) {
                continue;
            }

            String[] ingredients = recipe.getIngredients().split(",");
            boolean isMatch = true;

            for (String item : ingredients) {
                String ingredientName = DbHelper.normaliseName(item.trim());
                if (ingredientName.isEmpty()) {
                    continue;
                }

                double pantryQty = pantryMap.getOrDefault(ingredientName, 0.0);
                if (pantryQty < 1.0) {
                    isMatch = false;
                    break;
                }
            }

            if (isMatch) {
                matches.add(recipe);
            }
        }

        return matches;
    }
}
