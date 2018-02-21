package com.mentalmachines.droidcon_boston.utils;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import com.mentalmachines.droidcon_boston.R;

public class CustomTabUtil {

  public static void loadUriInCustomTab(Context context, String uriString) {
    Uri data = Uri.parse(uriString);
    CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
        .setToolbarColor(context.getResources().getColor(R.color.colorPrimary))
        .build();
    customTabsIntent.launchUrl(context, data);
  }
}
