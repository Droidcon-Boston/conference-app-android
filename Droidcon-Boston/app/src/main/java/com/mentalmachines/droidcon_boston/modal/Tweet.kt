package com.mentalmachines.droidcon_boston.modal

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Tweet(@PrimaryKey val id: Long,
                 val createdAt: Date,
                 val type: Int,
                 val name: String,
                 val screenName: String,
                 val profileImageUrl: String,
                 val text: String,
                 @Embedded(prefix = "quoted_")
                 val quotedTweet: QuotedTweet? = null)
