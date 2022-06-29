package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class contactCard extends AppCompatActivity {

    TextView contactName;
    TextView contactPhone;
    ImageView imageView;
    int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_card);
        contactName = findViewById(R.id.contactName);
        contactPhone = findViewById(R.id.phoneNumber);
        imageView = findViewById(R.id.contactImage);
        contactName.setText(getIntent().getExtras().getString("name"));
        contactPhone.setText(getIntent().getExtras().getString("phone"));
        if(getIntent().getParcelableExtra("img") != null){
            imageView.setImageBitmap(getIntent().getParcelableExtra("img"));
        }
        id = getIntent().getIntExtra("id", -1);
    }

    public void onEditContactClick(View view){
        Intent intent = new Intent(contactCard.this, createContactActivity.class);
        intent.putExtra("name", getIntent().getExtras().getString("name"));
        intent.putExtra("phone", getIntent().getExtras().getString("phone"));
        intent.putExtra("img", getIntent().getExtras().getString("img"));
        System.out.println("id contactCard: " + id);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}