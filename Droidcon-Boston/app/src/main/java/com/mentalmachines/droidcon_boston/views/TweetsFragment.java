package com.mentalmachines.droidcon_boston.views;

import android.support.v4.app.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mentalmachines.droidcon_boston.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;


/**
 * Created by Hariharan Sridharan on 4/3/17.
 */

public class TweetsFragment extends ListFragment {

    private Context context = null;
    private SwipeRefreshLayout swipeLayout = null;
    private final String droidconTwitterHashTag = "#droidconbos OR #Droidcon OR #DroidconBoston";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tweet_layout,container,false);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        final SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query(droidconTwitterHashTag)
                .build();

        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(context)
                .setTimeline(searchTimeline)
                .build();

        setListAdapter(adapter);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
                        swipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Toast.makeText(context, "No Tweets", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
