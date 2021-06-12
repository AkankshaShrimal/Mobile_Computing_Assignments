package com.example.myassignment4;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.sql.Timestamp;

// Force of gravity along x,y,z axis
@Entity(tableName = "Grav_table")
public class GravData {


    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "time")
    public Long mTime;

    @ColumnInfo(name = "x_axis")
    private float x_value;

    @ColumnInfo(name = "y_axis")
    private float y_value;

    @ColumnInfo(name = "z_axis")
    private float z_value;

    public GravData(@NonNull Long mTime, float x_value, float y_value, float z_value) {
        this.mTime = mTime;
        this.x_value = x_value;
        this.y_value = y_value;
        this.z_value = z_value;
    }

    public float getX_value() {
        return x_value;
    }

    public float getY_value() {
        return y_value;
    }

    public float getZ_value() {
        return z_value;
    }

    public void setmTime(@NonNull Long mTime) {
        this.mTime = mTime;
    }

    @NonNull
    public Long getmTime() {
        return mTime;
    }

    public void setX_value(float x_value) {
        this.x_value = x_value;
    }

    public void setY_value(float y_value) {
        this.y_value = y_value;
    }

    public void setZ_value(float z_value) {
        this.z_value = z_value;
    }

}

