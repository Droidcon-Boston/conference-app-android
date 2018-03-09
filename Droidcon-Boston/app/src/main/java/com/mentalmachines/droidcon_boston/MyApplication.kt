package com.mentalmachines.droidcon_boston

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mentalmachines.droidcon_boston.utils.NotificationUtils
import io.fabric.sdk.android.Fabric

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Fabric.with(this)

        AndroidThreeTen.init(this)

        NotificationUtils(this).scheduleMySessionNotifications()
    }
}
