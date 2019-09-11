package com.monstercode.contacts;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class, Site.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ContactDao contactDao();
    public abstract SiteDao siteDao();
}
