package com.example.alpay.learnwithdrawing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class BaseActivity extends AppCompatActivity {

    private String TAG;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID)
    {
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        mOptions = getResources().getStringArray(R.array.drawer_options);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mOptions));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (useToolbar())
        {
            setSupportActionBar(toolbar);
            setTitle("Activity Title");
        }
        else
        {
            toolbar.setVisibility(View.GONE);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }


    private void selectItem(int position) {
        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        switch (position) {
            case 0:
                sendIntentToPlay();
                break;
            case 1:
                sendIntentToFriendsActivity();
                break;
            case 2:
                sendIntentToChat();
                break;
            case 3:
                sendIntentToPlay();
                break;
            default:
                Log.d(TAG, "onOptionsItemSelected: Invalid Option");
                break;
        }
            mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void sendIntentToPlay()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void sendIntentToFriendsActivity()
    {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    public void sendIntentToChat()
    {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    protected boolean useToolbar()
    {
        return true;
    }
}
