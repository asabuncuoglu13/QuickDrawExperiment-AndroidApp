package com.example.alpay.learnwithdrawing;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private PaintView paintView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        paintView = (PaintView) findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.setDrawingCacheEnabled(true);
        paintView.init(metrics);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context context = getApplicationContext();
        switch(item.getItemId()) {
            case R.id.normal:
                paintView.normal();
                return true;
            case R.id.emboss:
                paintView.emboss();
                return true;
            case R.id.blur:
                paintView.blur();
                return true;
            case R.id.blue:
                paintView.changeColorToBlue();
                return true;
            case R.id.red:
                paintView.changeColorToRed();
                return true;
            case R.id.clear:
                new GetJsonTask().execute();
                paintView.clear();
                return true;
            case R.id.save:
                File_Ops.saveImage(paintView, context);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetJsonTask extends AsyncTask<Void, Void, Void> {

        String[] imageLinks = {" "," "," "," "};
        public Void doInBackground(Void... voids)
        {
            try {
                imageLinks = GetGoogleImagesResult.getFourImageLinks();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}