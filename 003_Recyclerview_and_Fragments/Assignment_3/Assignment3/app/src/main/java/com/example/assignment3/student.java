package com.example.assignment3;

import java.util.UUID;

//Class for retrieving the data ans actions associated
public class student {
    private String mId;
    private String mName;
    private String mEmail;
    private String mDepartment;


    public student() {


    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getDepartment() {
        return mDepartment;
    }

    public void setEmail(String e) {
        mEmail = e;
    }

    public  void setDepartment(String d) {
        mDepartment = d;
    }
    public  void setName(String n) {
        mName = n;
    }
    public  void setId(String i) {
        mId = i;
    }


}

