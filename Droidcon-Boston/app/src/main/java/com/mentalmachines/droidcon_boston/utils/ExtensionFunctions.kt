package com.mentalmachines.droidcon_boston.utils

import android.content.Context
import android.net.Uri
import android.text.Html
import android.text.Spanned
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.mentalmachines.droidcon_boston.R

fun String?.isNullorEmpty(): Boolean {
    return !(this != null && !this.isEmpty())
}

fun String.getHtmlFormattedSpanned(): Spanned {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
    } else {
        @Suppress("DEPRECATION") Html.fromHtml(this)
    }
}

fun Context.loadUriInCustomTab(uriString: String) {
    val data = Uri.parse(uriString)
    val customTabsIntent = CustomTabsIntent.Builder()
        .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary)).build()
    customTabsIntent.launchUrl(this, data)
}

fun View?.visibleIf(condition: Boolean?) {
    this?.visibility = if (condition == true) View.VISIBLE else View.GONE
}
