package com.mentalmachines.droidcon_boston.views.social

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.modal.Result
import com.mentalmachines.droidcon_boston.utils.TwitterViewModelFactory
import kotlinx.android.synthetic.main.twitter_fragment.*
import timber.log.Timber

class TwitterFragment : Fragment() {

    companion object {
        fun newInstance() = TwitterFragment()
    }

    private val twitterRecyclerViewAdapter: TwitterRecyclerViewAdapter by lazy {
        TwitterRecyclerViewAdapter()
    }

    private val twitterViewModelFactory: TwitterViewModelFactory by lazy {
        TwitterViewModelFactory(requireContext())
    }

    private val twitterViewModel: TwitterViewModel by lazy {
        ViewModelProviders.of(this, twitterViewModelFactory).get(TwitterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.twitter_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.adapter = twitterRecyclerViewAdapter
        twitterViewModel.tweets.observe(this, Observer {
            when(it) {
                is Result.Loading -> {
                    if (!swipeToRefreshLayout.isRefreshing) {
                        swipeToRefreshLayout.isRefreshing = true
                    }
                }
                is Result.Error -> {
                    swipeToRefreshLayout.isRefreshing = false
                    errorView.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    Timber.e(it.message)
                }
                is Result.Data -> {
                    swipeToRefreshLayout.isRefreshing = false
                    errorView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    twitterRecyclerViewAdapter.submitList(it.data)
                }
            }
        })
        swipeToRefreshLayout.setOnRefreshListener {
            twitterViewModel.refreshTweets()
        }
    }

}
