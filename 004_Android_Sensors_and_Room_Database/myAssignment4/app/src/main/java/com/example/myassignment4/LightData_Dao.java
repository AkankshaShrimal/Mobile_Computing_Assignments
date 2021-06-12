package com.example.myassignment4;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LightData_Dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LightData obj);

    @Query("DELETE FROM light_table")
    void deleteAll();

    @Query("SELECT * FROM light_table")
    List<LightData> get_all();

    @Query("SELECT AVG(illuminance) as illumination FROM light_table WHERE time > :val")
    AVG_light get_result(Long val);


}

