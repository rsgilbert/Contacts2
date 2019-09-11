package com.monstercode.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
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
    private DetailsAdapter detailsAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // adding divider
        DividerItemDecoration itemDecorator =  new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.divider));
        recyclerView.addItemDecoration(itemDecorator);

        getDetails();

        if(isNetworkAvailable()) {
            downloadDetails();
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
               detailsAdapter.searchFor(query);
               return true;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               detailsAdapter.searchFor(newText);
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

    // load contacts into DetailsAdapter
    private void getDetails() {
        Log.d(TAG, "getNames: Getting names");
        class GetDetails extends AsyncTask<Void, Void, List<Detail>> {
            @Override
            protected List<Detail> doInBackground(Void... voids) {
                AppDatabase appDatabase = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase();
                ContactDao contactDao = appDatabase.contactDao();
                SiteDao siteDao = appDatabase.siteDao();
                List<Contact> contactList = contactDao.getAll();
                List<Site> siteList = siteDao.getAll();
                return makeDetails(siteList, contactList);
            }
            @Override
            protected void onPostExecute(List<Detail> details) {
                super.onPostExecute(details);
                detailsAdapter = new DetailsAdapter(MainActivity.this, details);
                recyclerView.setAdapter(detailsAdapter);
            }
        }
        GetDetails getDetails = new GetDetails();
        getDetails.execute();
    }

    private List<Detail> makeDetails(List<Site> siteList, List<Contact> contactList) {
        List<Detail> detailList = new ArrayList<>();

        for(Contact contact: contactList) {
            Detail detail = new Detail();
            for(Site site: siteList) {
                if (site.getId()  == contact.getSiteId()) {
                    detail.setSiteId(site.getId());
                    detail.setSitename(site.getSitename());
                    detail.setCategory(site.getCategory());
                }
            }
            detail.setJob(contact.getJob());
            detail.setId(contact.getId());
            detail.setEmail(contact.getEmail());
            detail.setName(contact.getFirstname() + " " + contact.getLastname());
            detail.setTel1(contact.getTel1());
            detail.setTel2(contact.getTel2());

            detailList.add(detail);

        }
        return detailList;
    }

    // connect to ContactsAPI online, download contacts and put them into db
    private void downloadDetails() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();
        final DetailService detailService = retrofit.create(DetailService.class);

        // Http call to get all contacts
        Call<List<Contact>> contactsCall = detailService.getContacts();


        contactsCall.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                final List<Contact> contactsToUpdate = new ArrayList<>();
                contactsToUpdate.addAll(response.body());

                Call<List<Site>> sitesCall = detailService.getSites();
                sitesCall.enqueue(new Callback<List<Site>>() {
                    @Override
                    public void onResponse(Call<List<Site>> call, Response<List<Site>> response) {
                        Toast.makeText(MainActivity.this, "Connected: Downloaded sites", Toast.LENGTH_SHORT).show();
                        final List<Site> sitesToUpdate = new ArrayList<>();
                        sitesToUpdate.addAll(response.body());
                        saveDetails(sitesToUpdate, contactsToUpdate);
                    }

                    @Override
                    public void onFailure(Call<List<Site>> call, Throwable t) {

                    }
                });

            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });


    }

    private void saveDetails(final List<Site> sitesToUpdate, final List<Contact> contactsToUpdate) {
        // performing db operation in main thread will crash our app
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                // add to db
                AppDatabase appDatabase = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
                appDatabase.siteDao().insertAll(sitesToUpdate);
                appDatabase.contactDao().insertAll(contactsToUpdate);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d("PostExecute", "Saved contact");
                getDetails();
            }
        }

        SaveTask saveTask = new SaveTask();
        saveTask.execute();
    }



}
