package com.mentalmachines.droidcon_boston.views.detail

import android.app.Fragment
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.R.string
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase.ScheduleEventDetail
import com.mentalmachines.droidcon_boston.data.Schedule
import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleDetail
import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleRow
import com.mentalmachines.droidcon_boston.data.UserAgendaRepo
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import com.mentalmachines.droidcon_boston.utils.NotificationUtils
import com.mentalmachines.droidcon_boston.utils.ServiceLocator.Companion.gson
import com.mentalmachines.droidcon_boston.utils.getHtmlFormattedSpanned
import com.mentalmachines.droidcon_boston.views.transform.CircleTransform
import kotlinx.android.synthetic.main.agenda_detail_fragment.*


class AgendaDetailFragment : Fragment() {

    private lateinit var scheduleDetail: ScheduleDetail

    private val firebaseHelper = FirebaseHelper.instance

    private val userAgendaRepo: UserAgendaRepo
        get() = UserAgendaRepo.getInstance(fab_agenda_detail_bookmark.context)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.agenda_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemData = gson.fromJson(arguments.getString(Schedule.SCHEDULE_ITEM_ROW), ScheduleRow::class.java)
        fetchDataFromFirebase(itemData)
        populateView(itemData)
    }

    private fun populateView(itemData: ScheduleRow) {
        tv_agenda_detail_title.text = itemData.talkTitle
        tv_agenda_detail_room.text = resources.getString(R.string.str_agenda_detail_room, itemData.room)
        tv_agenda_detail_time.text = resources.getString(R.string.str_agenda_detail_time, itemData.startTime, itemData.endTime)

        fab_agenda_detail_bookmark.setOnClickListener({

            val nextBookmarkStatus = !userAgendaRepo.isSessionBookmarked(scheduleDetail.id)
            userAgendaRepo.bookmarkSession(scheduleDetail.id, nextBookmarkStatus)
            val context = tv_agenda_detail_title.context
            if (nextBookmarkStatus) {
                NotificationUtils(context).scheduleMySessionNotifications()
            } else {
                NotificationUtils(context).cancelNotificationAlarm(itemData.id)
            }

            Snackbar.make(agendaDetailView,
                    if (nextBookmarkStatus)
                        getString(R.string.saved_agenda_item)
                    else getString(R.string.removed_agenda_item),
                    Snackbar.LENGTH_SHORT).show()

            showBookmarkStatus(scheduleDetail)
        })

        populateSpeakersInformation(itemData)
    }

    private fun fetchDataFromFirebase(itemData: ScheduleRow) {
        firebaseHelper.speakerDatabase.orderByChild("name")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (speakerSnapshot in dataSnapshot.children) {
                            val detail = speakerSnapshot.getValue(ScheduleEventDetail::class.java)
                            if (detail != null) {
                                scheduleDetail = detail.toScheduleDetail(itemData)
                                showAgendaDetail(scheduleDetail)
                            }

                        }

                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e(javaClass.canonicalName, "detailQuery:onCancelled", databaseError.toException())
                    }
                })
    }


    private fun populateSpeakersInformation(itemData: ScheduleRow) = when {
        itemData.speakerNames.isEmpty() -> {
            tv_agenda_detail_speaker_name.visibility = View.GONE
            v_agenda_detail_speaker_divider.visibility = View.GONE
        }
        else -> {
            var speakerNames = ""
            var marginValue = 28
            itemData.speakerNames.forEach {
                val orgName: String? = itemData.speakerNameToOrgName[it]
                // append org name to speaker name
                speakerNames += it + when {
                    orgName != null -> " - $orgName"
                    else -> {
                        // Do nothing
                    }
                }

                if (itemData.speakerNames.size > 1) {
                    tv_agenda_detail_speaker_title.text = getString(string.header_speakers)

                    // if the current speaker name is not the last then add a line break
                    if (it != itemData.speakerNames.last()) {
                        speakerNames += "\n"
                    }
                } else {
                    tv_agenda_detail_speaker_title.text = getString(string.header_speaker)
                }


                // Add an imageview to the relative layout
                val tempImg = ImageView(activity)
                val lp = RelativeLayout.LayoutParams(150, 150)
                if (it == itemData.speakerNames.first()) {
                    lp.setMargins(28, 0, 0, 16)
                } else {
                    marginValue += 120
                    lp.setMargins(marginValue, 0, 0, 16)
                }

                // add the imageview above the textview for room data
                lp.addRule(RelativeLayout.ABOVE, tv_agenda_detail_room.id)
                tempImg.layoutParams = lp

                // add it as a child to the relative layout
                agendaDetailView.addView(tempImg)

                Glide.with(this)
                        .load(itemData.photoUrlMap[it])
                        .transform(CircleTransform(activity.applicationContext))
                        .placeholder(R.drawable.emo_im_cool)
                        .crossFade()
                        .into(tempImg)

            }
            tv_agenda_detail_speaker_name.text = speakerNames

        }
    }

    fun showAgendaDetail(scheduleDetail: ScheduleDetail) {
        populateSpeakersInformation(scheduleDetail.listRow)
        showBookmarkStatus(scheduleDetail)

        tv_agenda_detail_title.text = scheduleDetail.listRow.talkTitle
        tv_agenda_detail_description.text = scheduleDetail.listRow.talkDescription.getHtmlFormattedSpanned()
    }

    private fun showBookmarkStatus(scheduleDetail: ScheduleDetail) {
        val userAgendaRepo = userAgendaRepo
        fab_agenda_detail_bookmark.backgroundTintList = if (userAgendaRepo.isSessionBookmarked(scheduleDetail.id))
            ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorAccent))
        else
            ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorLightGray))
    }
}
