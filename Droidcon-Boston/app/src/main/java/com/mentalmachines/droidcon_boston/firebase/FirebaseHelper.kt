package com.mentalmachines.droidcon_boston.firebase


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseHelper private constructor() {

    val database: FirebaseDatabase
    val mainDatabase: DatabaseReference

    init {
        this.database = FirebaseDatabase.getInstance()
        this.mainDatabase = database.getReference()
    }
    private object Holder { val INSTANCE = FirebaseHelper() }

    companion object {
        val instance: FirebaseHelper by lazy { Holder.INSTANCE }
    }
}
