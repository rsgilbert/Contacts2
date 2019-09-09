package com.monstercode.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static final String TAG = "MainActivity";
    private final String API_BASE_URL = "https://contactsapi01.herokuapp.com";
    private ContactsAdapter contactsAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getContacts();

        if(isNetworkAvailable()) {
            downloadContacts();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

       final MenuItem searchItem = menu.findItem(R.id.menu_search);
       searchView = (SearchView) searchItem.getActionView();
       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               contactsAdapter.searchFor(query);
               return true;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               contactsAdapter.searchFor(newText);
               return true;
           }
       });
        return true;
    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    // methods


    // check for network connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    // load contacts into ContactsAdapter
    private void getContacts() {
        Log.d(TAG, "getNames: Getting names");
        class GetContacts extends AsyncTask<Void, Void, List<Contact>> {
            @Override
            protected List<Contact> doInBackground(Void... voids) {
                AppDatabase appDatabase = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase();
                ContactDao contactDao = appDatabase.contactDao();
                List<Contact> contactList = contactDao.getAll();
                Log.d(TAG, "doInBackground: Finished getting contacts from database");
                return contactList;
            }
            @Override
            protected void onPostExecute(List<Contact> contacts) {
                super.onPostExecute(contacts);
                contactsAdapter = new ContactsAdapter(MainActivity.this, contacts);
                recyclerView.setAdapter(contactsAdapter);
                Log.d(TAG, "onPostExecute: Finished setting up contacts adapter");
            }
        }
        GetContacts getContacts = new GetContacts();
        getContacts.execute();
    }

    // connect to ContactsAPI online, download contacts and put them into db
    private void downloadContacts() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();
        ContactService contactService = retrofit.create(ContactService.class);

        // Http call to get all contacts
        Call<List<Contact>> call = contactService.getContacts();

        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                Toast.makeText(MainActivity.this, "Connected: Downloaded contacts", Toast.LENGTH_SHORT).show();
                List<Contact> contactsToUpdate = response.body();
                saveContacts(contactsToUpdate);
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void saveContacts(final List<Contact> contacts) {
        // performing db operation in main thread will crash our app
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                // add to db
                AppDatabase appDatabase = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
                ContactDao contactDao = appDatabase.contactDao();
                int updated = contactDao.updateAll(contacts);
                Log.d(TAG, "doInBackground: updated " + updated);
//                for (Contact contact: contacts) {
//                    int updatedContactsCount = contactDao.updateOne(contact);
//                    if(updatedContactsCount == 0) {
//                        contactDao.insertOne(contact);
//                    }
//                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Saved contacts", Toast.LENGTH_SHORT).show();
                Log.d("PostExecute", "Saved contact");
                getContacts();
            }
        }

        SaveTask saveTask = new SaveTask();
        saveTask.execute();
    }
}

