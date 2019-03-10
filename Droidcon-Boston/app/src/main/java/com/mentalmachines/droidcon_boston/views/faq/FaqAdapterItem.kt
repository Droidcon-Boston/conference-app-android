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

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<androidx.recyclerview.widget.RecyclerView.ViewHolder>>
    ): ViewHolder {
        return FaqAdapterItem.ViewHolder(view, adapter)
    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<androidx.recyclerview.widget.RecyclerView.ViewHolder>>,
        holder: FaqAdapterItem.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

        holder.faqText.text = itemData.answer
        if (!TextUtils.isEmpty(itemData.photoLink)) {
            val context = holder.faqText.context
            Glide.with(context).load(itemData.photoLink).crossFade().centerCrop()
                .into(holder.faqPhoto)
            holder.faqPhoto.visibility = View.VISIBLE
        } else {
            holder.faqPhoto.visibility = View.GONE
        }

        if (!TextUtils.isEmpty(itemData.otherLink) || !TextUtils.isEmpty(itemData.mapLink)) {
            addBackgroundRipple(holder)
        }
    }

    private fun addBackgroundRipple(holder: ViewHolder) {
        val outValue = TypedValue()
        val context = holder.faqText.context
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        holder.rootLayout.setBackgroundResource(outValue.resourceId)
    }


    class ViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {

        lateinit var rootLayout: View

        lateinit var faqText: TextView

        lateinit var faqPhoto: ImageView

        private fun findViews(parent: View) {
            rootLayout = parent.findViewById(R.id.root_layout)
            faqText = parent.findViewById(R.id.faq_text)
            faqPhoto = parent.findViewById(R.id.faq_photo)
        }

        init {
            findViews(view)
        }
    }
}
