package com.example.assignment5.ui.dashboard;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.assignment5.DatabaseClient;
import com.example.assignment5.R;
import com.example.assignment5.warData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST = 1;
    private LineChart L1;
    private LineChart L2;
    private LineChart L3;
    private WifiManager wifiManager;
    private Button buttonMe;
//    private Button buttonStop;
    private List<ScanResult> results;
    private EditText mylocation;
    private long removalCounter = 0;
    private static final int VISIBLE_COUNT = 300;
    private boolean success;

//    private Long currTime = 0l;
//    private Long prevTime = 0l;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Getting all
        get_permission();
        L1 = root.findViewById(R.id.line1);
//        L2 = root.findViewById(R.id.line2);
//        L3 = root.findViewById(R.id.line3);
        buttonMe = root.findViewById(R.id.scan);
        mylocation = root.findViewById(R.id.myloc);


        buttonMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mylocation.getText().toString().isEmpty()){
                    scanWifi();
                }
                else{
                    Toast.makeText(getActivity(), "Enter Location ...", Toast.LENGTH_SHORT).show();
                }

            }
        });

//        buttonStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Stop_scanWifi();
//            }
//        });



        return root;
    }


    // To insert data into the database
    class InsertWarData extends AsyncTask<warData, Void, Void> {

        @Override
        protected Void doInBackground(warData... x) {
            DatabaseClient
                    .getInstance(getContext())
                    .getAppDatabase().war_dao().insert(x[0]);
            System.out.println("---- Stored in DB ---");
            return null;
        }
    }

    private void scanWifi() {

        try {
            if (wifiReceiver!=null) {

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
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
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

    public void store_data(List<ScanResult> scanVal){

        float e1 = 0;
        float e2 = 0;
        float e3 = 0;
        float e4 = 0;

        float ef1 = 0;
        float ef2 = 0;
        float ef3 = 0;
        float ef4 = 0;

        ArrayList<Float> currVal = new ArrayList<Float>();

        // Three Wifi fixed for wardriving
        String s1 = "58:7b:e9:11:1f:5c";
        String s2 = "5c:f9:fd:9c:04:69";
        String s3 = "00:1e:a6:dd:4c:18";
        String s4 = "be:39:60:61:04:a5";
//              S K Shrimal - Railtel
//            58:7b:e9:11:1f:5c
//                    -----
//                    TENGU _4G
//            5c:f9:fd:9c:04:69
//                    -----
//                    Jai Shree Ram
//            00:1e:a6:dd:4c:18
//        vivo 1917
//        be:39:60:61:04:a5



        for (ScanResult ap : scanVal) {
            String test = ap.BSSID;
            if (test.equals(s1)){
                e1 = ap.level;
                ef1 = wifiManager.calculateSignalLevel(ap.level, 10);
            }
            else if(test.equals(s2)) {
                e2 = ap.level;
                ef2 = wifiManager.calculateSignalLevel(ap.level, 10);

            }
            else if(test.equals(s3)) {
                e3 = ap.level;
                ef3 = wifiManager.calculateSignalLevel(ap.level, 10);
            }
            else if(test.equals(s4)) {
                e4 = ap.level;
                ef4 = wifiManager.calculateSignalLevel(ap.level, 10);
            }
        }

        currVal.add(ef1);
        currVal.add(ef2);
        currVal.add(ef3);
        currVal.add(ef4);
        plot_line_data(currVal);

        String loc = mylocation.getText().toString();
        warData obj = new warData(loc, e1,e2,e3,e4);

        new InsertWarData().execute(obj);
        Toast.makeText(getActivity(), "Storing ...", Toast.LENGTH_SHORT).show();

    }

    public void plot_line_data(List<Float> curVal){

        ArrayList<Entry> values = new ArrayList<>();
        int i = 1;
        for (Float f : curVal) {
            values.add(new Entry(i, f));
            i = i+1;

        }

        ArrayList<String> names = new ArrayList<>();
        names.add("AP1");
        names.add("AP2");
        names.add("AP3");

        LineDataSet set1;

        L1.setMinimumHeight(100);
        set1 = new LineDataSet(values, "Data Collected");
            set1.setDrawIcons(false);
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);

        set1.setDrawFilled(true);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_red1);
            set1.setFillDrawable(drawable);
        }
        else {
            set1.setFillColor(Color.BLACK);
        }

        XAxis xAxis = L1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setLabelRotationAngle(45);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

//        xAxis.setValueFormatter(new IndexAxisValueFormatter(names));

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            L1.setData(data);
            L1.animateY(2000);
        }


    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
            store_data(results);
            requireActivity().unregisterReceiver(this);

        };
    };


}
