package com.example.assignment2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;



public class DownloadMusicFragment extends Fragment {

    Button check_connect_button;
    EditText link_value;
    EditText name_value;
    private DownloadTask mDownloadTask;

    Button check_connection_button;



    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean mDownloading = false;


    public DownloadMusicFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_download_music, container, false);

        // Button for download song
        check_connect_button = v.findViewById(R.id.check_connection_button);
        // Button for connection check
        check_connection_button = v.findViewById(R.id.check);

        link_value = v.findViewById(R.id.link_input);
        name_value = v.findViewById(R.id.name_input);

        // Implementing the download of song
        check_connect_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // for consecutive download clicks and if initially connection is on
                if(!mDownloading){
                    Log.d("start download", "START DOWNLOAD FUNCTION");
                    // Execute the async download.
                    startDownload(link_value.getText().toString().trim(), name_value.getText().toString().trim());
                    mDownloading = true;
                }

            }
        });

        check_connection_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(new CheckNetwork(getActivity()).isNetworkAvailable()){
                    Toast.makeText(getActivity(),"You Are Connected", Toast.LENGTH_SHORT).show();


                }
                else{

                    Toast.makeText(getActivity(),"Not Active Connection", Toast.LENGTH_SHORT).show();
                    Log.d("NETWORK", "NOT CONNECTED");
                }
            }
        });


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        cancelDownload();
        super.onDestroy();
    }

    /**
     * Start non-blocking execution of DownloadTask.
     */
    public void startDownload(String link , String name) {
//        Log.d("ASYNC", "1");
        cancelDownload();
//        Log.d("ASYNC", "2");
        mDownloadTask = new DownloadTask();
//        Log.d("ASYNC", "3");
        mDownloadTask.execute(link, name);
//        Log.d("ASYNC", "4");
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing DownloadTask execution.
     */
    public void cancelDownload(){
        if (mDownloadTask != null) {

            mDownloadTask.cancel(true);
            mDownloadTask = null;
            mDownloading = false;

        }
    }

    // Class to check if there is any active connection or not , and connection is connected or not
    public class CheckNetwork {
        private Context context;

        public CheckNetwork(Context context) {
            this.context = context;
        }

        public boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager
                    .getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }


    // Async Task Class
    class DownloadTask extends AsyncTask<String, Integer, String> {

        ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            // If internet available start progess dialog box otherwise display no internet
            Log.d("ASYNC", "Welcome to OnPreExecute");
            if(!new CheckNetwork(getActivity()).isNetworkAvailable())
            {
                Toast.makeText(getActivity(), "no internet!", Toast.LENGTH_SHORT).show();
                cancel(true);
            }
            else{
                // Progress Dialog Box updations
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Download in progress...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMax(100);
            progressDialog.setProgress(0);
                progressDialog.show();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("ASYNC", "Welcome doInBack");
            // Checking cancelled or not then only doInBack
            if (!isCancelled() && params != null && params.length > 0) {
                // Setting path url and name of the downloading file
                String path = params[0];
                String name = params[1];
                // lengh of the file to be downloaded
                int file_length;
                // Connection object
                HttpURLConnection connection = null;
                Log.d("ASYNC", "Welcome");
                try {
                    // setting up connection to url
                    URL url = new URL(path);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {

                        Log.d("Network", "HTTP Error");
                        return null;
                    }


                    // Length og the file to be downloaded
                    file_length = connection.getContentLength();


                    /**
                     * Create an output file to store the image for download
                     */

                    OutputStream outputStream = new FileOutputStream(getActivity().getFilesDir() + "/" + name + ".mp3");
                    // input stream
                    InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                    byte[] data = new byte[1024];
                    int total = 0;
                    int count;
                    // Transfering of data ffro input to output and publish progress
                    while ((count = inputStream.read(data)) != -1) {
                        total += count;

                        outputStream.write(data, 0, count);
                        int progress = 100 * total / file_length;
                        publishProgress(progress);


                    }

                    if (inputStream != null)
                        inputStream.close();
                    if (outputStream != null)
                        outputStream.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null)
                        connection.disconnect();
                }

                return "Completed Download";

            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d("ASYNC", "Welcome to Onprogress");
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("ASYNC", "Welcome to onpost");
            progressDialog.hide();
            progressDialog.dismiss();
            if(result!=null)
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getActivity(), "Not Downloaded", Toast.LENGTH_LONG).show();

            mDownloading = false;


        }
//        Runs on the UI thread after cancel(boolean) is invoked and doInBackground(java.lang.Object[]) has finished.
        protected void onCancelled (){
            Log.d("ASYNC", "Task Cancelled");
            mDownloading = false;

        }
    }


}







//    class DownloadTask extends AsyncTask<String, Integer, String> {
//
//        ProgressDialog progressDialog;
//        private Context mContext;
//        /**
//         * Set up a ProgressDialog
//         */
//        @Override
//        protected void onPreExecute() {
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setTitle("Download in progress...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressDialog.setMax(100);
//            progressDialog.setProgress(0);
//            progressDialog.show();
//
//        }
//
//        /**
//         *  Background task
//         */
//        @Override
//        protected String doInBackground(String... params) {
//            String path = params[0];
//            String name = params[1];
//            int file_length;
//
////            Log.i("Info: path", path);
//            try {
//                URL url = new URL(path);
//                URLConnection urlConnection = url.openConnection();
//                urlConnection.setReadTimeout(3000);
//                urlConnection.setConnectTimeout(15000);
////                urlConnection.setRequestMethod("GET");
//
//                urlConnection.connect();
//                int responseCode;
////                responseCode = urlConnection.getResponseCode();
//////                Toast.makeText(getActivity(), "Connected", Toast.LENGTH_LONG).show();
//                file_length = urlConnection.getContentLength();
////                if (responseCode != HttpsURLConnection.HTTP_OK) {
////
////                    Toast.makeText(getActivity(), "Not Connected", Toast.LENGTH_SHORT).show();
////                    Log.i("CONNECTION MESSAGE", "NOT CONNECTED");
////                    throw new IOException("HTTP error code: " + responseCode);
////                }
////                else{
////                    Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
////                    Log.i("CONNECTION MESSAGE", "CONNECTED");
////                }
//
//                /**
//                 * Create an output file to store the image for download
//                 */
//
//                OutputStream outputStream = new FileOutputStream(getActivity().getFilesDir() + "/" + name +".mp3");
//
//                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
//                byte [] data = new byte[1024];
//                int total = 0;
//                int count;
//                while ((count = inputStream.read(data)) != -1) {
//                    total += count;
//
//                    outputStream.write(data, 0, count);
//                    int progress = 100 * total / file_length;
//                    publishProgress(progress);
//
////                    Log.i("Info", "Progress: " + Integer.toString(progress));
//                }
//                inputStream.close();
//                outputStream.close();
//
////                Log.i("Info", "file_length: " + Integer.toString(file_length));
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return "Download complete.";
//        }
//
//
//
//
////        try {
////            connection = (HttpsURLConnection) url.openConnection();
////
////            connection.setReadTimeout(3000);
////
////            connection.setConnectTimeout(3000);
////
////            connection.setRequestMethod("GET");
////
////
////            connection.setDoInput(true);
////
////            connection.connect();
////
////            int responseCode = connection.getResponseCode();
////            if (responseCode != HttpsURLConnection.HTTP_OK) {
////                throw new IOException("HTTP error code: " + responseCode);
////            }
////            // Retrieve the response body as an InputStream.
////            stream = connection.getInputStream();
////            publishProgress(DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);
////            if (stream != null) {
////                // Converts Stream to String with max length of 500.
////                result = readStream(stream, 500);
////                publishProgress(DownloadCallback.Progress.PROCESS_INPUT_STREAM_SUCCESS, 0);
////            }
////        } finally {
////            // Close Stream and disconnect HTTPS connection.
////            if (stream != null) {
////                stream.close();
////            }
////            if (connection != null) {
////                connection.disconnect();
////            }
////        }
//
//
//
//
//
//
//
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            progressDialog.setProgress(values[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            progressDialog.hide();
//            progressDialog.dismiss();
//            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
//            mDownloading = false;
//
//
//        }
//    }
