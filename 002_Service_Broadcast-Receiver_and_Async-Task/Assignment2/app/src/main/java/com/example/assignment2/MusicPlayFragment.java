package com.example.assignment2;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class MusicPlayFragment extends Fragment {

    ListView listview;
    Button stop_music;
    Button download_music;
    // To play the downloaded song
    ArrayList<String> songFiles = new ArrayList<String>();
    ArrayList<String> rawFiles = new ArrayList<String>();
    ArrayList<String> contextFiles = new ArrayList<String>();

    // Intents
    Intent startintent=new Intent();
    Intent stopintent=new Intent();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        get_songs();

    }

    public void get_songs(){


        // ADDING LIST OF SONGS
        // names of all files in res/raw
        String[] names = getRawFiles();
        File dirFiles = getActivity().getFilesDir();
        // Adding raw files
        for(String ele: names){
            if(!songFiles.contains(ele))
                songFiles.add(ele);
            if(!rawFiles.contains(ele))
                rawFiles.add(ele);

        }
        // Adding internal files with .mp3
        for (String f : dirFiles.list()) {
            if (f.endsWith((".mp3"))) {
                if(!songFiles.contains(f))
                    songFiles.add(f);
                if(!contextFiles.contains(f))
                    contextFiles.add(f);
            }

        }

        // Adding songs names in listview dynamically
        // Adapter is used to put elements inside a list, kind of connection between data and UI , holds data and placed in UI
        // Arguments :- context , .xml file where to add items ,where to add id of text view,  list of elements to be added
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.song_list, R.id.textView1, songFiles);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView T = (TextView)view;
                String title = T.getText().toString().trim();
                startintent.putExtra("songName", title);
                if(rawFiles.contains(title))
                {
                    startintent.putExtra("songTypeRaw","true");

                }
                else{
                    File d = getActivity().getFilesDir();
                    startintent.putExtra("songTypeRaw","false");
                    startintent.putExtra("path",d.toString());
                }
                getActivity().startService(startintent);

            }

    });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_music_play, container, false);

        // Intent for statring and stopping music service
        // Setting components of start and stop intents

        ComponentName c1 = new ComponentName(getActivity(),MusicService.class);
        ComponentName c2 = new ComponentName(getActivity(),MusicService.class);

        startintent.setComponent(c1);
        stopintent.setComponent(c2);


        // Declarations of UI
        listview = view.findViewById(R.id.music_listView);
        stop_music = view.findViewById(R.id.button_stop_music);
        download_music = view.findViewById(R.id.button_download_music);

        // ADDING BUTTONS FUNCTIONALITY
        stop_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                getActivity().stopService(stopintent);

            }
        });

        download_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(getActivity(), Activity2.class);
                startActivity(intent);

            }
        });

        return view;
    };



    // get the names of all songs present in raw folder inside res
    public String[] getRawFiles(){
        Field fields[] = R.raw.class.getDeclaredFields() ;
        String[] names = new String[fields.length] ;
        try {
            for( int i=0; i< fields.length; i++ ) {
                Field f = fields[i] ;
                names[i] = f.getName();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }
}



