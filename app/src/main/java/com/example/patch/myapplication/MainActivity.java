package com.example.patch.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView text;
    private EditText eText;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.text_view);
        eText = (EditText) findViewById(R.id.URL);
        btn = (Button) findViewById(R.id.connect_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Connecting().execute();
            }
        });
    }

    class Connecting extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            HttpURLConnection urlConnection = null;
            try
            {
                URL Url = new URL(eText.getText().toString());
                urlConnection = (HttpURLConnection) Url.openConnection();
                urlConnection.setReadTimeout(10000/*milliseconds*/);
                urlConnection.setConnectTimeout(15000/*milliseconds*/);
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();
                int response = urlConnection.getResponseCode();
                StringBuilder buf = new StringBuilder();

                if(response == HttpURLConnection.HTTP_OK);
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line = null;
                    while ((line = reader.readLine()) != null)
                        buf.append(line);
                }
                return buf.toString();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            text.setText(result);
        }

        @Override
        protected void onPreExecute()
        {
            ;
        }
    }
}
