package com.mentalmachines.droidcon_boston.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import com.mentalmachines.droidcon_boston.views.MainActivity;

public class NotificationUtils extends ContextWrapper {

    private NotificationManager notificationManager;

    public static final String ANDROID_CHANNEL_ID = "com.mentalmachines.droidcon_boston.ANDROID";

    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";

    public NotificationUtils(Context context) {
        super(context);
        createChannels();
    }

    public void createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create android channel
            NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                    ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            androidChannel.setLightColor(Color.GREEN);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getNotificationManager().createNotificationChannel(androidChannel);
        }

    }

    private NotificationManager getNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    private void sendChannelNotification(String title, String body, int notificationId, String
            channelId) {
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent
                .FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                channelId)
                .setContentTitle(title)
                .setContentText(body)
                .setTicker("Droidcon Boston")
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true)
                // for notification click action, also required on Gingerbread and below
                .setContentIntent(pi);

        getNotificationManager().notify(notificationId, builder.build());
    }

    public void sendAndroidChannelNotification(String title, String body, int notificationId) {
        sendChannelNotification(title, body, notificationId, ANDROID_CHANNEL_ID);
    }
}
