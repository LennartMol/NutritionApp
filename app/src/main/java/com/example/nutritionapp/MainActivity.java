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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private Button pickDateBtn;
    private TextView selectedDateTV;
    private String selectedDateString = "11-04-2024";
    private String currentFragmentType = "breakfast";

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
        checkForFoodFile();

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
                                if (currentFragmentType == "breakfast") {
                                    BreakfastFragment fragment = BreakfastFragment.newInstance(selectedDateString);
                                    replaceFragment(fragment);
                                } else if (currentFragmentType == "lunch") {
                                    LunchFragment fragment = LunchFragment.newInstance(selectedDateString);
                                    replaceFragment(fragment);
                                } else if (currentFragmentType == "dinner") {
                                    DinnerFragment fragment = DinnerFragment.newInstance(selectedDateString);
                                    replaceFragment(fragment);
                                } else if (currentFragmentType == "nutritionValues"){
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
            currentFragmentType = "breakfast";
        } else if (currentFragment instanceof LunchFragment) {
            currentFragmentType = "lunch";
        } else if (currentFragment instanceof DinnerFragment) {
            currentFragmentType = "dinner";
        } else if (currentFragment instanceof NutritionValuesFragment){
            currentFragmentType = "nutritionValues";
        }
    }

    public void startScanFoodActivity(){
        setCurrentFragmentType();
        Intent intent = new Intent(MainActivity.this, ScanFoodActivity.class);
        intent.putExtra("fragType", currentFragmentType);
        intent.putExtra("date", selectedDateString);
        startActivity(intent);
    }

    public void onClickAddBreakfastFoodButton(View view){
        startScanFoodActivity();
    }

    public void onClickAddLunchFoodButton(View view){
        startScanFoodActivity();
    }

    public void onClickAddDinnerFoodButton(View view){
        startScanFoodActivity();
    }

    public void checkForFoodFile(){
        try {
            // Get the path to the template file in the assets directory
            String templateFilePath = "food_per_day.json";

            // Get the destination file in the app's private files directory
            File destinationFile = new File(this.getFilesDir(), templateFilePath);

            // Check if the file already exists in the private files directory
            if (!destinationFile.exists()) {
                // File does not exist, so copy the template file from the assets directory

                // Get the InputStream for the template file from the assets
                InputStream inputStream = this.getAssets().open(templateFilePath);

                // Create a FileOutputStream for the destination file
                FileOutputStream outputStream = new FileOutputStream(destinationFile);

                // Copy the template file from the assets to the app's private files directory
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                // Close the streams
                inputStream.close();
                outputStream.close();

                // Optionally, log the path to the saved template file for debugging
                Log.d("TemplateFile", "Template file saved at: " + destinationFile.getAbsolutePath());
            } else {
                // File already exists in the private files directory, do nothing
                Log.d("TemplateFile", "Template file already exists at: " + destinationFile.getAbsolutePath());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        setCurrentFragmentType();
        if (currentFragmentType == "breakfast") {
            BreakfastFragment fragment = BreakfastFragment.newInstance(selectedDateString);
            replaceFragment(fragment);
        } else if (currentFragmentType == "lunch") {
            LunchFragment fragment = LunchFragment.newInstance(selectedDateString);
            replaceFragment(fragment);
        } else if (currentFragmentType == "dinner") {
            DinnerFragment fragment = DinnerFragment.newInstance(selectedDateString);
            replaceFragment(fragment);
        } else if (currentFragmentType == "nutritionValues"){
            NutritionValuesFragment fragment = NutritionValuesFragment.newInstance(selectedDateString);
            replaceFragment(fragment);
        }
    }
}
