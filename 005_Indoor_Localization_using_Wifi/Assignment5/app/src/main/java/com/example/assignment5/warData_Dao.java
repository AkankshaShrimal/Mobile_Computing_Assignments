package com.example.assignment5;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface warData_Dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(warData obj);

    @Query("DELETE FROM War_table")
    void deleteAll();

    @Query("SELECT * FROM War_table")
    List<warData> get_all();


}
