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
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.ui.home.HomeActivity;
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
        if (remoteMessage.getData().containsKey("data")) {

            String title = "";
            String message= "";
            String time= "";
            Long orderId = null;

            //remoteMessage.getData().getData()-->orderId


            if(!TextUtils.isEmpty(remoteMessage.getNotification().getTitle())){
                 title= remoteMessage.getNotification().getTitle();
            }


            if(remoteMessage.getData().containsKey("message")){
                message =remoteMessage.getData().get("message");
                message=Character.toUpperCase(message.charAt(0)) + message.substring(1);


            }

            if(remoteMessage.getData().containsKey("time")){
                time = remoteMessage.getData().get("time");
            }

            orderId = Long.parseLong(remoteMessage.getData().get("data"));
            int notf_count=sharedPreferenceHelper.getIntegerPreference("notf-count",-1);
            if(notf_count>-1){
                notf_count++;
                sharedPreferenceHelper.setIntegerPreference("notf-count",notf_count);
            }

            createDataNotification(message, title, orderId, time);

        }else{
            String body= "";
            if(!TextUtils.isEmpty(remoteMessage.getNotification().getBody())){
                body= remoteMessage.getNotification().getBody();
                createTopicNotification(body);
            }
        }


    }

    private void createDataNotification(String messageBody, String messageTitle, Long orderId, String time) {
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
                .setColor(getApplicationContext().getResources().getColor(R.color.primaryColor))
                .setSmallIcon(R.drawable.ic_notification)
                .setTicker("Sipariş")
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setSubText(time)
                .setAutoCancel(true)
                .setSound(notificationSoundURI);

        notificationManager.notify(/*notification id*/0, notificationBuilder.build());

    }

    private void createTopicNotification(String messageBody) {
        Intent intent = new Intent(this, HomeActivity.class);
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
                .setColor(getApplicationContext().getResources().getColor(R.color.primaryColor))
                .setSmallIcon(R.drawable.ic_notification)
                .setTicker("Uygulama")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(notificationSoundURI);

        notificationManager.notify(/*notification id*/0, notificationBuilder.build());

    }

}
