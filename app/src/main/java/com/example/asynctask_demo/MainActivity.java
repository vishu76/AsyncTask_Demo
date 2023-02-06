package com.example.asynctask_demo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String title, image, category;
    TextView titleTextView, categoryTextView;
    ProgressDialog progressDialog;
    Button displayData;
    ImageView imageView;
    String apiUrl = "https://vishu76.github.io/jsonApi/anasmovie.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get the reference of View's
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        categoryTextView = (TextView) findViewById(R.id.categoryTextView);
        displayData = (Button) findViewById(R.id.displayData);
        imageView = (ImageView) findViewById(R.id.imageView);
        // implement setOnClickListener event on displayData button
        displayData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create object of MyAsyncTasks class and execute it
                MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
                myAsyncTasks.execute();
            }
        });
    }
    public class MyAsyncTasks extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
             progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrl);


                    urlConnection = (HttpURLConnection) url
                            .openConnection();

                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                        System.out.print(current);
                        publishProgress(data);

                    }
                    // return the data to onPostExecute method
                    return current;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }



        @Override
        protected void onPostExecute(String s) {

           // Log.d("data", s.toString());
            // dismiss the progress dialog after receiving data from API
            progressDialog.dismiss();
            try {
                // JSON Parsing of data
                JSONArray jsonArray = new JSONArray(s);

                JSONObject oneObject = jsonArray.getJSONObject(0);
                // Pulling items from the array
                title = oneObject.getString("title");
                category = oneObject.getString("overview");
                image = oneObject.getString("poster");
                // display the data in UI
                titleTextView.setText("Title: "+title);
                categoryTextView.setText("Category: "+category);
                // Picasso library to display the image from URL
                /*Picasso.get(getApplicationContext())
                        .load(image)
                        .into(imageView);*/
                Picasso.get().load(image)
                        .into(imageView);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
             progressDialog.setProgress(values[0]);
             Log.i("check",""+values[0]);

        }
    }
}