package com.example.elad.finalproject;

import android.app.Activity;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends Activity {

    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(getApplicationContext(), TakePhotoActivity.class);
        startActivityForResult(intent, Constants.NEW_IMAGE_ACTIVITY);

        text = findViewById(R.id.mainactivity_text);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.NEW_IMAGE_ACTIVITY) {
            if (resultCode == Constants.SEND_NEW_IMAGE_SUCCEED) {
                String url = data.getStringExtra("url");
                if (url != null && !url.equals("")) {
                    new SendUrlToServerTask().execute(url);
                }
            }
        }
    }

    private class SendUrlToServerTask extends AsyncTask<String, String,String>{

        @Override
        protected String doInBackground(String... strings) {
        //    Looper.prepare(); //For Preparing Message Pool for the child Thread
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            String resMSG = null;
            try {
                String fullURL = "http://193.106.55.167:8889/get/uploadURL?URL=";
                fullURL += strings[0];

                HttpGet get = new HttpGet(fullURL);
                response = client.execute(get);

                if (response != null) { // check which responses the server returns
                    InputStream io = response.getEntity().getContent();
                    BufferedReader br = new BufferedReader(new InputStreamReader(io));
                    Log.d("TAG", "RESPONSE IS NOT NULL");
                    resMSG = br.readLine();
                    Log.d("TAG", resMSG);


                }

            } catch (Exception e) {
                e.printStackTrace();
            }

           // Looper.loop(); //Loop in the message queue
            return resMSG;
        }

        @Override
        protected void onPostExecute(String response) {
            // update gui with answer
            if(response != null)
                text.setText(response);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}
