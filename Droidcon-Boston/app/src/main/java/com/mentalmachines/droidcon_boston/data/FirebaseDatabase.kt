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
        val roomNames: HashMap<String, Boolean>? = null
        val speakerIds: HashMap<String, Boolean>? = null
        val roomIds: HashMap<String, Boolean>? = null
        val description: String? = null;
        val name: String? = null
        val photo: String? = null
        val startTime: String? = null

        fun toScheduleRow(): ScheduleRow {
            val row = ScheduleRow()
            row.room = roomNames!!.keys.first()
            val d = DateUtils.fromISO8601UTC(startTime)
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            val timeFormat = SimpleDateFormat("h:mm a", Locale.US)

            row.date = dateFormat.format(d)
            row.time = timeFormat.format(d)
            row.speakerName = speakerNames!!.keys.first()
            row.talkDescription = description
            row.talkTitle = name
            row.photo = photo // TODO: speaker photo currently not present in the events firebase child (may want to denormalize).
            return row
        }
    }

    class ScheduleEventDetail {
        val socialProfiles: HashMap<String, String>? = null
        var bio: String? = null

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

