package com.example.myassignment4;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {AccData.class, GravData.class, GyroData.class, LightData.class, ProxyData.class, GPSData.class}, version = 1, exportSchema = false)
public abstract class myDatabase extends RoomDatabase {

    public abstract AccData_Dao acc_dao();
    public abstract GravData_Dao grav_dao();
    public abstract GyroData_Dao gyro_dao();
    public abstract GPSData_Dao gps_dao();
    public abstract ProxyData_Dao proxy_dao();
    public abstract LightData_Dao Light_Dao();

    private static volatile myDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static myDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (myDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            myDatabase.class, "sensor_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}