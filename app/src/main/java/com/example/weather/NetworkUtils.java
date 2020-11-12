package com.example.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather"; // request to network

    //parameters from url
    private static final String PARAMS_API_KEY = "APPID";
    private static final String PARAMS_LANGUAGE = "lang";
    private static final String PARAMS_UNITS = "units";
    private static final String PARAMS_Q = "q";

    //values from parameters
    private static final String API_KEY = "48510a55626068bacf9ce2b99156fada";
    private static final String LANGUAGE = "en";
    private static final String UNITS = "metric";
    private static final String COUNTRY_CODE = ",kz";
    private static SQLiteDatabase database;
    private static Date date = new Date();


    public static void setDatabase(SQLiteDatabase database) {
        NetworkUtils.database = database;
    }

    public static URL buildSearchURl(String q) {
        URL result = null;
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_Q, q + COUNTRY_CODE)
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE)
                .appendQueryParameter(PARAMS_UNITS, UNITS).build();
        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static class JSONLoader extends AsyncTaskLoader<JSONObject>{

        private Bundle bundle;

        private OnStartLoadingListener onStartLoadingListener;

        public interface OnStartLoadingListener{
            void OnStartLoading();
        }

        public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
            this.onStartLoadingListener = onStartLoadingListener;
        }

        public JSONLoader(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if(onStartLoadingListener != null){
                onStartLoadingListener.OnStartLoading();
            }
            forceLoad();
        }

        @Nullable
        @Override
        public synchronized JSONObject loadInBackground() {
            if(bundle == null){
                return null;
            }
            String urlAsString = bundle.getString("url");
            int loaderId = bundle.getInt("loaderId");

            String cityName = bundle.getString("name");
            JSONObject jsonObject = getDataFromDb();
            try {
                if (jsonObject != null && jsonObject.getString("name").equals(cityName)) {
                    try {
                        jsonObject.put("loaderId", loaderId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return jsonObject;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            URL url = null;
            try {
                url = new URL(urlAsString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            JSONObject result = null;
            if (url == null) {
                return result;
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection(); //open connection
                InputStream inputStream = connection.getInputStream(); // inputstream
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream); // read inputStream
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder builder = new StringBuilder();
                String line = reader.readLine(); // read rows
                while (line != null){ // if line not null we add line to builder
                    builder.append(line);
                    line = reader.readLine();
                }
//
                result = new JSONObject(builder.toString());
                saveData(result, cityName);


                result.put("loaderId", loaderId);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null){
                    connection.disconnect(); // disconnect
                }
            }
            return result;
        }
        void saveData(JSONObject jsonObject, String name){
            ContentValues contentValues = new ContentValues();
            contentValues.put(WeatherContract.CityEntry.COLUMN_NAME, name);
            long timeMilliseconds = date.getTime();
            contentValues.put(WeatherContract.CityEntry.COLUMN_DATE, timeMilliseconds + 86400);
            contentValues.put(WeatherContract.CityEntry.COLUMN_JSON, jsonObject.toString());
            database.insert(WeatherContract.CityEntry.TABLE_NAME, null, contentValues);
        }
        JSONObject getDataFromDb(){
            JSONObject jsonObjectsFromDB = null;
            Cursor cursor = database.query(WeatherContract.CityEntry.TABLE_NAME, null, null, null, null, null, null);
            if (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(WeatherContract.CityEntry.COLUMN_NAME));
                int dateOfSaving = cursor.getInt(cursor.getColumnIndex(WeatherContract.CityEntry.COLUMN_DATE));
                String json = cursor.getString(cursor.getColumnIndex(WeatherContract.CityEntry.COLUMN_JSON));

                if (dateOfSaving > date.getTime()) {
                    database.delete(WeatherContract.CityEntry.TABLE_NAME, "name = " + name, null);
                    return jsonObjectsFromDB;
                }
                try {
                    jsonObjectsFromDB = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonObjectsFromDB;
        }
    }
}
