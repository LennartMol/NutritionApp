package com.example.nutritionapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.TextView;

public class FoodInfoActivity extends AppCompatActivity implements FetchProductTask.OnDataFetchedListener {

    private String fragType;
    private String date;
    private String barcode;

    private String urlBase = "https://world.openfoodfacts.org/api/v2/product/";

    private String urlFull;

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView productNameTextView;
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

        Intent intent = getIntent();
        fragType = intent.getStringExtra("fragType");
        date = intent.getStringExtra("date");
        barcode = intent.getStringExtra("barcode");
        barcode = "8004708048953";
        urlFull = String.format("%s%s.json", urlBase, barcode);

        productNameTextView = findViewById(R.id.productNameTextView);

        // Execute AsyncTask
        new FetchProductTask(this).execute(urlFull);
    }

    @Override
    public void onDataFetched(String productName) {
        // Update UI with the fetched data
        productNameTextView.setText(productName);
    }

    @Override
    public void onError(String errorMessage) {
        // Handle errors
        productNameTextView.setText("Error: " + errorMessage);
    }
}