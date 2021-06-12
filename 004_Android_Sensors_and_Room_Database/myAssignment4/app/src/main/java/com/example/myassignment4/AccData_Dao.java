package com.example.myassignment4;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AccData_Dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(AccData obj);

    @Query("DELETE FROM acc_table")
    void deleteAll();

    @Query("SELECT * FROM acc_table")
    List<AccData> get_all();

    @Query("SELECT AVG(x_axis) as x, AVG(y_axis) as y,AVG(z_axis) as z FROM acc_table WHERE time > :val")
    AVG_acc get_result(Long val);


}
