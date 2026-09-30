package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final List<Ingredient> ingredients = new ArrayList<>();
    private final OnIngredientClickListener listener;

    public interface OnIngredientClickListener {
        void onIngredientClick(Ingredient ingredient);
        void onIngredientLongClick(Ingredient ingredient);
    }

    public IngredientAdapter(OnIngredientClickListener listener) {
        this.listener = listener;
    }

    public void setIngredients(List<Ingredient> newIngredients) {
        ingredients.clear();
        ingredients.addAll(newIngredients);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.name.setText(DbHelper.formatIngredientName(ingredient.getName()));
        holder.details.setText(String.format("%.2f %s", ingredient.getQuantity(), ingredient.getUnit()));
        holder.expiry.setText(ingredient.getExpiryDate() != null && !ingredient.getExpiryDate().isEmpty()
                ? "Expires: " + ingredient.getExpiryDate() : "No expiry date");

        holder.itemView.setOnClickListener(v -> listener.onIngredientClick(ingredient));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onIngredientLongClick(ingredient);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView details;
        TextView expiry;

        IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ingredientNameText);
            details = itemView.findViewById(R.id.ingredientDetailText);
            expiry = itemView.findViewById(R.id.ingredientExpiryText);
        }
    }
}
