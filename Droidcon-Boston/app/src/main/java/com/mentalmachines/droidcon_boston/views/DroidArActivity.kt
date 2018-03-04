package com.mentalmachines.droidcon_boston.views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent



class DroidArActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, UnityPlayerActivity::class.java)
        startActivity(intent)
    }
}
