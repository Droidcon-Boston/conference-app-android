package com.mentalmachines.droidcon_boston.views.faq

import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase.FaqEvent.Answer
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractSectionableItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * Used for displaying the FAQ items
 */
class FaqAdapterItem internal constructor(val itemData: Answer, header: FaqAdapterItemHeader) :
    AbstractSectionableItem<FaqAdapterItem.ViewHolder, FaqAdapterItemHeader>(header) {

    override fun equals(other: Any?): Boolean {
        if (other is FaqAdapterItem) {
            val inItem = other as FaqAdapterItem?
            return this.itemData.answer == inItem!!.itemData.answer
        }
        return false
    }

    override fun hashCode(): Int {
        return itemData.answer.hashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.faq_item
    }

    override fun createViewHolder(view: View,
                                  adapter: FlexibleAdapter<IFlexible<androidx.recyclerview.widget.RecyclerView.ViewHolder>>): ViewHolder {
        return FaqAdapterItem.ViewHolder(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<androidx.recyclerview.widget.RecyclerView.ViewHolder>>,
                                holder: FaqAdapterItem.ViewHolder,
                                position: Int,
                                payloads: MutableList<Any>) {

        holder.faq_text.text = itemData.answer
        if (!TextUtils.isEmpty(itemData.photoLink)) {
            val context = holder.faq_text.context
            Glide.with(context).load(itemData.photoLink).crossFade().centerCrop()
                .into(holder.faq_photo)
            holder.faq_photo.visibility = View.VISIBLE
        } else {
            holder.faq_photo.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(itemData.otherLink) || !TextUtils.isEmpty(itemData.mapLink)) {
            addBackgroundRipple(holder)
        }
    }

    private fun addBackgroundRipple(holder: ViewHolder) {
        val outValue = TypedValue()
        val context = holder.faq_text.context
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        holder.root_layout.setBackgroundResource(outValue.resourceId)
    }


    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {

        lateinit var root_layout: View

        lateinit var faq_text: TextView

        lateinit var faq_photo: ImageView

        private fun findViews(parent: View) {
            root_layout = parent.findViewById(R.id.root_layout)
            faq_text = parent.findViewById(R.id.faq_text)
            faq_photo = parent.findViewById(R.id.faq_photo)
        }

        init {
            findViews(view)
        }
    }
}
