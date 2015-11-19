package com.jrn.vocabloom;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Jess on 6/27/2015.
 */

public class scan_history extends ListActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public String[] pastScansTime = new String[50];
    String[] pastScans = new String[50];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_history);

        Button backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(scan_history.this, main_menu.class));
            }
        });

        readScanTime();
        readScans();
    }

    private void readScanTime() {
        int i = 0;
        try {
            FileInputStream fis = new FileInputStream (new File(Environment.getExternalStorageDirectory()+"/scanTime.txt"));
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            //StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                //sb.append(line);
                Log.d("msg", "The line is: " + line);
                pastScansTime[i] = line;
                i++;
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get ListView object from xml
        ListView listView = getListView();
        listView.setOnItemClickListener(this);
        Log.d("msg", "Listview: " + listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pastScansTime);

        Log.d("msg", "Listview: " + listView);
        Log.d("msg", "Adapter: " + adapter);
        listView.setAdapter(adapter);
    }

    private void readScans() {
        int i = 0;
        try {
            FileInputStream fis = new FileInputStream (new File(Environment.getExternalStorageDirectory()+"/scanContent.txt"));
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.d("msg", "The line is: " + line);
                pastScans[i] = line;
                i++;
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToPreferencesTime(String time)
    {
        SharedPreferences pref = getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("time", time);
        editor.commit();
    }

    public void saveToPreferences(float score, String[] topTen)
    {
        SharedPreferences pref = getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat("score", score);
        for(int i =0; i<10; i++) {
            editor.putString(String.valueOf(i), topTen[i]);
        }
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        int position = (Integer)v.getTag();
        String [] topTen = new String[10];
        String[] splitStr = pastScans[position].split("\\s+");
        String score = splitStr[0];
        for(int k=1; k<splitStr.length; k++) {
            topTen[k-1] = splitStr[k];
        }
        saveToPreferencesTime(pastScansTime[position]);
        saveToPreferences(Float.parseFloat(score), topTen);
        Log.d("msg", "Successfully made it through scan saving");
        Intent intent = new Intent(this, results.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String [] topTen = new String[10];
        Log.d("msg", "PASTSCANS"+pastScans[position]);
        String[] splitStr = pastScans[position].split("\\s+");
        String score = splitStr[0];
        for(int k=1; k<splitStr.length; k++) {
            topTen[k-1] = splitStr[k];
        }
        saveToPreferencesTime(pastScansTime[position]);
        saveToPreferences(Float.parseFloat(score), topTen);
        Log.d("msg", "Successfully made it through scan saving");
        Intent intent = new Intent(this, results.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
