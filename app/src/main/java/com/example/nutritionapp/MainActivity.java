package com.example.nutritionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        replaceFragment(new BreakfastFragment());
    }

    public void onClickValuesButton(View view) {
        replaceFragment(new NutritionValuesFragment());
    }

    public void onClickBreakfastButton(View view) {
        replaceFragment(new BreakfastFragment());
    }

    public void onClickLunchButton(View view) {
        replaceFragment(new LunchFragment());
    }

    public void onClickDinnerButton(View view) {
        replaceFragment(new DinnerFragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fcvFragment, fragment);
        transaction.commit();
    }
}