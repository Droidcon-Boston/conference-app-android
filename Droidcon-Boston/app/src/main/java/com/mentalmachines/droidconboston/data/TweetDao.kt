package com.mentalmachines.droidconboston.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.mentalmachines.droidconboston.modal.Tweet
import com.mentalmachines.droidconboston.modal.TweetWithMedia

@Dao
abstract class TweetDao : BaseDao<Tweet> {

    @Transaction
    open fun updateTweets(tweets: List<Tweet>) {
        deleteAll()
        insertAll(tweets)
    }

    @Transaction
    @Query("SELECT * FROM Tweet ORDER BY createdAt DESC")
    abstract fun getTweets(): List<TweetWithMedia>

    @Query("DELETE FROM Tweet")
    abstract fun deleteAll()
}
