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

public class DinnerFragment extends Fragment {

    private List<FoodItem> dinnerItems;
    private RecyclerView dinnerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dinner, container, false);
        dinnerView = view.findViewById(R.id.dinnerView);
        loadData();
        return view;
    }

    private void loadData() {
        String jsonData = loadJSONFromAsset("food_per_day.json");
        dinnerItems = parseMealData(jsonData, "10-04-2024", "dinner"); // Update date as needed
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

    private void updateUI() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        dinnerView.setLayoutManager(layoutManager);
        FoodItemAdapter adapter = new FoodItemAdapter(dinnerItems);
        dinnerView.setAdapter(adapter);
    }
}