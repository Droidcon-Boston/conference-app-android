package com.mentalmachines.droidcon_boston.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    public static final String TABLE = "speakers";
    public static final String NAME = "name";
    public static final String TITLE = "talk";
    public static final String DESCRIPTION = "description";
    /* TODO
    public static final String PHOTO = "photo_link";
    public static final String ROOM = "room";*/

    public ScheduleDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Using a singleton to get the db, keep a single db open
     * Close cursors, do not close the db
     * If the feature set includes saving a user schedule we might make that preferences or set up a writable db
     * @return ScheduleDatabase
     */
    public static SQLiteDatabase getDatabase(Context ctx) {
        if (sDB == null) {
            sDB = (new ScheduleDatabase(ctx)).getReadableDatabase();
        }
        return sDB;
    }

    public static class Schedule {
        String speakerName;
        String talkTitle;
        String talkDescription;
    }

    public static class ScheduleAdapter extends RecyclerView.Adapter<TalkViewHolder> {
        final private Schedule[] items;

        public ScheduleAdapter(Context ctx) {
            Log.i(TAG, "adapter");
            final SQLiteDatabase db = getDatabase(ctx);
            final Cursor c = db.query(TABLE, null, null, null, null, null, null);
            //all rows
            if (c.moveToFirst()) {
                int dex = 0;
                items = new Schedule[c.getCount()];
                Schedule item;
                do {
                    item = new Schedule();
                    item.speakerName = c.getString(0);
                    Log.d(TAG, "speaker? " + item.speakerName);
                    item.talkTitle = c.getString(1);
                    item.talkDescription = c.getString(2);
                    items[dex++] = item;
                } while (c.moveToNext());
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
            final Schedule s = items[position];
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
