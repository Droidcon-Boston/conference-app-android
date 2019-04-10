package com.mentalmachines.droidconboston.modal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuotedTweet(
    @PrimaryKey
    @ColumnInfo(name = "quoted_id")
    val id: Long,
    @ColumnInfo(name = "quoted_name")
    val name: String,
    @ColumnInfo(name = "quoted_screenName")
    val screenName: String,
    @ColumnInfo(name = "quoted_profileImageUrl")
    val profileImageUrl: String,
    @ColumnInfo(name = "quoted_text")
    val text: String
)
