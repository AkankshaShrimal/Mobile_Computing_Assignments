package com.example.assignment5.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.example.assignment5.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST = 1;

    private WifiManager wifiManager;
    private Button listButton;
    private List<ScanResult> results;

    private ListView listView;

    private int size = 0;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.ap_list, container, false);

        get_permission();


        listButton = root.findViewById(R.id.temp2);


        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanWifi();
            }
        });

        listView = root.findViewById(R.id.wifiList);
        
        adapter = new ArrayAdapter<String>(getContext(), R.layout.activity_listview, arrayList);
        listView.setAdapter(adapter);

        return root;
    }



   public void display(List<ScanResult> scanVal) {

       arrayList.clear();
       for (ScanResult scanResult : results) {
           arrayList.add(scanResult.SSID + "   "+ " (RSSI Value) " + Float.toString(scanResult.level));

           adapter.notifyDataSetChanged();
       }

   }



    private void scanWifi() {

        requireActivity().registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();

        boolean success = wifiManager.startScan();
        if (!success) {
            // scan failure handling
            Toast.makeText(getActivity(), "Scanning Failure", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(), "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
        }

    }

    public void create_wifi_obj(){
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getActivity(), "WiFi is disabled ... We need to enable it", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

    }

    public void get_permission(){

        // check permission first
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_WIFI_STATE)
                + ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                + ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                + ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CHANGE_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // request the permission
            requestPermissions(new String[]{Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CHANGE_WIFI_STATE},
                    PERMISSIONS_REQUEST);

        }
        else {

            Toast.makeText(getActivity(), "all permissions present", Toast.LENGTH_LONG).show();
//            Toast.makeText(getActivity(), Boolean.toString(a) + Boolean.toString(b) + Boolean.toString(c) + Boolean.toString(d), Toast.LENGTH_LONG).show();
            // wifi manager created
            create_wifi_obj();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && (grantResults[0]
                        + grantResults[1]
                        + grantResults[2]
                        + grantResults[3]
                        == PackageManager.PERMISSION_GRANTED
                )) {

                    // permission was granted.
                    Toast.makeText(getActivity(), "PERMISSION GRANTED", Toast.LENGTH_LONG).show();
                    create_wifi_obj();
                }
                else {
                    // permission denied.
                    // tell the user the action is cancelled
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setMessage("Permission not granted ");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                return;
            }

        }
    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
            requireActivity().unregisterReceiver(this);
            display(results);
        };
    };

}







































