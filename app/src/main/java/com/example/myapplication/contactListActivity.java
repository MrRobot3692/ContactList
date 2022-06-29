package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class contactListActivity extends AppCompatActivity {

    ListView listView;
    List<String> listItems = new ArrayList<>();
    public static List<Contacts> listContact = new ArrayList<>();
    Contacts[] contacts = new Contacts[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            if(listContact.size()<1){
                for(int i = 0; i < contacts.length; i++){
                    contacts[i] = new Contacts("Имя " + i, "88005553535", null);
                    listContact.add(contacts[i]);
                }
            }
            listView = findViewById(R.id.contactList);
            listItems.clear();
            for(int i = 0; i < listContact.size(); i++){
                listItems.add(listContact.get(i).getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, listItems);
            listView.setAdapter(adapter);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(contactListActivity.this, contactCard.class);
                intent.putExtra("name", listContact.get(i).getName());
                intent.putExtra("phone", listContact.get(i).getPhone());
                intent.putExtra("img", listContact.get(i).getImgURL());
                intent.putExtra("id", i);
                startActivity(intent);
            }
        });
    }

    public void onAddContactClick(View view) {
        Intent intent = new Intent(contactListActivity.this, createContactActivity.class);
        startActivity(intent);
    }
}