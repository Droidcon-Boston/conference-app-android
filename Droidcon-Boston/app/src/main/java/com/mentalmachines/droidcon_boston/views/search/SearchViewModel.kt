package com.mentalmachines.droidcon_boston.views.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidcon_boston.BuildConfig
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase
import com.mentalmachines.droidcon_boston.data.Schedule
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import timber.log.Timber

class SearchViewModel : ViewModel() {
    private val firebaseHelper = FirebaseHelper.instance

    private val _scheduleRows = MutableLiveData<List<Schedule.ScheduleRow>>()
    val scheduleRows: LiveData<List<Schedule.ScheduleRow>> = _scheduleRows

    private val dataListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val rows = ArrayList<Schedule.ScheduleRow>()
            for (roomSnapshot in dataSnapshot.children) {
                val key = roomSnapshot.key ?: ""
                val data = roomSnapshot.getValue(FirebaseDatabase.ScheduleEvent::class.java)
                if (data != null) {
                    val scheduleRow = data.toScheduleRow(key)

                    if (scheduleRow.date.endsWith(BuildConfig.EVENT_YEAR.toString())) {
                        rows.add(scheduleRow)
                    }
                }
            }

            _scheduleRows.value = rows
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Timber.e(databaseError.toException())
        }
    }

    init {
        firebaseHelper.eventDatabase.addValueEventListener(dataListener)
    }

    override fun onCleared() {
        super.onCleared()
        firebaseHelper.eventDatabase.removeEventListener(dataListener)
    }
}
