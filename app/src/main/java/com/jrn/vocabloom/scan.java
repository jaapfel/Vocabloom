package com.jrn.vocabloom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jess on 6/27/2015.
 * Edits by Caitlin on 9/30/2015.
 * Edits by Jess on 10/4/2015.
 * Edits by Caitlin on 10/19/2015.
 * Edits by Jess on 12/7/2015
 */
public class scan extends Activity {

    private final int WAIT_TIME = 250000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanning);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        scanSentMessages();
        startActivity(new Intent(scan.this, scan_successful.class));
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
                        int tempNum = (int) map.get(word[i]) + 1;
                        map.put(word[i], tempNum);
                    }

                    // check if the word is an actual word or a determiner or contraction. If not then add the word to the dictionary
                    else {//if (dictionaryCheck(word[i])) {
                        map.put(word[i], 1); // it looks like maps only take string values
                    }
                }
            }
            analyze((HashMap<String, Integer>) map);
        }
    }

    /**
     * Remove all characters except for space and lowercase and uppercase letters
     *
     * @param msg
     * @return
     */
    public String[] parseMessage(String msg) {
        List<String> excludedWords = Arrays.asList("yeah", "were", "not", "we", "just", "ok", "okey", "okay", "up", "down", "left", "are", "still", "i'll", "a", "an", "and", "as", "at", "the", "to", "too", "for", "nor", "but", "or", "yet", "so", "if", "because", "now", "rather", "who", "what", "where", "when", "why", "how", "whenever", "whether", "which", "while", "whoever", "either", "neither", "it", "it's", "its", "i'm", "my", "your", "i", "be", "you", "me", "she", "he", "him", "her", "his", "this", "is", "of", "with", "can", "by", "then", "there", "here", "was", "would", "have", "had", "did", "do", "that", "their", "in", "on");
        String message = msg.replaceAll("[^a-zA-Z\\'\\s]", "");
        message = message.toLowerCase();
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
            int p = wordList.indexOf(temp);
            if(excludedWords.contains(temp))
            {
                it.remove();
            }
            else if(temp.indexOf('\'')!= -1)
            {
                int pos = temp.indexOf('\'');
                temp = temp.replace("'","");
                wordList.set(p, temp);
            }
        }

        // convert the list back into an array and reallocate the size
        word = wordList.toArray(new String[0]);

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

        String[] topTen = {sortedMap.get(0).getKey(), sortedMap.get(1).getKey(), sortedMap.get(2).getKey(), sortedMap.get(3).getKey(), sortedMap.get(4).getKey(),sortedMap.get(5).getKey(), sortedMap.get(6).getKey(), sortedMap.get(7).getKey(), sortedMap.get(8).getKey(), sortedMap.get(9).getKey(), sortedMap.get(10).getKey(), sortedMap.get(11).getKey(), sortedMap.get(12).getKey()};
        String[] thesaurus = new String[13];

        // Below we find the synonyms using the API
        for(int i=0; i<13; i++) {
            thesaurus[i] = checkThesaurus(topTen[i]);
        }

        // If the thesaurus returned nothing but suggestions, eliminate that word
        for(int i=0; i<13; i++) {
            if(thesaurus[i] == topTen[i]) {
                for(int j=i; j<12; j++) {
                    thesaurus[j] = thesaurus[j+1];
                    topTen[j] = topTen[j+1];
                }
            }
        }

        // Calculate the vocab score
        for (int i : map.values()) {
            sum += i;
        }

        score = (map.size()/((float)sum))*100;
        saveToPreferences(score, topTen, thesaurus);
    }

    /**
     * Save the score and top ten so we can use it in other activities
     *
     * @param score
     * @param topTen
     */
    public void saveToPreferences(float score, String[] topTen, String[] thesaurus)
    {
        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm");
        Date dateobj = new Date();

        SharedPreferences pref = getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat("score", score);
        for(int i =0; i<10; i++) {
            editor.putString(String.valueOf(i), topTen[i]);
            editor.putString(String.valueOf(i+10), thesaurus[i]);
        }
        editor.putString("time", df.format(dateobj));
        editor.commit();
    }

    public String checkThesaurus(final String word) {

        Log.d("msg", "Send Http GET request");
        scan s = new scan();
        scan.Checking check = s.new Checking(word);
        check.doInBackground();
        return check.getSynonym();
    }

    class Checking extends AsyncTask<URL, Integer, Long> {
        String synonym;

        Checking(String word) {
            synonym = word;
        }

        public String getSynonym() {
            return synonym;
        }

        protected Long doInBackground(URL... params) {

            long rand = 0;
            Log.d("msg", "Inside ASYNC Task");
            //String url = "http://www.dictionaryapi.com/api/v1/references/collegiate/xml/"+word+"?key=626f1e47-f620-4bd5-8dc5-a855d151e0a4";
            String url = "http://www.dictionaryapi.com/api/v1/references/thesaurus/xml/" + synonym + "?key=71e05083-b551-4ad9-9a33-4ab2872dbc05";

            URL obj = null;
            try {
                obj = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) obj.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // optional default is GET
            try {
                con.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            //add request header
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = 0;
            try {
                for(int i=0; i<20; i++){
                    responseCode = con.getResponseCode();
                    Log.d("msg", "Sending 'GET' request to URL : " + url);
                    Log.d("msg", "Response Code : " + responseCode);
                    if(responseCode == 200) break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            BufferedReader in = null;
            try {
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String inputLine;
            StringBuffer response = new StringBuffer();

            try {
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //print result
            if (!response.toString().contains("<syn>")) {
                Log.d("msg", "This is not a word. " + synonym);
                return rand;
            } else {
                int posSynonyms = response.toString().indexOf("<syn>");
                int endPosSyn = response.toString().indexOf("</syn>");

                // add 5 to the start position to account for the 5 characters that <syn> takes up
                synonym = response.toString().substring(posSynonyms+5, endPosSyn);
                synonym = synonym.replaceAll("<it>", "");
                synonym = synonym.replaceAll("</it>", "");
                Log.d("msg", "The synonyms are: " + synonym);
            }

            return rand;
        }
    }
}
