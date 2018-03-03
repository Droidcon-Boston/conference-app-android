package com.mentalmachines.droidcon_boston.utils

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Build.VERSION_CODES
import android.support.v4.app.NotificationCompat
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.views.MainActivity

class NotificationUtils(context: Context) : ContextWrapper(context) {

    init {
        createChannels()
    }

    @TargetApi(VERSION_CODES.O)
    fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create android channel
            val androidChannel = NotificationChannel(ANDROID_CHANNEL_ID,
                    ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true)
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true)
            // Sets the notification light color for notifications posted to this channel
            androidChannel.lightColor = Color.GREEN
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

            getNotificationManager().createNotificationChannel(androidChannel)
        }

    }

    private fun getNotificationManager(): NotificationManager {
        return getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun sendChannelNotification(title: String, body: String, notificationId: Int, channelId: String) {
        val resultIntent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent
                .FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(applicationContext,
                channelId)
                .setContentTitle(title)
                .setContentText(body)
                .setTicker(getString(R.string.conference_name))
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true)
                // for notification click action, also required on Gingerbread and below
                .setContentIntent(pi)

        getNotificationManager().notify(notificationId, builder.build())
    }

    fun sendAndroidChannelNotification(title: String, body: String, notificationId: Int) {
        sendChannelNotification(title, body, notificationId, ANDROID_CHANNEL_ID)
    }

    companion object {
        val ANDROID_CHANNEL_ID = "com.mentalmachines.droidcon_boston.ANDROID"
        val ANDROID_CHANNEL_NAME = "ANDROID CHANNEL"
    }
}
