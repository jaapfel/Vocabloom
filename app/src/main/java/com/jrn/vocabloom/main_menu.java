package com.jrn.vocabloom;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Jess on 6/27/2015.
 */
public class main_menu extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button scanButton = (Button)findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(main_menu.this, scan.class));
            }
        });

        Button reviewButton = (Button)findViewById(R.id.reviewButton);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(main_menu.this, scan_history.class));
            }
        });
    }
}
