package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class contactListActivity extends AppCompatActivity {

    ListView listView;
    String[] listItems = new String[20];

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

    }

    public void onAddContactClick(View view)
    {
        System.out.println("hello");
    }
}