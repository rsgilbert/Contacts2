package com.monstercode.contacts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface DetailService {
    @GET("/contacts/")
    public Call<List<Contact>> getContacts ();

    @GET("/contacts/{contactId}")
    public Call<Contact> getContact(
            @Path("contactId") int contactId
    );
    @GET("/sites")
    public Call<List<Site>> getSites();
}

