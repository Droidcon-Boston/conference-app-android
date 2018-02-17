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
        getResources().getString(R.string.    @Override
    public void onAttach(Context context) {
      super.onAttach(context);
      firebaseListenerRef = new FirebaseListenerRef(
          FirebaseDatabase.getInstance().getReference().child("conferenceData"),
          new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              Iterable<DataSnapshot> children = dataSnapshot.getChildren();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
          }
      );
    }

    @Override
    public void onDetach() {
      super.onDetach();
      if ( firebaseListenerRef != null ) {
        firebaseListenerRef.detach();
        firebaseListenerRef = null;
      }
    }
));
    Fabric.with(this, new Twitter(authConfig));

    FirebaseApp.initializeApp(this);
  }
}
