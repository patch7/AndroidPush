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
                new Connect().execute();
            }
        });
    }

    class Connect extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            HttpURLConnection urlConnection = null;
            try
            {
                URL Url = new URL(eText.getText().toString());
                urlConnection = (HttpURLConnection) Url.openConnection();
                int response = urlConnection.getResponseCode();

                if(response == HttpURLConnection.HTTP_OK)
                    //To be continue...
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
