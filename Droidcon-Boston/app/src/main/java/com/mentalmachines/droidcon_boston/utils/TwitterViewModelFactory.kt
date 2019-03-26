package com.mentalmachines.droidcon_boston.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mentalmachines.droidcon_boston.data.Repository
import com.mentalmachines.droidcon_boston.domain.TwitterUseCase
import com.mentalmachines.droidcon_boston.views.social.TwitterViewModel

class TwitterViewModelFactory(private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = TwitterViewModel(
        TwitterUseCase(Repository
            .getInstance(context))) as T
}
