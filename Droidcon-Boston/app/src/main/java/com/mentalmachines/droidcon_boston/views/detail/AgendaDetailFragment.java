package com.mentalmachines.droidcon_boston.views.detail;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.data.ConferenceData;
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase;
import com.mentalmachines.droidcon_boston.utils.StringUtils;
import com.mentalmachines.droidcon_boston.views.agenda.CircleTransform;

public class AgendaDetailFragment extends Fragment {

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

    @BindView(R.id.text_time)
    TextView textTime;

    @BindView(R.id.text_room)
    TextView textRoom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.agenda_detail_fragment, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        String speakerName = bundle.getString(ScheduleDatabase.NAME);
        //ScheduleDatabase.ScheduleDetail scheduleDetail = ScheduleDatabase.fetchDetailData(getActivity().getApplicationContext(), speakerName);
        ScheduleDatabase.ScheduleDetail scheduleDetail = ConferenceData.fetchDetailData(speakerName);
        showAgendaDetail(scheduleDetail);

        textTime.setText(bundle.getString(ScheduleDatabase.TALK_TIME));
        textRoom.setText(bundle.getString(ScheduleDatabase.ROOM));

        return view;
    }

    public void showAgendaDetail(ScheduleDatabase.ScheduleDetail scheduleDetail) {
        if (TextUtils.isEmpty(scheduleDetail.listRow.photo)) {
            Glide.with(this)
                    .load(scheduleDetail.listRow.photoResource)
                    .transform(new CircleTransform(getActivity().getApplicationContext()))
                    .crossFade()
                    .into(imageSpeaker);
        } else {
            Glide.with(this)
                    .load(scheduleDetail.listRow.photo)
                    .transform(new CircleTransform(getActivity().getApplicationContext()))
                    .placeholder(R.drawable.emo_im_cool)
                    .crossFade()
                    .into(imageSpeaker);
        }


        textTitle.setText(scheduleDetail.listRow.talkTitle);
        textSpeakerName.setText(scheduleDetail.listRow.speakerName);
        textSpeakerBio.setText(Html.fromHtml(scheduleDetail.speakerBio));
        textDescription.setText(Html.fromHtml(scheduleDetail.talkDescription));

        if (StringUtils.isNullorEmpty(scheduleDetail.twitter)) {
            imageTwitter.setVisibility(View.GONE);
        } else {
            imageTwitter.setTag(scheduleDetail.twitter);
        }
        if (StringUtils.isNullorEmpty(scheduleDetail.linkedIn)) {
            imageLinkedin.setVisibility(View.GONE);
        } else {
            imageLinkedin.setTag(scheduleDetail.linkedIn);
        }
        if (StringUtils.isNullorEmpty(scheduleDetail.facebook)) {
            imageFacebook.setVisibility(View.GONE);
        } else {
            imageFacebook.setTag(scheduleDetail.facebook);
        }
    }
}


