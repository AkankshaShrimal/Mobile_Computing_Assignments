package com.example.myassignment4;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GPSData_Dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(GPSData obj);

    @Query("DELETE FROM Gps_table")
    void deleteAll();

    @Query("SELECT * FROM Gps_table")
    List<GPSData> get_all();

    @Query("SELECT * FROM Gps_table WHERE time > :val")
    List<GPSData> get_result(Long val);


}
