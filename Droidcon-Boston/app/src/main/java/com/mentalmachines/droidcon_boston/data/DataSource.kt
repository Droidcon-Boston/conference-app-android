package com.mentalmachines.droidcon_boston.data

import com.mentalmachines.droidcon_boston.modal.Tweet

interface DataSource {
    fun getTweets(): List<Tweet>
}
