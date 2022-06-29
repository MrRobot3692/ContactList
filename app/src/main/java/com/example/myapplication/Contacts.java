package com.example.myapplication;

import android.graphics.Bitmap;

public class Contacts {
    String name;
    String phone;
    Bitmap imgBitmap;

    Contacts(String name, String phone, Bitmap imgBitmap){
        this.name = name;
        this.phone = phone;
        this.imgBitmap = imgBitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Bitmap getImgURL() {
        return imgBitmap;
    }

    public void setImgURL(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }
}
