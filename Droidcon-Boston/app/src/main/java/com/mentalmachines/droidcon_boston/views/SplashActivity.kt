package com.mentalmachines.droidcon_boston.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import androidx.appcompat.app.AppCompatActivity
import com.mentalmachines.droidcon_boston.R

class SplashActivity : AppCompatActivity() {

    lateinit var logoText: View
    lateinit var logoImage: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        logoText = findViewById(R.id.logoText)
        logoImage = findViewById(R.id.logoImage)

        logoImage.translationY = LOGO_START_TRANSLATION_Y

        logoImage.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {}

            override fun onViewAttachedToWindow(v: View?) {
                logoImage.animate()
                    .translationY(LOGO_END_TRANSLATION_Y)
                    .setDuration(LOGO_ENTER_DURATION)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .start()
            }
        })
    }

    override fun onStart() {
        super.onStart()

        Handler().postDelayed(this::fadeScreenElements, SPLASH_DURATION)
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    private fun fadeScreenElements() {
        fun createFadeAnimation() = AlphaAnimation(VISIBLE_OPACITY, GONE_OPACITY).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = FADE_DURATION
        }

        val textAnimation = createFadeAnimation()
        val logoAnimation = createFadeAnimation()

        textAnimation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                logoText.visibility = View.GONE
                startMainActivity()
            }
        })

        logoAnimation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                logoImage.visibility = View.GONE
            }
        })

        logoText.startAnimation(textAnimation)
        logoImage.startAnimation(logoAnimation)
    }

    companion object {
        const val FADE_DURATION: Long = 750
        const val SPLASH_DURATION: Long = 2000

        const val VISIBLE_OPACITY = 1.00f
        const val GONE_OPACITY = 0.00f

        const val LOGO_ENTER_DURATION = 500L
        const val LOGO_START_TRANSLATION_Y = 1000f
        const val LOGO_END_TRANSLATION_Y = 0f
    }
}
