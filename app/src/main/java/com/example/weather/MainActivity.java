package com.example.weather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private RecyclerView recyclerView;
    private WeatherAdapter adapter;
    private LoaderManager loaderManager;
    private EditText editText;
    private static String q;
    private static boolean isLoading = false;
    private static final int LOADER_ID = 12;
    private static int page = 1;
    private City city = new City();
    private SharedPreferences preferences;
    private List<String> cities = new ArrayList<String>();
    private static Context mContext;
    private Cursor cursor;

    public static Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        MainActivity.mContext = mContext;
    }
    private SharedPreferences sharedPreferences;

    private DbHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dbHelper = new DbHelper(this);
        database = dbHelper.getWritableDatabase();
        NetworkUtils.setDatabase(database);
        editText = findViewById(R.id.editTextSearch);
        loaderManager = LoaderManager.getInstance(this);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new WeatherAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(adapter);

        q = sharedPreferences.getString("value", "DEFAULT");
        adapter.setQ(q);
        if (q != null)
            DownloadData();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isLoading) {
                    page = 1;
                    q = s.toString();
                    if (q.length() >= 3) {
                        adapter.setQ(q);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("value", q);
                        editor.commit();

                        DownloadData();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }


    private void DownloadData() {
        List<String> searchResult = city.searchCities(q);
        for (int i = 0;i < searchResult.size(); i++) {

            if (checkInternet()) {
                URL url = NetworkUtils.buildSearchURl(searchResult.get(i));
                Bundle bundle = new Bundle();
                bundle.putString("url", url.toString());
                bundle.putInt("loaderId", i);
                bundle.putString("name", searchResult.get(i));
                loaderManager.restartLoader(i, bundle, this);
            } else {
                Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkInternet() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else {
            connected = false;
        }
        return connected;
    }



    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(this, args);
        jsonLoader.setOnStartLoadingListener(new NetworkUtils.JSONLoader.OnStartLoadingListener() {
            @Override
            public void OnStartLoading() {
                isLoading = true;
            }
        });
        return jsonLoader;
    }

    @Override
    public synchronized void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        List<Weather> weathers = JSONUtils.getWeatherFromJSON(data);
//        saveData(weathers);
        if(weathers != null && !weathers.isEmpty()) {
            if(page == 1) {
                adapter.setCity(weathers);
            }else{
                adapter.addCity(weathers);
            }
            page++;
        }

        try {
            int loaderId = data.getInt("loaderId");
            loaderManager.destroyLoader(loaderId);
            isLoading = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
//    public void saveData(List<Weather> weathers){
//        for(Weather weather: weathers){
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(WeatherContract.CityEntry.COLUMN_NAME, weather.getName());
//            contentValues.put(WeatherContract.CityEntry.C, weather.getTemp());
//            contentValues.put(WeatherContract.CityEntry.COLUMN_DESCRIPTION, weather.getDescription());
//            database.insert(WeatherContract.CityEntry.TABLE_NAME, null, contentValues);
//        }
//    }
//    public List<Weather> getDataFromDb(){
//        List<Weather> weathersFromDB = new ArrayList<>();
//        Cursor cursor = database.query(WeatherContract.CityEntry.TABLE_NAME, null, null, null, null, null, null);
//        while (cursor.moveToNext()){
//            String name = cursor.getString(cursor.getColumnIndex(WeatherContract.CityEntry.COLUMN_NAME));
//            String temp = cursor.getString(cursor.getColumnIndex(WeatherContract.CityEntry.COLUMN_TEMP));
//            String description = cursor.getString(cursor.getColumnIndex(WeatherContract.CityEntry.COLUMN_DESCRIPTION));
//            Weather weather = new Weather(name, temp, description);
//            weathersFromDB.add(weather);
//        }
//        return weathersFromDB;
//    }
}
