package com.javaman.olcudefteri.service;

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
import com.javaman.olcudefteri.ui.orders.OrderDetailActivity;
import com.javaman.olcudefteri.utill.SharedPreferenceHelper;

/**
 * Created by javaman on 19.02.2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    SharedPreferenceHelper sharedPreferenceHelper;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
        if (remoteMessage.getNotification() != null) {
            Log.i(TAG, "Coming Not sound  " + remoteMessage.getNotification().getSound());
            Log.i(TAG, "Coming Not title : " + remoteMessage.getNotification().getTitle());
            Log.i(TAG, "Coming Data : " + remoteMessage.getData().toString());

            //remoteMessage.getData().getData()-->orderId



            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getData().get("message");
            Long orderId = Long.parseLong(remoteMessage.getData().get("data"));
            String time = remoteMessage.getData().get("time");
            createNotification(message, title, orderId, time);

        }

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data: " + remoteMessage.getData());
        }
    }

    private void createNotification(String messageBody, String messageTitle, Long orderId, String time) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra(OrderDetailActivity.ARG_NOTIFICATION_ORDER, orderId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
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
                .setTicker("Sipariş")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setSubText(time)
                .setAutoCancel(true)
                .setSound(notificationSoundURI);

        notificationManager.notify(/*notification id*/0, notificationBuilder.build());

    }

}
