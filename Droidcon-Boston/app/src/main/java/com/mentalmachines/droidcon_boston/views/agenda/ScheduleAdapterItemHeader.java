package com.mentalmachines.droidcon_boston.views.agenda;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mentalmachines.droidcon_boston.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Sticky header for schedule view
 */
public class ScheduleAdapterItemHeader extends AbstractHeaderItem<ScheduleAdapterItemHeader.ViewHolder> {

    private String sessionTime;

    public ScheduleAdapterItemHeader(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ScheduleAdapterItemHeader) {
            ScheduleAdapterItemHeader inItem = (ScheduleAdapterItemHeader) o;
            return this.sessionTime.equals(inItem.sessionTime);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return sessionTime.hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.schedule_item_header;
    }

    @Override
    public ScheduleAdapterItemHeader.ViewHolder createViewHolder(FlexibleAdapter adapter,
            LayoutInflater inflater,
            ViewGroup parent) {
        return new ScheduleAdapterItemHeader.ViewHolder(
                inflater.inflate(getLayoutRes(), parent, false), adapter, true);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter,
            ScheduleAdapterItemHeader.ViewHolder holder,
            int position,
            List payloads) {

        holder.header.setText(sessionTime);
    }


    public static class ViewHolder extends FlexibleViewHolder {

        TextView header;

        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);

            findViews(view);
        }

        public ViewHolder(View view, FlexibleAdapter adapter, boolean stickyHeader) {
            super(view, adapter, stickyHeader);

            findViews(view);
        }

        private void findViews(View parent) {
            header = (TextView) parent.findViewById(R.id.header_text);
        }
    }
}
