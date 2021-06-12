package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;


public class Activity2 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        // creating a fragment manager
        FragmentManager fm = getSupportFragmentManager();
        // If curently there is no fragment on container add fragment
        if(fm.findFragmentById(R.id.fragment_container_activity1)==null){

            Log.d("Download Activtiy","fragment is  null so adding fragment");
            // adding the music download fragment to the main activity
            DownloadMusicFragment downloadMusicFragment=new DownloadMusicFragment();
            fm.beginTransaction().add(R.id.fragment_container_activity2, downloadMusicFragment, "MusicFragment")
                    .commit();
        }
    }



}

