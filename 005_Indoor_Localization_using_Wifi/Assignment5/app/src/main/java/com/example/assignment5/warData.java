package com.example.assignment5;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "War_table")
public class warData {


    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "location")
    private String loc;

    @ColumnInfo(name = "AP1")
    private float ap1;

    @ColumnInfo(name = "AP2")
    private float ap2;

    public warData(String loc, float ap1, float ap2, float ap3,float ap4) {
        this.loc = loc;
        this.ap1 = ap1;
        this.ap2 = ap2;
        this.ap3 = ap3;
        this.ap4 = ap4;
    }

    @ColumnInfo(name = "AP3")
    private float ap3;

    public float getAp4() {
        return ap4;
    }

    @ColumnInfo(name = "AP4")
    private float ap4;

    public void setAp4(float ap4) {
        this.ap4 = ap4;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public void setAp1(float ap1) {
        this.ap1 = ap1;
    }

    public void setAp2(float ap2) {
        this.ap2 = ap2;
    }

    public void setAp3(float ap3) {
        this.ap3 = ap3;
    }

    public int getId() {
        return id;
    }

    public String getLoc() {
        return loc;
    }

    public float getAp1() {
        return ap1;
    }

    public float getAp2() {
        return ap2;
    }

    public float getAp3() {
        return ap3;
    }
}
