package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class contactListActivity extends AppCompatActivity {

    private ListView listView;

    public static List<Contacts> listContact = new ArrayList<>();
    private final static List<String> listItems = new ArrayList<>();
    private final Contacts[] contacts = new Contacts[10];

    int nameIndex, phoneIndex, imageIndex;

    dbActivity sqlite;
    SQLiteDatabase database;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqlite = new dbActivity(this);
        database = sqlite.getWritableDatabase();
        cursor = database.query(dbActivity.table_name, null, null, null, null, null, null);

        if(cursor.moveToFirst() && listContact.size()<1){
            int loopIndex = 0;

            nameIndex = cursor.getColumnIndex(dbActivity.key_name);
            phoneIndex = cursor.getColumnIndex(dbActivity.key_phone);
            imageIndex = cursor.getColumnIndex(dbActivity.key_image);

            while (cursor.moveToNext()) {
                if(cursor.getBlob(imageIndex) != null)
                    contacts[loopIndex] = new Contacts(cursor.getString(nameIndex), cursor.getString(phoneIndex), cursor.getBlob(imageIndex));
                else
                    contacts[loopIndex] = new Contacts(cursor.getString(nameIndex), cursor.getString(phoneIndex), null);

                listContact.add(contacts[loopIndex]);
                loopIndex++;
            }
        }
        cursor.close();

        listView = findViewById(R.id.contactList);
        listItems.clear();
        for(int i = 0; i < listContact.size(); i++)
            listItems.add(listContact.get(i).getName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart(){
        super.onStart();

        listView.setOnItemClickListener((AdapterView<?> adapterView, View view, int i, long l) -> {
            Intent intent = new Intent(contactListActivity.this, contactCard.class);
            intent.putExtra("name", listContact.get(i).getName());
            intent.putExtra("phone", listContact.get(i).getPhone());
            intent.putExtra("img", listContact.get(i).getImgByte());
            intent.putExtra("id", i);
            startActivity(intent);
        });
    }

    public void onAddContactClick(View view) {
        Intent intent = new Intent(contactListActivity.this, createContactActivity.class);
        startActivity(intent);
    }
}