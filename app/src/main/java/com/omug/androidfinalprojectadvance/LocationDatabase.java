package com.omug.androidfinalprojectadvance;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Location.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
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

    public  void cleanUp(){
        INSTANCE = null;
    }
}
