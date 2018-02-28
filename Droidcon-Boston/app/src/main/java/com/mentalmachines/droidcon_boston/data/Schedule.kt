package com.mentalmachines.droidcon_boston.data


/*
 * View models for schedule and schedule detail items.
 */
public class Schedule {

    class ScheduleRow {

        var talkDescription: String? = null
        var talkTitle: String? = null
        var primarySpeakerName: String? = null
        var speakerNames: List<String>? = null
        var speakerCount: Int = 0
        var id: String? = null
        var photo: String? = null
        var utcStartTimeString: String? = null
        var startTime: String? = null
        var endTime: String? = null
        var room: String? = null
        var date: String? = null
        var trackSortOrder: Int? = null

        fun getFirstSpeaker(): String? = if (primarySpeakerName != null) primarySpeakerName!! else speakerNames?.get(0)

        fun getSpeakerString(): String? = speakerNames?.joinToString(", ")
    }

    class ScheduleDetail {

        var listRow: ScheduleRow? = null
            get() = field
        var speakerBio: String? = null
        var twitter: String? = null
        var linkedIn: String? = null
        var facebook: String? = null

        val id: String?
            get() = listRow!!.id
    }

    companion object {
        var SCHEDULE_ITEM_ROW = "schedule_item_row"
        var MONDAY = "03/26/2018"
        var TUESDAY = "03/27/2018"
    }
}
