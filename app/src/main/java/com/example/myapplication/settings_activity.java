package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class settings_activity extends AppCompatActivity {
    ListView listView;

    private static final int _export = 0, _import = 1, _clear = 2;
    private static final String[] list = {"Экспорт контактов", "Импорт контактов", "Очистить список контактов"};

    database_controller sqlite;

    private SQLiteDatabase database;
    private final ContentValues contentValues = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        listView = findViewById(R.id.settingsList);

        sqlite = new database_controller(this);
        database = sqlite.getWritableDatabase();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart(){
        super.onStart();
        listView.setOnItemClickListener((AdapterView<?> adapterView, View view, int i, long l) -> {
            String[] permissions;
            switch (i){
                case _export:
                    permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions, _export);
                    break;
                case _import:
                    permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, _import);
                    break;
                case _clear:
                    contact_list_activity.listContact.clear();
                    database.delete(database_controller.table_name, null, null);
                    Toast.makeText(getApplicationContext(), "Список контактов очищен", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(settings_activity.this, contact_list_activity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        });
    }

    ActivityResultLauncher<Intent> exportActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() != RESULT_OK)
                return;
            Uri uri = result.getData().getData();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String fileName = "contact " + dtf.format(now) + ".dat";
            fileName = fileName.replace(":", "-");
            fileName = fileName.replace(" ", "_");
            fileName = fileName.replace("/", "_");
            try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getFilePath(uri) + "/" + fileName))) {
                System.out.println(getFilePath(uri) + "/" + fileName);
                oos.writeObject(contact_list_activity.listContact);
                Toast.makeText(getApplicationContext(), "Экспорт успешно завершен", Toast.LENGTH_SHORT).show();
            } catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    });

    ActivityResultLauncher<Intent> importActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() != RESULT_OK)
                return;
            Uri uri = result.getData().getData();
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getFilePath(uri)))) {
                List<contacts> listContact = (List<contacts>) ois.readObject();
                for(int i = 0; i<listContact.size(); i++){
                    if (listContact.get(i).name == null)
                        return;
                    contentValues.put(database_controller.key_name, listContact.get(i).name);
                    contentValues.put(database_controller.key_phone, listContact.get(i).phone);
                    contentValues.put(database_controller.key_image, listContact.get(i).imgByte);
                    database.insert(database_controller.table_name, null, contentValues);
                    contact_list_activity.listContact.add(listContact.get(i));
                    contentValues.clear();
                    System.out.println(listContact.get(i).getName() + ": Импортирован успешно");
                }
                Intent intent = new Intent(settings_activity.this, contact_list_activity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Импорт успешно завершен", Toast.LENGTH_SHORT).show();
            } catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent intent;
        switch (requestCode) {
            case _export:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    break;
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    StorageManager sm = (StorageManager) getApplicationContext().getSystemService(Context.STORAGE_SERVICE);
                    intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
                    String startDir = "Documents";
                    Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");
                    String scheme = uri.toString();
                    scheme = scheme.replace("/root/", "/document/");
                    scheme += "%3A" + startDir;
                    uri = Uri.parse(scheme);
                    intent.putExtra("android.provider.extra.INITIAL_URI", uri);
                    exportActivity.launch(intent);
                }
                break;
            case _import:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT)
                            .addCategory(Intent.CATEGORY_OPENABLE)
                            .setType("*/*");
                    importActivity.launch(intent);
                }
                break;
        }
    }

    public static String getFilePath(final Uri uri ) {
        File file = new File(uri.getPath());
        String path = file.getAbsolutePath();
        int cut = path.lastIndexOf(":");
        if(cut != -1)
            path = "/storage/emulated/0/" + path.substring(cut + 1);
        return path;
    }
}