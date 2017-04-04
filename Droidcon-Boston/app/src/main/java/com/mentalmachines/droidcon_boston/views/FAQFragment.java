package com.mentalmachines.droidcon_boston.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase;


/**
 * Created by emezias 4/3/17
 */

public class FAQFragment extends ListFragment {
    public static final String TAG = "FAQFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.faq_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListAdapter(new FaqFragmentAdapter(getContext()));
    }

    public class FaqFragmentAdapter extends BaseAdapter {
        private final int QUESTION_VIEW = 0;
        private static final int ANSWER_VIEW = 1;
        private final Drawable mapIcon, moreIcon;

        final ScheduleDatabase.FaqData[] listItems;

        public FaqFragmentAdapter(Context ctx) {
            listItems = ScheduleDatabase.fetchFAQ(ctx);
            Log.i(TAG, "created FAQ " + listItems.length + " items");
            final Resources res = ctx.getResources();
            mapIcon = NavigationAdapter.buildIcon(res, android.R.drawable.ic_dialog_map);
            moreIcon = NavigationAdapter.buildIcon(res, android.R.drawable.ic_dialog_info);
        }

        @Override
        public int getCount() {
            return listItems == null? 0 : listItems.length;
        }

        @Override
        public Object getItem(int i) {
            return listItems == null? null : listItems[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position >= listItems.length) return null;
            Log.i(TAG, "get view ? " + position);
            final Context ctx = parent.getContext();
            //inflate and load the proper type
            switch (getItemViewType(position)) {
                case QUESTION_VIEW:
                    if (convertView == null || convertView.getTag() != null) {
                        convertView = LayoutInflater.from(ctx).inflate(R.layout.faq_header, null);
                        convertView.setTag(null);
                    }

                    ((TextView) convertView.findViewById(R.id.question_text)).setText(
                            listItems[position].question.toUpperCase());
                    return convertView;
                case ANSWER_VIEW:
                    final ScheduleDatabase.FaqData item = listItems[position];
                    if (convertView == null || convertView.getTag() == null) {
                        convertView = LayoutInflater.from(ctx).inflate(R.layout.faq_item, null);
                        convertView.setTag(convertView.findViewById(R.id.q_bottom));
                    }
                    if (position == listItems.length -1) {
                        ((View)convertView.getTag()).setVisibility(View.GONE);
                    } else {
                        ((View)convertView.getTag()).setVisibility(View.VISIBLE);
                    }
                    boolean hasData = !TextUtils.isEmpty(item.photoUrl);
                    convertView.findViewById(R.id.q_image).setVisibility(
                            hasData ? View.VISIBLE: View.GONE);
                    if (hasData) {
                        Log.i(TAG, "load photo ? " + item.photoUrl);
                        Glide.with(ctx)
                                .load(item.photoUrl)
                                .into((ImageView) convertView.findViewById(R.id.q_image));
                    } else {
                        Log.i(TAG, "no photo " + position);
                    }
                    ((TextView) convertView.findViewById(R.id.q_text)).setText(item.answer);
                    hasData = !TextUtils.isEmpty(item.bizLink);

                    if (!hasData && TextUtils.isEmpty(item.mapCoords)) {
                        //no extra data, text only
                        convertView.findViewById(R.id.q_button_row).setVisibility(View.GONE);
                    } else {
                        //make stuff visible
                        convertView.findViewById(R.id.q_button_row).setVisibility(View.VISIBLE);
                        ImageButton tmp = (ImageButton) convertView.findViewById(R.id.q_more);
                        tmp.setVisibility(hasData ? View.VISIBLE : View.GONE);
                        if (hasData) {
                            tmp.setImageDrawable(moreIcon);
                            Log.i(TAG, "load bizLink ? " + item.bizLink);
                            tmp.setTag(item.bizLink);
                        }

                        //okay to set null tag
                        tmp = (ImageButton) convertView.findViewById(R.id.q_map);
                        if (TextUtils.isEmpty(item.mapCoords)) {
                            tmp.setVisibility(View.GONE);
                        } else {
                            Log.i(TAG, "load geo ? " + TextUtils.isEmpty(item.mapCoords));
                            tmp.setVisibility(View.VISIBLE);
                            tmp.setImageDrawable(mapIcon);
                            tmp.setTag(item.mapCoords);
                        }
                    }
                    return convertView;
            }
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position >= listItems.length) return -1;
            if (listItems[position].answer == null) {
                return QUESTION_VIEW;
            } else {
                return ANSWER_VIEW;
            }
        }
    }

}
