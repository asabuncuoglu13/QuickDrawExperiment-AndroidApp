package com.example.alpay.learnwithdrawing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by alpay on 14.07.2017.
 */

public class GetGoogleImagesResult {

    private static String mCount = "4";
    private static String  mStartIndex = "1";
    private static String  mKey = "AIzaSyDoTp6UicPtIH_JVy-cFwoebTEp9-rRHYE";
    private static String  mCX = "007548910499588559159:lepkrgs8oig";
    public static Bitmap[] imageResults = new Bitmap[4];

    public static String[] getFourImageLinks() throws IOException, JSONException {
        String mImageURL = "https://www.googleapis.com/customsearch/v1?q=parrot&num=4&start=1&key=AIzaSyDoTp6UicPtIH_JVy-cFwoebTEp9-rRHYE&cx=007548910499588559159:lepkrgs8oig&searchType=image";
        JSONObject jsonObject = JSONparser.readJsonFromUrl(mImageURL);
        String imageLinks[] = JSONparser.readValueFromJSON(jsonObject);
        return imageLinks;
    }

    public static String[] getFourImageLinks(String searchTerm) throws IOException, JSONException {
        String mImageURL = "https://www.googleapis.com/customsearch/v1?q=" + searchTerm + "&num=" + mCount + "&start=" + mStartIndex + "&key=" + mKey + "&cx=" + mCX + "&searchType=image";
        JSONObject jsonObject = JSONparser.readJsonFromUrl(mImageURL);
        String imageLinks[] = JSONparser.readValueFromJSON(jsonObject);
        return imageLinks;
    }

    public static Bitmap[] getFourImages(String searchTerm) throws IOException, JSONException {
        String imageLinks[] = getFourImageLinks(searchTerm);
        for(int i = 0; i<imageLinks.length; i++)
        {
            URL imageurl = new URL(imageLinks[i]);
            imageResults[i] = BitmapFactory.decodeStream(imageurl.openConnection().getInputStream());
        }
        return imageResults;
    }
}
