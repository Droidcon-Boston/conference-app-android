package com.mentalmachines.droidcon_boston.data

import android.content.Context
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleDetail
import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleRow
import com.mentalmachines.droidcon_boston.utils.NotificationUtils
import com.mentalmachines.droidcon_boston.utils.getHtmlFormattedSpanned
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter


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
            var trackSortOrder: Int = 0) {

        fun getLocalStartTime(): LocalDateTime {
            return ZonedDateTime.parse(startTime).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
        }

        fun scheduleNotification(context: Context, eventId: String) {
            NotificationUtils(context).scheduleNotificationAlarm(getLocalStartTime().minusMinutes(SESSION_REMINDER_MINUTES_BEFORE),
                    eventId, context.getString(R.string.str_session_start_soon, name), description.getHtmlFormattedSpanned().toString())
        }

        fun toScheduleRow(scheduleId: String): ScheduleRow {
            val row = ScheduleRow()
            val startDateTime = ZonedDateTime.parse(startTime).withZoneSameInstant(ZoneId.systemDefault())
            row.utcStartTimeString = startTime

            if (startDateTime != null) {
                val dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy")
                val timeFormat = DateTimeFormatter.ofPattern("h:mm a")
                row.date = dateFormat.format(startDateTime)
                row.startTime = timeFormat.format(startDateTime).toLowerCase()
            }

            val endDateTime = ZonedDateTime.parse(endTime).withZoneSameInstant(ZoneId.systemDefault())
            if (endDateTime != null) {
                val timeFormat = DateTimeFormatter.ofPattern("h:mm a")
                row.endTime = timeFormat.format(endDateTime).toLowerCase()

                if (ZonedDateTime.now().isAfter(endDateTime)) {
                    row.isOver = true
                }
            }

            row.id = scheduleId
            row.room = roomNames.keys.first()
            row.trackSortOrder = trackSortOrder
            row.primarySpeakerName = primarySpeakerName
            row.speakerNames = speakerNames.keys.toList()
            row.speakerCount = speakerNames.size
            row.talkDescription = description
            row.talkTitle = name
            row.speakerNameToOrgName = speakerNameToOrg
            row.photoUrlMap = speakerNameToPhotoUrl
            return row
        }
    }

    data class SpeakerEvent(
            val socialProfiles: HashMap<String, String>? = HashMap(0),
            val pictureUrl: String = "",
            var bio: String = "",
            var name: String = "")

    data class VolunteerEvent(
            val twitter: String = "",
            val email: String = "",
            val pictureUrl: String = "",
            var position: String = "",
            var firstName: String = "",
            var lastName: String = "")

    data class ScheduleEventDetail(
            val socialProfiles: HashMap<String, String>? = HashMap(0),
            var bio: String = "",
            var title: String = "",
            var org: String = "",
            var name: String = "") {

        fun toScheduleDetail(listRow: ScheduleRow): ScheduleDetail {
            val detail = ScheduleDetail(listRow)
            detail.facebook = socialProfiles?.get("facebook") ?: ""
            detail.linkedIn = socialProfiles?.get("linkedIn") ?: ""
            detail.twitter = socialProfiles?.get("twitter") ?: ""
            detail.speakerBio = bio
            return detail
        }
    }

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
}

