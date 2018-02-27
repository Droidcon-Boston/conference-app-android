package com.mentalmachines.droidcon_boston.views.agenda

import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.data.Schedule
import com.mentalmachines.droidcon_boston.data.UserAgendaRepo
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractSectionableItem
import eu.davidea.viewholders.FlexibleViewHolder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Used for displaying the schedule with sticky headers with optional day filtering
 */
class ScheduleAdapterItem internal constructor(val itemData: Schedule.ScheduleRow,
                                               header: ScheduleAdapterItemHeader) :
        AbstractSectionableItem<ScheduleAdapterItem.ViewHolder, ScheduleAdapterItemHeader>(header) {

    var startTime: Date? = null

    var roomSortOrder = itemData.trackSortOrder

    val title: String
        get() = itemData.talkTitle

    init {
        val dateTimeString = itemData.date + " " + itemData.startTime
        val format = SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.US)
        try {
            startTime = format.parse(dateTimeString)
        } catch (e: ParseException) {
            Log.e("ScheduleAdapterItem", "Parse error: $e for $dateTimeString")
        }

    }

    override fun equals(other: Any?): Boolean {
        if (other is ScheduleAdapterItem) {
            val inItem = other as ScheduleAdapterItem?
            return this.itemData.talkTitle == inItem!!.itemData.talkTitle
        }
        return false
    }

    override fun hashCode(): Int {
        return itemData.talkTitle.hashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.schedule_item
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<*>): ViewHolder {
        return ScheduleAdapterItem.ViewHolder(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>,
                                holder: ScheduleAdapterItem.ViewHolder,
                                position: Int,
                                payloads: List<*>) {

        if (itemData.speakerName == null) {
            holder.sessionLayout.visibility = View.GONE
            holder.avatar.visibility = View.GONE
            holder.bigTitle.visibility = View.VISIBLE

            holder.bigTitle.text = itemData.talkTitle

            if (itemData.photo == null) {
                holder.rootLayout.background = null
            } else {
                addBackgroundRipple(holder)
            }

            holder.bookmarkIndicator.visibility = View.INVISIBLE
        } else {
            holder.sessionLayout.visibility = View.VISIBLE
            holder.avatar.visibility = View.VISIBLE
            holder.bigTitle.visibility = View.GONE

            holder.title.text = itemData.talkTitle
            holder.time.text = String.format("%s - %s", itemData.startTime, itemData.endTime)
            holder.speaker.text = itemData.speakerName
            holder.room.text = itemData.room

            holder.speakerCount.visibility = if (itemData.speakerCount > 1) View.VISIBLE else View.GONE
            holder.speakerCount.text = String.format("+%d", itemData.speakerCount-1)

            val context = holder.title.context
            Glide.with(context)
                    .load(itemData.photo)
                    .transform(CircleTransform(context))
                    .crossFade()
                    .into(holder.avatar)

            val userAgendaRepo = UserAgendaRepo.getInstance(holder.bookmarkIndicator.context)
            holder.bookmarkIndicator.visibility = if (userAgendaRepo.isSessionBookmarked(itemData.id))
                View.VISIBLE
            else
                View.INVISIBLE

            addBackgroundRipple(holder)
        }
    }

    private fun addBackgroundRipple(holder: ViewHolder) {
        val outValue = TypedValue()
        val context = holder.title.context
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        holder.rootLayout.setBackgroundResource(outValue.resourceId)
    }


    class ViewHolder : FlexibleViewHolder {

        lateinit var rootLayout: View

        lateinit var bookmarkIndicator: ImageView

        lateinit var avatar: ImageView

        lateinit var title: TextView

        lateinit var time: TextView

        lateinit var speaker: TextView

        lateinit var speakerCount: TextView

        lateinit var room: TextView

        lateinit var sessionLayout: View

        lateinit var bigTitle: TextView

        constructor(view: View, adapter: FlexibleAdapter<*>) : super(view, adapter) {

            findViews(view)
        }

        constructor(view: View, adapter: FlexibleAdapter<*>, stickyHeader: Boolean) : super(view, adapter, stickyHeader) {

            findViews(view)
        }

        private fun findViews(parent: View) {
            rootLayout = parent.findViewById(R.id.rootLayout)
            bookmarkIndicator = parent.findViewById(R.id.bookmark_indicator)
            avatar = parent.findViewById(R.id.speaker_image)
            title = parent.findViewById(R.id.title_text)
            time = parent.findViewById(R.id.time_text)
            speaker = parent.findViewById(R.id.speaker_name_text)
            speakerCount = parent.findViewById(R.id.speaker_count)
            room = parent.findViewById(R.id.room_text)
            sessionLayout = parent.findViewById(R.id.session_layout)
            bigTitle = parent.findViewById(R.id.bigtitle_text)
        }
    }
}
