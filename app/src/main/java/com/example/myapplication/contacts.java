package com.example.myapplication;

import java.io.Serializable;

public class contacts implements Serializable {
    String name;
    String phone;
    byte[] imgByte;

    contacts(String name, String phone, byte[] imgByte){
        this.name = name;
        this.phone = phone;
        this.imgByte = imgByte;
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

    public byte[] getImgByte() {
        return imgByte;
    }

    public void setImgByte(byte[] imgBitmap) {
        this.imgByte = imgBitmap;
    }
}
