package com.jrn.vocabloom;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import java.util.*;

/**
 * Created by Jess on 6/27/2015.
 * Edits by Caitlin on 9/30/2015.
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
        List vocabulary = new ArrayList(); // list for the words that are going to be parsed.
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
                // MSG is what you will do the parsing on in this while loop. Other values aren't necessary I think.
                // Might want to check type first to ensure we aren't trying to parse an image.
                String type = cursor1.getString(cursor1
                        .getColumnIndex(columns[4]));

                // NOTE: We'll probably want a check to see if the message is an image or text.
                //       I'm not really sure what "type" returns so I'm not really sure how to do the comparison

                // remove all characters except for space and lowercase and uppercase letters
                String message = msg.replaceAll("[^a-zA-Z\\s]", "");
                Log.e("msg", msg);
                // split the message up into words
                String[] word = message.split(" ");
                Log.d("words", "words: " + Arrays.toString(word));

                // The words will eventually be stored in a Map. For now though it will just be stored in an array.
                // It looks like the Dictionary functionality is now obsolete so we should use a map, which essentially does the same thing
                // NOTE: will want to replace "map" with whatever the dictionary/map is called
                /*for (int i = 0; i < word.length; i++) {
                    // if the word already exists in the dictionary/map then increment the count
                    if (map.containsKey(word[i]) {
                        int tempNum = Integer.parseInt(map.get(word[i])) + 1;
                        map.put(word[i], Integer.toString(tempNum));
                    }
                    // check if the word is an actual word or a determiner or contraction. If not then add the word to the dictionary
                    else if () {
                        map.put(word[i], "1"); // it looks like maps only take string values
                    }
                }*/
            }
        }
    }
}
