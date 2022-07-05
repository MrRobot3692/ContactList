package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class createContactActivity extends AppCompatActivity {

    private TextView contactName;
    private TextView contactPhone;
    private ImageView imageView;

    private static int id = -1;
    private byte[] image;

    dbActivity sqlite;
    Contacts contact;

    private SQLiteDatabase database;
    private final ContentValues contentValues = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        contactName = findViewById(R.id.TextPersonName);
        contactPhone = findViewById(R.id.TextPhone);
        imageView = findViewById(R.id.contactImage);

        sqlite = new dbActivity(this);
        database = sqlite.getWritableDatabase();

        try{
            id = getIntent().getIntExtra("id", -1);
            contactName.setText(getIntent().getExtras().getString("name"));
            contactPhone.setText(getIntent().getExtras().getString("phone"));
            if(getIntent().getByteArrayExtra("img") != null)
                imageView.setImageBitmap(convertByte.convertCompressedByteArrayToBitmap(getIntent().getByteArrayExtra("img")));
        } catch (Exception e){
            //e.printStackTrace();
        }
    }

    public void onSaveButtonClick(View view){

        boolean result = contactPhone.getText().toString().matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
        if(!result){
            Toast.makeText(this, "Номер телефона введен неверно", Toast.LENGTH_SHORT).show();
            return;
        }
        //contactListActivity.listContact.add(new Contacts(contactName.getText().toString(), contactPhone.getText().toString(), image));
        contact = new Contacts(contactName.getText().toString(), contactPhone.getText().toString(), image);

        contentValues.put(dbActivity.key_name, contactName.getText().toString());
        contentValues.put(dbActivity.key_phone, contactPhone.getText().toString());
        contentValues.put(dbActivity.key_image, image);

        if(id != -1){
            database.update(dbActivity.table_name, contentValues, dbActivity.key_name + " = ?", new String[]{getIntent().getExtras().getString("name")});
            contactListActivity.listContact.set(id, contact);
        } else {
            database.insert(dbActivity.table_name, null, contentValues);
            contactListActivity.listContact.add(contact);
        }

        database.close();

        Intent intent = new Intent(createContactActivity.this, contactListActivity.class);
        startActivity(intent);
    }

    public void onAddImageClick(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        mStartForResult.launch(photoPickerIntent);
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() != RESULT_OK)
                return;
            Uri selectedImage = result.getData().getData();
            try {
                image = convertByte.convertBitmapToByteArray(MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage));
                imageView.setImageBitmap(convertByte.convertCompressedByteArrayToBitmap(image));
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    });
}