package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dbActivity extends SQLiteOpenHelper {

    public static final int database_version = 2;
    public static String database_name = "contactsDB";
    public static String table_name = "contact";

    public static final String key_ID = "_id";
    public static final String key_name = "name";
    public static final String key_phone = "phone_number";
    public static final String key_image = "image";

    public dbActivity(@Nullable Context context) {
        super(context, database_name, null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + table_name + "("
                + key_ID + " integer primary key,"
                + key_name + " text,"
                + key_phone + " text,"
                + key_image + " blob"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + table_name);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int i, int i1){
        sqLiteDatabase.execSQL("drop table if exists " + table_name);
        onCreate(sqLiteDatabase);
    }
}
