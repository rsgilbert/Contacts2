package com.monstercode.contacts;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddContactActivity extends AppCompatActivity {
    private EditText editFirstName, editLastName;
    private Button saveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_name);

        editFirstName = findViewById(R.id.firstName);
        editLastName = findViewById(R.id.lastName);
        saveButton = findViewById(R.id.saveButton);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContact();
            }
        });
    }

    private void saveContact() {
        final  String sFirstName = editFirstName.getText().toString().trim();
        final String sLastName = editLastName.getText().toString().trim();


        // performing db operation in main thread will crash our app
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                Contact contact = new Contact();
                contact.setFirstname(sFirstName);
                contact.setLastname(sLastName);
                contact.setDesignation("Pastor");
                contact.setEmail("black@mail.com");
                contact.setSite(1);
                contact.setTel("02832981");


                // add to db
                AppDatabase appDatabase = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
                ContactDao contactDao = appDatabase.contactDao();
                contactDao.insertOne(contact);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(AddContactActivity.this, "Saved Name ", Toast.LENGTH_SHORT).show();
                Log.d("PostExecute", "Saved name");
            }
        }

        SaveTask saveTask = new SaveTask();
        saveTask.execute();
    }
}
