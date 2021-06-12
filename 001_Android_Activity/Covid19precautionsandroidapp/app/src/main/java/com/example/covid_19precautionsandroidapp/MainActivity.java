//package com.example.covid_19precautionsandroidapp;
package com.example.covid_19precautionsandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import java.util.ArrayList;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private EditText inputName;
    private int requestQode = 0;
    private CheckBox checkbox_p1;
    private CheckBox checkbox_p2;
    private CheckBox checkbox_p3;
    private CheckBox checkbox_p4;
    private CheckBox checkbox_p5;
    private String userState = null;
    private String prevState = null;
    private String temp = "state of activity: Main changed from ";
    private TextView userResult;
    private Button submitButton;
    private static final String TAG = "MainActivity";

    // Creating keys for saving and restoring
    private static final String KEY_NAME = "Name";
    private static final String USER_RESULT = "user_result";
    private static final String[] CHECKBOX_VALUES = {"ch1","ch2","ch3","ch4","ch5"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (prevState == null)
            prevState = "onCreate";


        Toast toast
                = Toast.makeText(
                getApplicationContext(),
                "state of activity: Main " + "onCreate",
                Toast.LENGTH_SHORT);
        toast.show();
        Log.i(TAG, "state of activity: Main " + "onCreate");

        inputName = (EditText) findViewById(R.id.inputName);
        checkbox_p1 = (CheckBox) findViewById(R.id.checkbox_p1);
        checkbox_p2 = (CheckBox) findViewById(R.id.checkbox_p2);
        checkbox_p3 = (CheckBox) findViewById(R.id.checkbox_p3);
        checkbox_p4 = (CheckBox) findViewById(R.id.checkbox_p4);
        checkbox_p5 = (CheckBox) findViewById(R.id.checkbox_p5);
        userResult = (TextView) findViewById(R.id.userState);
        submitButton = (Button) findViewById(R.id.button_send);

        // restoring state f saved already
        if(savedInstanceState != null){
            String user_name = savedInstanceState.getString(KEY_NAME, null);
            inputName.setText(user_name);

            String temp = savedInstanceState.getString(USER_RESULT, null);
            userResult.setText(temp);

            checkbox_p1.setChecked(savedInstanceState.getBoolean(CHECKBOX_VALUES[0]));
            checkbox_p2.setChecked(savedInstanceState.getBoolean(CHECKBOX_VALUES[1]));
            checkbox_p3.setChecked(savedInstanceState.getBoolean(CHECKBOX_VALUES[2]));
            checkbox_p4.setChecked(savedInstanceState.getBoolean(CHECKBOX_VALUES[3]));
            checkbox_p5.setChecked(savedInstanceState.getBoolean(CHECKBOX_VALUES[4]));

        }

        // Enable submit button only when name is input
        inputName.addTextChangedListener(enterName);

    }

    private  TextWatcher enterName = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String input = inputName.getText().toString().trim();
            submitButton.setEnabled(!input.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(KEY_NAME, inputName.getText().toString());
        savedInstanceState.putString(USER_RESULT, userResult.getText().toString());

        savedInstanceState.putBoolean(CHECKBOX_VALUES[0],checkbox_p1.isChecked());
        savedInstanceState.putBoolean(CHECKBOX_VALUES[1],checkbox_p2.isChecked());
        savedInstanceState.putBoolean(CHECKBOX_VALUES[2],checkbox_p3.isChecked());
        savedInstanceState.putBoolean(CHECKBOX_VALUES[3],checkbox_p4.isChecked());
        savedInstanceState.putBoolean(CHECKBOX_VALUES[4],checkbox_p5.isChecked());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String user_name = savedInstanceState.getString(KEY_NAME, null);
        inputName.setText(user_name);

        String temp = savedInstanceState.getString(USER_RESULT, null);
        userResult.setText(temp);

        checkbox_p1.setChecked(savedInstanceState.getBoolean(CHECKBOX_VALUES[0]));
        checkbox_p2.setChecked(savedInstanceState.getBoolean(CHECKBOX_VALUES[1]));
        checkbox_p3.setChecked(savedInstanceState.getBoolean(CHECKBOX_VALUES[2]));
        checkbox_p4.setChecked(savedInstanceState.getBoolean(CHECKBOX_VALUES[3]));
        checkbox_p5.setChecked(savedInstanceState.getBoolean(CHECKBOX_VALUES[4]));

    }

    // Clear Button functionality
    public void clearAction(View v) {
        // Clear the name edit text
        inputName.getText().clear();
        userResult.setText("");
        // Uncheck all the checkboxes if checked
        ArrayList<CheckBox> check_list = new ArrayList<CheckBox>() {
            {
                add(checkbox_p1);
                add(checkbox_p2);
                add(checkbox_p3);
                add(checkbox_p4);
                add(checkbox_p5);
            }
        };
        for (CheckBox item : check_list) {
            if (item.isChecked()) {
                item.setChecked(false);
            }
        }

    }

    // Submit Button functionality
    public void submitAction(View v) {


        ArrayList<CheckBox> check_list = new ArrayList<CheckBox>() {
            {
                add(checkbox_p1);
                add(checkbox_p2);
                add(checkbox_p3);
                add(checkbox_p4);
                add(checkbox_p5);
            }
        };

        ArrayList<String> pre_list = new ArrayList<String>();
        String userName = inputName.getText().toString();

        for (CheckBox item : check_list) {
            if (item.isChecked()) {
                pre_list.add(item.getText().toString());

            }
        }

        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("preList", pre_list);
        startActivityForResult(intent, requestQode);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode != Activity.RESULT_OK)
        {
            userResult.setText("");
//            Log.e(TAG, "1");
            return;
        }
        if (requestCode == requestQode) {
            if (data == null) {
                userResult.setText("");
//                Log.e(TAG, "2");
                return;
            }
            else{
//                Log.e(TAG, "3");
                userResult.setText("You are " + data.getStringExtra("userState"));
                Toast resultToast
                        = Toast.makeText(
                        getApplicationContext(),
                        "You are " + data.getStringExtra("userState"),
                        Toast.LENGTH_SHORT);
                resultToast.setGravity(Gravity.TOP | Gravity.LEFT, 150, 200);
                resultToast.show();
            }
        }
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