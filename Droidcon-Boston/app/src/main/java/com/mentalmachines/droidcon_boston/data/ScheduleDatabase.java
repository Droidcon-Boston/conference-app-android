package com.mentalmachines.droidcon_boston.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mentalmachines.droidcon_boston.R;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by emezias on 3/31/17.
 * This database can workaround certain issues with the Wordpress API
 * Another advantage is that local resources reduce the network resources required by the crowd using it
 */

public class ScheduleDatabase extends SQLiteAssetHelper {

    public static final String TAG = "ScheduleDatabase";
    private static SQLiteDatabase sDB;
    private static final String DATABASE_NAME = "droidconbos.db";
    private static final int DATABASE_VERSION = 1;
    //columns, database constants
    public static final String TABLE = "schedule";
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String TITLE = "talk";
    public static final String DESCRIPTION = "description";
    public static final String PHOTO = "photo_link";
    public static final String BIO = "bio";
    public static final String TALK_DATE = "date";
    public static final String TALK_TIME = "time";
    public static final String ROOM = "room";
    public static final String SPKR_TWEET = "twitter";
    public static final String SPKR_LINKD = "linkedin";
    public static final String SPKR_FB = "facebook";
    public static final int COL_ID = 0;
    public static final int COL_NAME = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_DESCRIPTION = 3;
    public static final int COL_PHOTO = 4;
    public static final int COL_BIO = 5;
    public static final int COL_TALK_DATE = 6;
    public static final int COL_TALK_TIME = 7;
    public static final int COL_ROOM = 8;
    public static final int COL_SPKR_TWEET = 9;
    public static final int COL_SPKR_LINKD = 10;
    public static final int COL_SPKR_FB = 11;

    public ScheduleDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Using a singleton to get the db, keep a single db open
     * Close cursors, do not close the db
     * If the feature set includes saving a user schedule we might make that preferences or set up a writable db
     * @return ScheduleDatabase
     */
    public static SQLiteDatabase getDatabase(@NonNull Context ctx) {
        if (sDB == null) {
            sDB = (new ScheduleDatabase(ctx)).getReadableDatabase();
        }
        return sDB;
    }

    public static class ScheduleRow {
        public String[] projection = new String[] { NAME,
                TITLE, DESCRIPTION, PHOTO,
                SPKR_TWEET, SPKR_LINKD, SPKR_FB,
                TALK_TIME, ROOM, TALK_DATE };

        String speakerName;
        String talkTitle;
        String talkDescription;
        String photo;
        String twitter;
        String linkedIn;
        String facebook;
        String time;
        String room;
        String date;
    }

    public static final String[] sDetailProjection = new String[] { BIO };
    public static final String sDetailWhere = "WHERE " + NAME + " LIKE ?";

    /**
     * Can use this data structure or just select the bio, the rest is already there
     * @param ctx
     * @param speakername
     * @return
     */
    public static final String fetchDetailData(@NonNull Context ctx, @NonNull String speakername) {
        final SQLiteDatabase db = getDatabase(ctx);
        final Cursor c = db.query(TABLE, sDetailProjection, sDetailWhere,
                new String[] { speakername },
                null, null, null);
        //should be exactly one field from one row
        if (c.moveToFirst()) {
            final String bio = c.getString(0);
            Log.d(TAG, "got bio for " + speakername);
            c.close();
            return bio;
        } else {
            Log.e(TAG, "Error reading bio for " + speakername);
            return null;
        }
    }

    public static class ScheduleAdapter extends RecyclerView.Adapter<TalkViewHolder> {
        final private ScheduleRow[] items;

        public ScheduleAdapter(Context ctx) {
            Log.i(TAG, "adapter");
            final SQLiteDatabase db = getDatabase(ctx);
            final Cursor c = db.query(TABLE, null, null, null, null, null, null);
            //all rows
            if (c.moveToFirst()) {
                int dex = 0;
                items = new ScheduleRow[c.getCount()];
                ScheduleRow item;
                do {
                    item = new ScheduleRow();
                    item.speakerName = c.getString(COL_NAME);
                    //DEBUG Log.d(TAG, "speaker? " + item.speakerName);
                    item.talkTitle = c.getString(COL_TITLE);
                    item.talkDescription = c.getString(COL_DESCRIPTION);
                    item.photo = c.getString(COL_PHOTO);
                    item.twitter = c.getString(COL_SPKR_TWEET);
                    item.linkedIn = c.getString(COL_SPKR_LINKD);
                    item.facebook = c.getString(COL_SPKR_FB);
                    item.time = c.getString(COL_TALK_TIME);
                    item.room = c.getString(COL_ROOM);
                    item.date = c.getString(COL_TALK_DATE);
                    items[dex++] = item;
                } while (c.moveToNext());
                Log.i(TAG, "finished adapter array");
                c.close();
            } else {
                Log.e(TAG, "Error reading database");
                items = null;
            }

        }

        @Override
        public TalkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.schedule_card_view, parent, false);
            return new TalkViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TalkViewHolder holder, int position) {
            if (items == null || position >+ items.length) {
                Log.e(TAG, "Error, bad item");
                return;
            }
            final ScheduleRow s = items[position];
            holder.speaker.setTag(s); //keep the data for the detail view
            holder.speaker.setText(s.speakerName);
            holder.title.setText(s.talkTitle);
            holder.location.setText(s.talkDescription);
        }


        @Override
        public int getItemCount() {
            return 0;
        }
    }

    public static class TalkViewHolder extends RecyclerView.ViewHolder {

        public final TextView title;
        public final TextView speaker;
        public final TextView location;
        public final TextView time;

        public TalkViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_text);
            speaker = (TextView) itemView.findViewById(R.id.speaker_name_text);
            location = (TextView) itemView.findViewById(R.id.location_text);
            time = (TextView) itemView.findViewById(R.id.time_text);
        }
    }
}
