package com.example.alpay.learnwithdrawing;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by alpay on 14.07.2017.
 */

public class JSONparser {

    private static final String TAG = "JSONParse";
    
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String urlString) throws IOException, JSONException {

        URL url = new URL(urlString);
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader reader = new InputStreamReader(in);
            String jsonText = readAll(reader);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            urlConnection.disconnect();
        }
    }

    /* This is not a general method*/
    /* This method is only written to get the 4 links for image objects in JSON file*/

    public static String[] readValueFromJSON(JSONObject jObj) throws JSONException {
        String imageLinkArray[] = new String[4];
        for(int i =0; i<imageLinkArray.length; i++)
        {
            imageLinkArray[i] = jObj.getJSONArray("items").getJSONObject(i).getString("link");
            Log.d(TAG, imageLinkArray[i]);
        }
        return imageLinkArray;
    }
}
