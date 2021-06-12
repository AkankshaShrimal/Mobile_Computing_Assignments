package com.example.assignment5;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {warData.class}, version = 1, exportSchema = false)
public abstract class myDatabase extends RoomDatabase {

    public abstract warData_Dao war_dao();

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