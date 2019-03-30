package com.mentalmachines.droidcon_boston.data

import androidx.annotation.LayoutRes
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.modal.Media
import com.mentalmachines.droidcon_boston.modal.QuotedTweet
import com.mentalmachines.droidcon_boston.modal.TweetWithMedia
import com.mentalmachines.droidcon_boston.utils.toDate
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.models.Tweet
import com.mentalmachines.droidcon_boston.modal.Tweet as ViewTweet
import java.text.SimpleDateFormat
import java.util.Locale

class RemoteDataSource : DataSource {

    companion object {

        private const val TWEET_COUNT = 100

        private lateinit var dataSource: RemoteDataSource

        fun getInstance(): RemoteDataSource {
            if (!::dataSource.isInitialized) {
                dataSource = RemoteDataSource()
            }
            return dataSource
        }
    }

    override fun getTweets(): List<TweetWithMedia> {
        val response = TwitterCore
            .getInstance()
            .guestApiClient
            .searchService
            .tweets(
                "%23DroidconBos",
                null,
                null,
                null,
                "latest",
                TWEET_COUNT,
                null,
                0,
                0,
                true
            ).execute()
        return mapTweets(response.body()?.tweets.orEmpty())
    }

    private fun mapTweets(tweets: List<Tweet>): List<TweetWithMedia> {
        val simpleDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
        return tweets.distinctBy {
            when {
                it.retweetedStatus != null -> it.retweetedStatus.id
                else -> it.id
            }
        }.map {
            when {
                it.retweetedStatus != null -> {
                    val type =
                        if (it.retweetedStatus.quotedStatus != null) R.layout.quoted_tweet_item else
                            R.layout.tweet_item_layout

                    mapTweetToTweetWithMedia(type, it.retweetedStatus, simpleDateFormat)
                }
                it.quotedStatus != null -> {
                    mapTweetToTweetWithMedia(R.layout.quoted_tweet_item, it, simpleDateFormat)
                }
                else -> {
                    mapTweetToTweetWithMedia(R.layout.tweet_item_layout, it, simpleDateFormat)
                }
            }
        }
    }

    private fun mapTweetToTweetWithMedia(
        @LayoutRes type: Int, tweet: Tweet,
        simpleDateFormat: SimpleDateFormat
    ): TweetWithMedia {
        val quotedTweet = tweet.quotedStatus?.run {
            QuotedTweet(
                id,
                user.screenName,
                user.name,
                user.profileImageUrlHttps,
                text
            )
        }
        return TweetWithMedia().apply {
            this.tweet = ViewTweet(
                tweet.id,
                tweet.createdAt.toDate(simpleDateFormat),
                type,
                tweet.user.screenName,
                tweet.user.name,
                tweet.user.profileImageUrlHttps,
                tweet.text,
                quotedTweet
            )

            media = tweet.extendedEntities?.media?.map { media ->
                Media(
                    media.id, tweet.id, media.type, media.mediaUrlHttps, media.url, tweet.id
                )
            }

            quotedMedia = tweet.quotedStatus?.extendedEntities?.media?.map { media ->
                Media(
                    media.id, tweet.id, media.type, media.mediaUrlHttps,
                    media.url, quotedTweetId = tweet.quotedStatus.id
                )
            }
        }
    }
}
