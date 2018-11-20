package com.mentalmachines.droidcon_boston.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mentalmachines.droidcon_boston.utils.NotificationUtils


class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            NotificationUtils(context).scheduleMySessionNotifications()
        }
    }
}