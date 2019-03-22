package com.mentalmachines.droidcon_boston.modal

import androidx.room.Embedded
import androidx.room.Relation

class TweetWithMedia {

    @Embedded
    lateinit var tweet: Tweet

    @Relation(parentColumn = "id", entityColumn = "tweet_id")
    var media: List<Media>? = null

    @Relation(parentColumn = "quoted_id", entityColumn = "quoted_tweet_id")
    var quotedMedia: List<Media>? = null

}