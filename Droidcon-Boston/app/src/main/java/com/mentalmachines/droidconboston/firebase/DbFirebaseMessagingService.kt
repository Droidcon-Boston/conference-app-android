package com.mentalmachines.droidconboston.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mentalmachines.droidconboston.R
import com.mentalmachines.droidconboston.utils.NotificationUtils
import timber.log.Timber

class DbFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        if (remoteMessage != null) {
            val payloadMap = remoteMessage.data
            var bodyStr: String? = "NA"

            // Check if message contains a data payload.
            if (payloadMap.isNotEmpty()) {
                Timber.d("Payload: ")
                for (key in payloadMap.keys) {
                    Timber.d("Key: $key, Value: ${payloadMap[key]}")
                }
                NotificationUtils(applicationContext).scheduleMySessionNotifications()
            }

            // Check if message contains a notification payload.
            if (remoteMessage.notification != null) {
                bodyStr = remoteMessage.notification!!.body
                Timber.d("Body: $bodyStr")
            }

            // Show the notification here
            val notificationUtils = NotificationUtils(this)
            notificationUtils.sendAndroidChannelNotification(
                getString(R.string.conference_name),
                bodyStr!!,
                NOTIFICATION_ID
            )
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 101
    }
}
