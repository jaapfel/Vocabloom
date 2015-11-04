package com.jrn.vocabloom;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jess on 6/27/2015.
 */
public class scan_history extends ListActivity {
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
        String[] pastScans = new String[50];
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
                pastScans[i] = line;
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
        Log.d("msg", "Listview: " + listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pastScans);

        Log.d("msg", "Listview: " + listView);
        Log.d("msg", "Adapter: " + adapter);
        listView.setAdapter(adapter);
        saveToPreferencesTime(pastScans[0]);
    }

    private void readScans() {
        String[] pastScans = new String[50];
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
        int j=0;
        while(pastScans[j] != null) {
            String [] topTen = new String[10];
            String[] splitStr = pastScans[j].split("\\s+");
            String score = splitStr[0];
            for(int k=1; k<splitStr.length; k++) {
                topTen[k-1] = splitStr[k];
            }
            saveToPreferences(Float.parseFloat(score), topTen);
            j++;
        }
        Log.d("msg","Successfully made it through scan saving");
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
}
