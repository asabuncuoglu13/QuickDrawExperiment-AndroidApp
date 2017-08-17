package com.example.alpay.learnwithdrawing;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationListenerService extends Service {
    public NotificationListenerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Notification notification;
    private String myUserName;
    String message;
    String user;

    public void listenForNotifications()
    {
        final DatabaseReference mFirebaseRef = FirebaseDatabase.getInstance().getReference("notificationRequests");

        Intent notificationIntent = new Intent(this, LoginActivity.class);

        myUserName = User.user.full_name;

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(LoginActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        final PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mFirebaseRef.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.d("Notification:", "onChildAdded: "+myUserName);
                        ChatMessage post = dataSnapshot.getValue(ChatMessage.class);
                        if(post != null)
                        {
                            message = post.getMessageText();
                            user = post.getMessageUser();
                            Log.d("Notification:", "onChildChanged: username is "+user+" message is: "+message);
                        }

                        if (message != null && user != null)
                        {
                            notification = new Notification.Builder(getApplicationContext())
                                    .setContentTitle("New mail from " + user)
                                    .setContentText(message)
                                    .setSmallIcon(R.mipmap.ic_new_mail)
                                    .setContentIntent(pendingIntent)
                                    .build();

                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(1, notification);
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot)
                    {
                        Log.d("Notification:", "onChildRemoved");
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName)
                    {
                        Log.d("Notification:", "onChildChanged");
                        ChatMessage post = dataSnapshot.getValue(ChatMessage.class);
                        if(post != null)
                        {
                            message = post.getMessageText();
                            user = post.getMessageUser();
                            Log.d("Notification:", "onChildChanged: username is "+user+" message is: "+message);
                        }

                        if (message != null && user != null)
                        {
                            notification = new Notification.Builder(getApplicationContext())
                                    .setContentTitle("New mail from " + user)
                                    .setContentText(message)
                                    .setSmallIcon(R.mipmap.ic_new_mail)
                                    .setContentIntent(pendingIntent)
                                    .build();

                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(1, notification);
                        }
                    }
                    @Override
                    public void onChildMoved(DataSnapshot snapshot, String previousChildName)
                    {
                        Log.d("Notification:", "onChildMoved");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        Log.d("Notification:", "onCancelled: Database error");
                    }
                });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        listenForNotifications();
        return START_STICKY;
    }
}
