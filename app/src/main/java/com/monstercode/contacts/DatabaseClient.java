package com.monstercode.contacts;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseClient {
    private Context context;
    private static DatabaseClient mInstance;

    // db object
    private AppDatabase appDatabase;

    private DatabaseClient(Context context) {
        this.context = context;
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "details")
//                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
//                .allowMainThreadQueries()
                .build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new DatabaseClient(context);
        }
        return mInstance;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            // create table Site
            String sql = "CREATE TABLE sites (id INTEGER, name TEXT, " +
                    "category TEXT, PRIMARY KEY(id))";
            database.execSQL(sql);

            // drop table contacts
            sql = "DROP TABLE contacts";
            database.execSQL(sql);

            sql = "CREATE TABLE contacts (id INTEGER NOT NULL PRIMARY KEY, firstname TEXT, lastname TEXT, tel1 TEXT, tel2 TEXT, " +
                    " email TEXT, siteId INTEGER NOT NULL, job TEXT)";
            database.execSQL(sql);
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // drop table sites
            String sql = "DROP TABLE sites";
            database.execSQL(sql);

            // create table Site
            sql = "CREATE TABLE sites (id INTEGER, name TEXT, " +
                    "category TEXT, PRIMARY KEY(id))";
            database.execSQL(sql);

            // drop table contacts
            sql = "DROP TABLE contacts";
            database.execSQL(sql);

            sql = "CREATE TABLE contacts (id INTEGER NOT NULL PRIMARY KEY, firstname TEXT, lastname TEXT, tel1 TEXT, tel2 TEXT, " +
                    " email TEXT, siteId INTEGER NOT NULL, job TEXT)";
            database.execSQL(sql);
        }
    };

    public AppDatabase getAppDatabase () {
        return appDatabase;
    }
}
