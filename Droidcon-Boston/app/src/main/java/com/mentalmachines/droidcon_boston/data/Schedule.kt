package com.mentalmachines.droidcon_boston.data


/*
 * View models for schedule and schedule detail items.
 */
class Schedule {

    class ScheduleRow {

        var primarySpeakerName: String = ""
        var id: String = ""
        var startTime: String = ""
        var talkTitle: String = ""
        var speakerCount: Int = 0

        var talkDescription: String? = null
        var speakerNames: List<String>? = null
        var photo: String? = null
        var utcStartTimeString: String? = null
        var endTime: String? = null
        var room: String? = null
        var date: String? = null
        var trackSortOrder: Int? = null

        fun hasSpeaker(): Boolean = speakerNames != null && speakerNames!!.isNotEmpty()

        fun hasMultipleSpeakers(): Boolean = speakerNames != null && speakerNames!!.size > 1

        fun getSpeakerString(): String? = speakerNames?.joinToString(", ")
    }

    class ScheduleDetail(val listRow: ScheduleRow) {

        var speakerBio: String? = null
        var twitter: String? = null
        var linkedIn: String? = null
        var facebook: String? = null

        val id: String get() = listRow.id
    }

    companion object {
        var SCHEDULE_ITEM_ROW = "schedule_item_row"
        var MONDAY = "03/26/2018"
        var TUESDAY = "03/27/2018"
    }
}
