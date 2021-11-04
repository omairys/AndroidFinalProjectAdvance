package com.omug.androidlabtest2;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LocationDao {
    @Query("SELECT * FROM Location")
    List<Location>  getAll();

    /*
     * Insert the object in database
     * @param person, object to be inserted
     */
    @Insert
    long insertPerson(Location location);

    /*
     * update the object in database
     * @param person, object to be updated
     */
    @Update
    void update(Location updLocation);

    /*
     * delete the object from database
     * @param person, object to be deleted
     */
    @Delete
    void delete(Location location);

}
