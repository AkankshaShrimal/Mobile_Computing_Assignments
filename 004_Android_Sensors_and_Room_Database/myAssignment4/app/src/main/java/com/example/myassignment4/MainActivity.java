package com.example.myassignment4;



import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;



public class MainActivity extends singleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new sensorFragment();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
//        if (requestCode == 1) {
//            if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                System.out.println(grantResults[0] + "---------------------------");
//            }
//            else{
//                System.out.println(grantResults[0] + "---------------------------");
//            }
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 99: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
//                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                        System.out.println(grantResults[0]);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

}