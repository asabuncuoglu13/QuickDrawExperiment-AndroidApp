package com.example.alpay.learnwithdrawing;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class FriendsActivity extends BaseActivity{

    private DatabaseReference mFirebaseRef;
    private String TAG = "FriendsActivity";
    private Cursor cursor;
    ListView friendsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        friendsList = (ListView) findViewById(R.id.friendslist);

        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView, View v,int position,long id) {
                        switch (position) {
                            case 0:
                                if (User.friends.get(0).user_id == "Trash") {
                                    Intent intent = new Intent(FriendsActivity.this, ChatActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(FriendsActivity.this, UserDetailActivity.class);
                                    intent.putExtra("userName", User.friends.get(position).full_name);
                                    intent.putExtra("userPoint", User.friends.get(position).point);
                                    startActivity(intent);
                                }
                                default:
                                    Intent intent = new Intent(FriendsActivity.this, UserDetailActivity.class);
                                    intent.putExtra("userName", User.friends.get(position).full_name);
                                    intent.putExtra("userPoint", User.friends.get(position).point);
                                    startActivity(intent);
                        }
                    }
                };

        friendsList.setOnItemClickListener(itemClickListener);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mFirebaseRef = database.getReference("users");

        mFirebaseRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectData((Map<String, Object>) dataSnapshot.getValue());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        if (User.friends.size() <= 0) {
            User.friends.add(new User("Trash", "You have no friends, let's add some!", 0));
        }
        ArrayList<String> friend_names = new ArrayList<String>();
        int i = 0;
        for (User u : User.friends) {
            friend_names.add(User.friends.get(i).full_name);
            i++;
        }

        ArrayAdapter<String> userlistadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friend_names);
        friendsList.setAdapter(userlistadapter);
    }

    @Override
    public boolean onSearchRequested()
    {
        Bundle appData = new Bundle();
        appData.putBoolean(SearchableActivity.JARGON, true);
        startSearch(null, false, appData, false);
        return super.onSearchRequested();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context context = getApplicationContext();
        switch(item.getItemId()) {
            case R.id.action_search:
                onSearchRequested();
                return true;
            default:
                Log.d(TAG, "onOptionsItemSelected: Undefined options menu item");
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =(SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    public void collectData(Map<String, Object> db_users) {

        for (Map.Entry<String, Object> entry : db_users.entrySet()) {

            Map singleUser = (Map) entry.getValue();
            String user_id = (String) singleUser.get("user_id");
            String full_name = (String) singleUser.get("full_name");
            long point = (long) singleUser.get("point");
            User user = new User(user_id, full_name, (int) point);
            User.friends.add(user);
        }
    }

}
