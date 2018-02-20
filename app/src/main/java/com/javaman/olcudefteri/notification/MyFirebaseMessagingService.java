package com.javaman.olcudefteri.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.orders.OrdersActivity;

/**
 * Created by javaman on 19.02.2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Title: " + remoteMessage.getNotification().getTitle());
            Log.e(TAG, "Body: " + remoteMessage.getNotification().getBody());

            createNotification(remoteMessage.getNotification().getBody());

        }

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data: " + remoteMessage.getData());
        }
    }




    private void createNotification(String messageBody){

        Intent intent = new Intent( this , OrdersActivity. class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "firebase_not_channel";
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Firebase Bildirimleri");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Hearty365")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Siparişler")
                .setContentText(messageBody)
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        notificationManager.notify(/*notification id*/0, notificationBuilder.build());
    }






}
