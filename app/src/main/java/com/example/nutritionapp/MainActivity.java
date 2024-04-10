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

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    private Button pickDateBtn;
    private TextView selectedDateTV;
    private String selectedDateString = "11-04-2024";
    private String currentFragmentType = "Breakfast";

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

        BreakfastFragment fragment = BreakfastFragment.newInstance(selectedDateString);
        replaceFragment(fragment);

        pickDateBtn = findViewById(R.id.idBtnPickDate);
        selectedDateTV = findViewById(R.id.idTVSelectedDate);
        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String formattedDay = String.format("%02d", dayOfMonth); // Add leading zero if less than 10
                                String formattedMonth = String.format("%02d", monthOfYear + 1); // Add leading zero if less than 10
                                selectedDateString = formattedDay + "-" + (formattedMonth) + "-" + year;
                                selectedDateTV.setText(selectedDateString);
                                setCurrentFragmentType();
                                if (currentFragmentType == "Breakfast") {
                                    BreakfastFragment fragment = BreakfastFragment.newInstance(selectedDateString);
                                    replaceFragment(fragment);
                                } else if (currentFragmentType == "Lunch") {
                                    LunchFragment fragment = LunchFragment.newInstance(selectedDateString);
                                    replaceFragment(fragment);
                                } else if (currentFragmentType == "Dinner") {
                                    DinnerFragment fragment = DinnerFragment.newInstance(selectedDateString);
                                    replaceFragment(fragment);
                                } else if (currentFragmentType == "NutritionValues"){
                                    NutritionValuesFragment fragment = NutritionValuesFragment.newInstance(selectedDateString);
                                    replaceFragment(fragment);
                                }

                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });
    }

    public void onClickValuesButton(View view) {
        NutritionValuesFragment fragment = NutritionValuesFragment.newInstance(selectedDateString);
        replaceFragment(fragment);
    }

    public void onClickBreakfastButton(View view) {
        BreakfastFragment fragment = BreakfastFragment.newInstance(selectedDateString);
        replaceFragment(fragment);
    }

    public void onClickLunchButton(View view) {
        LunchFragment fragment = LunchFragment.newInstance(selectedDateString);
        replaceFragment(fragment);
    }

    public void onClickDinnerButton(View view) {
        DinnerFragment fragment = DinnerFragment.newInstance(selectedDateString);
        replaceFragment(fragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fcvFragment, fragment);
        transaction.commit();
    }

    public void setCurrentFragmentType() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fcvFragment);
        if (currentFragment instanceof BreakfastFragment) {
            currentFragmentType = "Breakfast";
        } else if (currentFragment instanceof LunchFragment) {
            currentFragmentType = "Lunch";
        } else if (currentFragment instanceof DinnerFragment) {
            currentFragmentType = "Dinner";
        } else if (currentFragment instanceof NutritionValuesFragment){
            currentFragmentType = "NutritionValues";
        }
    }

    public void onClickAddBreakfastFoodButton(View view){
        setCurrentFragmentType();
        Intent intent = new Intent(MainActivity.this, AddFoodActivity.class);
        intent.putExtra("fragType", currentFragmentType);
        intent.putExtra("date", selectedDateString);
        startActivity(intent);
    }
}