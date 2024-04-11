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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DinnerFragment extends Fragment {

    private List<FoodItem> dinnerItems;
    private RecyclerView dinnerView;

    private String selectedDateText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dinner, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedDateText = bundle.getString("key");
        }
        dinnerView = view.findViewById(R.id.dinnerView);
        loadData();
        return view;
    }

    public static DinnerFragment newInstance(String data) {
        DinnerFragment fragment = new DinnerFragment();
        Bundle args = new Bundle();
        args.putString("key", data);
        fragment.setArguments(args);
        return fragment;
    }

    private void loadData() {
        String jsonData = loadJSONFromFile("food_per_day.json");
        dinnerItems = parseMealData(jsonData, selectedDateText, "dinner"); // Update date as needed
        updateUI();
    }

    private String loadJSONFromFile(String filename) {
        String json;
        try {
            // Get the path to the file in the app's private files directory
            File file = new File(getContext().getFilesDir(), filename);

            // Create an InputStream for the file
            InputStream is = new FileInputStream(file);

            // Read the contents of the file into a byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            // Close the InputStream
            is.close();

            // Convert the byte array to a String using UTF-8 encoding
            json = byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());
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