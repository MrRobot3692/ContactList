package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class contactListActivity extends AppCompatActivity {

    ListView listView;
    String[] listItems = new String[21];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart(){
        super.onStart();
        listView = findViewById(R.id.contactList);
        for(int i = 0; i<20; i++){
            listItems[i] = "Contact " + (i+1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(contactListActivity.this, contactCard.class);
                intent.putExtra("name", listView.getItemAtPosition(i).toString());
                intent.putExtra("phone", "88005553535");
                startActivity(intent);
            }
        });

    }

    public void onAddContactClick(View view) {
        Intent intent = new Intent(contactListActivity.this, createContactActivity.class);
        startActivity(intent);
    }
}