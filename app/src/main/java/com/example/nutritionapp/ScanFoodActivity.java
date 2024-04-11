package com.example.nutritionapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;

public class ScanFoodActivity extends AppCompatActivity {

    private String fragType;
    private String date;

    private String barcode;
    ListView listView;
    private EditText BarcodeEditText;

    String[] barcodes = {
            "3046920010047 - Dark chocolate",
            "8718906105935 - Butterscotch chocolate( No name)",
            "8004708048953 - Salt",
            "4027400148909 - Curry",
            "8719200250413 - Blueband"};

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
        BarcodeEditText = findViewById(R.id.barcodeText);

        listView = findViewById(R.id.listView);

        ArrayAdapter adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                barcodes
        );

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String selectedBarcode = adapter.getItem(position).toString();
                String[] parts = selectedBarcode.split(" - ");
                String barcode = parts[0].trim();
                BarcodeEditText.setText(barcode);
            }

        });
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