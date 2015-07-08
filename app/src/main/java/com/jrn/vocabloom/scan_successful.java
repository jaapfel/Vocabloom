package com.jrn.vocabloom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Jess on 7/3/2015.
 */
public class scan_successful extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_successful);

        Button clarificationButton = (Button)findViewById(R.id.clarification);
        clarificationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(scan_successful.this, clarification.class));
            }
        });

        Button viewResultsButton = (Button)findViewById(R.id.view_results);
        viewResultsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(scan_successful.this, results.class));
            }
        });

        Button mainMenuButton = (Button)findViewById(R.id.main_menu);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(scan_successful.this, main_menu.class));
            }
        });
    }
}
