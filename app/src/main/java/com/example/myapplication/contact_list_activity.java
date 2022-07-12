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
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class contact_list_activity extends AppCompatActivity {

    ListView listView;
    SearchView searchView;

    public static List<com.example.myapplication.contacts> listContact = new ArrayList<>();
    private final static List<String> listItems = new ArrayList<>();
    private final static int contactListSize = 255;
    private final com.example.myapplication.contacts[] contacts = new contacts[contactListSize];

    int nameIndex, phoneIndex, imageIndex;

    database_controller sqlite;
    SQLiteDatabase database;
    Cursor cursor;
    generate_contact generate;

    private boolean generateNewDB = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(generateNewDB)
            generate = new generate_contact(this);
        generateNewDB = false;

        listView = findViewById(R.id.contactList);
        searchView = findViewById(R.id.search);

        sqlite = new database_controller(this);
        database = sqlite.getWritableDatabase();
        cursor = database.query(database_controller.table_name, null, null, null, null, null, null);

        if(cursor.moveToFirst() && listContact.size()<1){
            int loopIndex = 0;

            nameIndex = cursor.getColumnIndex(database_controller.key_name);
            phoneIndex = cursor.getColumnIndex(database_controller.key_phone);
            imageIndex = cursor.getColumnIndex(database_controller.key_image);

            while (cursor.moveToNext()) {
                if(loopIndex>=contactListSize)
                    break;
                if(cursor.getBlob(imageIndex) != null)
                    contacts[loopIndex] = new contacts(cursor.getString(nameIndex), cursor.getString(phoneIndex), cursor.getBlob(imageIndex));
                else
                    contacts[loopIndex] = new contacts(cursor.getString(nameIndex), cursor.getString(phoneIndex), null);
                listContact.add(contacts[loopIndex]);
                loopIndex++;
            }
        }
        cursor.close();

        listItems.clear();
        for(int i = 0; i < listContact.size(); i++)
            if(listContact.get(i) != null)
                listItems.add(listContact.get(i).getName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onStart(){
        super.onStart();
        listView.setOnItemClickListener((AdapterView<?> adapterView, View view, int i, long l) -> {
            Intent intent = new Intent(contact_list_activity.this, contact_card_activity.class);
            for(int j = 0; j < listContact.size(); j++){
                try {
                    if(listContact.get(j).getName().equals(listItems.get(i))) {
                        intent.putExtra("name", listContact.get(j).getName());
                        intent.putExtra("phone", listContact.get(j).getPhone());
                        intent.putExtra("img", listContact.get(j).getImgByte());
                        intent.putExtra("id", j);
                        startActivity(intent);
                        break;
                    }
                } catch (Exception e){}
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });
    }

    public void search(String searchText){
        listItems.clear();
        for(int i = 0; i < listContact.size(); i++)
            if(listContact.get(i) != null && listContact.get(i).name.contains(searchText))
                listItems.add(listContact.get(i).getName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
    }

    public void onAddContactClick(View view) {
        Intent intent = new Intent(contact_list_activity.this, create_contact_activity.class);
        startActivity(intent);
    }

    public void onSettingsClick(View view) {
        Intent intent = new Intent(contact_list_activity.this, settings_activity.class);
        startActivity(intent);
    }
}