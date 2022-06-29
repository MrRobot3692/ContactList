package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class createContactActivity extends AppCompatActivity {

    TextView contactName;
    TextView contactPhone;
    ImageView imageView;
    static final int GALLERY_REQUEST = 1;
    Bitmap bitmap = null;
    int id = -1;
    Contacts contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        contactName = findViewById(R.id.TextPersonName);
        contactPhone = findViewById(R.id.TextPhone);
        imageView = findViewById(R.id.contactImage);
        id = getIntent().getIntExtra("id", -1);

        try{
            contactName.setText(getIntent().getExtras().getString("name"));
            contactPhone.setText(getIntent().getExtras().getString("phone"));
            if(getIntent().getParcelableExtra("img") != null){
                imageView.setImageBitmap(getIntent().getParcelableExtra("img"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onSaveButtonClick(View view){
        boolean result = contactPhone.getText().toString().matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
        if(result){
            contactListActivity contactListsActivity = new contactListActivity();
            contact = new Contacts(contactName.getText().toString(), contactPhone.getText().toString(), bitmap);

            if(id != -1){
                contactListsActivity.listContact.set(id, contact);
            } else {
                contactListsActivity.listContact.add(contact);
            }

            Intent intent = new Intent(createContactActivity.this, contactListActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Номер телефона введен неверно", Toast.LENGTH_SHORT).show();
        }
    }

    public void onAddImageClick(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(bitmap);
                }
        }
    }
}