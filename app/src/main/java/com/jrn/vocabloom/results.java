package com.jrn.vocabloom;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Jess on 7/3/2015.
 */
public class results extends ListActivity {

    private static final String TEXT1 = "text1";
    private static final String TEXT2 = "text2";

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
        final String[] thesaurus = new String[10];
        for(int i =0; i<10; i++) {
            topTen[i] = pref.getString(String.valueOf(i), "");
            Log.d("msg", "Top ten: " + topTen[i]);
            thesaurus[i] = pref.getString(String.valueOf(i+10), "");
            Log.d("msg", "Thesaurus: " + thesaurus[i]);
        }

        final ListAdapter listAdapter = createListAdapter(topTen, thesaurus);
        setListAdapter(listAdapter);

        Button saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                storeScanTime(pref.getString("time", null));
                storeScan(topTen, (pref.getFloat("score", 0)));
            }
        });
    }

    private List<Map<String, String>> makeListItems(final String[] topTen, final String[] thesaurus) {
        final List<Map<String, String>> listItem =
                new ArrayList<Map<String, String>>(topTen.length);

        for (int i = 0; i< 10; i++) {
            final Map<String, String> listItemMap = new HashMap<String, String>();
            listItemMap.put(TEXT1, topTen[i]);
            listItemMap.put(TEXT2, thesaurus[i]);
            listItem.add(Collections.unmodifiableMap(listItemMap));
        }

        return Collections.unmodifiableList(listItem);
    }

    private ListAdapter createListAdapter(final String[] topTen, final String[] thesaurus) {
        final String[] fromMapKey = new String[] {TEXT1, TEXT2};
        final int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};
        final List<Map<String, String>> list = makeListItems(topTen, thesaurus);

        return new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_2,
                fromMapKey, toLayoutId);
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
