package com.example.assignment2;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.media.MediaPlayer;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;


public class MusicService extends Service {

    private NotificationManagerCompat notificationManager;
    public static final String CHANNEL_ID = "channel-1";
    private static final String TAG = null;
    MediaPlayer player;
    // Service is not binding
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // Music player initialised and network manager initialised to show notification
        super.onCreate();
        player = new MediaPlayer();
        player.setLooping(true); // Set looping
        player.setVolume(80,80);
        notificationManager = NotificationManagerCompat.from(this);

    }

     private void createNotificationsChannel(){
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             // channel created with ID and importance level
             NotificationChannel channel1 = new NotificationChannel(
                     CHANNEL_ID,
                     "Channel 1",
                     NotificationManager.IMPORTANCE_HIGH
             );
             // Description of channel visible to used
             channel1.setDescription("This is music channel");

             NotificationManager manager = getSystemService(NotificationManager.class);
             manager.createNotificationChannel(channel1);

         }
     }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        String songName = intent.getStringExtra("songName");
        String songType = intent.getStringExtra("songTypeRaw");

        // If song already playing and another song started play new song
            if(player.isPlaying())
            {
                player.stop();
                player.release();
                if(songType.equals("true"))
                {
                    player = MediaPlayer.create(this,  getResources().getIdentifier(songName, "raw", getPackageName()));
                    // prepare not necessary after on create, it does it automatically
                    player.start();
                }
                else{
                    // If file is internal file then play using set data source
                    player = new MediaPlayer();
                    File internalFile = getFileStreamPath(songName);
                    Uri internal = Uri.fromFile(internalFile);
                    try {
                        player.setDataSource(getApplicationContext(),internal);
                        // when set datasource used prepare is necessary before start
                        player.prepare();
                        player.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }



            }
            else{
                if(songType.equals("true"))
                {
                    player = MediaPlayer.create(this,  getResources().getIdentifier(songName, "raw", getPackageName()));
                    player.start();
                }
                else{

                    File internalFile = getFileStreamPath(songName);
                    Uri internal = Uri.fromFile(internalFile);
                    try {
                        player.setDataSource(getApplicationContext(),internal);
                        player.prepare();
                        player.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


            }



        // Create notification channel
        createNotificationsChannel();

         // Display notification
        Intent inten =new Intent(this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,inten,0);
        Notification notification=new NotificationCompat.Builder(this,CHANNEL_ID).setContentTitle("music service")
                .setContentText("Playing -> " + songName)
                .setSmallIcon(R.drawable.music_ic)
                .setContentIntent(pendingIntent)
                .build();
        // starting foreground service
        startForeground(1,notification);


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
        super.onDestroy();
    }

}

// ERROR WHEN CLICKING ON NOTIFICATION NEW INSTANCE CREATED
