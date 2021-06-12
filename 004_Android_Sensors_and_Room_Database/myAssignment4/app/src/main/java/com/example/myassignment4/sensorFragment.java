package com.example.myassignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import static android.content.Context.*;
import static android.content.Context.SENSOR_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class sensorFragment<sensorManager, smm> extends Fragment {


    Button get_acc_data;
    Button get_temp_data;
    Button get_mobility;
    TextView acc_mean_val;
    TextView temp_mean_val;
    Switch sw1;
    Switch sw2;
    Switch sw3;
    Switch sw4;
    Switch sw5;
    Switch sw6;
    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    TextView t5;
    TextView t6;
    TextView m1;

    // sensor related fields
    SensorManager smm;
    LocationManager Lmm;
    List<Sensor> sensor;
    private Sensor mLight;
    private Sensor mGrav;
    private Sensor mGyro;
    private Sensor mAcc;
    private Sensor mProxy;

    float mAccelLast;
    float mAccelCurrent;
    float mAccel = 0.00f;
    Long res_time = 0l;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


//    1) accelerometer, 2) Gravity 3) Light 4) Gyroscope 5) GPS, and 6) proximity

    SensorEventListener sel = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            Sensor sensor = event.sensor;
            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                t1.setText(Float.toString(event.values[0]));
                compute_mobility(event.values[0], event.values[1], event.values[2]);

                Long mTime = System.currentTimeMillis();
                AccData obj = new AccData(mTime, event.values[0], event.values[1], event.values[2]);
                new InsertAccData().execute(obj);


            } else if (sensor.getType() == Sensor.TYPE_GRAVITY) {
                t2.setText(Float.toString(event.values[0]));
                Long mTime = System.currentTimeMillis();
                GravData obj = new GravData(mTime, event.values[0], event.values[1], event.values[2]);
                new InsertGravData().execute(obj);

            } else if (sensor.getType() == Sensor.TYPE_LIGHT) {
                t3.setText(Float.toString(event.values[0]));
                Long mTime = System.currentTimeMillis();
                LightData obj = new LightData(mTime, event.values[0]);
                new InsertLightData().execute(obj);

            } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                t4.setText(Float.toString(event.values[0]));
                Long mTime = System.currentTimeMillis();
                GyroData obj = new GyroData(mTime, event.values[0], event.values[1], event.values[2]);
                new InsertGyroData().execute(obj);

            } else if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
                t6.setText(Float.toString(event.values[0]));
                Long mTime = System.currentTimeMillis();
                ProxyData obj = new ProxyData(mTime, event.values[0]);

                new InsertProximityData().execute(obj);
            }
        }
    };

    LocationListener location_list = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location event) {

            System.out.println(event.getLatitude() + "====Acc1===-----------------");
            t5.setText(Double.toString(event.getLatitude()));
            Long mTime = System.currentTimeMillis();
            GPSData obj = new GPSData(mTime, (float) event.getLongitude(), (float) event.getLatitude());
            new InsertGPSData().execute(obj);

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }

    };
    private int requestCode;
    private String[] permissions;
    private int[] grantResults;

    class InsertGPSData extends AsyncTask<GPSData, Void, Void> {

        @Override
        protected Void doInBackground(GPSData... x) {
            DatabaseClient
                    .getInstance(getContext())
                    .getAppDatabase().gps_dao().insert(x[0]);
            return null;
        }
    }

    class InsertAccData extends AsyncTask<AccData, Void, Void> {

        @Override
        protected Void doInBackground(AccData... x) {
            DatabaseClient
                    .getInstance(getContext())
                    .getAppDatabase().acc_dao().insert(x[0]);
//            List<AccData> temp =  DatabaseClient
//                    .getInstance(getContext())
//                    .getAppDatabase().acc_dao().get_all();
//            System.out.println(temp.size());
            return null;
        }
    }

    class InsertGravData extends AsyncTask<GravData, Void, Void> {

        @Override
        protected Void doInBackground(GravData... x) {
            DatabaseClient
                    .getInstance(getContext())
                    .getAppDatabase().grav_dao().insert(x[0]);
            return null;
        }
    }

    class InsertProximityData extends AsyncTask<ProxyData, Void, Void> {

        @Override
        protected Void doInBackground(ProxyData... x) {
            DatabaseClient
                    .getInstance(getContext())
                    .getAppDatabase().proxy_dao().insert(x[0]);
            return null;
        }
    }

    class InsertLightData extends AsyncTask<LightData, Void, Void> {

        @Override
        protected Void doInBackground(LightData... x) {
            DatabaseClient
                    .getInstance(getContext())
                    .getAppDatabase().Light_Dao().insert(x[0]);
            return null;
        }
    }

    class InsertGyroData extends AsyncTask<GyroData, Void, Void> {

        @Override
        protected Void doInBackground(GyroData... x) {
            DatabaseClient
                    .getInstance(getContext())
                    .getAppDatabase().gyro_dao().insert(x[0]);
            return null;
        }
    }

    class AvgAccData extends AsyncTask<Long, Void, Void> {

        @Override
        protected Void doInBackground(Long... x) {
            AVG_acc myRestult = DatabaseClient
                    .getInstance(getContext())
                    .getAppDatabase().acc_dao().get_result(x[0]);

            Float x1 = myRestult.x;
            Float y1 = myRestult.y;
            Float z1 = myRestult.z;
            String ans = "x1 = ".concat(Float.toString(x1)).concat(",y1 = ").concat(Float.toString(y1)).concat(",z1 = ").concat(Float.toString(z1));
//            System.out.println(y1);
            acc_mean_val.setText(ans);

            return null;
        }
    }

    class AvgLightData extends AsyncTask<Long, Void, Void> {

        @Override
        protected Void doInBackground(Long... x) {
            AVG_light myRestult = DatabaseClient
                    .getInstance(getContext())
                    .getAppDatabase().Light_Dao().get_result(x[0]);

            String ans = "Avg Illuminance = ".concat(Float.toString(myRestult.illumination));
//            System.out.println(y1);
            temp_mean_val.setText(ans);

            return null;
        }
    }

    public void compute_mobility(double x, double y, double z) {


        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);


        double delta = mAccelCurrent - mAccelLast;
        mAccel = (float) (mAccel * 0.9f + delta);

        if (mAccel > 1.5) // 0.5 is a threshold, you can test it and change it
        {
            System.out.println("detected");
            res_time = System.currentTimeMillis();
            m1.setText("Moving");
        }
        Long temp = System.currentTimeMillis();

        if (temp - res_time > 2000)
            m1.setText("Not Moving");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_sensor_fragment, container, false);


        // All Ui componenents
        get_acc_data = (Button) v.findViewById(R.id.acc_button);
        get_temp_data = (Button) v.findViewById(R.id.temp_button);
        get_mobility = (Button) v.findViewById(R.id.mobility_button);
        acc_mean_val = (TextView) v.findViewById(R.id.tv8);
        temp_mean_val = (TextView) v.findViewById(R.id.tv7);

        t1 = (TextView) v.findViewById(R.id.rv1);
        t2 = (TextView) v.findViewById(R.id.rv2);
        t3 = (TextView) v.findViewById(R.id.rv3);
        t4 = (TextView) v.findViewById(R.id.rv4);
        t5 = (TextView) v.findViewById(R.id.rv5);
        t6 = (TextView) v.findViewById(R.id.rv6);
        m1 = (TextView) v.findViewById(R.id.mob);

//        1) accelerometer, 2) linear-acceleration 3) temperature, 4) light, 5) GPS, and 6) proximity

        sw1 = (Switch) v.findViewById(R.id.switch1);
        sw2 = (Switch) v.findViewById(R.id.switch2);
        sw3 = (Switch) v.findViewById(R.id.switch3);
        sw4 = (Switch) v.findViewById(R.id.switch4);
        sw5 = (Switch) v.findViewById(R.id.switch5);
        sw6 = (Switch) v.findViewById(R.id.switch6);

        // Getting different types of sensors available
        smm = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        Lmm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (Lmm == null)
            System.out.println("obj not created --------");

        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        mAcc = smm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGrav = smm.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mLight = smm.getDefaultSensor(Sensor.TYPE_LIGHT);
        mGyro = smm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mProxy = smm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        // Add GPS later on
//        smm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Implementing the buttons
        get_acc_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long time = System.currentTimeMillis();
                Long from = time - 60 * 60 * 1000;
                new AvgAccData().execute(from);

            }
        });

        get_temp_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long time = System.currentTimeMillis();
                Long from = time - 60 * 60 * 1000;
                new AvgLightData().execute(from);
            }
        });

        get_mobility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        checkLocationPermission();
        check_set_get();
        return v;
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Please Give Permission")
                        .setMessage("Permission required to check your location")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Lmm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, location_list);

            } else {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Lmm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, location_list);
            }
        }
    }


    
    public void check_set_get(){

        if(sw1.isChecked() && mAcc!=null){smm.registerListener(sel,mAcc,10000000);}
        if(sw2.isChecked() && mGrav!=null){smm.registerListener(sel,mGrav,10000000);}
        if(sw3.isChecked()&& mLight!=null){smm.registerListener(sel,mLight,10000000);}
        if(sw4.isChecked()&& mGyro!=null){smm.registerListener(sel,mGyro,10000000);}
        if(sw5.isChecked()&& Lmm!=null){

            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
//                System.out.println("-------------Location Permision Granted-----------------");
                Lmm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, location_list);
            }
            else{
//                System.out.println("-------------NOT Location Permision Granted-----------------");
                Lmm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, location_list);
            }
        }
        if(sw6.isChecked()&& mProxy!=null){smm.registerListener(sel,mProxy,10000000);}

    }
// 1) accelerometer, 2) Gravity 3) Light 4) Gyroscope 5) GPS, and 6) proximity

    @Override
    public void onResume(){
        super.onResume();
        check_set_get();
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on)
                {
                    if(mAcc!=null) {
                        smm.registerListener(sel,mAcc,10000000);
                    }
                    else{
                        System.out.print("------");
                    }
                    //Do something when Switch button is on/checked
                }
                else
                {
                    if(mAcc!=null) {
                        smm.unregisterListener(sel, mAcc);
                    }
                    //Do something when Switch is off/unchecked

                }
            }
        });
        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on)
                {
                    if(mGrav!=null){
                        smm.registerListener(sel,mGrav,10000000);
                    }
                }
                else
                {
                    if(mGrav!=null){
                        smm.unregisterListener(sel, mGrav);
                    }
                }
            }
        });
        sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on)
                {
                    if(mLight!=null){
                        smm.registerListener(sel,mLight,10000000);
                    }
                }
                else
                {
                    if(mLight!=null){
                        smm.unregisterListener(sel, mLight);
                    }
                }
            }
        });
        sw4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on)
                {
                    if(mGyro!=null){
                        smm.registerListener(sel,mGyro,10000000);
                    }
                }
                else
                {
                    if(mGyro!=null){
                        smm.unregisterListener(sel, mGyro);
                    }
                }
            }
        });
        sw5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on)
                {
                    //Do something when Switch button is on/checked

                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
//                System.out.println("-------------Location Permision Granted-----------------");
                        Lmm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, location_list);
                    }
                    else{
//                System.out.println("-------------NOT Location Permision Granted-----------------");
                        Lmm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, location_list);
                    }

                }
                else
                {
                    Lmm.removeUpdates(location_list);

                }
            }
        });
        sw6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on)
                {
                    if(mProxy!=null){smm.registerListener(sel,mProxy,10000000);}
                }
                else
                {
                   if(mProxy!=null){smm.unregisterListener(sel, mProxy);}

                }
            }
        });

    }
    @Override
    public void onPause(){
        super.onPause();
        smm.unregisterListener(sel, mAcc);
        smm.unregisterListener(sel, mLight);
        Lmm.removeUpdates(location_list);
        smm.unregisterListener(sel, mGyro);
        smm.unregisterListener(sel, mProxy);
        smm.unregisterListener(sel, mGrav);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            Lmm.removeUpdates(location_list);
        }



    }
}
