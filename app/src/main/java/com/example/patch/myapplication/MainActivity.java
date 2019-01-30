package com.example.patch.myapplication;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
  private TextView text;
  private Button   btn;
  private Button   clear;
  private Button   save;

  class Connecting extends AsyncTask<String, Void, String>
  {
    @Override protected void onPreExecute() {}

    @Override protected String doInBackground(String... params)
    {
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

    @Override protected void onPostExecute(String result)
    {
      text.setText(result);
      text.setMovementMethod(new ScrollingMovementMethod());
    }
  }

  @Override public void onClick(View v)
  {
    switch (v.getId()) {
      case R.id.connect_button: new Connecting().execute(); break;
      case R.id.clear_button:   text.setText("");           break;
      case R.id.save_btn:       saveInFileSD();             break;
    }
  }

  @Override protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    text  = (TextView) findViewById(R.id.text_view);
    btn   = (Button)   findViewById(R.id.connect_button);
    clear = (Button)   findViewById(R.id.clear_button);
    save  = (Button)   findViewById(R.id.save_btn);

    btn.setOnClickListener(this);
    clear.setOnClickListener(this);
    save.setOnClickListener(this);
  }

  private void saveInFileSD()
  {
    if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
      text.setText("Save failed!\nSD card not found!");

    File path = Environment.getExternalStorageDirectory();
    File file = new File(path, "DB.txt");

    try{
      BufferedWriter bwrite = new BufferedWriter(new FileWriter(file));
      bwrite.write(text.getText().toString());
      bwrite.close();
      text.setText("Successful!");
    }
    catch(IOException e) {
      text.setText(e.getMessage() + "Exception");
    }
  }
}
