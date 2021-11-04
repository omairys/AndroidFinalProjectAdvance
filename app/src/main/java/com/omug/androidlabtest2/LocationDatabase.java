package com.omug.androidlabtest2;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Location.class}, version = 1, exportSchema = false)
public abstract class LocationDatabase extends RoomDatabase {
    public abstract LocationDao personDao();

    private static LocationDatabase INSTANCE;


    /*
    Creating instance of database is quite costly so we will apply a Singleton Pattern to create
    and use already instantiated single instance for every database access.
     */

    public static /*synchronized*/ LocationDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocationDatabase.class,
                            "person_database").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
