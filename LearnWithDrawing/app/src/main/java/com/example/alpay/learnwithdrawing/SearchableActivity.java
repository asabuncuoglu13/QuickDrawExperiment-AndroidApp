package com.example.alpay.learnwithdrawing;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class SearchableActivity extends AppCompatActivity {

    private ListView listView;
    SQLiteDatabase userDB;
    DatabaseHelper userDBHelper;
    Cursor cursor;
    static String JARGON;
    private String TAG = "Search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Bundle appData = getIntent().getBundleExtra(SearchManager.APP_DATA);

        // create database object databaseObject = new DbBackend(SearchableActivity.this);
        listView = (ListView) findViewById(R.id.searchlist);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                        Intent intent = new Intent(SearchableActivity.this, UserDetailActivity.class);
                        intent.putExtra("userName", User.users.get(position).full_name);
                        intent.putExtra("userPoint", User.users.get(position).point);
                        startActivity(intent);
                    }
                });

        if (appData != null) {
            Log.d(TAG, "onCreate: Jargon is " + SearchableActivity.JARGON);
            boolean jargon = appData.getBoolean(SearchableActivity.JARGON);
        }

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            userDBHelper = new DatabaseHelper(this);
            Log.d(TAG, "onCreate: Query is" + query);
            doMySearch(query);
        }
    }


    private void doMySearch(String query) {
        try {
            userDB = userDBHelper.getReadableDatabase();
            cursor = userDB.query("USERS", new String[]{"_id", "FULLNAME"}, "FULLNAME LIKE ?", new String[]{"%" + query + "%"}, null, null, null);
            if (cursor.moveToFirst()) {
                Log.d(TAG, "onCreateView: getting data from SQLite");
                CursorAdapter listAdapter = new SimpleCursorAdapter(this,
                        android.R.layout.simple_list_item_1,
                        cursor,
                        new String[]{"FULLNAME"},
                        new int[]{android.R.id.text1},
                        0);
                listView.setAdapter(listAdapter);
            } else {
                Toast.makeText(this, "No data found with: " + query, Toast.LENGTH_SHORT);
            }
        } catch (SQLiteException error) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null)
            cursor.close();
        if (userDB != null)
            userDB.close();
    }

}
