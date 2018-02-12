package com.mentalmachines.droidcon_boston.data;

import android.content.Context;
import android.support.annotation.NonNull;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase.ScheduleRow;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by emezias on 2/10/18 to read static data and create a local cache in memory
 * Got a lot of code from the script and data model by Ken Yee
 * com.mentalmachines.droidcon_boston.preprocess.ConferenceDataMode.kt
 * first name of the speaker is the key for getting the photo resource packaged in the apk
 */

public class ConferenceData {
    public static final String TAG = ConferenceData.class.getSimpleName();
    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.US);
    public static ConferenceDataModel sConfData;

    /**
     * Context is needed to get the json file from assets
     * @param ctx
     * @return a list of all of the agenda events
     */
    public static List<ScheduleRow> fetchScheduleList(Context ctx) {
        ScheduleRow listItem;
        if (sConfData == null) {
            sConfData = ConferenceDataUtils.Companion.parseData(ctx);
        }
        ArrayList<ScheduleRow> eventData = new ArrayList<>();
        for (EventModel event: sConfData.getEvents().values()) {
            listItem = setScheduleRow(event);
            eventData.add(listItem);
        }
        return eventData;
    }

    /**
     * This is used to populate the page opened with a tap on an agenda event
     * @param event - input the JSON map from the conference data model
     * @return - the details to display for a single event - speaker bio and links
     */
    static ScheduleRow setScheduleRow(EventModel event) {
        final ScheduleRow listItem = new ScheduleRow();
        if (event.getSpeakerNames() != null) {
            listItem.speakerName = (String) event.getSpeakerNames().keySet().toArray()[0];
            if (listItem.speakerName.indexOf(" ") > 0) {
                listItem.photoResource = uglySwitch(ADRIAN);
                //Log.d(TAG, "resource set? " + listItem.photoResource);
            }
        }
        listItem.talkTitle = event.getName();
        if (event.getStartTime() != null) {
            listItem.time = sDateFormat.format(event.getStartTime());
        }
        if (event.getRoomIds() != null) {
            listItem.room = (String) event.getRoomNames().keySet().toArray()[0];
        }
        return listItem;
    }

    public static ScheduleDatabase.ScheduleDetail fetchDetailData(@NonNull String speakername) {
        ScheduleDatabase.ScheduleDetail detailData = new ScheduleDatabase.ScheduleDetail();
        ScheduleRow listItem;
        for (EventModel event: sConfData.getEvents().values()) {
            if (event.getSpeakerNames() != null) {
                if (speakername.equals(event.getSpeakerNames().keySet().toArray()[0])) {
                    listItem = setScheduleRow(event);
                    detailData.listRow = listItem;
                    detailData.talkDescription = event.getDescription();
                    break;
                }
            }
        }
        if (detailData.listRow == null) return null;
        for (SpeakerModel speakerData: sConfData.getSpeakers().values()) {
            if (speakerData.getName().equals(detailData.listRow.speakerName)) {
                detailData.speakerBio = speakerData.getBio();
                //socialProfiles
                if (speakerData.getSocialProfiles() != null && !speakerData.getSocialProfiles().isEmpty()) {
                    detailData.facebook = speakerData.getSocialProfiles().get(FB);
                    detailData.linkedIn = speakerData.getSocialProfiles().get(LINKD);
                    detailData.twitter = speakerData.getSocialProfiles().get(TWITTER);
                }
            }
        }
        return detailData;
    }

    static int uglySwitch(String speakerFirstName) {
        switch (speakerFirstName) {
            case ADRIAN:
                return R.drawable.adrian_catalan;
            case JULIAN:
                return R.drawable.julian_krzeminski;
            case ALICE:
                return R.drawable.alice_yuan;
            case KELLY:
                return R.drawable.kelly_schuster;
            case ANA:
                return R.drawable.ana_baotic;
            case LISA:
                return R.drawable.lisa_wray;
            case DANNY:
                return R.drawable.danny_preussler;
            case LORA:
                return R.drawable.lora_kulm;
            case DOUG:
                return R.drawable.doug_stevenson;
            case MANNY:
                return R.drawable.manuel_vicente_vivo;
            case ELIZA:
                return R.drawable.eliza_camberogiannis;
            case MERCEDES:
                return R.drawable.mercedes_wyss;
            case GARIMA:
                return R.drawable.garima_jain;
            case NATE:
                return R.drawable.nate_ebel;
            case JEB:
                return R.drawable.jeb_ware;
            case NICK:
                return R.drawable.nicholas_dipatri;
            case JOE:
                return R.drawable.joe_birch;
            case SAM:
                return R.drawable.sam_edwards;
        }
        return  - 1;
    }

    public static final String FB = "facebook";
    public static final String TWITTER = "twitter";
    public static final String WEB = "web";
    public static final String GITHUB  = "github";
    public static final String LINKD = "linkedIn";
    //constants for the speaker images in res
    public static final String ADRIAN = "adrian";
    public static final String JULIAN = "julian";
    public static final String ALICE = "alice";
    public static final String KELLY = "kelly";
    public static final String ANA = "ana";
    public static final String LISA = "lisa";
    public static final String DANNY = "danny";
    public static final String LORA = "lora";
    public static final String DOUG = "doug";
    public static final String MANNY = "manuel";
    public static final String ELIZA = "eliza";
    public static final String MERCEDES = "mercedes";
    public static final String GARIMA = "garima";
    public static final String NATE = "nate";
    public static final String JEB = "jeb";
    public static final String NICK = "nicholas";
    public static final String JOE = "joe";
    public static final String SAM = "sam";
}
