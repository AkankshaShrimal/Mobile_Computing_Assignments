package com.example.assignment3;
import android.content.Context;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class studentLab {
    private static studentLab mstudentLab;

    private  List<student> mStudents;

    private List<String> name= new ArrayList<>(Arrays.asList("Aman", "Amayra", "Aashi", "Akanksha","Raghav","Nimisha","Sakshi","Harsh","Medha","Archit","Manvi", "Riya",
            "Ram","Shyam","Anjali","Anuj","Ashmita","Nikhil","Golu","Vinod","Vineet","Shivam","Shasheat","Sourav","Abhi","Abhimanyu","Reela","Devang","chirayu",
    "Priyanshi","Ritika","Chinu"));
    private List<String> dept =  new ArrayList<>(Arrays.asList("CSE", "AI", "DE","MC","IS"));


    public static studentLab get(Context context) {
        if (mstudentLab == null) {
            mstudentLab = new studentLab(context);
        }

        return mstudentLab;
    }

    private studentLab(Context context) {
        mStudents = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            student s = new student();
            s.setId(String.valueOf(i+1));
            s.setDepartment(dept.get(i%5));
            s.setEmail(name.get(i)+"@iiitd.ac.in");
            s.setName(name.get(i));

            mStudents.add(s);
        }
    }

    public  List<student> getstudents() {
        return mStudents;
    }

    public student getStudent(String id) {
        for (student s : mStudents) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }
}

