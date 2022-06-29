package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Pattern p = Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
        Matcher m = p.matcher(contactPhone.getText().toString());
        if(m.matches()){
            Intent intent = new Intent(contactCard.this, createContactActivity.class);
            intent.putExtra("name", getIntent().getExtras().getString("name"));
            intent.putExtra("phone", getIntent().getExtras().getString("phone"));
            intent.putExtra("img", getIntent().getExtras().getString("img"));
            System.out.println("id contactCard: " + id);
            intent.putExtra("id", id);
            startActivity(intent);
        } else {
            Context context = null;
            new AlertDialog.Builder(context)
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete this entry?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}