package com.monstercode.contacts;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contacts")
    List<Contact> getAll();


    // this query was not necessary in the long run
    @Query("SELECT * FROM contacts WHERE " +
            "firstname LIKE '%' || :searchWord || '%' OR " +
            "lastname LIKE '%' || :searchWord || '%' OR " +
            "tel LIKE '%' || :searchWord || '%' OR " +
            "designation LIKE '%' || :searchWord || '%'")
    List<Contact> query(String searchWord);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateAll(List<Contact> contacts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(Contact... contacts);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateOne(Contact contact);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOne(Contact contact);
}
