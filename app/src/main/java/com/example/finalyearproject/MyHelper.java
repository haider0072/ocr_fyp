package com.example.finalyearproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

import androidx.annotation.Nullable;

public class MyHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Information.db";
    public static final String TABLE_name = "info";
    public static final String storeName = "storeName";
    public static final String storeDateDay = "storeDateDay";
    public static final String storeDateMonth = "storeDateMonth";
    public static final String storeDateYear = "storeDateYear";
    public static final String storePrice = "storePrice";

    public MyHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_name + "(storeName TEXT,storeDateDay TEXT,storeDateMonth TEXT,storeDateYear TEXT,storePrice REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_name);
        onCreate(db);
    }

    public Boolean insertData(String storeName, String storeDateDay, String storeDateMonth, String storeDateYear,String storePrice){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyHelper.storeName,storeName);
        contentValues.put(MyHelper.storeDateDay, storeDateDay);
        contentValues.put(MyHelper.storeDateMonth, storeDateMonth);
        contentValues.put(MyHelper.storeDateYear, storeDateYear);
        contentValues.put(MyHelper.storePrice,storePrice);
        sqLiteDatabase.insert(TABLE_name,null,contentValues);
        return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT * FROM info",null);
        return data;
    }

    public Cursor getNameBaseData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("select storeName, sum(storePrice) from info group by storeName",null);
        return data;
    }

    public Cursor getMonthBaseData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("select storeDateMonth, sum(storePrice) from info group by storeDateMonth",null);
        return data;
    }

}
