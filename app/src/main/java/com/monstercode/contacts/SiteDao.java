package com.monstercode.contacts;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SiteDao {
    @Query("SELECT * FROM sites")
    List<Site> getAll();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAll(List<Site> sites);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Site> sites);
}
