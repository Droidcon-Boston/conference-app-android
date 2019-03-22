package com.mentalmachines.droidcon_boston.views.social

import androidx.recyclerview.widget.DiffUtil
import com.mentalmachines.droidcon_boston.modal.TweetWithMedia

class TweetsDiffCallback : DiffUtil.ItemCallback<TweetWithMedia>() {
    override fun areItemsTheSame(oldItem: TweetWithMedia, newItem: TweetWithMedia): Boolean {
        return oldItem.tweet.id == newItem.tweet.id
    }

    override fun areContentsTheSame(oldItem: TweetWithMedia, newItem: TweetWithMedia): Boolean {
        return oldItem.tweet == newItem.tweet
    }
}
