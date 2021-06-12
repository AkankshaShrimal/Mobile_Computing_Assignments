package com.example.covid_19precautionsandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.content.Intent;
import android.util.Log;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private ArrayList<String> preList;
    private String userName;
    private int count = 1;
    private String safeVar = null;
    private LinearLayout resultLayout;
    private String prevState = null;
    private String temp = "state of activity: Result changed from ";
    private static final String TAG = "ResultActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if (prevState == null)
            prevState = "onCreate";
        Toast toast
                = Toast.makeText(
                getApplicationContext(),
                "state of activity: Result " + "onCreate",
                Toast.LENGTH_SHORT);
        toast.show();

        resultLayout = (LinearLayout)findViewById(R.id.resultLayout);

        Intent intent = getIntent();
        preList = intent.getStringArrayListExtra("preList");
        userName = intent.getStringExtra("userName");

        TextView nameField = new TextView(this);
        nameField.setText("Hello! " + userName + " ,you selected:");
        resultLayout.addView(nameField);

        if(preList.size() == 0){
            TextView temp = new TextView(this);
            temp.setText("None of the options selected");
            resultLayout.addView(temp);
        }
        else{
            for (String item : preList){
                TextView tv = new TextView(this);
                tv.setText(Integer.toString(count) + ") " +  item);
                resultLayout.addView(tv);
                count = count + 1;

            }
        }


    }



    // Check Button functionality
    public void checkAction(View v) {


        if(preList.size() == 5)
        {
            safeVar = "safe";
        }
        else{
            safeVar = "unsafe";
        }
//        Log.e(TAG, "done2");
        Intent intent = new Intent();
        intent.putExtra("userState",safeVar);
        setResult(Activity.RESULT_OK,intent);

    }


    @Override
    protected void onStart()
    {
        super.onStart();
        Toast toast
                = Toast.makeText(
                getApplicationContext(),
                temp + prevState + " to onStart",
                Toast.LENGTH_SHORT);
        toast.show();
        Log.i(TAG, temp + prevState + " to onStart");

        prevState = "onStart";
    }
    @Override
    protected void onRestart()
    {

        super.onRestart();
        Toast toast
                = Toast.makeText(
                getApplicationContext(),
                temp + prevState + " to onRestart ",
                Toast.LENGTH_SHORT);
        toast.show();
        Log.i(TAG, temp + prevState + " to onRestart ");
        prevState = "onRestart";
    }
    @Override
    protected void onResume()
    {

        super.onResume();
        Toast toast
                = Toast.makeText(
                getApplicationContext(),
                temp + prevState + " to onResume",
                Toast.LENGTH_SHORT);
        toast.show();
        Log.i(TAG, temp + prevState + " to onResume");
        prevState = "onResume";
    }
    @Override
    protected void onPause()
    {

        super.onPause();
        Toast toast
                = Toast.makeText(
                getApplicationContext(),
                temp + prevState + " to onPause",
                Toast.LENGTH_SHORT);
        toast.show();
        Log.i(TAG, temp + prevState + " to onPause");
        prevState = "onPause";
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Toast toast
                = Toast.makeText(
                getApplicationContext(),
                temp + prevState + " to onStop",
                Toast.LENGTH_SHORT);
        toast.show();
        Log.i(TAG, temp + prevState + " to onStop");
        prevState = "onStop";
    }
    @Override
    protected void onDestroy()
    {

        super.onDestroy();
        Toast toast
                = Toast.makeText(
                getApplicationContext(),
                temp + prevState + " to onDestroy",
                Toast.LENGTH_SHORT);
        toast.show();
        Log.i(TAG, temp + prevState + " to onDestroy");
        prevState = "onDestroy";
    }
}

