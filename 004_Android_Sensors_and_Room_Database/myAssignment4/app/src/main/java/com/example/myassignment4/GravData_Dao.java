package com.example.myassignment4;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GravData_Dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(GravData obj);

    @Query("DELETE FROM grav_table")
    void deleteAll();

    @Query("SELECT * FROM grav_table")
    List<GravData> get_all();

    @Query("SELECT * FROM grav_table WHERE time > :val")
    List<GravData> get_result(Long val);



}


