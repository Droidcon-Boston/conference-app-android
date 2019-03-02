package com.mentalmachines.droidcon_boston.views.agenda

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mentalmachines.droidcon_boston.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractHeaderItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * Sticky header for schedule view
 */
class ScheduleAdapterItemHeader internal constructor(private val sessionTime: String) :
    AbstractHeaderItem<ScheduleAdapterItemHeader.ViewHolder>() {

    override fun equals(other: Any?): Boolean {
        if (other is ScheduleAdapterItemHeader) {
            val inItem = other as ScheduleAdapterItemHeader?
            return this.sessionTime == inItem!!.sessionTime
        }
        return false
    }

    override fun hashCode(): Int {
        return sessionTime.hashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.schedule_item_header
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return ScheduleAdapterItemHeader.ViewHolder(view, adapter, true)
    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>,
        holder: ScheduleAdapterItemHeader.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.header.text = sessionTime
    }

    class ViewHolder : FlexibleViewHolder {

        lateinit var header: TextView

        constructor(view: View, adapter: FlexibleAdapter<*>) : super(view, adapter) {
            findViews(view)
        }

        internal constructor(
            view: View,
            adapter: FlexibleAdapter<*>,
            stickyHeader: Boolean
        ) : super(view, adapter, stickyHeader) {
            findViews(view)
        }

        private fun findViews(parent: View) {
            header = parent.findViewById(R.id.header_text)
        }
    }
}
