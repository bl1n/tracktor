package com.elegion.tracktor.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.elegion.tracktor.App;
import com.elegion.tracktor.R;
import com.elegion.tracktor.ui.map.MainActivity;
import com.elegion.tracktor.util.StringUtil;

public class NotificationHelper {

    public static final int REQUEST_CODE_LAUNCH = 0;

    public static final String CHANNEL_ID = "counter_service";
    public static final String CHANNEL_NAME = "Counter Service";

    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;



    public NotificationHelper() {

        mNotificationManager = (NotificationManager) App.getContext().getSystemService(Context.NOTIFICATION_SERVICE);


    }

    public  Notification buildNotification() {
        return buildNotification("", "");
    }

    public Notification buildNotification(String time, String distance) {
        if (mNotificationBuilder == null) {
            configureNotificationBuilder();
        }

        String message = App.getContext().getResources().getString(R.string.notify_info, time, distance);

        return mNotificationBuilder
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .build();

    }

    public void configureNotificationBuilder() {
        Intent notificationIntent = new Intent(App.getContext(), MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(
                App.getContext(), REQUEST_CODE_LAUNCH, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mNotificationBuilder = new NotificationCompat.Builder(App.getContext(), CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_my_location_white_24dp)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(App.getContext().getResources().getString(R.string.route_active))
                .setVibrate(new long[]{0})
                .setColor(ContextCompat.getColor(App.getContext(), R.color.colorAccent));
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        if (mNotificationManager != null && mNotificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            NotificationChannel chan = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mNotificationManager.createNotificationChannel(chan);
        }
    }

    public void notify(int id, String timeText, String distanceText){
        Notification notification = buildNotification(timeText, distanceText);
        mNotificationManager.notify(id, notification);
    }
}
