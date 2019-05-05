package com.mentalmachines.droidconboston.views.faq

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mentalmachines.droidconboston.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractHeaderItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * Header for FAQ view
 */
class FaqAdapterItemHeader internal constructor(private val question: String) :
    AbstractHeaderItem<FaqAdapterItemHeader.ViewHolder>() {

    override fun equals(other: Any?): Boolean {
        if (other is FaqAdapterItemHeader) {
            val inItem = other as FaqAdapterItemHeader?
            return this.question == inItem!!.question
        }
        return false
    }

    override fun hashCode(): Int {
        return question.hashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.faq_header
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): ViewHolder {
        return FaqAdapterItemHeader.ViewHolder(view, adapter, true)
    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>,
        holder: FaqAdapterItemHeader.ViewHolder,
        position: Int,
        payloads: List<*>
    ) {
        holder.header.text = question
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
            header = parent.findViewById(R.id.question_text)
        }
    }
}
