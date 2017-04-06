package com.mentalmachines.droidcon_boston.views.agenda;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mentalmachines.droidcon_boston.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jinn on 3/27/17.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleItemViewHolder> {

    private Context mContext;

    public static final int TYPE_GENERAL = 0;
    public static final int TYPE_SINGLE_ITEM = 1;
    public static final int TYPE_DOUBLE_ITEM = 2;
    public static final int TYPE_TRIPLE_ITEM = 3;

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
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
    }

    public static class ScheduleItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title_text)
        TextView titleText;
        @BindView(R.id.time_text)
        TextView timeText;
        @BindView(R.id.description_text)
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

