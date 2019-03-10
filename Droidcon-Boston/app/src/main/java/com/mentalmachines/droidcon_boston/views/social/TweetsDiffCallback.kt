package com.mentalmachines.droidcon_boston.views.social

import androidx.recyclerview.widget.DiffUtil
import com.mentalmachines.droidcon_boston.modal.Tweet

class TweetsDiffCallback : DiffUtil.ItemCallback<Tweet>() {
    override fun areItemsTheSame(oldItem: Tweet, newItem: Tweet): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Tweet, newItem: Tweet): Boolean {
        return oldItem == newItem
    }
}