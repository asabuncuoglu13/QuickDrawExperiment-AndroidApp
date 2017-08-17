package com.example.alpay.learnwithdrawing;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class ChatActivity extends FragmentActivity implements UserListFragment.UserListListener{


    int user_id;
    private String mUserName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
             mUserName = bundle.getString("userName");

        setContentView(R.layout.activity_chat);
    }

    @Override
    public void itemClicked(long id) {

        View fragmentContainer = findViewById(R.id.chatfragment_container);

        int array_id = (int) id;
        User selectedUser = User.users.get(array_id);

        if (fragmentContainer != null) {
            UserDetailFragment userdetails = new UserDetailFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            userdetails.setUserName(selectedUser.full_name);
            if(mUserName != null)
                userdetails.setUserName(mUserName);
            ft.replace(R.id.chatfragment_container, userdetails);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        } else {
            if(User.users.get(0).user_id == "Trash")
                return;
            Intent intent = new Intent(this, UserDetailActivity.class);
            intent.putExtra("userName", selectedUser.full_name);
            intent.putExtra("userPoint", selectedUser.point);
            startActivity(intent);
        }
    }
}
