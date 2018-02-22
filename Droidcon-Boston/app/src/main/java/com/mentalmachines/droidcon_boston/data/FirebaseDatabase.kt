package com.mentalmachines.droidcon_boston.data

import com.mentalmachines.droidcon_boston.data.ScheduleDatabase.ScheduleDetail
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase.ScheduleRow
import com.mentalmachines.droidcon_boston.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


open class FirebaseDatabase {

    class ScheduleEvent() {
        val speakerNames: HashMap<String, Boolean>? = null
        val speakerNameToPhotoUrl: HashMap<String, String>? = null
        val roomNames: HashMap<String, Boolean>? = null
        val speakerIds: HashMap<String, Boolean>? = null
        val roomIds: HashMap<String, Boolean>? = null
        val description: String? = null;
        val name: String? = null
        val photo: String? = null
        val startTime: String? = null
        val trackSortOrder: Int? = 0

        fun toScheduleRow(): ScheduleRow {
            val row = ScheduleRow()
            val d = DateUtils.fromISO8601UTC(startTime)

            if (d != null) {
                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
                val timeFormat = SimpleDateFormat("h:mm a", Locale.US)
                row.date = dateFormat.format(d)
                row.time = timeFormat.format(d)
            } else {
                row.date = null
                row.time = null
            }

            row.room = roomNames!!.keys.first()
            row.trackSortOrder = trackSortOrder;
            row.speakerName = speakerNames!!.keys.first()
            row.talkDescription = description
            row.talkTitle = name
            row.photo = speakerNameToPhotoUrl!!.get(row.speakerName)
            return row
        }
    }

    class ScheduleEventDetail {
        val socialProfiles: HashMap<String, String>? = null
        var bio: String? = null
        var title: String? = null
        var org: String? = null
        var name: String? = null

        fun toScheduleDetail(listRow: ScheduleRow) : ScheduleDetail {
            val detail = ScheduleDetail()
            if (socialProfiles == null) {
                detail.facebook = ""
                detail.linkedIn = ""
                detail.twitter = ""
            } else {
                detail.facebook = socialProfiles.get("facebook")
                detail.linkedIn = socialProfiles.get("linkedIn")
                detail.twitter = socialProfiles.get("twitter")
            }
            detail.listRow = listRow
            detail.speakerBio = bio
            return detail
        }
    }
}

