package com.mentalmachines.droidcon_boston.views.detail;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase;
import com.mentalmachines.droidcon_boston.utils.StringUtils;
import com.mentalmachines.droidcon_boston.views.NavigationAdapter;
import com.mentalmachines.droidcon_boston.views.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jinn on 3/31/17.
 */

public class AgendaDetailFragment extends BaseFragment {

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                final FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.agenda_detail_fragment, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        String speakerName = bundle.getString(ScheduleDatabase.NAME);
        ScheduleDatabase.ScheduleDetail scheduleDetail = ScheduleDatabase.fetchDetailData(getContext(), speakerName);
        showAgendaDetail(scheduleDetail);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setHasOptionsMenu(true);
        //TODO
        return view;
    }

    public void showAgendaDetail(ScheduleDatabase.ScheduleDetail scheduleDetail) {
        Glide.with(this)
                .load(scheduleDetail.listRow.photo)
                .placeholder(R.drawable.emo_im_cool)
                .crossFade()
                .override(1000, 1000)
                .centerCrop()
                .into(imageSpeaker);

        textTitle.setText(scheduleDetail.listRow.talkTitle);
        textSpeakerName.setText(scheduleDetail.listRow.speakerName);
        textSpeakerBio.setText(scheduleDetail.speakerBio);
        textDescription.setText(scheduleDetail.talkDescription);
        final Resources res = getContext().getResources();
        if (StringUtils.isNullorEmpty(scheduleDetail.twitter)) {
            imageTwitter.setVisibility(View.GONE);
        } else {
            imageTwitter.setTag(scheduleDetail.twitter);
            imageTwitter.setImageDrawable(buildIcon(res, R.drawable.twitter));
        }
        if (StringUtils.isNullorEmpty(scheduleDetail.linkedIn)) {
            imageLinkedin.setVisibility(View.GONE);
        } else {
            imageLinkedin.setTag(scheduleDetail.linkedIn);
            imageLinkedin.setImageDrawable(buildIcon(res, R.drawable.linkedin));
        }
        if (StringUtils.isNullorEmpty(scheduleDetail.facebook)) {
            imageFacebook.setVisibility(View.GONE);
        } else {
            imageFacebook.setTag(scheduleDetail.facebook);
            imageFacebook.setImageDrawable(buildIcon(res, R.drawable.facebook));
        }
    }

    public static Drawable buildIcon(Resources res, int baseIcon) {
        StateListDrawable iconStates = new StateListDrawable();
        final Drawable stateImage = res.getDrawable(baseIcon);
        iconStates.addState(NavigationAdapter.pressed, stateImage);
        final Drawable pressImage = res.getDrawable(baseIcon);
        pressImage.setColorFilter(res.getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        iconStates.addState(NavigationAdapter.normal, pressImage);
        return iconStates;
    }
}


