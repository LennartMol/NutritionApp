package com.example.nutritionapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class ScanFoodActivity extends AppCompatActivity {

    private String fragType;
    private String date;

    private String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scan_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        fragType = intent.getStringExtra("fragType");
        date = intent.getStringExtra("date");
    }

    public void onClickViewInfoButton(View view){
        EditText barcodeEditText = findViewById(R.id.barcodeText);
        barcode = barcodeEditText.getText().toString();
        Intent intent = new Intent(ScanFoodActivity.this, FoodInfoActivity.class);
        intent.putExtra("fragType", fragType);
        intent.putExtra("date", date);
        intent.putExtra("barcode", barcode);
        startActivity(intent);
    }
}