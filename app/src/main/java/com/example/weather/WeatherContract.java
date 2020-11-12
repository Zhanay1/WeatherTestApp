package com.example.weather;

import android.provider.BaseColumns;

public class WeatherContract {
    public static final class CityEntry implements BaseColumns{
        public static final String TABLE_NAME = "cities";
        public static final String OPTIONS = "options";
        public static final String CODE = "code";
        public static final String CODE_VALUE = "value";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_JSON = "description";
        public static final String COLUMN_DATE = "date";

        public static final String TYPE_TEXT = "TEXT";
        public static final String TYPE_INTEGER = "INTEGER";


        public static final String CREATE_COMMAND = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" + _ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME +
                " " + TYPE_TEXT + ", " + COLUMN_DATE + " " + TYPE_INTEGER + ", " + COLUMN_JSON +
                " " + TYPE_TEXT + ")";

        public static final String CREATE_LAST_QUERY_TABLE = "CREATE TABLE IF NOT EXISTS " + OPTIONS +
                "(" + _ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, " + CODE +
                " " + TYPE_TEXT + ", " + CODE_VALUE + " " + TYPE_TEXT + ")";

        public static final String DROP_COMMAND = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String DROP_LAST_QUERY_TABLE = "DROP TABLE IF EXISTS " + OPTIONS;


    }
}
