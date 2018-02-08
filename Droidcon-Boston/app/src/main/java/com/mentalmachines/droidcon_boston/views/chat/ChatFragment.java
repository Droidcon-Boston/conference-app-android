package com.mentalmachines.droidcon_boston.views.chat;

import static io.fabric.sdk.android.Fabric.TAG;

import android.content.Intent;
import android.support.annotation.NonNull;
import com.mentalmachines.droidcon_boston.views.MainActivity;
import com.mentalmachines.droidcon_boston.views.base.MaterialFragment;

/**
 * Created by jinn on 1/28/18.
 */

public class ChatFragment extends MaterialFragment {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseUser user;


    @Override
    public int getLayout() {
        return 0;
    }

    public ChatFragment() {
        super();
    }

    private void initFirebase() {
        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    StaticConfig.UID = user.getUid();
                } else {
                    MainActivity.this.finish();
                    // User is signed in
                    startActivity(new Intent(MainActivity.this, ChatLoginFragment.class));
                    Timber.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }
}
