package com.omug.androidfinalprojectadvance;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LocationDao {
    @Query("SELECT * FROM Location")
    List<Location> getAll();

    @Query("SELECT * FROM Location WHERE locationTitle LIKE :title")
    List<Location> getOne(String title);

    /*
     * Insert the object in database
     * @param location, object to be inserted
     */
    @Insert
    long insertLocation(Location location);

    /*
     * update the object in database
     * @param location, object to be updated
     */
    @Update
    void update(Location updLocation);

    /*
     * delete the object from database
     * @param location, object to be deleted
     */
    @Delete
    void delete(Location location);

}
