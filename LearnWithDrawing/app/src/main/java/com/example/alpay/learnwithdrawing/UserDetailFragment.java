package com.example.alpay.learnwithdrawing;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetailFragment extends android.app.Fragment implements View.OnClickListener {

    private TextView mUserNameText;
    private TextView mPointText;
    private String mUserName;
    private int mUserPoint;
    private FirebaseListAdapter<ChatMessage> adapter;
    View view;

    public void setUserName(String user_name) {
        this.mUserName = user_name;
    }
    public void setUserPoint(int user_point) {
        this.mUserPoint = user_point;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        Firebase.setAndroidContext(view.getContext());

        mUserNameText = (TextView) view.findViewById(R.id.user_name);
        mUserNameText.setText(mUserName);

        mPointText = (TextView) view.findViewById(R.id.user_point);
        mPointText.setText(Integer.toString(mUserPoint));

        view.findViewById(R.id.send_button).setOnClickListener(this);
        view.findViewById(R.id.send_game_request_button).setOnClickListener(this);
        FirebaseMessaging.getInstance().subscribeToTopic("NewGameRequest");
        displayChatMessages();
        return view;
    }


    public void sendChatMessage() {
        EditText input = (EditText) view.findViewById(R.id.input);

        // Read the input field and push a new instance
        // of ChatMessage to the Firebase database
        FirebaseDatabase.getInstance()
                .getReference()
                .push()
                .setValue(new ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getDisplayName())
                );
        input.setText("");
    }

    public void displayChatMessages() {
        ListView listOfMessages = (ListView) view.findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(getActivity(), ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

            }
        };

        listOfMessages.setAdapter(adapter);
    }

    public static void sendNotificationToUser(String user, final String message) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("notificationRequests");
        ChatMessage cm = new ChatMessage(message, user);
        ref.push().setValue(cm);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.send_button) {
            sendChatMessage();
            displayChatMessages();
        } else if (i == R.id.send_game_request_button) {
            sendNotificationToUser(mUserName, "Play a game?");
        } else {
            Log.d("UserDetailFragment:", "onClick: Invalid button selection.");
        }
    }
}

