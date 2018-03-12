package com.mentalmachines.droidcon_boston

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mentalmachines.droidcon_boston.utils.NotificationUtils

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        NotificationUtils(this).scheduleMySessionNotifications()
    }
}
