package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class contactCard extends AppCompatActivity {

    TextView contactName;
    TextView contactPhone;
    ImageView imageView;
    private static int id = -1;

    dbActivity sqlite;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_card);

        contactName = findViewById(R.id.contactName);
        contactPhone = findViewById(R.id.phoneNumber);
        imageView = findViewById(R.id.contactImage);

        sqlite = new dbActivity(this);
        database = sqlite.getWritableDatabase();

        id = getIntent().getIntExtra("id", -1);
        contactName.setText(getIntent().getExtras().getString("name"));
        contactPhone.setText(getIntent().getExtras().getString("phone"));
        if(getIntent().getByteArrayExtra("img") != null){
            try{
                imageView.setImageBitmap(convertByte.convertCompressedByteArrayToBitmap(getIntent().getByteArrayExtra("img")));
            } catch (Exception e){}
        }
    }

    public void onEditContactClick(View view){
        Intent intent = new Intent(contactCard.this, createContactActivity.class);

        intent.putExtra("id", id);
        System.out.println(id);
        intent.putExtra("name", getIntent().getExtras().getString("name"));
        intent.putExtra("phone", getIntent().getExtras().getString("phone"));
        intent.putExtra("img", getIntent().getByteArrayExtra("img"));

        startActivity(intent);
    }

    public void onDeleteContactClick(View view){
        database.delete(dbActivity.table_name, dbActivity.key_name + "=?", new String[]{getIntent().getExtras().getString("name")});
        contactListActivity.listContact.set(id, null);
        System.out.println(id);
        Intent intent = new Intent(contactCard.this, contactListActivity.class);
        startActivity(intent);
    }

    public void onCallClick(View view) throws SecurityException{
        String toDial="tel:"+contactPhone.getText().toString();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(toDial));
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
    }
}