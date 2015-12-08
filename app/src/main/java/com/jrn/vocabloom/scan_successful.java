package com.jrn.vocabloom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Jess on 7/3/2015.
 */
public class scan_successful extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_successful);

        final Context context = this;

        Button clarificationButton = (Button)findViewById(R.id.clarification);
        clarificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Vocab Score:");

                // set dialog message
                alertDialogBuilder
                        .setMessage("This score is calculated by the ratio of your unique words to total words used.")
                        .setCancelable(false)
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
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

        SharedPreferences pref = getApplicationContext().getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);

        TextView time = (TextView) findViewById(R.id.time);
        time.append(pref.getString("time", null));

        TextView score = (TextView) findViewById(R.id.vocab_score);
        score.append(Float.toString(pref.getFloat("score", 0)));
    }
}
