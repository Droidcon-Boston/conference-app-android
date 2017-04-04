package com.mentalmachines.droidcon_boston.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase;

import static com.mentalmachines.droidcon_boston.views.detail.AgendaDetailFragment.TAG;


/**
 * Created by emezias 4/3/17
 */

public class FAQFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.faq_fragment, container, false);
        //setListAdapter(adapter);
        return view;

    }

    public class FaqFragment extends BaseAdapter {
        private final int QUESTION_VIEW = R.layout.schedule_item_header;
        private static final int ANSWER_VIEW = 99;

        final ScheduleDatabase.FaqData[] listItems;

        public FaqFragment(Context ctx) {
            listItems = ScheduleDatabase.fetchFAQ(ctx);
            Log.i(TAG, "created FAQ");
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
            final Context ctx = parent.getContext();
            //inflate and load the proper type
            switch (getItemViewType(position)) {
                case QUESTION_VIEW:
                    if (convertView == null || convertView.getTag() != null) {
                        convertView = LayoutInflater.from(ctx).inflate(R.layout.schedule_item_header, null);
                    }
                    convertView.setTag(null);
                    ((TextView) convertView.findViewById(R.id.header_text)).setText(listItems[position].question);
                    break;
                case ANSWER_VIEW:
                    final ScheduleDatabase.FaqData item = listItems[position];
                    if (convertView == null || convertView.getTag() == null) {
                        convertView = LayoutInflater.from(ctx).inflate(R.layout.faq_item, null);
                    }
                    convertView.setTag(true);
                    boolean hasData = TextUtils.isEmpty(item.photoUrl);
                    convertView.findViewById(R.id.q_image).setVisibility(
                            hasData ? View.GONE : View.VISIBLE);
                    if (!hasData) {
                        Glide.with(ctx)
                                .load(item.photoUrl)
                                .into((ImageView) convertView.findViewById(R.id.q_image));
                    }
                    ((TextView) convertView.findViewById(R.id.q_text)).setText(item.answer);
                    hasData = TextUtils.isEmpty(item.bizLink);

                    if (!hasData && TextUtils.isEmpty(item.mapCoords)) {
                        //no extra data, text only
                        convertView.findViewById(R.id.q_button_row).setVisibility(View.GONE);
                    } else {
                        //make stuff visible
                        convertView.findViewById(R.id.q_button_row).setVisibility(View.VISIBLE);
                        View tmp = convertView.findViewById(R.id.q_more);
                        tmp.setVisibility(hasData ? View.GONE : View.VISIBLE);
                        tmp.setTag(item.bizLink);
                        //okay to set null tag
                        tmp = convertView.findViewById(R.id.q_map);
                        tmp.setVisibility(TextUtils.isEmpty(item.mapCoords) ? View.GONE : View.VISIBLE);
                        tmp.setTag(item.mapCoords);
                    }
                    break;
            }
            return convertView;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (listItems[position].answer == null) {
                return QUESTION_VIEW;
            } else {
                return ANSWER_VIEW;
            }
        }
    }

}
