package com.example.alpay.learnwithdrawing;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */

public class UserListFragment extends ListFragment {

    static interface UserListListener {
        void itemClicked(long id);
    }

    String TAG = "UserListFragment";

    int user_id;
    SQLiteDatabase userDB;
    DatabaseHelper userDBHelper;

    private UserListFragment.UserListListener listener;
    UserDetailFragment userdetails = new UserDetailFragment();
    Activity activity;
    DatabaseReference mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private ListAdapter mUserListAdapter;

    private Cursor cursor;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userDBHelper = new DatabaseHelper(inflater.getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mFirebaseRef = database.getReference("users");
        if (isOnline()) {
            Log.d(TAG, "onCreateView: Getting data from firebase");
            mFirebaseRef.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Get map of users in datasnapshot
                            collectData((Map<String, Object>) dataSnapshot.getValue());
                            UserListAdapter adapter = new UserListAdapter(inflater.getContext(), User.users);
                            setListAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                            Log.d(TAG, "onCancelled: Database error");
                        }
                    });
        } else {
            userDB = userDBHelper.getReadableDatabase();
            cursor = userDB.query("USERS", new String[] {"FULLNAME", "POINT"}, "_id = ?", null, null, null, null);
            if (cursor.moveToFirst()) {
                Log.d(TAG, "onCreateView: getting data from SQLite");
                CursorAdapter listAdapter = new SimpleCursorAdapter(inflater.getContext(),
                        android.R.layout.simple_list_item_1,
                        cursor,
                        new String[]{"FULLNAME", "POINT"},
                        new int[]{android.R.id.text1, android.R.id.text1},
                        0);
                setListAdapter(listAdapter);

            } else {
                Log.d(TAG, "onCreateView: No Internet Connection");
                ArrayList<User> no_connection = new ArrayList<User>();
                no_connection.add(new User("Trash", "No Internet Connection", 505));
                User.users = no_connection;
                UserListAdapter adapter = new UserListAdapter(inflater.getContext(), User.users);
                setListAdapter(adapter);
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public void collectData(Map<String, Object> db_users) {

        for (Map.Entry<String, Object> entry : db_users.entrySet()) {

            Map singleUser = (Map) entry.getValue();
            String user_id = (String) singleUser.get("user_id");
            String full_name = (String) singleUser.get("full_name");
            long point = (long) singleUser.get("point");
            User user = new User(user_id, full_name, (int) point);
            Log.d(TAG, user.toString());
            User.users.add(user);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.listener = (UserListFragment.UserListListener) activity;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        new saveToDatabase().execute(User.users);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(cursor!=null)
            cursor.close();
        if(userDB !=null)
            userDB.close();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (listener != null) {
            listener.itemClicked(id);
            int array_id = (int) id;
            User selectedUser = User.users.get(array_id);
            userdetails.setUserName(selectedUser.full_name);
            Log.d("id:", "id is " + id);
        }
    }

    private class saveToDatabase extends AsyncTask<ArrayList<User>, Void, Void> {

        public Void doInBackground(ArrayList<User>... users)
        {
            userDB = userDBHelper.getWritableDatabase();
            userDB = userDBHelper.getWritableDatabase();
            for(int i=0; i<users[0].size(); i++)
                userDBHelper.addUser(userDB, users[0].get(i).user_id, users[0].get(i).full_name, users[0].get(i).point);
            return null;
        }

    }


}
