package com.example.darre_000.scoutr;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ScoutrDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "scoutr.db";
    public static final String TABLE_NAME = "location_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "LAT";
    public static final String COL_4 = "LONG";
    public static final String COL_5 = "WC";
    public static final String COL_6 = "WIFI";
    public static final String COL_7 = "POWER";
    public static final String COL_8 = "ACCESSIBILITY";
    public static final String COL_9 = "SUNLIGHT";
    public static final String COL_10 = "FILEPATH";

    public ScoutrDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY," +
//                "NAME TEXT,LAT TEXT,LONG TEXT,WC BOOLEAN,WIFI BOOLEAN,POWER BOOLEAN" +
//                "ACCESSIBILITY BOOLEAN,SUNLIGHT BOOLEAN)");
        db.execSQL("create table " + TABLE_NAME + " (ID TEXT PRIMARY KEY," +
                                                    "NAME TEXT," +
                                                    "LAT TEXT," +
                                                    "LONG TEXT," +
                                                    "WC BOOLEAN," +
                                                    "WIFI BOOLEAN," +
                                                    "POWER BOOLEAN," +
                                                    "ACCESSIBILITY BOOLEAN," +
                                                    "SUNLIGHT BOOLEAN," +
                                                    "FILEPATH TEXT" +
                                                    ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String id, String name,String lat, String lng, String wc,
                              String wifi,String power, String access,String sunlight,
                              String filepath){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,lat);
        contentValues.put(COL_4,lng);
        contentValues.put(COL_5,wc);
        contentValues.put(COL_6,wifi);
        contentValues.put(COL_7,power);
        contentValues.put(COL_8,access);
        contentValues.put(COL_9,sunlight);
        contentValues.put(COL_10,filepath);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateData(String id, String name,String lat, String lng, String wc,
                              String wifi,String power, String access,String sunlight,
                              String filepath){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,lat);
        contentValues.put(COL_4,lng);
        contentValues.put(COL_5,wc);
        contentValues.put(COL_6,wifi);
        contentValues.put(COL_7,power);
        contentValues.put(COL_8,access);
        contentValues.put(COL_9,sunlight);
        contentValues.put(COL_10,filepath);
        long result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID = ?",new String[] { id });
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "  + TABLE_NAME, null);
        return res;
    }
}