package com.jrn.vocabloom;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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


/**
 * Created by Jess on 7/3/2015.
 */
public class results extends ListActivity{

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

        SharedPreferences pref = getApplicationContext().getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);

        TextView time = (TextView) findViewById(R.id.scanTime);
        time.append(pref.getString("time", null));

        String[] topTen = new String[10];
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

    }
}
