package com.example.assignment2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.ACTION_BATTERY_LOW.equals(intent.getAction())) {

                Toast.makeText(context, "Batttery Low", Toast.LENGTH_SHORT).show();
                Log.d("Broadcast message", "Battery Low");

        }
        if (intent.ACTION_BATTERY_OKAY.equals(intent.getAction())) {

            Toast.makeText(context, "Battery Okay", Toast.LENGTH_SHORT).show();
            Log.d("Broadcast message", "Battery Okay");

        }
        if (intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())) {

            Toast.makeText(context, "Power Disconnected", Toast.LENGTH_SHORT).show();
            Log.d("Broadcast message", "Power Disconnected");

        }

    }
}
