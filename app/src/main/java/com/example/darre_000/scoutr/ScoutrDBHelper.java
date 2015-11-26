package com.example.darre_000.scoutr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by charlespierse on 26/11/15.
 */
public class ScoutrDBHelper extends SQLiteOpenHelper {

    public ScoutrDBHelper(Context context){
        super(context, ScoutrDBContract.DB_NAME,null,ScoutrDBContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScoutrDBContract.Table.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ScoutrDBContract.Table.DELETE_TABLE);
        onCreate(db);
    }
}
