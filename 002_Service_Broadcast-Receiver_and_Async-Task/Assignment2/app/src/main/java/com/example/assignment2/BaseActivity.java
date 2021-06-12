package com.example.assignment2;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

// This task of receiving Can be done Dynamically or statically ( manifest file)
// Broadcast can be registered-de registered either in complete application , Activity from create to destroy , or Activtity foregraound between resume and pause
// For doing it for complete Application use Application class or Base Class like done here
// A base class for all kinds of broadcast receivers
public class BaseActivity extends AppCompatActivity {

    MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    // Broadcast receiver registered and de registered between onResume and onPause for each activity
    protected void onResume() {
        super.onResume();
        // Broad Cast receiver
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_BATTERY_OKAY);

        registerReceiver(myBroadcastReceiver, filter);
        Log.d("BROADCAST RECEIVER", "registered");
        //registerReceiver Here
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myBroadcastReceiver);
        Log.d("BROADCAST RECEIVER", "unregistered ");
        //unregisterReceiver here
    }

}