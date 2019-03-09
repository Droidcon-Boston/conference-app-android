package com.mentalmachines.droidcon_boston.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


/*
 * View models for schedule and schedule detail items.
 */
class Schedule {

    @Parcelize
    data class ScheduleRow(
        var primarySpeakerName: String = "",
        var id: String = "",
        var startTime: String = "",
        var talkTitle: String = "",
        var speakerCount: Int = 0,
        var talkDescription: String = "",
        var speakerNames: List<String> = emptyList(),
        var speakerNameToOrgName: HashMap<String, String> = HashMap(0),
        var utcStartTimeString: String = "",
        var endTime: String = "",
        var room: String = "",
        var date: String = "",
        var trackSortOrder: Int = 0,
        var photoUrlMap: HashMap<String, String> = HashMap(0),
        var isOver: Boolean = false,
        var isCurrentSession: Boolean = false
    ) : Parcelable {

        fun hasSpeaker(): Boolean = speakerNames.isNotEmpty()

        fun hasMultipleSpeakers(): Boolean = speakerNames.size > 1

        fun getSpeakerString(): String? = speakerNames.joinToString(", ")

        fun containsKeyword(keyword: String): Boolean {
            return this.talkTitle.contains(keyword, ignoreCase = true)
                    || this.talkDescription.contains(keyword, ignoreCase = true)
                    || this.speakerNames.any { it.contains(keyword, ignoreCase = true) }
        }
    }

    data class ScheduleDetail(
        val listRow: ScheduleRow,
        var speakerBio: String = "",
        var twitter: String = "",
        var linkedIn: String = "",
        var facebook: String = ""
    ) {
        val id: String get() = listRow.id
    }

    companion object {
        var SCHEDULE_ITEM_ROW = "schedule_item_row"
    }
}
