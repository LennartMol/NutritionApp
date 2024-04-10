package com.example.nutritionapp;

public class FoodItem {
    private String name;
    private String carbs;
    private String calories;
    private String protein;
    private String fat;

    public FoodItem(String name, String carbs, String calories, String protein, String fat) {
        this.name = name;
        this.carbs = carbs;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
    }

    public String getName() {
        return name;
    }

    public String getCarbs() {
        return carbs;
    }

    public String getCalories() {
        return calories;
    }

    public String getProtein() {
        return protein;
    }

    public String getFat() {
        return fat;
    }
}
