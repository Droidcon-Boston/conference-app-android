package com.mentalmachines.droidcon_boston.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.mentalmachines.droidcon_boston.modal.QuotedTweet

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
