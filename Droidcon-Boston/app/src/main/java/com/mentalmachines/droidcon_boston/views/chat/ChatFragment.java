package com.mentalmachines.droidcon_boston.views.chat;

import static io.fabric.sdk.android.Fabric.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mentalmachines.droidcon_boston.views.MainActivity;
import com.mentalmachines.droidcon_boston.views.base.MaterialFragment;

/**
 * Created by jinn on 1/28/18.
 */

public class ChatFragment extends MaterialFragment {

    private FirebaseAuth auth;

    private FirebaseAuth.AuthStateListener authListener;

    private FirebaseUser user;

    private ListMessageAdapter adapter;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.searchBar)
    SearchView search;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLayout() {
        return R.layout.chat_fragment;
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

    private void showEditDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ChatLoginFragment chatLoginDialog = ChatLoginFragment.newInstance();
        chatLoginDialog.show(fm, "ChatLoginFragment");
    }

}
