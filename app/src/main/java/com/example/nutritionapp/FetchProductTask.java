package com.example.nutritionapp;

import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class FetchProductTask extends AsyncTask<String, Void, String> {

    private OnDataFetchedListener listener;

    // Interface to communicate results back to the caller
    public interface OnDataFetchedListener {
        void onDataFetched(String productName);
        void onError(String errorMessage);
    }

    public FetchProductTask(OnDataFetchedListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... urls) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResponse = null;

        try {
            // Create URL object
            URL url = new URL(urls[0]);

            // Establish connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            if (builder.length() == 0) {
                return null;
            }
            jsonResponse = builder.toString();
        } catch (IOException e) {
            listener.onError(e.getMessage());
        } finally {
            // Close connection
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    listener.onError(e.getMessage());
                }
            }
        }
        return jsonResponse;
    }

    @Override
    protected void onPostExecute(String jsonResponse) {
        super.onPostExecute(jsonResponse);

        if (jsonResponse != null) {
            try {
                // Parse JSON response
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONObject productObject = jsonObject.getJSONObject("product");
                JSONObject nutrimentsObject = productObject.getJSONObject("nutriments");

                String productName = productObject.getString("product_name");
                String productCarbs = nutrimentsObject.optString("carbohydrates_serving");
                String productCalories = nutrimentsObject.optString("energy-kcal_serving");
                String productProtein = nutrimentsObject.optString("proteins_serving");
                String productFat = nutrimentsObject.optString("fat_serving");


                List<String> productDetailsList = new ArrayList<>();
                productDetailsList.add(productName);
                productDetailsList.add(productCarbs);
                productDetailsList.add(productCalories);
                productDetailsList.add(productProtein);
                productDetailsList.add(productFat);

                // Send result back to the caller
                listener.onDataFetched(productDetailsList.toString());
            } catch (JSONException e) {
                listener.onError(e.getMessage());
            }
        } else {
            listener.onError("No data received from the server");
        }
    }
}

