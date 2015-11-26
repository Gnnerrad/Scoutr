package com.example.darre_000.scoutr;

import android.provider.BaseColumns;

/**
 * Created by charlespierse on 26/11/15.
 */
public final class ScoutrDBContract {
    // basis for building DB was taken from tutorial on android site from http://developer.android.com/training/basics/data-storage/databases.html
    public static final int DB_VERSION  =1;
    public static final String DB_NAME = "scoutrDB.db";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String COMMA_SEP          = ",";

    private ScoutrDBContract() {}

    public static abstract class Table implements BaseColumns{
        public static final String TABLE_NAME = "locationInfoTable";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                LATITUDE + TEXT_TYPE + COMMA_SEP +
                LONGITUDE + TEXT_TYPE + COMMA_SEP + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
