package com.mentalmachines.droidcon_boston.data

import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleDetail
import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleRow
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter


open class FirebaseDatabase {

    data class ScheduleEvent(
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

