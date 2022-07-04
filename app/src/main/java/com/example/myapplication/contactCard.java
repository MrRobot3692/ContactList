package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class contactCard extends AppCompatActivity {

    TextView contactName;
    TextView contactPhone;
    ImageView imageView;
    private static int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_card);

        contactName = findViewById(R.id.contactName);
        contactPhone = findViewById(R.id.phoneNumber);
        imageView = findViewById(R.id.contactImage);

        id = getIntent().getIntExtra("id", -1);
        contactName.setText(getIntent().getExtras().getString("name"));
        contactPhone.setText(getIntent().getExtras().getString("phone"));
        if(getIntent().getByteArrayExtra("img") != null){
            imageView.setImageBitmap(convertByte.convertCompressedByteArrayToBitmap(getIntent().getByteArrayExtra("img")));
        }
    }

    public void onEditContactClick(View view){
        Intent intent = new Intent(contactCard.this, createContactActivity.class);

        intent.putExtra("id", id);
        intent.putExtra("name", getIntent().getExtras().getString("name"));
        intent.putExtra("phone", getIntent().getExtras().getString("phone"));
        intent.putExtra("img", getIntent().getByteArrayExtra("img"));

        startActivity(intent);
    }
}