package com.mentalmachines.droidconboston.data

import com.mentalmachines.droidconboston.modal.TweetWithMedia

interface DataSource {
    fun getTweets(): List<TweetWithMedia>
}
