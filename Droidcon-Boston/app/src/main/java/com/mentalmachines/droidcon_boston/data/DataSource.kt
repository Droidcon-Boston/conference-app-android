package com.mentalmachines.droidcon_boston.data

import com.mentalmachines.droidcon_boston.modal.TweetWithMedia

interface DataSource {
    fun getTweets(): List<TweetWithMedia>
}
