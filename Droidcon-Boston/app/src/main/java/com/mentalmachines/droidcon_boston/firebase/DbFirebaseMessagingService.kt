package com.mentalmachines.droidcon_boston.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.utils.NotificationUtils

class DbFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = javaClass.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        if (remoteMessage != null) {
            val payloadMap = remoteMessage.data
            var bodyStr: String? = "NA"

            // Check if message contains a data payload.
            if (payloadMap.isNotEmpty()) {
                Log.d(TAG, "Payload: ")
                for (key in payloadMap.keys) {
                    Log.d(TAG, "Key: " + key + ", Value: " + payloadMap[key])
                }
            }

            // Check if message contains a notification payload.
            if (remoteMessage.notification != null) {
                bodyStr = remoteMessage.notification!!.body
                Log.d(TAG, "Body: " + bodyStr!!)
            }

            // Show the notification here
            val notificationUtils = NotificationUtils(this)
            notificationUtils.sendAndroidChannelNotification(getString(R.string.conference_name), bodyStr!!, 101)
        }
    }
}
