package com.mentalmachines.droidcon_boston.views.volunteer

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase.VolunteerEvent
import com.mentalmachines.droidcon_boston.views.transform.CircleTransform
import com.mentalmachines.droidcon_boston.views.volunteer.VolunteerAdapterItem.ViewHolder
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * Used for displaying volunteer list items on the all volunteers "volunteers" page.
 */
class VolunteerAdapterItem internal constructor(val itemData: VolunteerEvent) :
        AbstractFlexibleItem<ViewHolder>() {

    override fun bindViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>?, holder: ViewHolder, position: Int, payloads: MutableList<Any>?) {



        var bodyText = itemData.position
        if (!itemData.twitter.isEmpty()) {
            bodyText += "\nTwitter: @${itemData.twitter}"
        }

        if (!itemData.email.isEmpty()) {
            bodyText += "\nEmail: ${itemData.email}"
        }

        holder.name.text = String.format("%s %s", itemData.firstName, itemData.lastName)
        holder.bio.text = bodyText

        val context = holder.name.context

        Glide.with(context)
                .load(itemData.pictureUrl)
                .transform(CircleTransform(context))
                .placeholder(R.drawable.emo_im_cool)
                .crossFade()
                .into(holder.avatar)
    }

    override fun equals(other: Any?): Boolean {
        if (other is VolunteerAdapterItem) {
            val inItem = other as VolunteerAdapterItem?
            return this.itemData.firstName == inItem?.itemData?.firstName
        }
        return false
    }

    override fun hashCode(): Int {
        return itemData.firstName.hashCode()
    }

    override fun getLayoutRes(): Int {
        return R.layout.volunteer_item
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<*>): ViewHolder {
        return ViewHolder(view, adapter)
    }

    class ViewHolder : FlexibleViewHolder {

        private lateinit var rootLayout: View
        lateinit var avatar: ImageView
        private lateinit var avatarLayout: View
        lateinit var name: TextView
        lateinit var bio: TextView

        constructor(view: View, adapter: FlexibleAdapter<*>) : super(view, adapter) {
            findViews(view)
        }

        constructor(view: View, adapter: FlexibleAdapter<*>, stickyHeader: Boolean) : super(view, adapter, stickyHeader) {
            findViews(view)
        }

        private fun findViews(parent: View) {
            rootLayout = parent.findViewById(R.id.volunteerRootLayout)
            avatar = parent.findViewById(R.id.volunteer_image)
            avatarLayout = parent.findViewById(R.id.avatar_layout)
            bio = parent.findViewById(R.id.bio_text)
            name = parent.findViewById(R.id.name_text)
        }
    }
}
