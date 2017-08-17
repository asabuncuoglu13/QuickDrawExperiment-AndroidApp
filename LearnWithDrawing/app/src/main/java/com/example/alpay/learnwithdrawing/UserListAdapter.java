package com.example.alpay.learnwithdrawing;

/**
 * Created by alpay on 11.08.2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserListAdapter extends ArrayAdapter<User> {

    public UserListAdapter(Context context, ArrayList<User> users) {
        super(context, android.R.layout.simple_list_item_1, users);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.user, parent, false);
        }
        // Lookup view for data population
        String full_name = user.getFull_name();
        int point = user.getPoint();
        TextView fullnameText = (TextView) view.findViewById(R.id.fullnametext);
        fullnameText.setText(full_name + ": ");
        TextView pointText = (TextView) view.findViewById(R.id.pointtext);
        pointText.setText(Integer.toString(point));
        // Return the completed view to render on screen
        return view;
    }
}