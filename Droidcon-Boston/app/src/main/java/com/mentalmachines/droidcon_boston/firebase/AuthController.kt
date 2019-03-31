package com.mentalmachines.droidcon_boston.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase
import com.mentalmachines.droidcon_boston.data.UserAgendaRepo
import timber.log.Timber

object AuthController {

    val isLoggedIn: Boolean
        get() = (FirebaseAuth.getInstance().currentUser != null)

    val userId: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid

    fun login(activity: AppCompatActivity, resultCode: Int, @DrawableRes loginScreenAppIcon: Int) {
        activity.startActivityForResult(
            getAuthIntent(loginScreenAppIcon), resultCode
        )
    }

    fun login(fragment: Fragment, resultCode: Int, @DrawableRes loginScreenAppIcon: Int) {
        fragment.startActivityForResult(
            getAuthIntent(loginScreenAppIcon), resultCode
        )
    }

    private fun getAuthIntent(
        loginScreenAppIcon: Int
    ): Intent {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(loginScreenAppIcon)
            .build()
    }

    /***
     * Returns an error message if there was a login error
     * or null if successful
     */
    fun handleLoginResult(context: Context, resultCode: Int, data: Intent?): String? {
        val response = IdpResponse.fromResultIntent(data)

        return if (resultCode == Activity.RESULT_OK) {
            FirebaseAuth.getInstance().currentUser?.let { user ->
                FirebaseHelper.instance.userDatabase.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Timber.e(error.toException())
                    }

                    override fun onDataChange(nodes: DataSnapshot) {
                        // only add user node if it doesn't exist so we don't overwrite favorites
                        if (!nodes.hasChild(user.uid)) {
                            FirebaseHelper.instance.userDatabase.child(user.uid)
                                .setValue(
                                    createLocalUser(
                                        UserAgendaRepo.getInstance(context).savedSessionIds
                                            .values.toSet(),
                                        user
                                    )
                                )
                        }
                    }
                })
            }
            null
        } else {
            response?.error?.message
        }
    }

    fun logout(context: Context, completeCallback: () -> Unit) {
        AuthUI.getInstance().signOut(context).addOnCompleteListener{
            completeCallback()
        }
    }

    @VisibleForTesting
    fun createLocalUser(sessionIds: Set<String>, user: FirebaseUser): FirebaseDatabase.User {
        return FirebaseDatabase.User(
            user.uid,
            user.email,
            user.photoUrl?.toString(),
            user.displayName,
            "",
            sessionIds.map { it to it } .toMap()
        )
    }
}
