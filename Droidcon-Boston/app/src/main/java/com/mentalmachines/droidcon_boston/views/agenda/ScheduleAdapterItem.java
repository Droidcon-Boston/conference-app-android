package com.mentalmachines.droidcon_boston.views.agenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractSectionableItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Used for displaying the schedule with sticky headers with optional day filtering
 */
public class ScheduleAdapterItem extends
        AbstractSectionableItem<ScheduleAdapterItem.ViewHolder, ScheduleAdapterItemHeader> {

    private ScheduleDatabase.ScheduleRow itemData;

    public ScheduleAdapterItem(ScheduleDatabase.ScheduleRow scheduleRow,
                               ScheduleAdapterItemHeader header) {
        super(header);
        this.itemData = scheduleRow;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ScheduleAdapterItem) {
            ScheduleAdapterItem inItem = (ScheduleAdapterItem) o;
            return this.itemData.talkTitle.equals(inItem.itemData.talkTitle);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return itemData.talkTitle.hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.schedule_item;
    }

    @Override
    public ScheduleAdapterItem.ViewHolder createViewHolder(FlexibleAdapter adapter,
                                                           LayoutInflater inflater,
                                         ViewGroup parent) {
        return new ScheduleAdapterItem.ViewHolder(
                inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter,
                               ScheduleAdapterItem.ViewHolder holder,
                               int position,
                               List payloads) {

        holder.title.setText(itemData.talkTitle);
        holder.speaker.setText(itemData.speakerName);
        holder.room.setText(itemData.room);

        Context context = holder.title.getContext();
        Glide.with(context)
                .load(itemData.photo)
                .transform(new CircleTransform(context))
                .crossFade()
                .into(holder.avatar);
    }


    public static class ViewHolder extends FlexibleViewHolder {

        ImageView avatar;
        TextView title;
        TextView speaker;
        TextView room;

        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);

            findViews(view);
        }

        public ViewHolder(View view, FlexibleAdapter adapter, boolean stickyHeader) {
            super(view, adapter, stickyHeader);

            findViews(view);
        }

        private void findViews(View parent) {
            avatar = (ImageView) parent.findViewById(R.id.speaker_image);
            title = (TextView) parent.findViewById(R.id.title_text);
            speaker = (TextView) parent.findViewById(R.id.speaker_name_text);
            room = (TextView) parent.findViewById(R.id.room_text);
        }
    }
}
