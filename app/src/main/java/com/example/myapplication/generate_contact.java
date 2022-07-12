package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class generate_contact {
    database_controller sqlite;

    private int a = 0, b = 10;
    private byte[] image;

    private SQLiteDatabase database;
    private final ContentValues contentValues = new ContentValues();

    generate_contact(Context context){
        sqlite = new database_controller(context);
        database = sqlite.getWritableDatabase();
        database.delete(database_controller.table_name, null, null);
        for(int i = 0; i < 30; i++){
            contentValues.put(database_controller.key_name, generateFirstName((int) (Math.random()*(b-a)+a)));
            contentValues.put(database_controller.key_phone, generatePhoneNumber());
            contentValues.put(database_controller.key_image, image);
            database.insert(database_controller.table_name, null, contentValues);
            contentValues.clear();
        }
        database.close();
    }

    private String generateFirstName(int i){
        String fullName;
        switch (i){
            case 1:
                fullName = "Степан";
                break;
            case 2:
                fullName = "Анастасия";
                break;
            case 3:
                fullName = "Владимир";
                break;
            case 4:
                fullName = "Вероника";
                break;
            case 5:
                fullName = "Вера";
                break;
            case 6:
                fullName = "Ева";
                break;
            case 7:
                fullName = "Юрий";
                break;
            case 8:
                fullName = "Даниил";
                break;
            case 9:
                fullName = "Максим";
                break;
            case 10:
                fullName = "Алексей";
                break;
            default:
                fullName = "Арина";
                break;
        }
        if(fullName.endsWith("а") || fullName.endsWith("я"))
            fullName = fullName + " " + genetateLastName((int) (Math.random()*(b-a)+a)) + "а";
        else
            fullName = fullName + " " + genetateLastName((int) (Math.random()*(b-a)+a));
        return fullName;
    }

    private String genetateLastName(int i){
        String lastName;
        switch (i){
            case 1:
                lastName = "Сергеев";
                break;
            case 2:
                lastName = "Соболев";
                break;
            case 3:
                lastName = "Егоров";
                break;
            case 4:
                lastName = "Серов";
                break;
            case 5:
                lastName = "Виноградов";
                break;
            case 6:
                lastName = "Соколов";
                break;
            case 7:
                lastName = "Маслов";
                break;
            case 8:
                lastName = "Воронцов";
                break;
            case 9:
                lastName = "Дроздов";
                break;
            case 10:
                lastName = "Кузнецов";
                break;
            default:
                lastName = "Малинин";
                break;
        }
        return lastName;
    }
    private String generatePhoneNumber(){
        String number = "+7" + "(" + (int)(Math.random()*1000) + ")" + (int)(Math.random()*1000) + "-" + (int)(Math.random()*100) + "-" + (int)(Math.random()*100);
        return number;
    }
}
