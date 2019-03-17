package com.mentalmachines.droidcon_boston.views.social

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.modal.Tweet
import com.mentalmachines.droidcon_boston.views.transform.CircleTransform
import kotlinx.android.synthetic.main.quoted_tweet_item.view.*
import kotlinx.android.synthetic.main.tweet_item_layout.view.*

class TwitterRecyclerViewAdapter : ListAdapter<Tweet, TwitterRecyclerViewAdapter.ViewHolder>
    (TweetsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tweet: Tweet) {
            itemView.run {
                if (tweet.type == R.layout.tweet_item_layout) {
                    // Removing '_normal' in profile image url because it's a low resolution image and
                    // will look blurry. There is no alternative solution for this and twitter
                    // recommends this. Url after removing `_normal` gives high resolution image.
                    Glide.with(context).load(tweet.profileImageUrl.replace("_normal", ""))
                        .transform(CircleTransform(context))
                        .crossFade()
                        .into(profileImage)
                    screenName.text = tweet.screenName
                    name.text = String.format(context.getString(R.string.twitter_handel), tweet.name)
                    content.text = tweet.text
                } else {
                    // Removing '_normal' in profile image url because it's a low resolution image and
                    // will look blurry. There is no alternative solution for this and twitter
                    // recommends this. Url after removing `_normal` gives high resolution image.
                    Glide.with(context).load(tweet.profileImageUrl.replace("_normal", ""))
                        .transform(CircleTransform(context))
                        .crossFade()
                        .into(quotedProfileImage)
                    quotedScreenName.text = tweet.screenName
                    quotedName.text = String.format(context.getString(R.string.twitter_handel), tweet.name)
                    quotedContent.text = tweet.text
                    quotedTweetScreenName.text = tweet.quotedTweet?.screenName
                    quotedTweetName.text = String.format(context.getString(R.string.twitter_handel),
                        tweet.quotedTweet?.name)
                    quotedTweetContent.text = tweet.quotedTweet?.text
                }
            }
        }
    }
}