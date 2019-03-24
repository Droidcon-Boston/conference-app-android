package com.mentalmachines.droidcon_boston.modal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["id1", "id2"],
    foreignKeys = [ForeignKey(entity = QuotedTweet::class, parentColumns = ["quoted_id"],
        childColumns = ["quoted_tweet_id"], onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Tweet::class, parentColumns = ["id"], childColumns = ["tweet_id"],
        onDelete = ForeignKey.CASCADE)])
data class Media(val id1: Long, val id2: Long, val type: String, val mediaUrlHttps: String,
                 val url: String,
                 @ColumnInfo(name = "tweet_id") val tweetId: Long? = null,
                 @ColumnInfo(name = "quoted_tweet_id") val quotedTweetId: Long? = null) {
    companion object {
        const val MEDIA_TYPE_PHOTO = "photo"
        const val MEDIA_TYPE_GIF = "animated_gif"
        const val MEDIA_TYPE_VIDEO = "video"
    }
}
