package com.mentalmachines.droidcon_boston.views.detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase
import com.mentalmachines.droidcon_boston.data.Schedule
import com.mentalmachines.droidcon_boston.data.UserAgendaRepo
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import com.mentalmachines.droidcon_boston.utils.NotificationUtils

class AgendaDetailViewModel(
    private val scheduleRowItem: Schedule.ScheduleRow,
    private val userAgendaRepo: UserAgendaRepo,
    private val firebaseHelper: FirebaseHelper = FirebaseHelper.instance
) : ViewModel() {
    private val eventSpeakers: HashMap<String, FirebaseDatabase.EventSpeaker> = hashMapOf()

    private val _scheduleDetail = MutableLiveData<Schedule.ScheduleDetail>()
    val scheduleDetail: LiveData<Schedule.ScheduleDetail> = _scheduleDetail

    private val dataListener: ValueEventListener = object : ValueEventListener {
        override fun onCancelled(databaseError: DatabaseError) {
            Log.e(javaClass.canonicalName, "detailQuery:onCancelled", databaseError.toException())
        }

        override fun onDataChange(databaseSnapshot: DataSnapshot) {
            databaseSnapshot.children.forEach { speakerSnapshot ->
                val speaker = speakerSnapshot.getValue(FirebaseDatabase.EventSpeaker::class.java)
                speaker?.let {
                    eventSpeakers[speaker.name] = speaker

                    if (scheduleRowItem.primarySpeakerName == speaker.name) {
                        _scheduleDetail.value = speaker.toScheduleDetail(scheduleRowItem)
                    }
                }
            }
        }
    }

    val talkTitle: String
        get() = scheduleRowItem.talkTitle

    val room: String
        get() = scheduleRowItem.room

    val startTime: String
        get() = scheduleRowItem.startTime

    val endTime: String
        get() = scheduleRowItem.endTime

    val schedulerowId: String
        get() = scheduleRowItem.id

    val speakerNames: List<String>
        get() = scheduleRowItem.speakerNames

    private val sessionId: String
        get() = scheduleDetail.value?.id.orEmpty()

    val isBookmarked: Boolean
        get() {
            return sessionId.isNotEmpty() && userAgendaRepo.isSessionBookmarked(sessionId)
        }

    val bookmarkSnackbarRes: Int
        get() = if (isBookmarked) R.string.saved_agenda_item else R.string.removed_agenda_item

    val bookmarkColorRes: Int
        get() = if (isBookmarked) R.color.colorAccent else R.color.colorLightGray

    fun loadData() {
        firebaseHelper.speakerDatabase.orderByChild("name").addValueEventListener(dataListener)
    }

    fun removeListener() {
        firebaseHelper.speakerDatabase.removeEventListener(dataListener)
    }

    fun getSpeaker(speakerName: String): FirebaseDatabase.EventSpeaker? {
        return eventSpeakers[speakerName]
    }

    fun getOrganizationForSpeaker(speakerName: String): String? {
        return scheduleRowItem.speakerNameToOrgName[speakerName]
    }

    fun getPhotoForSpeaker(speakerName: String): String? {
        return scheduleRowItem.photoUrlMap[speakerName]
    }

    fun toggleBookmark(context: Context) {
        val nextBookmarkStatus = !isBookmarked
        userAgendaRepo.bookmarkSession(sessionId, nextBookmarkStatus)

        if (nextBookmarkStatus) {
            NotificationUtils(context).scheduleMySessionNotifications()
        } else {
            NotificationUtils(context).cancelNotificationAlarm(schedulerowId)
        }
    }
}