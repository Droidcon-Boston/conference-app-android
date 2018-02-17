package com.mentalmachines.droidcon_boston.firebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidcon_boston.data.ConferenceData.EventData
import com.mentalmachines.droidcon_boston.data.ConferenceDataModel

/**
 * Created by emezias on 2/13/18.
 */

object ScheduleUpdateUtils {
    val TAG = ScheduleUpdateUtils::class.java.simpleName
    val EVENTS = "events"
    val ROOMS = "rooms"
    val SECTIONS = "sections"
    val SPEAKERS = "speakers"
    val TRAX = "tracks"

    fun checkForChanges(staticData: ConferenceDataModel, timestamp: Long): ConferenceDataModel {
        val root = FirebaseDatabase.getInstance().getReference("conferenceData")
        // Read from the database
        val getScheduleData = root.child(EVENTS)//.startAt("1516821464815");
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.childrenCount == 0L) {
                    Log.d(TAG, "cancelled " + "no children returned")
                    //return;
                }
                Log.d(TAG, "got data " + dataSnapshot.childrenCount)
                val eventsfound: ArrayList<EventData> = ArrayList()
                dataSnapshot.children.mapNotNullTo(eventsfound) { it.getValue<EventData>(EventData::class.java) }

                for (eventItem in eventsfound) {
                    //val note = noteDataSnapshot.getValue(ConferenceData.EventData::class.java)
                    Log.d(TAG, "got event " + eventItem!!.name)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "cancelled " + databaseError.message)
            }
        }
        getScheduleData.addListenerForSingleValueEvent(listener)
        Log.d(TAG, "set listener")
        return staticData
    }
}
