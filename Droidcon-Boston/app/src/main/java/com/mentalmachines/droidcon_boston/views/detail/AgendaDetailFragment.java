package com.mentalmachines.droidcon_boston.views.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase;
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

    public void showAgendaDetail(ScheduleDatabase.ScheduleDetail scheduleDetail) {
        Glide.with(this)
                .load(scheduleDetail.listRow.photo)
                .into(imageSpeaker);

        textTitle.setText(scheduleDetail.listRow.talkTitle);
        textSpeakerName.setText(scheduleDetail.listRow.speakerName);
        textSpeakerBio.setText(scheduleDetail.speakerBio);
        textDescription.setText(scheduleDetail.talkDescription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
