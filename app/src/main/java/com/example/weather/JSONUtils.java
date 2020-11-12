package com.example.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils {
    private static final String KEY_NAME = "name";
    private static final String KEY_TEMP = "temp";
    private static final String KEY_DESCRIPTION = "description";

    public static List<Weather> getWeatherFromJSON(JSONObject jsonObject){ //  we get array
        List<Weather> result = new ArrayList<>();
        if(jsonObject == null){
            return result;
        }
        try {
                String name = jsonObject.getString(KEY_NAME);
                String temp = jsonObject.getJSONObject("main").getString(KEY_TEMP);
                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                JSONObject weather = jsonArray.getJSONObject(0);
                String description = weather.getString(KEY_DESCRIPTION);
                Weather weathers = new Weather(name, temp, description);
                result.add(weathers);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
