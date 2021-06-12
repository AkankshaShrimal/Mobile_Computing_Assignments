package com.example.myassignment4;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Gps_table")
public class GPSData {


    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "time")
    public Long mTime;

    @ColumnInfo(name = "longitude")
    private float lon_value;

    @ColumnInfo(name = "latitude")
    private float lat_value;

    public GPSData(@NonNull Long mTime, float lon_value, float lat_value) {
        this.mTime = mTime;
        this.lon_value = lon_value;
        this.lat_value = lat_value;
    }

    public void setmTime(@NonNull Long mTime) {
        this.mTime = mTime;
    }

    public void setLon_value(float lon_value) {
        this.lon_value = lon_value;
    }

    public void setLat_value(float lat_value) {
        this.lat_value = lat_value;
    }

    @NonNull
    public Long getmTime() {
        return mTime;
    }


    public float getLon_value() {
        return lon_value;
    }

    public float getLat_value() {
        return lat_value;
    }
}