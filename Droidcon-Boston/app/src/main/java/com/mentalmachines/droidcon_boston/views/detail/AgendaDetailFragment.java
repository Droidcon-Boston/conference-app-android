package com.mentalmachines.droidcon_boston.views.detail;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.bumptech.glide.Glide;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase;
import com.mentalmachines.droidcon_boston.data.UserAgendaRepo;
import com.mentalmachines.droidcon_boston.utils.StringUtils;
import com.mentalmachines.droidcon_boston.views.agenda.CircleTransform;

public class AgendaDetailFragment extends Fragment {

    @BindView(R.id.image_speaker)
    ImageView imageSpeaker;

    @BindView(R.id.text_title)
    TextView textTitle;

    @BindView(R.id.image_bookmark)
    ImageView imageBookmark;

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

    private ScheduleDatabase.ScheduleDetail scheduleDetail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.agenda_detail_fragment, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        String speakerName = bundle.getString(ScheduleDatabase.NAME);
        scheduleDetail = ScheduleDatabase.fetchDetailData(getActivity().getApplicationContext(), speakerName);
        showAgendaDetail(scheduleDetail);

        textTime.setText(bundle.getString(ScheduleDatabase.TALK_TIME));
        textRoom.setText(bundle.getString(ScheduleDatabase.ROOM));

        return view;
    }

    @OnClick(R.id.image_bookmark)
    protected void bookmarkClicked() {
        if (scheduleDetail != null) {
            UserAgendaRepo userAgendaRepo = getUserAgendaRepo();
            userAgendaRepo.bookmarkSession(scheduleDetail.getId(),
                                           !userAgendaRepo.isSessionBookmarked(scheduleDetail.getId()));
            showBookmarkStatus(scheduleDetail);
        }
    }

    public void showAgendaDetail(ScheduleDatabase.ScheduleDetail scheduleDetail) {
        Glide.with(this)
                .load(scheduleDetail.listRow.photo)
                .transform(new CircleTransform(getActivity().getApplicationContext()))
                .placeholder(R.drawable.emo_im_cool)
                .crossFade()
                .into(imageSpeaker);

        textTitle.setText(scheduleDetail.listRow.talkTitle);
        textSpeakerName.setText(scheduleDetail.listRow.speakerName);
        textSpeakerBio.setText(scheduleDetail.speakerBio);
        textDescription.setText(scheduleDetail.talkDescription);

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

        showBookmarkStatus(scheduleDetail);
    }

    private void showBookmarkStatus(ScheduleDatabase.ScheduleDetail scheduleDetail) {
        UserAgendaRepo userAgendaRepo = getUserAgendaRepo();
        imageBookmark.setImageResource(userAgendaRepo.isSessionBookmarked(scheduleDetail.listRow.talkTitle)
                                       ? R.drawable.ic_star_black_24dp : R.drawable.ic_star_border_black_24dp);
    }

    private UserAgendaRepo getUserAgendaRepo() {
        return UserAgendaRepo.Companion.getInstance(imageBookmark.getContext());
    }
}


