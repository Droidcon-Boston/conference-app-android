package com.mentalmachines.droidcon_boston.modal

data class Tweet(val id: Long,
                 val type: Int,
                 val name: String,
                 val screenName: String,
                 val profileImageUrl: String,
                 val text: String,
                 val quotedTweet: Tweet? = null)