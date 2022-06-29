package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class createContactActivity extends AppCompatActivity {

    TextView personName;
    TextView personNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        personName = findViewById(R.id.TextPersonName);
        personNumber = findViewById(R.id.TextPhone);
    }

    public void onSaveButtonClick(View view){
        Intent intent = new Intent(createContactActivity.this, contactCard.class);
        intent.putExtra("name", personName.getText().toString());
        intent.putExtra("phone", personNumber.getText().toString());
        startActivity(intent);
    }
}