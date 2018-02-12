package com.mentalmachines.droidcon_boston.views.agenda;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractSectionableItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.Date;
import java.util.List;

/**
 * Used for displaying the schedule with sticky headers with optional day filtering
 */
public class ScheduleAdapterItem extends
        AbstractSectionableItem<ScheduleAdapterItem.ViewHolder, ScheduleAdapterItemHeader> {

    private ScheduleDatabase.ScheduleRow itemData;

    private Integer roomOrder;

    public ScheduleDatabase.ScheduleRow getItemData() {
        return itemData;
    }

    public ScheduleAdapterItem(ScheduleDatabase.ScheduleRow scheduleRow,
            ScheduleAdapterItemHeader header) {
        super(header);
        this.itemData = scheduleRow;

        if ("THEATER 1".equals(scheduleRow.room)) {
            roomOrder = 1;
        } else if ("THEATER 2".equals(scheduleRow.room)) {
            roomOrder = 2;
        } else if ("CYCLORAMA".equals(scheduleRow.room)) {
            roomOrder = 3;
        }
    }

    public String getTitle() {
        return itemData.talkTitle;
    }

    public Date getStartTime() {
        return itemData.date;
    }

    public Integer getRoomSortOrder() {
        return roomOrder;
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

        if (itemData.speakerName == null) {
            holder.sessionLayout.setVisibility(View.GONE);
            holder.avatar.setVisibility(View.GONE);
            holder.bigTitle.setVisibility(View.VISIBLE);

            holder.bigTitle.setText(itemData.talkTitle);

            if (itemData.photo == null) {
                holder.rootLayout.setBackground(null);
            } else {
                addBackgroundRipple(holder);
            }
        } else {
            holder.sessionLayout.setVisibility(View.VISIBLE);
            holder.avatar.setVisibility(View.VISIBLE);
            holder.bigTitle.setVisibility(View.GONE);

            holder.title.setText(itemData.talkTitle);
            holder.speaker.setText(itemData.speakerName);
            holder.room.setText(itemData.room);

            final Context context = holder.title.getContext();
            Glide.with(context)
                    .load(TextUtils.isEmpty(itemData.photo)? itemData.photoResource: itemData.photo)
                    .transform(new CircleTransform(context))
                    .crossFade()
                    .into(holder.avatar);
            addBackgroundRipple(holder);
        }
    }

    private void addBackgroundRipple(ViewHolder holder) {
        TypedValue outValue = new TypedValue();
        holder.title.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        holder.rootLayout.setBackgroundResource(outValue.resourceId);
    }


    static class ViewHolder extends FlexibleViewHolder {

        View rootLayout;

        ImageView avatar;

        TextView title;

        TextView speaker;

        TextView room;

        View sessionLayout;

        TextView bigTitle;

        ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);

            findViews(view);
        }

        public ViewHolder(View view, FlexibleAdapter adapter, boolean stickyHeader) {
            super(view, adapter, stickyHeader);

            findViews(view);
        }

        private void findViews(View parent) {
            rootLayout = parent.findViewById(R.id.rootLayout);
            avatar = (ImageView) parent.findViewById(R.id.speaker_image);
            title = (TextView) parent.findViewById(R.id.title_text);
            speaker = (TextView) parent.findViewById(R.id.speaker_name_text);
            room = (TextView) parent.findViewById(R.id.room_text);
            sessionLayout = parent.findViewById(R.id.session_layout);
            bigTitle = (TextView) parent.findViewById(R.id.bigtitle_text);
        }
    }
}
