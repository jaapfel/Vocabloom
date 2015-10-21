package com.jrn.vocabloom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jess on 6/27/2015.
 * Edits by Caitlin on 9/30/2015.
 * Edits by Jess on 10/4/2015.
 * Edits by Caitlin on 10/19/2015.
 */
public class scan extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanning);

        scanSentMessages();
    }

    /**
     * Scan the sent text messages of the phone and save them in a hashmap
     */
    public void scanSentMessages() {
        Map map = new HashMap<String, Integer>();
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
                String type = cursor1.getString(cursor1
                        .getColumnIndex(columns[4]));

                // remove all characters except for space and lowercase and uppercase letters
                String[] word = parseMessage(msg);

                // It looks like the Dictionary functionality is now obsolete so we should use a map, which essentially does the same thing
                // NOTE: will want to replace "map" with whatever the dictionary/map is called
                for (int i = 0; i < word.length; i++) {
                    // if the word already exists in the dictionary/map then increment the count
                    if (!map.isEmpty() && map.containsKey(word[i])) {
                        int tempNum = (int)map.get(word[i]) + 1;
                        map.put(word[i], tempNum);
                    }

                    // check if the word is an actual word or a determiner or contraction. If not then add the word to the dictionary
                    else {//if (dictionaryCheck(word[i])) {
                        map.put(word[i], 1); // it looks like maps only take string values
                        Log.d("msg", "Word added to dictionary");
                    }
                }
            }
            analyze((HashMap<String, Integer>) map);
        }
        startActivity(new Intent(scan.this, scan_successful.class));
    }

    /**
     * Remove all characters except for space and lowercase and uppercase letters
     *
     * @param msg
     * @return
     */
    public String[] parseMessage(String msg) {
        List<String> excludedWords = Arrays.asList("a", "an", "and", "as", "at", "the", "to", "too", "for", "nor","but", "or", "yet", "so", "if", "because", "now", "rather", "who", "what", "where", "when", "why", "how", "whenever", "whether", "which", "while", "whoever", "either", "neither", "it", "i", "be", "you", "me", "she", "he", "him", "her", "his", "this", "is", "of", "with", "can", "by", "then", "there", "here", "was", "would", "have", "had", "did", "do", "that", "their", "in", "on");
        String message = msg.replaceAll("[^a-zA-Z\\s]", "");
        message = message.toLowerCase();
        Log.e("msg", msg);
        // split the message up into words
        String [] word = message.split(" ");

        // switch to a list because it's easier to  search through
        List<String> wordList = new ArrayList<String>(Arrays.asList(word));

        // checks for an empty string and removes it if it exists
        if (wordList.contains(""))
        {
            wordList.removeAll(Arrays.asList(""));
        }

        // iterate through wordList and remove any word from the list that is common (ex: it, as, a, etc...)
        Iterator<String> it = wordList.iterator();
        while(it.hasNext())
        {
            String temp = it.next();
            if(excludedWords.contains(temp))
            {
                it.remove();
            }
        }

        // convert the list back into an array and reallocate the size
        word = wordList.toArray(new String[0]);
        Log.d("words", "words: " + Arrays.toString(word));

        return word;
    }

    /**
     * Figure out the vocab score and top ten used words
     *
     * @param map
     */
    public void analyze(HashMap<String, Integer> map) {

        int sum = 0;
        float score = 0;

        // Sort the list by values to find the top ten used words
        List<Map.Entry<String,Integer>> sortedMap = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
        Collections.sort(sortedMap,
                new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                }
        );

        String[] topTen = {sortedMap.get(0).getKey(), sortedMap.get(1).getKey(), sortedMap.get(2).getKey(), sortedMap.get(3).getKey(), sortedMap.get(4).getKey(),sortedMap.get(5).getKey(), sortedMap.get(6).getKey(), sortedMap.get(7).getKey(), sortedMap.get(8).getKey(), sortedMap.get(9).getKey(),};

        // Calculate the vocab score
        for (int i : map.values()) {
            sum += i;
        }

        score = (map.size()/((float)sum))*100;
        saveToPreferences(score, topTen);
    }

    /**
     * Save the score and top ten so we can use it in other activities
     *
     * @param score
     * @param topTen
     */
    public void saveToPreferences(float score, String[] topTen)
    {
        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm");
        Date dateobj = new Date();

        SharedPreferences pref = getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat("score", score);
        for(int i =0; i<10; i++) {
            editor.putString(String.valueOf(i), topTen[i]);
        }
        editor.putString("time", df.format(dateobj));
        editor.commit();
    }

    /**
     * Determines if a word is in the English Language
     *
     * @param word
     * @return true if found in dictionary, false if not found
     */
    public static boolean dictionaryCheck(String word) {
        try {
            BufferedReader in = new BufferedReader(new FileReader("/usr/share/dict/american-english"));
            String dictionary;
            while((dictionary = in.readLine()) != null) {
                if(dictionary.indexOf(word) != -1) {
                    return true;
                }
            }
            in.close();
        } catch(IOException e) {
            Log.e("msg", "Failing dictionary Check on: " + word);
        }
        return false;
    }

}
