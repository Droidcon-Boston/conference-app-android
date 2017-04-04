package com.mentalmachines.droidcon_boston.views.detail;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase;
import com.mentalmachines.droidcon_boston.utils.StringUtils;
import com.mentalmachines.droidcon_boston.views.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jinn on 3/31/17.
 */

public class AgendaDetailFragment extends BaseFragment {
    public static String TAG = "AGENDA_DETAIL_FRAGMENT";
    String speakerName;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayout() {
        return R.layout.agenda_detail_fragment;
    }

    @BindView(R.id.image_speaker)
    ImageView imageSpeaker;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_speaker_name)
    TextView textSpeakerName;
    @BindView(R.id.text_speaker_bio)
    TextView textSpeakerBio;
    @BindView(R.id.text_description)
    TextView textDescription;
    @BindView(R.id.image_twitter)
    ImageView imageTwitter;
    @BindView(R.id.image_linkedin)
    ImageView imageLinkedin;
    @BindView(R.id.image_facebook)
    ImageView imageFacebook;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.agenda_detail_fragment, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        String speakerName = bundle.getString("speaker_name");
        ScheduleDatabase.ScheduleDetail scheduleDetail = ScheduleDatabase.fetchDetailData(getContext(), speakerName);
        showAgendaDetail(scheduleDetail);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void showAgendaDetail(ScheduleDatabase.ScheduleDetail scheduleDetail) {
        Glide.with(this)
                .load(scheduleDetail.listRow.photo)
                .into(imageSpeaker);

        textTitle.setText(scheduleDetail.listRow.talkTitle);
        textSpeakerName.setText(scheduleDetail.listRow.speakerName);
        textSpeakerBio.setText(scheduleDetail.speakerBio);
        textDescription.setText(scheduleDetail.talkDescription);

        if (StringUtils.isNullorEmpty(scheduleDetail.twitter)) {
            imageTwitter.setVisibility(View.GONE);
        }
        if (StringUtils.isNullorEmpty(scheduleDetail.linkedIn)) {
            imageLinkedin.setVisibility(View.GONE);
        }
        if (StringUtils.isNullorEmpty(scheduleDetail.facebook)) {
            imageFacebook.setVisibility(View.GONE);
        }

        imageTwitter.setOnClickListener(view -> {
            Intent intent = null;
            try {
                // get the Twitter app if possible
                getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=USERID"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } catch (Exception e) {
                // no Twitter app, revert to browser
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/PROFILENAME"));
            }
        });

        imageFacebook.setOnClickListener(view -> {
            Intent intent;
            //Uri uri = Uri.parse(url);
            try {
                ApplicationInfo applicationInfo = getActivity().getPackageManager().getApplicationInfo("com.facebook.katana", 0);
                if (applicationInfo.enabled) {
                    // http://stackoverflow.com/a/24547437/1048340
                    // uri = Uri.parse("fb://facewebmodal/f?href=" + url);
                }
            } catch (PackageManager.NameNotFoundException ignored) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/PROFILENAME"));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
