package com.mentalmachines.droidconboston.views.social

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mentalmachines.droidconboston.R
import com.mentalmachines.droidconboston.modal.Media
import com.mentalmachines.droidconboston.modal.TweetWithMedia
import com.mentalmachines.droidconboston.views.transform.CircleTransform
import kotlinx.android.synthetic.main.quoted_tweet_item.view.*
import kotlinx.android.synthetic.main.tweet_item_layout.view.*
import kotlinx.android.synthetic.main.twitter_four_image_layout.view.*
import kotlinx.android.synthetic.main.twitter_gif_layout.view.*
import kotlinx.android.synthetic.main.twitter_one_image_layout.view.*
import kotlinx.android.synthetic.main.twitter_three_image_layout.view.*
import kotlinx.android.synthetic.main.twitter_two_image_layout.view.*
import kotlinx.android.synthetic.main.twitter_video_layout.view.*

class TwitterRecyclerViewAdapter(private val onMediaClickListener: OnMediaClickListener) :
    ListAdapter<TweetWithMedia, TwitterRecyclerViewAdapter.ViewHolder>(TweetsDiffCallback()) {

    interface OnMediaClickListener {
        fun onVideoOrGifClick(url: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                viewType, parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onMediaClickListener)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).tweet.type
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(tweet: TweetWithMedia, onMediaClickListener: OnMediaClickListener) {
            itemView.run {
                if (tweet.tweet.type == R.layout.tweet_item_layout) {
                    // Removing '_normal' in profile image url because it's a low resolution image and
                    // will look blurry. There is no alternative solution for this and twitter
                    // recommends this. Url after removing `_normal` gives high resolution image.
                    Glide.with(context).load(
                        tweet.tweet.profileImageUrl.replace(
                            "_normal",
                            ""
                        )
                    ).transform(CircleTransform(context)).crossFade().into(profileImage)
                    screenName.text = tweet.tweet.screenName
                    name.text = String.format(
                        context.getString(R.string.twitter_handel),
                        tweet.tweet.name
                    )
                    content.text = tweet.tweet.text

                    if (tweet.media.isNullOrEmpty()) {
                        mediaContainer.visibility = View.GONE
                    } else {
                        renderMedia(mediaContainer, tweet.media!!, onMediaClickListener)
                    }
                } else {
                    // Removing '_normal' in profile image url because it's a low resolution image and
                    // will look blurry. There is no alternative solution for this and twitter
                    // recommends this. Url after removing `_normal` gives high resolution image.
                    Glide.with(context).load(
                        tweet.tweet.profileImageUrl.replace(
                            "_normal",
                            ""
                        )
                    ).transform(CircleTransform(context)).crossFade().into(quotedProfileImage)
                    quotedScreenName.text = tweet.tweet.screenName
                    quotedName.text = String.format(
                        context.getString(R.string.twitter_handel),
                        tweet.tweet.name
                    )
                    quotedContent.text = tweet.tweet.text
                    if (tweet.media.isNullOrEmpty()) {
                        quotedMediaContainer.visibility = View.GONE
                    } else {
                        renderMedia(quotedMediaContainer, tweet.media!!, onMediaClickListener)
                    }
                    quotedTweetScreenName.text = tweet.tweet.quotedTweet?.screenName
                    quotedTweetName.text = String.format(
                        context.getString(R.string.twitter_handel),
                        tweet.tweet.quotedTweet?.name
                    )
                    quotedTweetContent.text = tweet.tweet.quotedTweet?.text

                    if (tweet.quotedMedia.isNullOrEmpty()) {
                        quotedTweetMediaContainer.visibility = View.GONE
                    } else {
                        renderMedia(
                            quotedTweetMediaContainer, tweet.quotedMedia!!,
                            onMediaClickListener
                        )
                    }
                }
            }
        }

        private fun renderMedia(
            mediaContainer: ViewGroup,
            media: List<Media>,
            onMediaClickListener: OnMediaClickListener
        ) {
            mediaContainer.visibility = View.VISIBLE
            when (media.size) {
                1 -> {
                    when (media[0].type) {
                        Media.MEDIA_TYPE_PHOTO -> {
                            renderPhotos(mediaContainer, media)
                        }
                        Media.MEDIA_TYPE_GIF -> {
                            mediaContainer.run {
                                mediaOne.visibility = View.GONE
                                twoMediaContainer.visibility = View.GONE
                                threeMediaContainer.visibility = View.GONE
                                fourMediaContainer.visibility = View.GONE
                                videoContainer.visibility = View.GONE
                                gifContainer.visibility = View.VISIBLE
                                Glide.with(context).load("${media[0].mediaUrlHttps}:small")
                                    .crossFade().into(gifImage)
                                gifContainer.setOnClickListener {
                                    onMediaClickListener.onVideoOrGifClick(media[0].url)
                                }
                            }
                        }
                        Media.MEDIA_TYPE_VIDEO -> {
                            mediaContainer.run {
                                mediaOne.visibility = View.GONE
                                twoMediaContainer.visibility = View.GONE
                                threeMediaContainer.visibility = View.GONE
                                fourMediaContainer.visibility = View.GONE
                                videoContainer.visibility = View.VISIBLE
                                gifContainer.visibility = View.GONE
                                Glide.with(context).load("${media[0].mediaUrlHttps}:small")
                                    .crossFade().into(videoImage)
                                videoContainer.setOnClickListener {
                                    onMediaClickListener.onVideoOrGifClick(media[0].url)
                                }
                            }
                        }
                    }
                }
                else -> {
                    renderPhotos(mediaContainer, media)
                }
            }
        }

        private fun renderPhotos(mediaContainer: ViewGroup, media: List<Media>) {
            mediaContainer.run {
                mediaOne.visibility = if (media.size == 1) View.VISIBLE else View.GONE
                twoMediaContainer.visibility = if (media.size == 2) View.VISIBLE else View.GONE
                threeMediaContainer.visibility = if (media.size == 3) View.VISIBLE else View.GONE
                fourMediaContainer.visibility = if (media.size == 4) View.VISIBLE else View.GONE
                videoContainer.visibility = View.GONE
                gifContainer.visibility = View.GONE
                when (media.size) {
                    1 -> {
                        Glide.with(context).load("${media[0].mediaUrlHttps}:small")
                            .crossFade().into(mediaOne)
                    }
                    2 -> {
                        Glide.with(context).load("${media[0].mediaUrlHttps}:small")
                            .crossFade().into(twoImageMediaOne)
                        Glide.with(context).load("${media[1].mediaUrlHttps}:small")
                            .crossFade().into(twoImageMediaTwo)
                    }
                    3 -> {
                        Glide.with(mediaContainer.context).load("${media[0].mediaUrlHttps}:small")
                            .crossFade().into(threeImageMediaOne)
                        Glide.with(mediaContainer.context).load("${media[1].mediaUrlHttps}:small")
                            .crossFade().into(threeImageMediaTwo)
                        Glide.with(mediaContainer.context).load("${media[2].mediaUrlHttps}:small")
                            .crossFade().into(threeImageMediaThree)
                    }
                    4 -> {
                        Glide.with(mediaContainer.context).load("${media[0].mediaUrlHttps}:small")
                            .crossFade().into(fourImageMediaOne)
                        Glide.with(mediaContainer.context).load("${media[1].mediaUrlHttps}:small")
                            .crossFade().into(fourImageMediaTwo)
                        Glide.with(mediaContainer.context).load("${media[2].mediaUrlHttps}:small")
                            .crossFade().into(fourImageMediaThree)
                        Glide.with(mediaContainer.context).load("${media[3].mediaUrlHttps}:small")
                            .crossFade().into(fourImageMediaFour)
                    }
                }
                return@run
            }
        }
    }
}
