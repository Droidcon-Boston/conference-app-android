package com.mentalmachines.droidcon_boston.data


/*
 * View models for schedule and schedule detail items.
 */
class Schedule {

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
            var isOver: Boolean = false) {

        fun hasSpeaker(): Boolean = speakerNames.isNotEmpty()

        fun hasMultipleSpeakers(): Boolean = speakerNames.size > 1

        fun getSpeakerString(): String? = speakerNames.joinToString(", ")
    }

    class ScheduleDetail(val listRow: ScheduleRow) {

        var speakerBio: String = ""
        var twitter: String = ""
        var linkedIn: String = ""
        var facebook: String = ""

        val id: String get() = listRow.id
    }

    companion object {
        var SCHEDULE_ITEM_ROW = "schedule_item_row"
        var MONDAY = "03/26/2018"
        var TUESDAY = "03/27/2018"
    }
}
