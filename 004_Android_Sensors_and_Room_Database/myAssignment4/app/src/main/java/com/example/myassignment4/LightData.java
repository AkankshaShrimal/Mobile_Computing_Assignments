package com.example.myassignment4;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.sql.Timestamp;

@Entity(tableName = "Light_table")
public class LightData {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "time")
    public Long mTime;

    @ColumnInfo(name = "illuminance")
    private float illuminance;

    public LightData(@NonNull Long mTime, float illuminance) {
        this.mTime = mTime;
        this.illuminance = illuminance;
    }

    @NonNull
    public Long getmTime() {
        return mTime;
    }

    public void setmTime(@NonNull Long mTime) {
        this.mTime = mTime;
    }

    public float getIlluminance() {
        return illuminance;
    }

    public void setIlluminance(float illuminance) {
        this.illuminance = illuminance;
    }


}

