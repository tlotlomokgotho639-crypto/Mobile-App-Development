package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_INGREDIENTS = "ingredients";
    public static final String TABLE_RECIPES = "recipes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_EXPIRY_DATE = "expiry_date";

    public static final String COLUMN_RECIPE_NAME = "recipe_name";
    public static final String COLUMN_RECIPE_INGREDIENTS = "ingredients";
    public static final String COLUMN_RECIPE_STEPS = "steps";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createIngredients = "CREATE TABLE " + TABLE_INGREDIENTS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_QUANTITY + " REAL NOT NULL, "
                + COLUMN_UNIT + " TEXT NOT NULL, "
                + COLUMN_EXPIRY_DATE + " TEXT)";

        String createRecipes = "CREATE TABLE " + TABLE_RECIPES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_RECIPE_NAME + " TEXT NOT NULL, "
                + COLUMN_RECIPE_INGREDIENTS + " TEXT NOT NULL, "
                + COLUMN_RECIPE_STEPS + " TEXT NOT NULL)";

        db.execSQL(createIngredients);
        db.execSQL(createRecipes);
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    public long addIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, normaliseName(ingredient.getName()));
        values.put(COLUMN_QUANTITY, ingredient.getQuantity());
        values.put(COLUMN_UNIT, ingredient.getUnit());
        values.put(COLUMN_EXPIRY_DATE, ingredient.getExpiryDate());
        return db.insert(TABLE_INGREDIENTS, null, values);
    }

    public int updateIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, normaliseName(ingredient.getName()));
        values.put(COLUMN_QUANTITY, ingredient.getQuantity());
        values.put(COLUMN_UNIT, ingredient.getUnit());
        values.put(COLUMN_EXPIRY_DATE, ingredient.getExpiryDate());
        return db.update(TABLE_INGREDIENTS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(ingredient.getId())});
    }

    public void deleteIngredient(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INGREDIENTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INGREDIENTS, null, null, null, null, null, COLUMN_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                ingredient.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                ingredient.setQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)));
                ingredient.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT)));
                ingredient.setExpiryDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPIRY_DATE)));
                ingredients.add(ingredient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ingredients;
    }

    public Ingredient getIngredientById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INGREDIENTS, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            ingredient.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
            ingredient.setQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)));
            ingredient.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT)));
            ingredient.setExpiryDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPIRY_DATE)));
            cursor.close();
            return ingredient;
        }
        cursor.close();
        return null;
    }

    public static String normaliseName(String name) {
        if (name == null) {
            return "";
        }
        String cleaned = name.trim().replaceAll("\\s+", " ").toLowerCase();
        if (cleaned.isEmpty()) {
            return "";
        }

        String[] words = cleaned.split(" ");
        for (int i = 0; i < words.length; i++) {
            words[i] = singularise(words[i]);
        }
        return String.join(" ", words);
    }

    private static String singularise(String word) {
        if (word.endsWith("ies") && word.length() > 3) {
            return word.substring(0, word.length() - 3) + "y";
        }
        if (word.endsWith("sses") && word.length() > 4) {
            return word.substring(0, word.length() - 2);
        }
        if (word.endsWith("s") && !word.endsWith("ss") && word.length() > 3) {
            return word.substring(0, word.length() - 1);
        }
        return word;
    }

    public static String formatIngredientName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "";
        }
        String[] words = name.trim().split(" ");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                builder.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase());
                builder.append(" ");
            }
        }
        return builder.toString().trim();
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, null, null, null, null, COLUMN_RECIPE_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME)));
                recipe.setIngredients(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_INGREDIENTS)));
                recipe.setSteps(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_STEPS)));
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipes;
    }

    private void seedRecipes(SQLiteDatabase db) {
        String[][] recipeData = {
                {"Tomato Pasta", "tomato, garlic, basil, pasta, olive oil", "1. Boil pasta. 2. Add garlic and tomato. 3. Stir in basil and olive oil. 4. Serve."},
                {"Vegetable Omelette", "egg, spinach, tomato, onion, pepper", "1. Whisk eggs. 2. Fry onion, pepper and spinach. 3. Add eggs and tomato. 4. Fold and cook."},
                {"Chicken Rice Bowl", "chicken breast, rice, broccoli, carrot, soy sauce", "1. Cook rice. 2. Sauté chicken and vegetables. 3. Add soy sauce and serve."},
                {"Bean Salad", "beans, cucumber, tomato, onion, lemon", "1. Mix beans and chopped vegetables. 2. Add lemon juice. 3. Season and serve."},
                {"Spinach Smoothie", "spinach, banana, milk, yogurt, oats", "1. Blend all ingredients. 2. Serve chilled."},
                {"Pesto Pasta", "pasta, basil, garlic, pine nuts, olive oil", "1. Cook pasta. 2. Blend basil, garlic and pine nuts. 3. Toss with olive oil and pasta."},
                {"Chickpea Curry", "chickpeas, coconut milk, onion, garlic, curry powder", "1. Sauté onion and garlic. 2. Add chickpeas and coconut milk. 3. Stir in curry powder and simmer."},
                {"Greek Salad", "tomato, cucumber, feta, olives, onion", "1. Chop all vegetables. 2. Add feta and olives. 3. toss with dressing."},
                {"Avocado Toast", "bread, avocado, tomato, lemon, chili flakes", "1. Toast bread. 2. Mash avocado with lemon. 3. Top with tomato and chili."},
                {"Fruit Yogurt Bowl", "yogurt, banana, strawberries, granola, honey", "1. Layer yogurt and fruit. 2. Top with granola and honey."},
                {"Vegetable Stir Fry", "rice, broccoli, carrot, pepper, soy sauce", "1. Cook rice. 2. Stir fry vegetables. 3. Add soy sauce and serve."},
                {"Tomato Soup", "tomato, onion, garlic, basil, stock", "1. Sauté onion and garlic. 2. Simmer with tomato and stock. 3. Blend and add basil."},
                {"Egg Fried Rice", "rice, egg, peas, carrot, soy sauce", "1. Fry egg. 2. Add rice and vegetables. 3. Season with soy sauce."},
                {"Mango Salsa Bowl", "rice, mango, black beans, corn, lime", "1. Cook rice. 2. Mix salsa ingredients. 3. Serve over rice."},
                {"Lentil Soup", "lentils, carrot, onion, garlic, stock", "1. Cook onion and garlic. 2. Add lentils, carrot and stock. 3. Simmer until tender."},
                {"Caesar Wrap", "wrap, lettuce, chicken breast, tomato, dressing", "1. Fill wrap with ingredients. 2. Roll and serve."}
        };

        for (String[] recipe : recipeData) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_RECIPE_NAME, recipe[0]);
            values.put(COLUMN_RECIPE_INGREDIENTS, recipe[1]);
            values.put(COLUMN_RECIPE_STEPS, recipe[2]);
            db.insert(TABLE_RECIPES, null, values);
        }
    }
}
