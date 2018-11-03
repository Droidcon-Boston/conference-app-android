package com.mentalmachines.droidcon_boston.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import com.mentalmachines.droidcon_boston.R
import kotlinx.android.synthetic.main.splash_activity.*


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
    }

    override fun onStart() {
        super.onStart()

        Handler().postDelayed(this::fadeImage, SPLASH_DURATION)
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    private fun fadeImage() {
        val a = AlphaAnimation(1.00f, 0.00f)

        a.interpolator = AccelerateDecelerateInterpolator()
        a.duration = FADE_DURATION

        a.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                logo_text.visibility = View.GONE
                startMainActivity()
            }
        })

        logo_text.startAnimation(a)
    }

    companion object {
        const val FADE_DURATION: Long = 750
        const val SPLASH_DURATION: Long = 1500
    }
}
