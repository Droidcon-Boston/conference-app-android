package com.mentalmachines.droidcon_boston.views.detail;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase.ScheduleEventDetail;
import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleDetail;
import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleRow;
import com.mentalmachines.droidcon_boston.data.Schedule;
import com.mentalmachines.droidcon_boston.data.UserAgendaRepo;
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper;
import com.mentalmachines.droidcon_boston.utils.StringUtils;
import com.mentalmachines.droidcon_boston.views.MainActivity;
import com.mentalmachines.droidcon_boston.views.agenda.AgendaDayFragment;
import com.mentalmachines.droidcon_boston.views.agenda.CircleTransform;
import java.util.Locale;

public class AgendaDetailFragment extends Fragment {

    private static final String TAG = AgendaDayFragment.class.getName();
    private static final Gson gson = new Gson();

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

    private ScheduleDetail scheduleDetail;
    private FirebaseHelper firebaseHelper = FirebaseHelper.Companion.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.agenda_detail_fragment, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        final ScheduleRow itemData = gson.fromJson(bundle.getString(Schedule.Companion.getSCHEDULE_ITEM_ROW()), ScheduleRow.class);

        textTime.setText(itemData.getStartTime());
        textRoom.setText(itemData.getRoom());

        firebaseHelper.getSpeakerDatabase().orderByChild("name").equalTo(itemData.getPrimarySpeakerName())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot speakerSnapshot : dataSnapshot.getChildren()) {
                            ScheduleEventDetail detail = speakerSnapshot.getValue(ScheduleEventDetail.class);
                            if (detail != null) {
                                scheduleDetail = detail.toScheduleDetail(itemData);
                                showAgendaDetail(scheduleDetail);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "detailQuery:onCancelled", databaseError.toException());
                    }
                });

        return view;
    }

    @OnClick(R.id.image_back)
    protected void backClicked() {
        ((MainActivity) getActivity()).replaceFragment(getString(R.string.str_agenda));
    }

    @OnClick(R.id.image_bookmark)
    protected void bookmarkClicked() {
        if (scheduleDetail != null) {
            UserAgendaRepo userAgendaRepo = getUserAgendaRepo();
            final boolean nextBookmarkStatus = !userAgendaRepo.isSessionBookmarked(scheduleDetail.getId());
            userAgendaRepo.bookmarkSession(scheduleDetail.getId(), nextBookmarkStatus);
            Snackbar.make(getView().findViewById(R.id.agendaDetailView),
                    nextBookmarkStatus ? getString(R.string.saved_agenda_item) : getString(R.string.removed_agenda_item),
                    Snackbar.LENGTH_SHORT).show();
            showBookmarkStatus(scheduleDetail);
        }
    }

    public void showAgendaDetail(ScheduleDetail scheduleDetail) {
        final ScheduleRow listRow = scheduleDetail.getListRow();
        Glide.with(this)
                .load(listRow.getPhoto())
                .transform(new CircleTransform(getActivity().getApplicationContext()))
                .placeholder(R.drawable.emo_im_cool)
                .crossFade()
                .into(imageSpeaker);

        textTitle.setText(listRow.getTalkTitle());
        textSpeakerName.setText(listRow.getSpeakerString());

        final String speakerBio;
        if (listRow.hasMultipleSpeakers()) {
            speakerBio = String.format(Locale.getDefault(), "Primary Speaker: %s\n\n%s",
                listRow.getPrimarySpeakerName(), scheduleDetail.getSpeakerBio());
        } else {
            speakerBio = scheduleDetail.getSpeakerBio();
        }

        textSpeakerBio.setText(speakerBio);
        textDescription.setText(listRow.getTalkDescription());

        if (StringUtils.isNullorEmpty(scheduleDetail.getTwitter())) {
            imageTwitter.setVisibility(View.GONE);
        } else {
            imageTwitter.setTag(scheduleDetail.getTwitter());
        }
        if (StringUtils.isNullorEmpty(scheduleDetail.getLinkedIn())) {
            imageLinkedin.setVisibility(View.GONE);
        } else {
            imageLinkedin.setTag(scheduleDetail.getLinkedIn());
        }
        if (StringUtils.isNullorEmpty(scheduleDetail.getFacebook())) {
            imageFacebook.setVisibility(View.GONE);
        } else {
            imageFacebook.setTag(scheduleDetail.getFacebook());
        }

        showBookmarkStatus(scheduleDetail);
    }


    private void showBookmarkStatus(ScheduleDetail scheduleDetail) {
        UserAgendaRepo userAgendaRepo = getUserAgendaRepo();
        imageBookmark.setImageResource(userAgendaRepo.isSessionBookmarked(scheduleDetail.getId())
                ? R.drawable.ic_star_black_24dp : R.drawable.ic_star_border_black_24dp);
    }

    private UserAgendaRepo getUserAgendaRepo() {
        return UserAgendaRepo.Companion.getInstance(imageBookmark.getContext());
    }
}


