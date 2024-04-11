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
import java.util.List;
import java.util.ArrayList;

public class FoodInfoActivity extends AppCompatActivity implements FetchProductTask.OnDataFetchedListener {

    private String fragType;
    private String date;
    private String barcode;

    private String urlBase = "https://world.openfoodfacts.org/api/v2/product/";

    private String urlFull;
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView productNameTextView;
    private RecyclerView infoView;

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

        Intent intent = getIntent();
        fragType = intent.getStringExtra("fragType");
        date = intent.getStringExtra("date");
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
        List<FoodItem> foodItem = new ArrayList<>();
        foodItem.add(item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        infoView.setLayoutManager(layoutManager);
        FoodItemAdapter adapter = new FoodItemAdapter(foodItem);
        infoView.setAdapter(adapter);
    }

    @Override
    public void onError(String errorMessage) {
        // Handle errors
        productNameTextView.setText(String.format("Error: %s", errorMessage));
    }
}