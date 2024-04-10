package com.example.nutritionapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;


public class BreakfastFragment extends Fragment {

    private List<FoodItem> breakfastItems;
    private RecyclerView breakfastView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_breakfast, container, false);
        breakfastView = view.findViewById(R.id.dinnerView);
        loadData();
        return view;


    }

    private void loadData() {
        String jsonData = loadJSONFromAsset("food_per_day.json");
        breakfastItems = parseBreakfastData(jsonData, "10-04-2024"); // should be based on the selected date in future
        updateUI();
        int test = 1;

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

    private List<FoodItem> parseBreakfastData(String jsonData, String date) {
        List<FoodItem> breakfastItems = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject dateObject = jsonObject.getJSONObject(date);
            JSONObject breakfastObject = dateObject.getJSONObject("breakfast");
            JSONArray namesArray = breakfastObject.names();
            if (namesArray != null) {
                for (int i = 0; i < namesArray.length(); i++) {
                    String itemName = namesArray.getString(i);
                    JSONObject foodObject = breakfastObject.getJSONObject(itemName);
                    String carbs = foodObject.getString("carbs");
                    String calories = foodObject.getString("calories");
                    String protein = foodObject.getString("protein");
                    String fat = foodObject.getString("fat");
                    // Create a FoodItem object and add it to the list
                    breakfastItems.add(new FoodItem(itemName, carbs, calories, protein, fat));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return breakfastItems;
    }

    private void updateUI() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        breakfastView.setLayoutManager(layoutManager);
        FoodItemAdapter adapter = new FoodItemAdapter(breakfastItems);
        breakfastView.setAdapter(adapter);
    }
}

