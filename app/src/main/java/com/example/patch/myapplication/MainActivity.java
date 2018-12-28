package com.example.patch.myapplication;

import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private TextView text;
  private Button   btn;

  class Connecting extends AsyncTask<String, Void, String> {
    @Override protected void onPreExecute() {}

    @Override protected String doInBackground(String... params) {
      //HttpURLConnection urlConnection = null;

      try {
        /*URL Url = new URL("https://www.stoloto.ru/rapido/archive");
        urlConnection = (HttpURLConnection) Url.openConnection();
        urlConnection.setReadTimeout(10000*//*milliseconds*//*);
        urlConnection.setConnectTimeout(15000*//*milliseconds*//*);
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoInput(true);
        urlConnection.connect();
        int response = urlConnection.getResponseCode();*/
        StringBuilder buf = new StringBuilder();

        /*if(response == HttpURLConnection.HTTP_OK) {
          BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
          String line = null;

          while ((line = reader.readLine()) != null)
            buf.append(line);
        }*/
        int max_tiraz = 0;
        Document doc = Jsoup.connect("https://www.stoloto.ru/rapido/archive").get();
        for(Element el : doc.getElementsByClass("elem")) {
          for(Element e : el.getElementsByClass("draw")) {
            if(max_tiraz < Integer.valueOf(e.text()))
              max_tiraz = Integer.valueOf(e.text());
          }
        }

        for(int i = 1; i < max_tiraz / 100; i += 50) {
          doc = Jsoup.connect("https://www.stoloto.ru/rapido/archive?firstDraw=" + i + "&lastDraw=" + (i + 49) + "&mode=draw").get();
          for(Element el : doc.getElementsByClass("elem")) {
            buf.append(el.text());
            buf.append('\n');
          }
        }
        //buf.append(max_tiraz);
        return buf.toString();
      }
      catch(Exception e) {
        e.printStackTrace();
      }
      /*finally {
        if(urlConnection != null)
          urlConnection.disconnect();
      }*/
      return null;
    }

    @Override protected void onPostExecute(String result) {
      text.setText(result);
    }
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.connect_button:
        new Connecting().execute();
        break;
    }
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    text  = (TextView) findViewById(R.id.text_view);
    btn   = (Button)   findViewById(R.id.connect_button);

    btn.setOnClickListener(this);
  }
}
