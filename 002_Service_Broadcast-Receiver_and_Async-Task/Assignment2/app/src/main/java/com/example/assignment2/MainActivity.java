package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.View;


public class MainActivity extends BaseActivity  {

//    MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting layout of activty main
        setContentView(R.layout.activity_main);
        // creating a fragment manager
        FragmentManager fm = getSupportFragmentManager();
        // If curently there is no fragment on container add fragment
        if(fm.findFragmentById(R.id.fragment_container_activity1)==null){

            Log.d("Main Activtiy","fragment is  null so adding fragment");
            // adding the music fragment to the main activity
            MusicPlayFragment musicFragment=new MusicPlayFragment();
            fm.beginTransaction().add(R.id.fragment_container_activity1, musicFragment, "MusicFragment")
                    .commit();
        }


        // Broad Cast receiver
//        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
//        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
//        filter.addAction(Intent.ACTION_BATTERY_OKAY);
//        registerReceiver(myBroadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(myBroadcastReceiver);
    }
}

