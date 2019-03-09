package com.mentalmachines.droidcon_boston.data

import android.content.Context
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleDetail
import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleRow
import com.mentalmachines.droidcon_boston.utils.NotificationUtils
import com.mentalmachines.droidcon_boston.utils.ServiceLocator
import com.mentalmachines.droidcon_boston.utils.getHtmlFormattedSpanned
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

const val TIME_BETWEEN_SESSIONS: Long = 15

open class FirebaseDatabase {
    data class ScheduleEvent(
        private val SESSION_REMINDER_MINUTES_BEFORE: Long = 10,

        var primarySpeakerName: String = "",
        var startTime: String = "",
        var name: String = "",

        var speakerNames: HashMap<String, Boolean> = HashMap(0),
        var speakerNameToPhotoUrl: HashMap<String, String> = HashMap(0),
        var speakerNameToOrg: HashMap<String, String> = HashMap(0),
        var roomNames: HashMap<String, Boolean> = HashMap(0),
        var speakerIds: HashMap<String, Boolean> = HashMap(0),
        var roomIds: HashMap<String, Boolean> = HashMap(0),
        var description: String = "",
        var photo: HashMap<String, String> = HashMap(0),
        var endTime: String = "",
        var trackSortOrder: Int = 0
    ) {

        private val conferenceTZ: ZoneId = ZoneId.of("America/New_York")

        fun getLocalStartTime(): LocalDateTime {
            return ZonedDateTime.parse(startTime).withZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime()
        }

        fun scheduleNotification(context: Context, eventId: String, sessionDetail: ScheduleRow) {
            NotificationUtils(context).scheduleNotificationAlarm(
                getLocalStartTime().minusMinutes(
                    SESSION_REMINDER_MINUTES_BEFORE
                ),
                eventId,
                context.getString(R.string.str_session_start_soon, name),
                description.getHtmlFormattedSpanned().toString(),
                ServiceLocator.gson.toJson(sessionDetail, ScheduleRow::class.java)
            )
        }

        fun toScheduleRow(scheduleId: String): ScheduleRow {

            val row = ScheduleRow()
            val startDateTime = ZonedDateTime.parse(startTime).withZoneSameInstant(conferenceTZ)
            row.utcStartTimeString = startTime

            if (startDateTime != null) {
                val dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy")
                val timeFormat = DateTimeFormatter.ofPattern("h:mm a")
                row.date = dateFormat.format(startDateTime)
                row.startTime = timeFormat.format(startDateTime).toLowerCase()
            }

            val endDateTime = ZonedDateTime.parse(endTime).withZoneSameInstant(conferenceTZ)
            if (endDateTime != null) {
                val timeFormat = DateTimeFormatter.ofPattern("h:mm a")
                row.endTime = timeFormat.format(endDateTime).toLowerCase()

                if (ZonedDateTime.now().isAfter(endDateTime)) {
                    row.isOver = true
                }
            }

            row.id = scheduleId
            row.room = roomNames.keys.firstOrNull()
            row.trackSortOrder = trackSortOrder
            row.primarySpeakerName = primarySpeakerName
            row.speakerNames = speakerNames.keys.toList()
            row.speakerCount = speakerNames.size
            row.talkDescription = description
            row.talkTitle = name
            row.speakerNameToOrgName = speakerNameToOrg
            row.photoUrlMap = speakerNameToPhotoUrl

            if (startDateTime != null && endDateTime != null) {
                val now = ZonedDateTime.now()
                if (now.isAfter(startDateTime.minusMinutes(TIME_BETWEEN_SESSIONS)) &&
                        now.isBefore(endDateTime)
                ) {
                    row.isCurrentSession = true
                }
            }

            return row
        }
    }

    data class EventSpeaker(
        val pictureUrl: String = "",
        val socialProfiles: HashMap<String, String>? = HashMap(0),
        var bio: String = "",
        var title: String = "",
        var org: String = "",
        var name: String = ""
    ) {

        fun toScheduleDetail(listRow: ScheduleRow): ScheduleDetail {
            val detail = ScheduleDetail(listRow)
            detail.facebook = socialProfiles?.get("facebook") ?: ""
            detail.linkedIn = socialProfiles?.get("linkedIn") ?: ""
            detail.twitter = socialProfiles?.get("twitter") ?: ""
            detail.speakerBio = bio
            return detail
        }

        companion object {
            var SPEAKER_ITEM_ROW = "speaker_item_row"
        }
    }

    data class VolunteerEvent(
        val twitter: String = "",
        val pictureUrl: String = "",
        var position: String = "",
        var firstName: String = "",
        var lastName: String = ""
    )

    class FaqEvent {

        data class Answer(
            var answer: String = "",
            var photoLink: String = "",
            var mapLink: String = "",
            var otherLink: String = ""
        )

        var answers: List<Answer> = emptyList()
        var question: String = ""
    }

    data class User(
        val id: String? = "",
        val username: String? = "",
        val pictureUrl: String? = "",
        val displayName: String? = "",
        val twitter: String? = "",
        val savedSessionIds: List<String> = ArrayList()
    )
}

