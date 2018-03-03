package com.mentalmachines.droidcon_boston.utils;

import android.text.Html;
import android.text.Spanned;

public class StringUtils {

  public static boolean isNullorEmpty(String s) {
    return !(s != null && !s.isEmpty());
  }


  public static Spanned getHtmlFormattedSpanned(String data) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
      return Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT);
    } else {
      return Html.fromHtml(data);
    }
  }
}
