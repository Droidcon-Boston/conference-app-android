package com.mentalmachines.droidconboston.firebase


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
    val userDatabase: DatabaseReference

    init {
        // Enable disk persistence, https://firebase.google.com/docs/database/android/offline-capabilities
        database.setPersistenceEnabled(true)
        mainDatabase = database.reference
        eventDatabase = mainDatabase.child("conferenceData").child("events")
        speakerDatabase = mainDatabase.child("conferenceData").child("speakers")
        volunteerDatabase = mainDatabase.child("volunteers")
        aboutDatabase = mainDatabase.child("about")
        faqDatabase = mainDatabase.child("faq")
        cocDatabase = mainDatabase.child("conductCode")
        userDatabase = mainDatabase.child("users")
    }

    private object Holder {
        val INSTANCE = FirebaseHelper()
    }

    companion object {
        val instance: FirebaseHelper by lazy { Holder.INSTANCE }
    }
}
