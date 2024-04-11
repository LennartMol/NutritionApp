package com.example.nutritionapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.ArrayList;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.io.FileWriter;
import java.io.IOException;
import android.content.Context;

public class FoodInfoActivity extends AppCompatActivity implements FetchProductTask.OnDataFetchedListener {

    private String fragType;
    private String date;
    private String barcode;
    private String urlBase = "https://world.openfoodfacts.org/api/v2/product/";
    private String urlFull;
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView infoView;
    private TextView EatMomentTV;
    private TextView DateTV;
    private FoodItem food;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        infoView = findViewById(R.id.infoView);
        EatMomentTV = findViewById(R.id.EatMomentTextView);
        DateTV = findViewById(R.id.DateTextView);


        Intent intent = getIntent();
        fragType = intent.getStringExtra("fragType");
        EatMomentTV.setText(fragType);
        date = intent.getStringExtra("date");
        DateTV.setText(date);

        barcode = intent.getStringExtra("barcode");
        //barcode = "3046920010047";
        //barcode = "8718906105935";
        barcode = "8004708048953";
        urlFull = String.format("%s%s.json", urlBase, barcode);

        // Execute AsyncTask
        new FetchProductTask(this).execute(urlFull);
    }

    @Override
    public void onDataFetched(String productDetails) {
        // Split the productDetails string by comma and remove the brackets
        String[] detailsArray = productDetails.substring(1, productDetails.length() - 1).split(",");

        // Extract individual details starting from the end of the array
        String fat = detailsArray[detailsArray.length - 1].trim();
        String protein = detailsArray[detailsArray.length - 2].trim();
        String calories = detailsArray[detailsArray.length - 3].trim();
        String carbohydrates = detailsArray[detailsArray.length - 4].trim();

        // Combine the remaining parts to form the product name
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < detailsArray.length - 4; i++) {
            nameBuilder.append(detailsArray[i]);
            if (i < detailsArray.length - 5) {
                nameBuilder.append(",");
            }
        }
        String name = nameBuilder.toString().trim();

        FoodItem item = new FoodItem(name, carbohydrates, calories, protein, fat);
        food = item;
        List<FoodItem> foodItem = new ArrayList<>();
        foodItem.add(item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        infoView.setLayoutManager(layoutManager);
        FoodItemAdapter adapter = new FoodItemAdapter(foodItem);
        infoView.setAdapter(adapter);
    }

    @Override
    public void onError(String errorMessage) {
    }

    public void onClickAddToFoodLogButton(View view) {
        try {
            //File file = new File(getContext().getFilesDir(), "food_per_day.json");

            String jsonData = loadJSONFromFile("food_per_day.json");
            JSONObject jsonObject = new JSONObject(jsonData);

            // Get the JSONObject for the given date, creating a new one if it doesn't exist
            JSONObject dateObject;
            if (jsonObject.has(date)) {
                dateObject = jsonObject.getJSONObject(date);
            } else {
                dateObject = new JSONObject();
                dateObject.put("breakfast", new JSONObject());
                dateObject.put("lunch", new JSONObject());
                dateObject.put("dinner", new JSONObject());
                jsonObject.put(date, dateObject);
                writeJSONToFile(this,jsonObject.toString(), "food_per_day.json");
            }

            // Get the meal moment JSONObject (e.g., breakfastObject, lunchObject, dinnerObject)
            JSONObject mealObject = dateObject.getJSONObject(fragType);

            // Create a new JSONObject for the food item and add its nutritional values
            JSONObject foodObject = new JSONObject();
            foodObject.put("carbs", food.getCarbs());
            foodObject.put("calories", food.getCalories());
            foodObject.put("protein", food.getProtein());
            foodObject.put("fat", food.getFat());

            // Add the food JSONObject to the meal moment JSONObject
            mealObject.put(food.getName(), foodObject);

            // Write the updated JSON data back to the file
            writeJSONToFile(this, jsonObject.toString(), "food_per_day.json");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromFile(String filename) {
        String json;
        try {
            // Get the path to the file in the app's private files directory
            File file = new File(this.getFilesDir(), filename);

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

    public static void writeJSONToFile(Context context, String jsonData, String filename) {
        try {
            // Get the path to the file in the app's private files directory
            File file = new File(context.getFilesDir(), filename);

            // Create a FileWriter to write to the file
            FileWriter fileWriter = new FileWriter(file);

            // Write the JSON data to the file
            fileWriter.write(jsonData);

            // Close the FileWriter
            fileWriter.close();

            System.out.println("Successfully wrote JSON data to file: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error writing JSON data to file: " + filename);
        }
    }
}