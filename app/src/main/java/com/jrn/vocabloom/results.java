package com.jrn.vocabloom;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


/**
 * Created by Jess on 7/3/2015.
 */
public class results extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        Button viewResultsButton = (Button)findViewById(R.id.backButton);
        viewResultsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(results.this, scan_successful.class));
            }
        });

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);

        TextView time = (TextView) findViewById(R.id.scanTime);
        time.append(pref.getString("time", null));

        final String[] topTen = new String[10];
        for(int i =0; i<10; i++) {
            topTen[i] = pref.getString(String.valueOf(i), "");
            Log.d("msg", "Top ten: " + topTen[1]);
        }

        // Get ListView object from xml
        ListView listView = getListView();
        Log.d("msg", "Listview: " + listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topTen);
        Log.d("msg", "Listview: " + listView);
        Log.d("msg", "Adapter: " + adapter);
        listView.setAdapter(adapter);

        Button saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                storeScanTime(pref.getString("time", null));
                storeScan(topTen, (pref.getFloat("score", 0)));
            }
        });
    }

    private void storeScanTime(String timeScanned) {
        try {
            File scanFile = new File(Environment.getExternalStorageDirectory()+"/scanTime.txt");
            if(!scanFile.exists()){
                scanFile.createNewFile();
            }
            FileOutputStream fOut = new FileOutputStream(scanFile, true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
            outputStreamWriter.append(timeScanned);
            Log.d("msg", "The time being stored is: " + timeScanned);
            outputStreamWriter.append("\n");
            outputStreamWriter.close();
            fOut.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void storeScan(String[] topTen, float score) {
        try {
            File scanFile = new File(Environment.getExternalStorageDirectory()+"/scanContent.txt");
            if(!scanFile.exists()){
                scanFile.createNewFile();
            }
            FileOutputStream fOut = new FileOutputStream(scanFile, true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
            outputStreamWriter.append(Float.toString(score));
            Log.d("msg", "The score being stored is: " + score);
            for (int i = 0; i < topTen.length; i++) {
                outputStreamWriter.append(" " + topTen[i]);
                Log.d("msg", "The thing being stored is: " + topTen[i]);
            }
            outputStreamWriter.append("\n");
            outputStreamWriter.close();
            fOut.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
