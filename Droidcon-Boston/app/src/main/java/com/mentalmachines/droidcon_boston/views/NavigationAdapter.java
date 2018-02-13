package com.mentalmachines.droidcon_boston.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mentalmachines.droidcon_boston.R;

/**
 * Class to populate a slide out navigation drawer
 * keeping the agenda display clear and still easy to get to other content
 */

public class NavigationAdapter extends ArrayAdapter<NavigationAdapter.NavItem> {

    final String[] mTitles;
    //parallel arrays
    final Drawable[] mIcons;
    private int selectedIndex = 0;

    //static int primaryDark, primary, accent;

    public NavigationAdapter(@NonNull Context ctx) {
        super(ctx, R.layout.nav_list_item);
        final Resources res = ctx.getResources();

        mTitles = res.getStringArray(R.array.navigation_drawer);
        //build drawables for nav list
        mIcons = new Drawable[] {
                buildIcon(res, R.drawable.ic_schedule_white_24dp),
                buildIcon(res, R.drawable.ic_twitter_white_24dp),
                buildIcon(res, android.R.drawable.ic_dialog_info),
                res.getDrawable(R.mipmap.ic_launcher),
                buildIcon(res, R.drawable.ic_facebook),
                buildIcon(res, R.drawable.ic_instagram),
                buildIcon(res, R.drawable.ic_linkedin),
                buildIcon(res, R.drawable.ic_twitter)
        };
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public NavItem getItem(int i) {
        if (mTitles == null || mIcons == null) return null;
        final NavItem item = new NavItem();
        item.icon = mIcons[i];
        item.title = mTitles[i];
        return item;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View	getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = (LayoutInflater.from(parent.getContext())).inflate(R.layout.nav_list_item, parent, false);
            convertView.setTag(convertView.findViewById(R.id.nav_icon));
        }
        ((ImageView) convertView.getTag()).setImageDrawable(mIcons[position]);
        convertView.setBackgroundResource(
                position > 2 ? R.drawable.bg_social_btn: android.R.color.transparent);

        if (position == selectedIndex) {
            ((ImageView) convertView.getTag()).setSelected(true);
        } else {
            ((ImageView) convertView.getTag()).setSelected(false);
        }

        ((TextView) convertView.findViewById(R.id.nav_title)).setText(mTitles[position]);
        return convertView;
    }

    public void setSelectedIndex(int dex) {
        selectedIndex = dex;
        notifyDataSetChanged();
    }

    public static final int[] normal = new int[] { };
    public static final int[] selected = new int[] { android.R.attr.state_selected };
    public static final int[] pressed = new int[] { android.R.attr.state_pressed, android.R.attr.state_focused };


    public static Drawable buildIcon(Resources res, int baseIcon) {
        StateListDrawable iconStates = new StateListDrawable();
        Drawable stateImage = res.getDrawable(baseIcon);
        stateImage.setColorFilter(res.getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        iconStates.addState(selected, stateImage);
        stateImage = res.getDrawable(baseIcon);
        stateImage.setColorFilter(res.getColor(R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);
        iconStates.addState(pressed, stateImage);
        stateImage = res.getDrawable(baseIcon);
        stateImage.setColorFilter(res.getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        iconStates.addState(normal, stateImage);
        return iconStates;
    }

    public class NavItem {
        Drawable icon;
        String title;
    }
}
