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

    private DatabaseReference mFirebaseRef;
    private Notification notification;
    String message;

    public void listenForNotifications()
    {
        final DatabaseReference mFirebaseRef = FirebaseDatabase.getInstance().getReference("notificationRequests");

        Intent notificationIntent = new Intent(this, LoginActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(LoginActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        final PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mFirebaseRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ChatMessage post = dataSnapshot.getValue(ChatMessage.class);
                        message = post.getMessageText();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        Log.d("Notification:", "onCancelled: Database error");
                    }
                });

        notification = new Notification.Builder(this)
                .setContentTitle("New mail from " + message)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_new_mail)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        listenForNotifications();
        return START_STICKY;
    }
}
