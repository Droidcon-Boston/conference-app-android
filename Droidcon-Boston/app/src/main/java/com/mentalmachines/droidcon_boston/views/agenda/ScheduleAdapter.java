package com.mentalmachines.droidcon_boston.views.agenda;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.data.model.DroidconSchedule;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jinn on 3/27/17.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleItemViewHolder> {
    private List<DroidconSchedule> schedule;
    private Context mContext;

    public static final int TYPE_GENERAL = 0;
    public static final int TYPE_SINGLE_ITEM = 1;
    public static final int TYPE_DOUBLE_ITEM = 2;
    public static final int TYPE_TRIPLE_ITEM = 3;


    public void setSchedule(List<DroidconSchedule> schedule) {
        schedule = schedule;
    }

    public ScheduleAdapter(ArrayList<DroidconSchedule> schedule, Context context) {
        schedule = schedule;
        mContext = context;
    }

    // Not use static
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text);
            itemView.setOnClickListener(v -> Toast.makeText(
                    mContext,
                    "onItemClick - " + getPosition() + " - "
                            + mTextView.getText().toString() + " - "
                            + schedule.get(getPosition()), Toast.LENGTH_LONG).show());
        }
    }

    @Override
    public int getItemCount() {
        return schedule.size();
    }

    @Override
    public ScheduleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.schedule_card_view, parent, false);
        ScheduleItemViewHolder holder = new ScheduleItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ScheduleItemViewHolder holder, int position) {
        holder.titleText.setText(schedule.get(position).title.toString());
        holder.timeText.setText(schedule.get(position).title.toString());
        holder.locationText.setText(schedule.get(position).title.toString());
        holder.speakerNameText.setText(schedule.get(position).title.toString());
    }

    public static class ScheduleItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title_text)
        TextView titleText;
        @BindView(R.id.time_text)
        TextView timeText;
        @BindView(R.id.location_text)
        TextView locationText;
        @BindView(R.id.speaker_name_text)
        TextView speakerNameText;

        ScheduleItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
            });
        }
    }
}

