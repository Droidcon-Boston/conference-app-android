package com.mentalmachines.droidcon_boston.views.social

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.modal.SocialModal
import java.util.*

internal class RVSocialListAdapter(private var socialList: ArrayList<SocialModal>) :
    Adapter<RVSocialListAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : ViewHolder(itemView) {

        internal var imageView: ImageView = itemView.findViewById(R.id.social_item_img)

        internal var txtView: TextView

        init {
            txtView = itemView.findViewById(R.id.social_item_tv)
        }
    }

    override fun getItemCount(): Int {
        return socialList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val socialModal = socialList[position]

        holder.txtView.text = socialModal.name

        holder.imageView.setImageResource(socialModal.image_resid)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.social_list_item, parent, false)
        return ListViewHolder(view)
    }
}
