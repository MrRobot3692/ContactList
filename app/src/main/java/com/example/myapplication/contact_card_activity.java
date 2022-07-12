package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class contact_card_activity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    TextView contactName;
    TextView contactPhone;
    ImageView imageView;
    private static int id = -1;

    database_controller sqlite;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_card);

        contactName = findViewById(R.id.contactName);
        contactPhone = findViewById(R.id.phoneNumber);
        imageView = findViewById(R.id.contactImage);

        sqlite = new database_controller(this);
        database = sqlite.getWritableDatabase();

        id = getIntent().getIntExtra("id", -1);
        contactName.setText(getIntent().getExtras().getString("name"));
        contactPhone.setText(getIntent().getExtras().getString("phone"));
        if(getIntent().getByteArrayExtra("img") != null){
            try{
                imageView.setImageBitmap(convert_byte.convertCompressedByteArrayToBitmap(getIntent().getByteArrayExtra("img")));
            } catch (Exception e){}
        }
    }

    public void onEditContactClick(View view){
        Intent intent = new Intent(contact_card_activity.this, create_contact_activity.class);

        intent.putExtra("id", id);
        System.out.println(id);
        intent.putExtra("name", getIntent().getExtras().getString("name"));
        intent.putExtra("phone", getIntent().getExtras().getString("phone"));
        intent.putExtra("img", getIntent().getByteArrayExtra("img"));

        startActivity(intent);
    }

    public void onDeleteContactClick(View view){
        database.delete(database_controller.table_name, database_controller.key_name + "=?", new String[]{getIntent().getExtras().getString("name")});
        contact_list_activity.listContact.set(id, null);
        System.out.println(id);
        Intent intent = new Intent(contact_card_activity.this, contact_list_activity.class);
        startActivity(intent);
    }

    public void onCallClick(View view) throws SecurityException{
        String toDial = "tel:"+contactPhone.getText().toString();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
        } else startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(toDial)));
    }
}