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

public class HomeFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST = 1;
    private BarChart B1;
    private WifiManager wifiManager;
    private Button buttonScan;
    private List<ScanResult> results;
    private boolean success;
    ArrayList<BarEntry> Val = new ArrayList<>();
    ArrayList<String> wifiNames = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        if(savedInstanceState!=null)
        {
            Val = savedInstanceState.getParcelableArrayList("WifiValues");
            wifiNames = savedInstanceState.getStringArrayList("Wifi");
            B1 = root.findViewById(R.id.myChart);
//            plotChart_initial(Val , wifiNames);
        }

        get_permission();
        // Displaying the Bar chart
        B1 = root.findViewById(R.id.myChart);

        // scanning functionality
        buttonScan = root.findViewById(R.id.scanBtn);

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanWifi();
            }
        });

        // Get permission and create wifi manager


        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("Wifi",
                wifiNames);
        outState.putParcelableArrayList("WifiValues", Val);
    }

    public void getPlotData(List<ScanResult> scanVal){

        Val.removeAll(Val);
        wifiNames.removeAll(wifiNames);

        int i = 0;
        for (ScanResult ap : scanVal) {
//            if (!ap.BSSID.equals("00:1e:a6:dd:4c:18"))

//                Val.add(new BarEntry(i,wifiManager.calculateSignalLevel(ap.level, 10)));
                Val.add(new BarEntry(i,ap.level));
                wifiNames.add(ap.SSID);
                System.out.println("-----");
                System.out.println(ap.SSID);
                System.out.println(ap.BSSID);
                System.out.println(wifiNames);
                System.out.println(Val);
                i = i+1;

        }

    }

    public void plotChart_initial(ArrayList<BarEntry> val2 , ArrayList<String> WifiNames2){

        System.out.println("-----called------");

        B1.getAxisLeft().setInverted(true);
        B1.getAxisRight().setEnabled(false);

        XAxis xAxis = B1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setLabelRotationAngle(90);
        xAxis.setTextSize(5f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(WifiNames2));

        B1.getDescription().setText("WIFI ACCESS POINTS DATA");

        BarDataSet bs = new BarDataSet(val2,"ACCESS POINTS");

        bs.setValueTextColor(Color.BLACK);
        bs.setValueTextSize(16f);
        bs.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData bardata = new BarData(bs);
        B1.setFitBars(true);
        B1.setData(bardata);
        B1.animateY(2000);

    }

    public void plotChart(List<ScanResult> scanVal){

        getPlotData(scanVal);
        B1.getAxisLeft().setInverted(true);
        B1.getAxisRight().setEnabled(false);
        XAxis xAxis = B1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setLabelRotationAngle(45);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(wifiNames));

        B1.getDescription().setText("WIFI ACCESS POINTS DATA");

        BarDataSet bs = new BarDataSet(Val,"ACCESS POINTS");
        bs.setValueTextColor(Color.BLACK);
        bs.setValueTextSize(16f);
        bs.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData bardata = new BarData(bs);
        B1.setFitBars(true);
        B1.setData(bardata);
        B1.animateY(2000);

    }
    private void scanWifi() {

        requireActivity().registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();

        success = wifiManager.startScan();
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
            // has the permission.
//            boolean a = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_WIFI_STATE)== PackageManager.PERMISSION_GRANTED;
//            boolean b = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED;
//            boolean c = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED;
//            boolean d = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CHANGE_WIFI_STATE)== PackageManager.PERMISSION_GRANTED;

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
            plotChart(results);
        };
    };

}




































