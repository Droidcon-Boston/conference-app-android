package com.mentalmachines.droidcon_boston.firebase;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.utils.NotificationUtils;
import java.util.Map;

public class DbFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage != null) {
            Map<String, String> payloadMap = remoteMessage.getData();
            String bodyStr = "NA";

            // Check if message contains a data payload.
            if (payloadMap.size() > 0) {
                Log.d(TAG, "Payload: ");
                for (String key : payloadMap.keySet()) {
                    Log.d(TAG, "Key: " + key + ", Value: " + payloadMap.get(key));
                }
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                bodyStr = remoteMessage.getNotification().getBody();
                Log.d(TAG, "Body: " + bodyStr);
            }

            // Show the notification here
            NotificationUtils notificationUtils = new NotificationUtils(this);
            notificationUtils.sendAndroidChannelNotification(getString(R.string.conference_name), bodyStr, 101);
        }


    }
}
