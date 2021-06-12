package com.example.assignment5.ui.notifications;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.assignment5.ui.dashboard.DashboardFragment;
import com.example.assignment5.warData;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NotificationsFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST = 1;
    private WifiManager wifiManager;
    private Button mbuttonPredict;
    private Button Find_k_btn;

    private List<ScanResult> results;
    private EditText k_val;
    private EditText result_val;
    private float e1 = 0;
    private float e2 = 0;
    private float e3 = 0;
    private float e4 = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        mbuttonPredict = root.findViewById(R.id.buttonPredict);
        Find_k_btn = root.findViewById(R.id.buttonK);
        k_val = root.findViewById(R.id.k);
        result_val = root.findViewById(R.id.mylocpredict);
        get_permission();

        mbuttonPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!k_val.getText().toString().isEmpty()){
                    // scan and predict your location by given k value
                    scanWifi();
                }
                else{
                    Toast.makeText(getActivity(), "Enter k value ...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Find_k_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Returning optimal k value ...", Toast.LENGTH_SHORT).show();
                new GetWarData_for_k().execute();
            }
        });


        return root;
    }

    // To Get data from the database
    class GetWarData extends AsyncTask<Void, Void, List<warData>> {

        @Override
        protected List<warData> doInBackground(Void... x) {
            List<warData> myData = DatabaseClient
                    .getInstance(getContext())
                    .getAppDatabase().war_dao().get_all();

            return myData;
        }

        @Override
        protected void  onPostExecute(List<warData> result) {
            compute(result);
            return;
        }
    }
    class GetWarData_for_k extends AsyncTask<Void, Void, List<warData>> {

        @Override
        protected List<warData> doInBackground(Void... x) {
            List<warData> myData = DatabaseClient
                    .getInstance(getContext())
                    .getAppDatabase().war_dao().get_all();

            return myData;
        }

        @Override
        protected void  onPostExecute(List<warData> result) {
            compute_opt_k(result);
            return;
        }
    }

    public void compute_opt_k(List<warData> result){

//        shuffled list done
        Collections.shuffle(result, new Random(5));
        System.out.println("-----Data Shuffled sequence----");
        System.out.println(result);

        int total = result.size();

        double train_size = Math.floor(0.7 * new Float(total));
        double test_size = new Double(total) - train_size;
        System.out.println("-----train/test size----");
        System.out.println(train_size);
        System.out.println(test_size);


        List<warData> X_train = result.subList(0, (int) train_size);
        List<warData> X_test = result.subList((int) train_size, total-1);

//        Errros corresponsing to each k
        ArrayList<Float> E = new ArrayList<Float>();

        for(int k_f=1;k_f<=20;k_f++){
            int total_wrong = 0;
            for(warData instance : X_test){
                Float r1 = instance.getAp1();
                Float r2 = instance.getAp2();
                Float r3 = instance.getAp3();
                Float r4 = instance.getAp4();
                String y_true = instance.getLoc();

                String y_hat = compute_predict_over_train(r1, r2, r3, r4,X_train,k_f);

                if (!y_true.equals(y_hat)){
                    total_wrong = total_wrong+1;
                }
            }
            E.add((float) (total_wrong/test_size));
            System.out.println("---------");
            System.out.printf("First k: %d\nLast Error: %f",k_f, (float) (total_wrong/test_size));

//            Toast.makeText(getActivity(), "Done calculation for k .." + Integer.toString(k_f), Toast.LENGTH_SHORT).show();
        }
        System.out.println("-----All K val Errors----");
        System.out.println(E);

        Float min_so_far = 100f;
        int index_val;
        int opt_k = 0;
        for(index_val=0;index_val<E.size(); index_val++ ){
            if (index_val == 0){
                min_so_far = E.get(index_val);
                opt_k = index_val+1;
            }
            else if(E.get(index_val)< min_so_far){

                min_so_far = E.get(index_val);
                opt_k = index_val+1;
            }

        }
        System.out.printf("OPTIMAL K: %d\n",opt_k);
//        k_val.setText("");
//        k_val.setText(opt_k);
        Toast.makeText(getActivity(), "OPTIMAL K = " + Integer.toString(opt_k), Toast.LENGTH_SHORT).show();
    }


    public String compute_predict_over_train(Float m1, Float m2, Float m3, Float m4,List<warData> result,int k_f){

        // Data Locations and numerical values
        ArrayList<String> dataLocations = new ArrayList<String>();
        ArrayList<Double> dataDis = new ArrayList<Double>();
        List<ArrayList<Float>> listOfLists = new ArrayList<ArrayList<Float>>();

        for (warData row : result) {

            Float y1 = row.getAp1();
            Float y2 = row.getAp2();
            Float y3 = row.getAp3();
            Float y4 = row.getAp4();

            // location stored
            dataLocations.add(row.getLoc());
            // distance computed and stored
            Double temp = euclidean_get( m1, m2, m3,  m4,  y1, y2,  y3,  y4);
            dataDis.add(temp);
        }

        // converting arraylist to array
        Double[] D = new Double[dataDis.size()];
        D = dataDis.toArray(D);

        String[] L = new String[dataLocations.size()];
        L = dataLocations.toArray(L);

        // Sorting the arrays for distance and similar fashion location sorted
        // Sorted locations by distances from less to more
        // select top k and do max voting
        String[] loc_sorted = sort(D,L);
        String[] k_Array = Arrays.copyOfRange(loc_sorted,0 ,k_f);
//        String[] k_Array = Arrays.copyOfRange(loc_sorted,0 ,5);

        ArrayList<Integer> count = new ArrayList<Integer>();
        ArrayList<String> temp  = new ArrayList<String>();

        for(String ele : k_Array ){
//            System.out.println(ele);
            if(temp.indexOf(ele) == -1){
                temp.add(ele);
                count.add(1);
            }
            else{
                int r = temp.indexOf(ele);
                int v = count.get(r);
                count.set(r,v+1);
            }
        }
//        System.out.println(temp);
//        System.out.println(count);

        // converting arraylist to array
        String[] r1 = new String[temp.size()];
        r1 = temp.toArray(r1);

        Integer[] r2 = new Integer[count.size()];
        r2 = count.toArray(r2);

        String[] ans = sort2(r2,r1);
        return ans[ans.length-1];

    }

    public void compute(List<warData> result){

//        System.out.println(result.get(0).getAp1());

        // Data Locations and numerical values
        ArrayList<String> dataLocations = new ArrayList<String>();
        ArrayList<Double> dataDis = new ArrayList<Double>();
        List<ArrayList<Float>> listOfLists = new ArrayList<ArrayList<Float>>();

        for (warData row : result) {

            Float y1 = row.getAp1();
            Float y2 = row.getAp2();
            Float y3 = row.getAp3();
            Float y4 = row.getAp4();

            // location stored
            dataLocations.add(row.getLoc());
            // distance computed and stored
            Double temp = euclidean_get( e1, e2, e3,  e4,  y1, y2,  y3,  y4);
            dataDis.add(temp);

        }
        // converting arraylist to array
        Double[] D = new Double[dataDis.size()];
        D = dataDis.toArray(D);

        String[] L = new String[dataLocations.size()];
        L = dataLocations.toArray(L);

        // Sorting the arrays for distance and similar fashion location sorted
        // Sorted locations by distances from less to more
        // select top k and do max voting
        String[] loc_sorted = sort(D,L);
        String[] k_Array = Arrays.copyOfRange(loc_sorted,0 ,Integer.parseInt(k_val.getText().toString()));
//        String[] k_Array = Arrays.copyOfRange(loc_sorted,0 ,5);

        ArrayList<Integer> count = new ArrayList<Integer>();
        ArrayList<String> temp  = new ArrayList<String>();

        for(String ele : k_Array ){
//            System.out.println(ele);
            if(temp.indexOf(ele) == -1){
                temp.add(ele);
                count.add(1);
            }
            else{
                int r = temp.indexOf(ele);
                int v = count.get(r);
                count.set(r,v+1);
            }
        }
//        System.out.println(temp);
//        System.out.println(count);

        // converting arraylist to array
        String[] r1 = new String[temp.size()];
        r1 = temp.toArray(r1);

        Integer[] r2 = new Integer[count.size()];
        r2 = count.toArray(r2);

        String[] ans = sort2(r2,r1);
        result_val.setText("");
        result_val.setText(ans[ans.length-1]);
        Toast.makeText(getActivity(), "Done predicting ..", Toast.LENGTH_SHORT).show();

//        Double arr[] = {Double.valueOf(1), Double.valueOf(11), Double.valueOf(5), Double.valueOf(4), Double.valueOf(1)};
//        String arr2[] = {String.valueOf('a'),String.valueOf('b'),String.valueOf('c'),String.valueOf('d'), String.valueOf('e')};

    }

    public String[] sort2(Integer arr[],String arr2[])
    {

        int n = arr.length;
        for (int i=1; i<n; ++i)
        {
            int key = arr[i];
            String key2 = arr2[i];

            int j = i-1;
            while (j>=0 && arr[j] > key)
            {
                arr[j+1] = arr[j];
                arr2[j+1] = arr2[j];
                j = j-1;
            }
            arr[j+1] = key;
            arr2[j+1] = key2;
        }
        return arr2;
    }


    public String[] sort(Double arr[],String arr2[])
    {
//
//        for(Double i : arr){
//            System.out.println(i);
//        }
//        for(String i : arr2){
//            System.out.println(i);
//        }
//        System.out.println("-----here------");

        int n = arr.length;
        for (int i=1; i<n; ++i)
        {
            Double key = arr[i];
            String key2 = arr2[i];

            int j = i-1;

            while (j>=0 && arr[j] > key)
            {
                arr[j+1] = arr[j];
                arr2[j+1] = arr2[j];
                j = j-1;
            }
            arr[j+1] = key;
            arr2[j+1] = key2;
        }

//        for(Double i : arr){
//            System.out.println(i);
//        }
//        for(String i : arr2){
//            System.out.println(i);
//        }
        return arr2;
    }



    public double euclidean_get(Float x1,Float x2,Float x3, Float x4, Float y1, Float y2, Float y3, Float y4){
        double dis;
        dis = Math.sqrt((y1-x1)*(y1-x1)+  (y2-x2)*(y2-x2) + (y3-x3)*(y3-x3) + (y4-x4)*(y4-x4));
        return dis;
    }

    private void scanWifi() {

        try {
            if (wifiReceiver!=null) {

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
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }
    private void predictMyLoc(List<ScanResult> scanVal){

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
            }
            else if(test.equals(s2)) {
                e2 = ap.level;
            }
            else if(test.equals(s3)) {
                e3 = ap.level;
            }
            else if(test.equals(s4)) {
                e4 = ap.level;
            }
        }

        Toast.makeText(getActivity(), "predicting ...", Toast.LENGTH_SHORT).show();
        new GetWarData().execute();


    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
            predictMyLoc(results);
            requireActivity().unregisterReceiver(this);

        };
    };

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

}