package com.mentalmachines.droidcon_boston.views.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.data.Schedule

class ScheduleSearchAdapter(
    context: Context,
    private val layoutRes: Int,
    private val scheduleRows: List<Schedule.ScheduleRow>
): ArrayAdapter<Schedule.ScheduleRow>(context, layoutRes, scheduleRows) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(layoutRes, parent, false)

        val scheduleRow = getItem(position)
        view.findViewById<TextView>(R.id.talk_title).text = scheduleRow?.talkTitle.orEmpty()

        return view
    }

    override fun getItem(position: Int): Schedule.ScheduleRow? {
        return scheduleRows[position]
    }

    override fun getCount(): Int {
        return scheduleRows.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val suggestions = scheduleRows.filter {
                    it.containsKeyword(constraint?.toString().orEmpty())
                }

                return FilterResults().apply {
                    values = suggestions
                    count = suggestions.size
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                clear()

                (results?.values as? List<*>)
                    ?.filterIsInstance(Schedule.ScheduleRow::class.java)
                    ?.forEach {
                        add(it)
                    }

                notifyDataSetChanged()
            }
        }
    }
}