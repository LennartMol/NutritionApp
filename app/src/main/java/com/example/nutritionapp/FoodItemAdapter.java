package com.example.nutritionapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> {
    private List<FoodItem> foodItemList;

    public FoodItemAdapter(List<FoodItem> foodItemList) {
        this.foodItemList = foodItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem foodItem = foodItemList.get(position);
        holder.bind(foodItem);
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView carbsTextView;
        private TextView caloriesTextView;
        private TextView proteinTextView;
        private TextView fatTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            carbsTextView = itemView.findViewById(R.id.carbsTextView);
            caloriesTextView = itemView.findViewById(R.id.caloriesTextView);
            proteinTextView = itemView.findViewById(R.id.proteinTextView);
            fatTextView = itemView.findViewById(R.id.fatTextView);
        }

        public void bind(FoodItem foodItem) {
            nameTextView.setText(foodItem.getName());
            carbsTextView.setText(String.format("Carbs: %s", foodItem.getCarbs()));
            caloriesTextView.setText(String.format("Calories: %s", foodItem.getCalories()));
            proteinTextView.setText(String.format("Protein: %s", foodItem.getProtein()));
            fatTextView.setText(String.format("Fat: %s", foodItem.getFat()));
        }
    }
}
