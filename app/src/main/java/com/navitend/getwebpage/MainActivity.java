/*

MainActivity.java
Frank Ableson for IBM Developerworks



 */
package com.navitend.getwebpage;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity {
    EditText eText;
    TextView tView;
    Button button;
    String tag = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(tag,"onCreate");

        eText = (EditText) findViewById(R.id.address);
        tView = (TextView) findViewById(R.id.pagetext);
        tView.setMovementMethod(new ScrollingMovementMethod());     // allow textView to scroll

        button = (Button) findViewById(R.id.ButtonGo);

        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String url = eText.getText().toString();
                new GetData(tView).execute(url);
            }
        });
    }       // onCreate

    private class GetData extends AsyncTask<String, Void, String> {
        private TextView textView;

        public GetData(TextView textView) {
            this.textView = textView;
        }

        @Override
        protected String doInBackground(String... strings) {
            String webpageData = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder sb = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    sb.append(inputString);
                }

                webpageData = sb.toString();
                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(tag,"Data retrieved from site:" + webpageData);
            return webpageData;
        }

        @Override
        protected void onPostExecute(String pageText) {
            textView.setText(pageText);
        }
    }   // GetData
} // MainActivity class

