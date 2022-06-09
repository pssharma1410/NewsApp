package com.example.newsreader;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listv;
    ListViewAdapter listViewAdapter;
    ArrayList<detailsOfNews> newsl;
    Spinner spinner;
    String[] categories;
    ArrayAdapter<String> adapter;
    ImageView buffering;
    //to be used in webActivity
    static detailsOfNews currclickednews;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Internet is Off!!!", Toast.LENGTH_LONG).show();
        }
    }

    public void bufferinVisible(boolean visible){
        if(visible)
            buffering.setVisibility(View.VISIBLE);
        else
            buffering.setVisibility(View.INVISIBLE);
    }
    public void listVisible(boolean visible){
        if(visible)
            listv.setVisibility(View.VISIBLE);
        else
            listv.setVisibility(View.INVISIBLE);
    }
    public class JsonData extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            //make buffering image
            //make listv invisible
            StringBuilder sb = new StringBuilder();
            URL url = null;
            try {
                url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1){
                    sb.append((char) data+"");
                    data = reader.read();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            bufferinVisible(false);
            listVisible(true);
            try {
                JSONObject jsonObject = new JSONObject(s);
                //news result
                String newsr = jsonObject.getString("results");
                //news array
                JSONArray newsa = new JSONArray(newsr);
                detailsOfNews temp = null;
                JSONObject tempo = null;
                newsl = new ArrayList<>() ;
                for(int i = 0; i <= 20 && i < newsa.length(); i++){
                    tempo = newsa.getJSONObject(i);
                    temp = new detailsOfNews(tempo.getString("title"),tempo.getString("link"));
                    newsl.add(temp);
                }
                listViewAdapter = new ListViewAdapter(getApplicationContext(), newsl);
                listv.setAdapter(listViewAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //add Icon image to everyheadline

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        listv = findViewById(R.id.list);
        newsl = new ArrayList<>();
        buffering = findViewById(R.id.imageView);
        spinner = findViewById(R.id.spinner);
        categories = new String[]{"business","entertainment","environment","food","health","politics","science","sports","technology","top","world"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bufferinVisible(true);
                listVisible(false);
                JsonData jsontemp = new JsonData();
                jsontemp.execute("https://newsdata.io/api/1/news?apikey=pub_81536fd581bb8e7f6ed0a6805ad068b181df&country=in&category="+categories[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               return;
            }
        });
        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currclickednews = newsl.get(position);
                Intent intent = new Intent(MainActivity.this,webActivity.class);
                startActivity(intent);
            }
        });
//
    }
}