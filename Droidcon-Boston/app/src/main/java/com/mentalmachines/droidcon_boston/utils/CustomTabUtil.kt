package com.mentalmachines.droidcon_boston.utils

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import com.mentalmachines.droidcon_boston.R

object CustomTabUtil {

    fun loadUriInCustomTab(context: Context, uriString: String) {
        val data = Uri.parse(uriString)
        val customTabsIntent = CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .build()
        customTabsIntent.launchUrl(context, data)
    }
}
