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

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateAll(List<Contact> contacts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Contact> contacts);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateOne(Contact contact);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOne(Contact contact);
}
