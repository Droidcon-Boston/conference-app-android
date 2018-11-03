package com.mentalmachines.droidcon_boston.firebase


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseHelper private constructor() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mainDatabase: DatabaseReference
    val eventDatabase: DatabaseReference
    val speakerDatabase: DatabaseReference
    val aboutDatabase: DatabaseReference
    val faqDatabase: DatabaseReference
    val cocDatabase: DatabaseReference
    val volunteerDatabase: DatabaseReference

    init {

        // Enable disk persistence, https://firebase.google.com/docs/database/android/offline-capabilities
        this.database.setPersistenceEnabled(true)
        this.mainDatabase = database.reference
        this.eventDatabase = mainDatabase.child("conferenceData").child("events")
        this.speakerDatabase = mainDatabase.child("conferenceData").child("speakers")
        this.volunteerDatabase = mainDatabase.child("volunteers")
        this.aboutDatabase = mainDatabase.child("about")
        this.faqDatabase = mainDatabase.child("faq")
        this.cocDatabase = mainDatabase.child("conductCode")
    }

    private object Holder {
        val INSTANCE = FirebaseHelper()
    }

    companion object {
        val instance: FirebaseHelper by lazy { Holder.INSTANCE }
    }
}
