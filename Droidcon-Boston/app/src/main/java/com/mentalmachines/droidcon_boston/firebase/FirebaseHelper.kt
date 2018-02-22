package com.mentalmachines.droidcon_boston.firebase


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseHelper private constructor() {

    val database: FirebaseDatabase
    val mainDatabase: DatabaseReference
    val eventDatabase: DatabaseReference
    val speakerDatabase: DatabaseReference

    init {
        this.database = FirebaseDatabase.getInstance()
        // Enable disk persistence, https://firebase.google.com/docs/database/android/offline-capabilities
        this.database.setPersistenceEnabled(true)
        this.mainDatabase = database.getReference()
        this.eventDatabase = mainDatabase.child("conferenceData").child("events")
        this.speakerDatabase = mainDatabase.child("conferenceData").child("speakers")
    }
    private object Holder { val INSTANCE = FirebaseHelper() }

    companion object {
        val instance: FirebaseHelper by lazy { Holder.INSTANCE }
    }
}
