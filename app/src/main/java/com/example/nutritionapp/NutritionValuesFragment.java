package com.example.nutritionapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class NutritionValuesFragment extends Fragment {

    private List<FoodItem> totalValue;
    private RecyclerView valuesView;
    private String selectedDateText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition_values, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedDateText = bundle.getString("key");
        }
        valuesView = view.findViewById(R.id.valuesView);
        loadData();
        return view;
    }

    public static NutritionValuesFragment newInstance(String data) {
        NutritionValuesFragment fragment = new NutritionValuesFragment();
        Bundle args = new Bundle();
        args.putString("key", data);
        fragment.setArguments(args);
        return fragment;
    }

    private void loadData() {
        String jsonData = loadJSONFromAsset("food_per_day.json");
        List<FoodItem> breakfastItems = parseMealData(jsonData, selectedDateText, "breakfast");
        List<FoodItem> lunchItems = parseMealData(jsonData, selectedDateText, "lunch");
        List<FoodItem> dinnerItems = parseMealData(jsonData, selectedDateText, "dinner");
        totalValue = getTotalNutritionalValue(breakfastItems, lunchItems, dinnerItems);
        updateUI();
    }

    private String loadJSONFromAsset(String filename) {
        String json;
        try {
            InputStream is = getActivity().getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    private List<FoodItem> parseMealData(String jsonData, String date, String mealType) {
        List<FoodItem> mealItems = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject dateObject = jsonObject.getJSONObject(date);
            JSONObject mealObject = dateObject.getJSONObject(mealType);
            JSONArray namesArray = mealObject.names();
            if (namesArray != null) {
                for (int i = 0; i < namesArray.length(); i++) {
                    String itemName = namesArray.getString(i);
                    JSONObject foodObject = mealObject.getJSONObject(itemName);
                    String carbs = foodObject.getString("carbs");
                    String calories = foodObject.getString("calories");
                    String protein = foodObject.getString("protein");
                    String fat = foodObject.getString("fat");
                    // Create a FoodItem object and add it to the list
                    mealItems.add(new FoodItem(itemName, carbs, calories, protein, fat));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mealItems;
    }

    private List<FoodItem> getTotalNutritionalValue(List<FoodItem> breakfastList, List<FoodItem> luncList, List<FoodItem> dinnerList){

        List<FoodItem> mealItems = new ArrayList<>();
        double  totalCarbs = 0;
        double  totalCalories = 0;
        double  totalProtein = 0;
        double  totalFat = 0;

        for (FoodItem item : breakfastList) {
            totalCarbs += Double.parseDouble(item.getCarbs());
            totalCalories += Double.parseDouble(item.getCalories());
            totalProtein += Double.parseDouble(item.getProtein());
            totalFat += Double.parseDouble(item.getFat());
        }

        for (FoodItem item : luncList) {
            totalCarbs += Double.parseDouble(item.getCarbs());
            totalCalories += Double.parseDouble(item.getCalories());
            totalProtein += Double.parseDouble(item.getProtein());
            totalFat += Double.parseDouble(item.getFat());
        }

        for (FoodItem item : dinnerList) {
            totalCarbs += Double.parseDouble(item.getCarbs());
            totalCalories += Double.parseDouble(item.getCalories());
            totalProtein += Double.parseDouble(item.getProtein());
            totalFat += Double.parseDouble(item.getFat());
        }

        mealItems.add(new FoodItem("Total", String.valueOf(totalCarbs), String.valueOf(totalCalories), String.valueOf(totalProtein), String.valueOf(totalFat)));

        return mealItems;
    }

    private void updateUI() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        valuesView.setLayoutManager(layoutManager);
        FoodItemAdapter adapter = new FoodItemAdapter(totalValue);
        valuesView.setAdapter(adapter);
    }
}