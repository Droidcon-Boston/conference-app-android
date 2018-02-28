package com.mentalmachines.droidcon_boston.firebase


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseHelper private constructor() {

    private val database: FirebaseDatabase
    private val mainDatabase: DatabaseReference
    val eventDatabase: DatabaseReference
    val speakerDatabase: DatabaseReference
    val aboutDatabase: DatabaseReference
    val faqDatabase: DatabaseReference

    init {
        this.database = FirebaseDatabase.getInstance()

        // Enable disk persistence, https://firebase.google.com/docs/database/android/offline-capabilities
        this.database.setPersistenceEnabled(true)
        this.mainDatabase = database.reference
        this.eventDatabase = mainDatabase.child("conferenceData").child("events")
        this.speakerDatabase = mainDatabase.child("conferenceData").child("speakers")
        this.aboutDatabase = mainDatabase.child("about")
        this.faqDatabase = mainDatabase.child("faq")
    }

    private object Holder {
        val INSTANCE = FirebaseHelper()
    }

    companion object {
        val instance: FirebaseHelper by lazy { Holder.INSTANCE }
    }
}
