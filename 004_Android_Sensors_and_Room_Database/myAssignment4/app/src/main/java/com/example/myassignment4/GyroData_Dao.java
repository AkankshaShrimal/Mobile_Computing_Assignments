package com.example.myassignment4;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GyroData_Dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(GyroData obj);

    @Query("DELETE FROM gyro_table")
    void deleteAll();

    @Query("SELECT * FROM gyro_table")
    List<GyroData> get_all();

    @Query("SELECT * FROM gyro_table WHERE time > :val")
    List<GyroData> get_result(Long val);



}
