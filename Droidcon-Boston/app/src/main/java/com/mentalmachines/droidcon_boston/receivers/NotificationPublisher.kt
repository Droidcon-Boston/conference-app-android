package com.mentalmachines.droidcon_boston.receivers

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationPublisher : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = intent.getParcelableExtra<Notification>(NOTIFICATION)
        val id = intent.getIntExtra(NOTIFICATION_ID, 0)
        notificationManager.notify(id, notification)
    }

    companion object {

        var NOTIFICATION_ID = "notification-id"
        var NOTIFICATION = "notification"
        var SESSION_ID = "session-id"
    }
}