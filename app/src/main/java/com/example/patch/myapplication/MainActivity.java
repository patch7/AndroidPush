package com.example.patch.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private TextView text;
  private Button   btn;

  class Connecting extends AsyncTask<String, Void, String> {
    @Override protected void onPreExecute() {}

    @Override protected String doInBackground(String... params) {
      try {
        StringBuilder buf = new StringBuilder();

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
        return buf.toString();
      }
      catch(Exception e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override protected void onPostExecute(String result) {
      text.setText(result);
      text.setMovementMethod(new ScrollingMovementMethod());
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
