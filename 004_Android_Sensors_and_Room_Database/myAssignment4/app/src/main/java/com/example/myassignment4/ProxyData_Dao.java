package com.example.myassignment4;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProxyData_Dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ProxyData obj);

    @Query("DELETE FROM proxy_table")
    void deleteAll();

    @Query("SELECT * FROM proxy_table")
    List<ProxyData> get_all();

    @Query("SELECT * FROM proxy_table WHERE time > :val")
    List<ProxyData> get_result(Long val);

}
