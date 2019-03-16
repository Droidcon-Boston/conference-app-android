package com.mentalmachines.droidcon_boston.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase
import com.mentalmachines.droidcon_boston.data.UserAgendaRepo

object AuthController {

    private var user: FirebaseUser? = null

    val isLoggedIn: Boolean
        get() = (user != null)

    val userId: String?
        get() = user?.uid

    fun login(activity: AppCompatActivity, resultCode: Int, @DrawableRes loginScreenAppIcon: Int) {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        activity.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(loginScreenAppIcon)
                .build(), resultCode
        )
    }

    /***
     * Returns an error message if there was a login error
     * or null if successful
     */
    fun handleLoginResult(context: Context, resultCode: Int, data: Intent?): String? {
        val response = IdpResponse.fromResultIntent(data)

        return if (resultCode == Activity.RESULT_OK) {
            user = FirebaseAuth.getInstance().currentUser
            user?.let { user ->
                FirebaseHelper.instance.userDatabase.child(user.uid)
                    .setValue(
                        createLocalUser(
                            UserAgendaRepo.getInstance(context).savedSessionIds,
                            user
                        )
                    )
            }
            null
        } else {
            response?.error?.message
        }
    }

    fun logout(context: Context) {
        AuthUI.getInstance().signOut(context)
        user?.email?.let { email ->
            FirebaseHelper.instance.userDatabase.child(email).setValue(
                null
            )
        }
        user = null
    }

    @VisibleForTesting
    fun createLocalUser(sessionIds: Set<String>, user: FirebaseUser): FirebaseDatabase.User {
        return FirebaseDatabase.User(
            user.uid,
            user.email,
            user.photoUrl?.toString(),
            user.displayName,
            "",
            sessionIds.toList()
        )
    }
}
