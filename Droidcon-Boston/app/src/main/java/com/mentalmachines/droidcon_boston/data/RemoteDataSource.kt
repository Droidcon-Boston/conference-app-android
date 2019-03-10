package com.mentalmachines.droidcon_boston.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.modal.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.models.Search
import com.twitter.sdk.android.core.models.Tweet
import com.mentalmachines.droidcon_boston.modal.Tweet as ViewTweet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RemoteDataSource : DataSource {

    private lateinit var tweets: MutableLiveData<Result<List<ViewTweet>>>

    companion object {
        private lateinit var dataSource: DataSource

        fun getInstance(): DataSource {
            if (!::dataSource.isInitialized) {
                dataSource = RemoteDataSource()
            }
            return dataSource
        }

    }

    override fun getTweets(): LiveData<Result<List<ViewTweet>>> {
        tweets = MutableLiveData()
        tweets.postValue(Result.Loading())
        refreshTweets()
        return tweets
    }

    override fun refreshTweets() {
        TwitterCore
            .getInstance()
            .guestApiClient
            .searchService
            .tweets("%23DroidconBos",
                null,
                null,
                null,
                "latest",
                100,
                null,
                0,
                0,
                true)
            .enqueue(object : Callback<Search> {
                override fun onFailure(call: Call<Search>, t: Throwable) {
                    //TODO: Decide message
                    tweets.postValue(Result.Error("Error"))
                }

                override fun onResponse(call: Call<Search>, response: Response<Search>) {
                    //TODO : check why quoted tweets are null
                    tweets.postValue(Result.Data(mapTweets(response.body()?.tweets ?: Collections.emptyList())))
                }

            })
    }

    private fun mapTweets(tweets: List<Tweet>): List<ViewTweet> {
        return tweets.map {
            when {
                it.retweetedStatus != null -> {
                    val quotedTweet = if (it.retweetedStatus.quotedStatus != null) {
                        it.retweetedStatus.quotedStatus.run {
                            ViewTweet(id,
                                R.layout.tweet_item_layout,
                                user.screenName,
                                user.name,
                                user.profileImageUrlHttps,
                                text)
                        }
                    } else null
                    val type = if (quotedTweet != null) R.layout.quoted_tweet_item else R.layout.tweet_item_layout
                    ViewTweet(it.retweetedStatus.id,
                        type,
                        it.retweetedStatus.user.screenName,
                        it.retweetedStatus.user.name,
                        it.retweetedStatus.user.profileImageUrlHttps,
                        it.retweetedStatus.text,
                        quotedTweet)
                }
                it.quotedStatus != null -> {
                    val quotedTweet = ViewTweet(it.quotedStatus.id,
                        R.layout.tweet_item_layout,
                        it.quotedStatus.user.screenName,
                        it.quotedStatus.user.name,
                        it.quotedStatus.user.profileImageUrlHttps,
                        it.quotedStatus.text)
                    ViewTweet(it.id,
                        R.layout.quoted_tweet_item,
                        it.user.screenName,
                        it.user.name,
                        it.user.profileImageUrlHttps,
                        it.text,
                        quotedTweet)
                }
                else -> { ViewTweet(it.id,
                    R.layout.tweet_item_layout,
                    it.user.screenName,
                    it.user.name,
                    it.user.profileImageUrlHttps,
                    it.text)
                }
            }
        }.distinctBy { it.id }
    }
}