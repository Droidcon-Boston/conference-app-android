package com.mentalmachines.droidcon_boston.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class FirebaseListenerRef {

    private ValueEventListener listener;
    private DatabaseReference ref;

    public FirebaseListenerRef(DatabaseReference ref, ValueEventListener listener) {
        this.listener = listener;
        this.ref = ref;

        // Start listening on this ref.
        ref.addValueEventListener(listener);
    }

    public void detach() {
        ref.removeEventListener(listener);
    }
}
