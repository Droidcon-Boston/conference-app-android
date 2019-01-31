package com.mentalmachines.droidcon_boston.firebase


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

open class FirebaseHelper private constructor() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mainDatabase: DatabaseReference = database.reference

    open val eventDatabase: DatabaseReference =
        mainDatabase.child("conferenceData").child("events")

    open val speakerDatabase: DatabaseReference =
        mainDatabase.child("conferenceData").child("speakers")

    open val aboutDatabase: DatabaseReference = mainDatabase.child("about")
    open val faqDatabase: DatabaseReference = mainDatabase.child("faq")
    open val cocDatabase: DatabaseReference = mainDatabase.child("conductCode")
    open val volunteerDatabase: DatabaseReference = mainDatabase.child("volunteers")

    init {
        // Enable disk persistence, https://firebase.google.com/docs/database/android/offline-capabilities
        this.database.setPersistenceEnabled(true)
    }

    private object Holder {
        val INSTANCE = FirebaseHelper()
    }

    companion object {
        val instance: FirebaseHelper by lazy { Holder.INSTANCE }
    }
}
