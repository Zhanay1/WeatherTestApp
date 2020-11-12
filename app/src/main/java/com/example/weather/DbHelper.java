package com.example.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "cities.db";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WeatherContract.CityEntry.CREATE_COMMAND);
        db.execSQL(WeatherContract.CityEntry.CREATE_LAST_QUERY_TABLE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(WeatherContract.CityEntry.CODE, "last");
        contentValues.put(WeatherContract.CityEntry.CODE_VALUE, (String) null);
        db.insert(WeatherContract.CityEntry.OPTIONS, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(WeatherContract.CityEntry.DROP_COMMAND);
        db.execSQL(WeatherContract.CityEntry.DROP_LAST_QUERY_TABLE);
        onCreate(db);
    }

}
