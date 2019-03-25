package com.mentalmachines.droidcon_boston.data

import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.modal.Media
import com.mentalmachines.droidcon_boston.modal.QuotedTweet
import com.mentalmachines.droidcon_boston.modal.TweetWithMedia
import com.mentalmachines.droidcon_boston.utils.toDate
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.models.Tweet
import com.mentalmachines.droidcon_boston.modal.Tweet as ViewTweet
import java.text.SimpleDateFormat
import java.util.Collections
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
        return mapTweets(response.body()?.tweets ?: Collections.emptyList())
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
                    val quotedTweet = it.retweetedStatus.quotedStatus?.run {
                        QuotedTweet(
                            id,
                            user.screenName,
                            user.name,
                            user.profileImageUrlHttps,
                            text
                        )
                    }
                    val type = if (quotedTweet != null) R.layout.quoted_tweet_item else
                        R.layout.tweet_item_layout

                    TweetWithMedia().apply {
                        tweet = ViewTweet(
                            it.retweetedStatus.id,
                            it.createdAt.toDate(simpleDateFormat),
                            type,
                            it.retweetedStatus.user.screenName,
                            it.retweetedStatus.user.name,
                            it.retweetedStatus.user.profileImageUrlHttps,
                            it.retweetedStatus.text, quotedTweet
                        )

                        media = it.retweetedStatus.extendedEntities?.media?.map { media ->
                            Media(
                                media.id, it.retweetedStatus.id,
                                media.type, media.mediaUrlHttps, media.url, it.retweetedStatus.id
                            )
                        }

                        quotedMedia =
                            it.retweetedStatus.quotedStatus?.extendedEntities?.media?.map { media ->
                                Media(
                                    media.id,
                                    it.retweetedStatus.id,
                                    media.type,
                                    media.mediaUrlHttps,
                                    media.url,
                                    quotedTweetId = it.retweetedStatus.quotedStatus.id
                                )
                            }
                    }
                }
                it.quotedStatus != null -> {
                    val quotedTweet = QuotedTweet(
                        it.quotedStatus.id,
                        it.quotedStatus.user.screenName,
                        it.quotedStatus.user.name,
                        it.quotedStatus.user.profileImageUrlHttps,
                        it.quotedStatus.text
                    )

                    TweetWithMedia().apply {
                        tweet = ViewTweet(
                            it.id,
                            it.createdAt.toDate(simpleDateFormat),
                            R.layout.quoted_tweet_item,
                            it.user.screenName,
                            it.user.name,
                            it.user.profileImageUrlHttps,
                            it.text, quotedTweet
                        )

                        media = it.extendedEntities?.media?.map { media ->
                            Media(
                                media.id, it.id, media.type, media.mediaUrlHttps, media.url, it.id
                            )
                        }

                        quotedMedia = it.quotedStatus.extendedEntities?.media?.map { media ->
                            Media(
                                media.id, it.id, media.type, media.mediaUrlHttps,
                                media.url, quotedTweetId = it.quotedStatus.id
                            )
                        }
                    }
                }
                else -> {
                    TweetWithMedia().apply {
                        tweet = ViewTweet(
                            it.id,
                            it.createdAt.toDate(simpleDateFormat),
                            R.layout.tweet_item_layout,
                            it.user.screenName,
                            it.user.name,
                            it.user.profileImageUrlHttps,
                            it.text
                        )

                        media = it.extendedEntities?.media?.map { media ->
                            Media(
                                media.id, it.id, media.type, media.mediaUrlHttps, media.url, it.id
                            )
                        }
                    }
                }
            }
        }
    }
}
