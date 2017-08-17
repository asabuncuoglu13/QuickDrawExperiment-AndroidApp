package com.example.alpay.learnwithdrawing;

import android.os.Bundle;
import android.view.View;
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

public class UserDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView mUserNameText;
    private TextView mPointText;
    private String mUserName;
    private int mUserPoint;
    private String myUserName;
    private FirebaseListAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        Firebase.setAndroidContext(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            mUserName = bundle.getString("userName");
            mUserPoint = bundle.getInt("userPoint");
        }

        if(User.user.full_name != "trash")
        {
            myUserName = User.user.full_name;
        }

        mUserNameText = (TextView) findViewById(R.id.user_name);
        mUserNameText.setText(mUserName);

        mPointText = (TextView) findViewById(R.id.user_point);
        mPointText.setText(Integer.toString(mUserPoint));

        findViewById(R.id.send_button).setOnClickListener(this);
        findViewById(R.id.send_game_request_button).setOnClickListener(this);
        FirebaseMessaging.getInstance().subscribeToTopic("notificationRequests");
        displayChatMessages();
    }



    public void sendChatMessage()
    {
        EditText input = (EditText)findViewById(R.id.input);

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

    public void displayChatMessages()
    {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);

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
        if (i == R.id.send_button)
        {
            sendChatMessage();
            displayChatMessages();
        }else if (i == R.id.send_game_request_button)
        {
            sendNotificationToUser(myUserName, "Play a game?");
        }else
        {
            Toast.makeText(this, "Invalid button selection.", Toast.LENGTH_SHORT);
        }
    }
}
