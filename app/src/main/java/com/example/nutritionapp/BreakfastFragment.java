package com.example.nutritionapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private String selectedDateText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_breakfast, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedDateText = bundle.getString("key");
        }
        breakfastView = view.findViewById(R.id.breakfastView);
        loadData();
        return view;
    }

    public static BreakfastFragment newInstance(String data) {
        BreakfastFragment fragment = new BreakfastFragment();
        Bundle args = new Bundle();
        args.putString("key", data);
        fragment.setArguments(args);
        return fragment;
    }

    private void loadData() {
        String jsonData = loadJSONFromAsset("food_per_day.json");
        breakfastItems = parseBreakfastData(jsonData, String.valueOf(selectedDateText)); // should be based on the selected date in future
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

