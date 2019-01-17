package com.mentalmachines.droidcon_boston.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthController(@DrawableRes private val loginScreenAppIcon: Int) {
    private var user: FirebaseUser? = null

    val isLoggedIn: Boolean
        get() = (user != null)

    fun login(activity: AppCompatActivity, resultCode: Int) {

        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build())

        activity.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(loginScreenAppIcon)
                .build(),
            resultCode)
    }

    /***
     * Returns an error message if there was a login error
     * or null if successful
     */
    fun handleLoginResult(resultCode: Int, data: Intent?): String? {
        val response = IdpResponse.fromResultIntent(data)

        return if (resultCode == Activity.RESULT_OK) {
            user = FirebaseAuth.getInstance().currentUser
            null
        } else {
            response?.error?.message
        }
    }

    fun logout(context: Context) {
        AuthUI.getInstance()
            .signOut(context)
        user = null
    }
}
