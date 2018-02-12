package com.mentalmachines.droidcon_boston.views.detail;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase;
import com.mentalmachines.droidcon_boston.views.agenda.CircleTransform;

public class AgendaDetailFragment extends Fragment {

  @BindView(R.id.agenda_detail_fab)
  FloatingActionButton favFab;

  @BindView(R.id.agenda_detail_img_headerbg)
  ImageView imgHeaderBg;

  @BindView(R.id.agenda_detail_img_speaker)
  ImageView imageSpeaker;

  @BindView(R.id.agenda_detail_tv_talkdescription)
  TextView textDescription;

  @BindView(R.id.agenda_detail_tv_talkroom)
  TextView textRoom;

  @BindView(R.id.agenda_detail_tv_speaker_bio)
  TextView textSpeakerBio;

  @BindView(R.id.agenda_detail_tv_speaker_name)
  TextView textSpeakerName;

  @BindView(R.id.agenda_detail_tv_talktime)
  TextView textTime;

  @BindView(R.id.agenda_detail_tv_talktitle)
  TextView textTitle;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    View view = inflater.inflate(R.layout.agenda_detail_fragment, container, false);
    ButterKnife.bind(this, view);

    Bundle bundle = getArguments();
    String speakerName = bundle.getString(ScheduleDatabase.NAME);
    ScheduleDatabase.ScheduleDetail scheduleDetail = ScheduleDatabase
        .fetchDetailData(getActivity().getApplicationContext(), speakerName);
    showAgendaDetail(scheduleDetail);

    textTime.setText(bundle.getString(ScheduleDatabase.TALK_TIME));
    textRoom.setText(bundle.getString(ScheduleDatabase.ROOM));

    return view;
  }

  public void showAgendaDetail(ScheduleDatabase.ScheduleDetail scheduleDetail) {
    Glide.with(this)
        .load(scheduleDetail.listRow.photo)
        .transform(new CircleTransform(getActivity().getApplicationContext()))
        .placeholder(R.drawable.emo_im_cool)
        .crossFade()
        .into(imageSpeaker);

    Glide.with(this)
        .load(R.drawable.navigation_header_image)
        .placeholder(R.drawable.placeholder)
        .crossFade()
        .into(imgHeaderBg);

    textTitle.setText(scheduleDetail.listRow.talkTitle);
    textSpeakerName.setText(scheduleDetail.listRow.speakerName);
    textSpeakerBio.setText("Android Developer");
    textDescription.setText(scheduleDetail.talkDescription);

    favFab.setOnClickListener(v -> {
      // Todo: Save fav talk
    });
  }
}


