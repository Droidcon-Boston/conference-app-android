package com.mentalmachines.droidconboston.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mentalmachines.droidconboston.data.Repository
import com.mentalmachines.droidconboston.domain.TwitterUseCase
import com.mentalmachines.droidconboston.views.social.TwitterViewModel

class TwitterViewModelFactory(private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = TwitterViewModel(
        TwitterUseCase(Repository
            .getInstance(context))) as T
}
