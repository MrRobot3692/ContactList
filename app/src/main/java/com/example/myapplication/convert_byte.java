package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class convert_byte {
    public static byte[] convertBitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        }finally {
            if(baos != null){
                try {
                    baos.close();
                } catch (IOException e) {
                    //Log.e(BitmapUtils.class.getSimpleName(), "ByteArrayOutputStream was not closed");
                }
            }
        }
    }
    public static byte[] convertBitmapToByteArrayUncompressed(Bitmap bitmap){
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(byteBuffer);
        byteBuffer.rewind();
        return byteBuffer.array();
    }
    public static Bitmap convertCompressedByteArrayToBitmap(byte[] src){
        return BitmapFactory.decodeByteArray(src, 0, src.length);
    }
}
