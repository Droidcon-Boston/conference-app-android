package com.mentalmachines.droidcon_boston.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emezias on 3/31/17.
 * This database can workaround certain issues with the Wordpress API
 * Another advantage is that local resources reduce the network resources required by the crowd using it
 */

public class ScheduleDatabase extends SQLiteAssetHelper {

    public static final String TAG = "ScheduleDatabase";
    private static SQLiteDatabase sDB;
    private static final String DATABASE_NAME = "droidconboston.db";
    private static final int DATABASE_VERSION = 1;
    //columns, database constants
    public static final String TABLE = "schedule";
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

    public static final int COL_NAME = 0;
    public static final int COL_TITLE = 1;
    public static final int COL_DESCRIPTION = 2;
    public static final int COL_PHOTO = 3;
    public static final int COL_BIO = 4;
    public static final int COL_TALK_DATE = 5;
    public static final int COL_TALK_TIME = 6;
    public static final int COL_ROOM = 7;
    public static final int COL_SPKR_TWEET = 8;
    public static final int COL_SPKR_LINKD = 9;
    public static final int COL_SPKR_FB = 10;

    public static class ScheduleRow {
        public String speakerName;
        public String talkTitle;
        public String photo;
        public String time;
        public String room;
        public String date;
    }

    public static class ScheduleDetail {
        public ScheduleRow listRow;
        public String talkDescription;
        public String speakerBio;
        public String twitter;
        public String linkedIn;
        public String facebook;
    }

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
            sDB = (new ScheduleDatabase(ctx)).getWritableDatabase();
        }
        return sDB;
    }

    /**
     * Can use this data structure or just select the bio, the rest is already there
     * @param ctx
     * @param speakername
     * @return
     */
    public static ScheduleDetail fetchDetailData(@NonNull Context ctx, @NonNull String speakername) {
        final SQLiteDatabase db = getDatabase(ctx);
        final Cursor c = db.query(TABLE, null, sDetailWhere, new String[] { speakername },
                null, null, null);
        //should be exactly one field from one row
        if (c.moveToFirst()) {
            final ScheduleDetail talkData = new ScheduleDetail();
            talkData.listRow = new ScheduleRow();
            talkData.listRow.speakerName = c.getString(COL_NAME);
            talkData.listRow.talkTitle = c.getString(COL_TITLE);
            talkData.listRow.photo = c.getString(COL_PHOTO);
            if (!c.isNull(COL_TALK_TIME)) {
                talkData.listRow.room = c.getString(COL_ROOM);
                talkData.listRow.time = c.getString(COL_TALK_TIME);
                talkData.listRow.date = c.getString(COL_TALK_DATE);
            }

            talkData.talkDescription = c.getString(COL_DESCRIPTION);
            talkData.speakerBio = c.getString(COL_BIO);
            if (c.isNull(COL_SPKR_FB)) talkData.facebook = null;
            else talkData.facebook = c.getString(COL_SPKR_FB);
            if (c.isNull(COL_SPKR_LINKD)) talkData.linkedIn = null;
            else talkData.linkedIn = c.getString(COL_SPKR_LINKD);
            if (c.isNull(COL_SPKR_TWEET)) talkData.twitter = null;
            else talkData.twitter = c.getString(COL_SPKR_TWEET);
            return talkData;
        } else {
            Log.e(TAG, "Error reading bio for " + speakername);
            return null;
        }
    }

    public static ScheduleRow[] fetchScheduleData(Context ctx) {
        final SQLiteDatabase db = getDatabase(ctx);
        final Cursor c = db.query(TABLE, null, null, null, null, null, null);
        //all rows
        if (c.moveToFirst()) {
            int dex = 0;
            final ScheduleRow[] items = new ScheduleRow[c.getCount()];
            ScheduleRow item;
            do {
                item = new ScheduleRow();
                item.speakerName = c.getString(0);
                //DEBUG Log.d(TAG, "speaker? " + item.speakerName);
                item.talkTitle = c.getString(1);
                item.photo = c.getString(2);
                if (!c.isNull(3)){
                    item.time = c.getString(3);
                    item.room = c.getString(4);
                    item.date = c.getString(5);
                } else {
                    item.time = "unscheduled";
                }
                items[dex++] = item;
            } while (c.moveToNext());
            Log.i(TAG, "finished adapter arrray, length? " + items.length);
            c.close();
            return items;
        } else {
            Log.e(TAG, "Error reading database");
            return null;
        }
    }

    public static String[] sAgendaProjection = new String[] { NAME,
            TITLE, PHOTO, TALK_TIME, ROOM, TALK_DATE };

    public static final String sDetailWhere = " " + NAME + " LIKE ?";
    public static final String sDayWhere = " " + TALK_DATE + " LIKE ?";
    public static final String MONDAY = "4/10/2017";
    public static final String TUESDAY = "4/11/2017";

    public static List<ScheduleRow> fetchScheduleListByDay(Context ctx, String date) {
        List<ScheduleRow> items;

        String filter = (date == null) ? null : sDayWhere;
        String params[] = (date == null) ? null : new String[] { date };
        String orderBy =  TALK_DATE + " ASC, " + TALK_TIME + " ASC, " + ROOM + " ASC";

        final SQLiteDatabase db = getDatabase(ctx);
        final Cursor c = db.query(TABLE, sAgendaProjection, filter, params, null, null, orderBy);
        //all rows
        if (c.moveToFirst()) {
            //why not use square brackets?
            items = new ArrayList<ScheduleRow>(c.getCount());
            ScheduleRow item;
            do {
                item = new ScheduleRow();
                item.speakerName = c.getString(0);
                //DEBUG Log.d(TAG, "speaker? " + item.speakerName);
                item.talkTitle = c.getString(1);
                item.photo = c.getString(2);
                item.time = c.getString(3);
                item.room = c.getString(4);
                item.date = c.getString(5);
                items.add(item);
            } while (c.moveToNext());
            Log.i(TAG, "finished adapter array");
            c.close();
        } else {
            Log.e(TAG, "Error reading database");
            items = null;
        }

        return items;
    }
    /*************************FAQ******************/
    public static final String FAQ_TABLE = "faq";
    /*public static final String QUESTIONS = "Question";
    public static final String ANSWRS = "Answers";
    public static final String OTHER_LNK = "other_link";
    public static final String MAP_COORDS = "map_link";*/

    public static class FaqData {
        public String question;
        public String answer;
        public String photoUrl;
        public String mapCoords;
        public String bizLink;
    }

    public static FaqData[] fetchFAQ(@NonNull Context ctx) {
        final SQLiteDatabase db = getDatabase(ctx);
        final Cursor c = db.query(FAQ_TABLE, null, null, null, null, null, null);
        //all rows
        ArrayList<FaqData> items = null;
        if (c.moveToFirst()) {
            int dex = 0;
            String question = null;
            items = new ArrayList<>(c.getCount());
            FaqData item;
            do {
                item = new FaqData();
                item.question = c.getString(0);
                //creating Question (dummy entry) for special handling in the adapter
                if (!question.equals(item.question)) {
                    items.add(item);
                    item = new FaqData();
                    item.question = c.getString(0);
                    question = item.question;
                }
                item.answer = c.getString(1);
                item.photoUrl = c.isNull(2)? null : c.getString(2);
                item.mapCoords = c.isNull(3)? null : c.getString(3);
                item.bizLink = c.isNull(4)? null : c.getString(4);
                items.add(item);
                Log.d(TAG, "answer? " + item.answer);
            } while (c.moveToNext());
        }
        c.close();
        if (items != null) {
            return items.toArray(new FaqData[items.size()]);
        }
        Log.e(TAG, "problem fetching FAQ data");
        return null;
    }


    //DEBUG code for dev, run in Main onCreate
    public static void testDb(@NonNull Context ctx) {
        final SQLiteDatabase db = getDatabase(ctx);
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type like 'table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                Log.d(TAG, "Table Name=> " + c.getString(0));
                c.moveToNext();
            }
        }
        final ScheduleRow[] items = fetchScheduleData(ctx);
        if (items == null) {
            Log.e(TAG, "Error reading database");
            return;
        }
        Log.i(TAG, "finished adapter arrray, length? " + items.length);
        c.close();
        fetchDetailData(ctx, items[0].speakerName);
    }
}
