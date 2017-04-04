package com.mentalmachines.droidcon_boston.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by Hariharan Sridharan on 4/02/17.
 */

public class ChatFragment extends Fragment {

    private boolean shouldFireIntent = true;

    public static final String SLACK_TEAMID_KEY = "SLACK_TEAMID";
    public static final String SLACK_CHANNELID_KEY = "SLACK_CHANNELID";

    private String slack_teamId = null;
    private String slack_channelId = null;

    private Context context = null;

    public static ChatFragment newInstance(final String slackTeamId, final String slackChannelId){
        ChatFragment chatFragment = new ChatFragment();

        Bundle bundle = new Bundle();
        bundle.putString(SLACK_TEAMID_KEY, slackTeamId);
        bundle.putString(SLACK_CHANNELID_KEY, slackChannelId);

        chatFragment.setArguments(bundle);

        return chatFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!(getArguments() == null)){
            slack_teamId = getArguments().getString(SLACK_TEAMID_KEY);
            slack_channelId = getArguments().getString(SLACK_CHANNELID_KEY);
        }

        shouldFireIntent = true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(shouldFireIntent){
            //Uri slackUri = constructSlackUri("T2M1BL9EU","C2M1UNB0A");
            Uri slackUri = constructSlackUri(slack_teamId, slack_channelId);
            openSlack(slackUri);
        }
    }

    private Uri constructSlackUri(String teamId, String channel){

        final String scheme = "slack";
        final String authority = "channel";
        final String queryParameter1 = "team";
        final String queryParameter2 = "id";

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(scheme)
                  .authority(authority)
                  .appendQueryParameter(queryParameter1,teamId)
                  .appendQueryParameter(queryParameter2,channel);

        String uri = uriBuilder.toString();

        return uriBuilder.build();
    }

    private void openSlack(Uri slackUri){
        Intent slackIntent = new Intent(Intent.ACTION_VIEW, slackUri);

        // Verify that the intent will resolve to an activity
        if (slackIntent.resolveActivity(context.getPackageManager()) != null){
            startActivity(slackIntent);
        }else{
            downloadSlack();
        }

    }

    private void downloadSlack(){
        final String slackPlayStoreUrl = "market://details?id=com.Slack&hl=en";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(slackPlayStoreUrl));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "No Play store...please install Slack manually", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        shouldFireIntent = false;
    }
}
