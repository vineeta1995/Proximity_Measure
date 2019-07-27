package com.example.vineeta.proximitymeasure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by vineeta on 13-04-2018.
 */

public class databaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "fingerPrinting";
    public static final String TABLE_NAME = "fingerPrint";
    public static final String place = "place";
    public static final String signal = "signal";
    public static final String distance = "distance";

    public databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (place TEXT PRIMARY KEY ,signal TEXT,distance TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String Place,String Signal ,String Distance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(place,Place);
        contentValues.put(signal,Signal);
        contentValues.put(distance,Distance);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from  fingerPrint order BY distance asc",null);
        return res;
    }

    public Cursor getData(int sign1, int sign2 ) {
        String TYPE=signal;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + signal +" , " + distance + " FROM " + TABLE_NAME + " where signal BETWEEN " + sign1 + " and " +  sign2 ,null);
        return res;
    }

    public Integer deleteData (String Place) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "place = ?",new String[] {Place});
    }
}