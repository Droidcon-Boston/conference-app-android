package com.mentalmachines.droidcon_boston.utils

import android.text.Html
import android.text.Spanned

object StringUtils {
    fun isNullorEmpty(s: String?): Boolean {
        return !(s != null && !s.isEmpty())
    }


    fun getHtmlFormattedSpanned(data: String): Spanned {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(data)
        }
    }
}
