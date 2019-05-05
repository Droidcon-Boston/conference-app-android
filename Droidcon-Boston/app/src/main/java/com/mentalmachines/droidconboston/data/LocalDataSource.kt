package com.mentalmachines.droidconboston.data

import android.content.Context
import com.mentalmachines.droidconboston.modal.Media
import com.mentalmachines.droidconboston.modal.QuotedTweet
import com.mentalmachines.droidconboston.modal.Tweet
import com.mentalmachines.droidconboston.modal.TweetWithMedia

class LocalDataSource private constructor(private val appDatabase: AppDatabase) : DataSource {

    companion object {
        private var dataSource: LocalDataSource? = null
        fun getInstance(context: Context): LocalDataSource {
            return dataSource ?: LocalDataSource(AppDatabase.getInstance(context)).also {
                dataSource = it
            }
        }
    }

    override fun getTweets(): List<TweetWithMedia> {
        return appDatabase.tweetDao().getTweets()
    }

    fun updateTweets(tweets: List<TweetWithMedia>) {
        segregateEntity(tweets)
    }

    private fun segregateEntity(tweetsWithMedia: List<TweetWithMedia>) {
        val tweets: MutableList<Tweet> = arrayListOf()
        val quotedTweet: MutableList<QuotedTweet> = arrayListOf()
        val media: MutableList<Media> = arrayListOf()

        tweetsWithMedia.forEach { tweetWithMedia ->
            tweets.add(tweetWithMedia.tweet)
            tweetWithMedia.media?.let { media.addAll(it) }
            tweetWithMedia.quotedMedia?.let { media.addAll(it) }
            tweetWithMedia.tweet.quotedTweet?.let { quotedTweet.add(it) }
        }

        if (tweets.isNotEmpty()) {
            appDatabase.tweetDao().updateTweets(tweets)
        }

        if (quotedTweet.isNotEmpty()) {
            appDatabase.quotedTweetDao().updateTweets(quotedTweet)
        }

        if (media.isNotEmpty()) {
            appDatabase.mediaDao().insertAll(media)
        }
    }
}
