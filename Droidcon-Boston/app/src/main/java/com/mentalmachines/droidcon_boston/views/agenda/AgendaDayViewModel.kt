package com.mentalmachines.droidcon_boston.views.agenda

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase
import com.mentalmachines.droidcon_boston.data.Schedule
import com.mentalmachines.droidcon_boston.data.UserAgendaRepo
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import timber.log.Timber

class AgendaDayViewModel(
    private val dayFilter: String,
    val onlyMyAgenda: Boolean,
    private val userAgendaRepo: UserAgendaRepo
) : ViewModel() {
    private val firebaseHelper = FirebaseHelper.instance

    private val _scheduleRows = MutableLiveData<List<Schedule.ScheduleRow>>()
    private val _activeFilter = MutableLiveData<String>()

    /**
     * Whenever an active filter is set, we filter out the [_scheduleRows] for any that contain the
     * filter within the title, description, or speaker names.
     */
    val scheduleRows: LiveData<List<Schedule.ScheduleRow>> =
        Transformations.map(_activeFilter) { constraint ->
            _scheduleRows.value?.filter { itemData ->
                itemData.containsKeyword(constraint)
            }
        }

    private val dataListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val rows = ArrayList<Schedule.ScheduleRow>()
            for (roomSnapshot in dataSnapshot.children) {
                val key = roomSnapshot.key ?: ""
                val data = roomSnapshot.getValue(FirebaseDatabase.ScheduleEvent::class.java)
                Timber.d("Event: $data")
                if (data != null) {
                    val scheduleRow = data.toScheduleRow(key)
                    val matchesDay = scheduleRow.date == dayFilter
                    val isPublicView = !onlyMyAgenda
                    val isPrivateAndBookmarked = onlyMyAgenda && userAgendaRepo
                        .isSessionBookmarked(scheduleRow.id)

                    if (matchesDay && (isPublicView || isPrivateAndBookmarked)) {
                        rows.add(scheduleRow)
                    }
                }
            }

            _scheduleRows.value = rows
            _activeFilter.value = ""
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Timber.e(databaseError.toException())
        }
    }

    fun fetchScheduleData() {
        firebaseHelper.eventDatabase.removeEventListener(dataListener)
        firebaseHelper.eventDatabase.addValueEventListener(dataListener)
    }

    fun setActiveFilter(filter: String) {
        _activeFilter.value = filter
    }

    override fun onCleared() {
        super.onCleared()
        firebaseHelper.eventDatabase.removeEventListener(dataListener)
    }
}
