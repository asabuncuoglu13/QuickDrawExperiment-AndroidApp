package com.example.alpay.learnwithdrawing;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private PaintView paintView;
    private TextView text;
    Context context;
    private String[] mOptions;
    private String[] mDrawerOptions;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Menu mOptionsMenu;
    int wantedNumber ;
    int detectedNumber;
    String[] imageLinks = {" "," "," "," "};
    Bitmap[] imageBitmaps = new Bitmap[4];

    private DigitDetector mDetector = new DigitDetector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.loadLibrary("tensorflow_mnist");

        wantedNumber = getRandomNumber(0,9);
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        context = getApplicationContext();
        mOptions = getResources().getStringArray(R.array.options_array);

        ListView listOfMessages = (ListView)findViewById(R.id.main_menu_list);
        if(listOfMessages != null)
        {
            mDrawerOptions = getResources().getStringArray(R.array.drawer_options);
            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDrawerOptions);
            listOfMessages.setAdapter(itemsAdapter);
        }
        findViewById(R.id.send).setOnClickListener(this);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        text = (TextView) findViewById(R.id.begin_text);
        text.setText("Draw: "+Integer.toString(wantedNumber));

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mOptions));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle("Paint");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle("Draw Settings");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        paintView = (PaintView) findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.setDrawingCacheEnabled(true);
        paintView.init(metrics);
        new GetJsonTask().execute("lion");
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        switch (position) {
            case 0:
                paintView.blur();
                break;
            case 1:
                paintView.emboss();
                break;
            case 2:
                paintView.clear();
                break;
            case 3:
                paintView.changeColorToRed();
            case 4:
                paintView.changeColorToBlue();
            default:
                paintView.normal();
        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        mOptionsMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.settings).setVisible(!drawerOpen);
        mOptionsMenu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private int getRandomNumber(int min,int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context context = getApplicationContext();
        switch(item.getItemId()) {
            case R.id.settings:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                return true;
            case R.id.chat:
                Intent intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
                return true;
            case R.id.save:
                File_Ops.saveImage(paintView, context);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetJsonTask extends AsyncTask<String, String, String> {
        public String doInBackground(String... searchterm)
        {
            try {
                imageLinks = GetGoogleImagesResult.getFourImageLinks(searchterm[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    private class GetBitmapTask extends AsyncTask<String, String, String> {
        public String doInBackground(String... searchterm)
        {
            try {
                imageBitmaps = GetGoogleImagesResult.getFourImages(searchterm[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public boolean detectDigit(PaintView pv, int wantedNumber)
    {
        Bitmap bm = File_Ops.scaleBitmap(pv, 28, 28);
        int[] pixels = File_Ops.BitmapToPixelArray(bm);
        detectedNumber = mDetector.detectDigit(pixels);
        if(wantedNumber == detectedNumber)
            return true;
        else
            return false;
    }

    public void sendFalseIntent(String[] imageLinks)
    {
        Intent intent = new Intent(MainActivity.this, WrongAnswerActivity.class);
        intent.putExtra("object", Integer.toString(wantedNumber));
        startActivity(intent);
    }

    public void sendTrueIntent()
    {
        Intent intent = new Intent(MainActivity.this, TrueAnswerActivity.class);
        startActivity(intent);
    }

    public void onClick(View v)
    {
        int i = v.getId();
        if (i == R.id.send)
        {
            Boolean detectedTrue = false;
            try{
                detectedTrue = detectDigit(paintView, wantedNumber);
            }catch (java.lang.UnsatisfiedLinkError u)
            {
                Log.d("Detection:", "UnsatisfiedLinkError");
            }
            if(!detectedTrue)
                sendFalseIntent(imageLinks);
            else
                sendTrueIntent();
        }else
        {
            Toast.makeText(this, "Not a valid button", Toast.LENGTH_SHORT).show();
        }

    }

}