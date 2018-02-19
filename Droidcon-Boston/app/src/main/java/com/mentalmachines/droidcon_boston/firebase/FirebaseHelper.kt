package com.mentalmachines.droidcon_boston.firebase


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase.ScheduleRow

class FirebaseHelper {

    val database: FirebaseDatabase
    val mainDatabase: DatabaseReference

    init {
        this.database = FirebaseDatabase.getInstance()
        this.mainDatabase = database.getReference()
    }
}
