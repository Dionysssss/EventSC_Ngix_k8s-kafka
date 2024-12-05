package com.example.project2_login.model;

import android.util.Log;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class GeocodingHelper {

    private static final String TAG = "GeocodingHelper";
    private static final String GOOGLE_MAPS_API_KEY = "AIzaSyCN6PowW-UE7LwpHIOTp6KRojRApbabW_U";

    public interface GeocodingCallback {
        void onAddressFetched(String address);
        void onError(String errorMessage);
    }

    public static void fetchAddress(double latitude, double longitude, GeocodingCallback callback) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                + latitude + "," + longitude
                + "&key=" + GOOGLE_MAPS_API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error fetching address", e);
                callback.onError("Error fetching address: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject json = new JSONObject(responseBody);
                        JSONArray results = json.getJSONArray("results");
                        if (results.length() > 0) {
                            JSONObject firstResult = results.getJSONObject(0);
                            String address = firstResult.getString("formatted_address");
                            callback.onAddressFetched(address);
                        } else {
                            callback.onError("No address found for the given location.");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing address", e);
                        callback.onError("Error parsing address: " + e.getMessage());
                    }
                } else {
                    callback.onError("Failed to fetch address. Response code: " + response.code());
                }
            }
        });
    }

    public static String getAddressFromLatLng(double latitude, double longitude, String apiKey) throws Exception {
        OkHttpClient client = new OkHttpClient();

        // Construct the URL for the API request
        String url = String.format("%s?latlng=%.6f,%.6f&key=%s", GOOGLE_MAPS_API_KEY, latitude, longitude, apiKey);

        // Make the HTTP request
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Failed to fetch address: " + response.message());
            }

            // Parse the JSON response
            String jsonResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.getJSONArray("results");

            if (results.length() > 0) {
                return results.getJSONObject(0).getString("formatted_address");
            } else {
                return "No address found";
            }
        }
    }
}
