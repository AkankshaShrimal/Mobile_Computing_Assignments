package com.example.assignment3;

import android.content.Intent;

import androidx.fragment.app.Fragment;

public class DetailActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

        Intent intent = getIntent();
        String id  = intent.getStringExtra("student_id");
//        System.out.println("D-------------A----------------" + id);
        return DetailFragment.newInstance(id);
    };
};
