package com.mentalmachines.droidconboston.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.mentalmachines.droidconboston.modal.QuotedTweet

@Dao
abstract class QuotedTweetDao : BaseDao<QuotedTweet> {

    @Transaction
    open fun updateTweets(tweets: List<QuotedTweet>) {
        deleteAll()
        insertAll(tweets)
    }

    @Query("DELETE FROM QuotedTweet")
    abstract fun deleteAll()
}
