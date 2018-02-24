package com.mentalmachines.droidcon_boston.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class FaqDatabase extends SQLiteAssetHelper {

    private static final String TAG = FaqDatabase.class.getName();
    private static final String DATABASE_NAME = "DroidconBoston17.db";
    private static SQLiteDatabase sDB;
    private static final int DATABASE_VERSION = 1;

    FaqDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Using a singleton to get the db, keep a single db open
     * Close cursors, do not close the db
     * If the feature set includes saving a user schedule we might make that preferences or set up a writable db
     *
     * @return FaqDatabase
     */
    public static SQLiteDatabase getDatabase(@NonNull Context ctx) {
        if (sDB == null) {
            sDB = (new FaqDatabase(ctx)).getWritableDatabase();
        }
        return sDB;
    }

    /*************************FAQ******************/
    public static final String FAQ_TABLE = "faq";
    public static final String QUESTIONS = "Question";

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
            String question = "";
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
                item.photoUrl = c.isNull(2) ? null : c.getString(2);
                item.mapCoords = c.isNull(3) ? null : c.getString(3);
                item.bizLink = c.isNull(4) ? null : c.getString(4);
                items.add(item);
                //Log.d(TAG, "photo url? " + item.photoUrl.toString());
            } while (c.moveToNext());
        }
        c.close();
        if (items != null) {
            Log.d(TAG, "returning FAQ answers " + items.size());
            return items.toArray(new FaqData[items.size()]);
        }
        Log.e(TAG, "problem fetching FAQ data");
        return null;
    }

    public static HashMap<Integer, FaqData[]> makeAnswers(@NonNull Context ctx, @NonNull String[] questions) {
        final HashMap<Integer, FaqData[]> answerData = new HashMap<>();
        final FaqData[] masterList = fetchFAQ(ctx);
        final ArrayList<FaqData> qData = new ArrayList<>();
        int dex = 0;
        for (String q : questions) {
            for (FaqData answer : masterList) {
                if (q.equals(answer.question) && !TextUtils.isEmpty(answer.answer)) {
                    qData.add(answer);
                }
            } //end question q
            qData.trimToSize();
            answerData.put(dex++, qData.toArray(new FaqData[qData.size()]));
            qData.clear();
        } //every question answered
        return answerData;
    }

    public static String[] fetchQuestions(@NonNull Context ctx) {
        final SQLiteDatabase db = getDatabase(ctx);
        final Cursor c = db.query(true, FAQ_TABLE, new String[]{QUESTIONS}, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            String[] groups = new String[c.getCount()];
            int dex = 0;
            do {
                groups[dex++] = c.getString(0);
                Log.d(TAG, "questions?" + groups[dex - 1]);
            } while (c.moveToNext());
            c.close();
            Log.d(TAG, "returning questions " + groups.length);
            return groups;
        } else {
            Log.e(TAG, "problem fetching FAQ data");
            return null;
        }
    }
}
