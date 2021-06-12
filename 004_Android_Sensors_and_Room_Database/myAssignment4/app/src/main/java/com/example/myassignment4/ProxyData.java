package com.example.myassignment4;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.sql.Timestamp;

@Entity(tableName = "Proxy_table")
public class ProxyData {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "time")
    public Long mTime;

    @ColumnInfo(name = "dist")
    private float dist;

    public ProxyData(@NonNull Long mTime, float dist) {
        this.mTime = mTime;
        this.dist = dist;
    }

    @NonNull
    public Long getmTime() {
        return mTime;
    }



    public float getDist() {
        return dist;
    }


    public void setDist(float dist) {
        this.dist = dist;
    }

    public void setmTime(@NonNull Long mTime) {
        this.mTime = mTime;
    }
}

