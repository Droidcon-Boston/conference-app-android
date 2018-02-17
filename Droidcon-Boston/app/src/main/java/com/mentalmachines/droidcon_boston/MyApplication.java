package com.mentalmachines.droidcon_boston;

import android.app.Application;
import com.google.firebase.FirebaseApp;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class MyApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.twitter_key),
        getResources().getString(R.string.twitter_secret));
    Fabric.with(this, new Twitter(authConfig));

    FirebaseApp.initializeApp(this);
  }
}
