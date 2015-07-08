package com.jrn.vocabloom;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


/**
 * Created by Jess on 6/27/2015.
 */
public class scan extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanning);
        Uri mSmsinboxQueryUri = Uri.parse("content://sms/sent");
        Cursor cursor1 = getContentResolver().query(mSmsinboxQueryUri, new String[]{"_id", "thread_id", "address", "person", "date", "body", "type"}, null, null, null);
        startManagingCursor(cursor1);
        String[] columns = new String[]{"address", "person", "date", "body",
                "type"};
        if (cursor1.getCount() > 0) {
            String count = Integer.toString(cursor1.getCount());
            Log.e("Count", count);
            while (cursor1.moveToNext()) {
                String address = cursor1.getString(cursor1
                        .getColumnIndex(columns[0]));
                String name = cursor1.getString(cursor1
                        .getColumnIndex(columns[1]));
                String date = cursor1.getString(cursor1
                        .getColumnIndex(columns[2]));
                String msg = cursor1.getString(cursor1
                        .getColumnIndex(columns[3]));
                String type = cursor1.getString(cursor1
                        .getColumnIndex(columns[4]));
            }
        }
    }
}
