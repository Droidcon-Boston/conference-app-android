package com.mentalmachines.droidcon_boston

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import io.fabric.sdk.android.Fabric

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Fabric.with(this)

        AndroidThreeTen.init(this)
    }
}
