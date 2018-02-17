package com.mentalmachines.droidcon_boston.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.mentalmachines.droidcon_boston.R;
import com.mentalmachines.droidcon_boston.data.ScheduleDatabase.ScheduleRow;
import com.mentalmachines.droidcon_boston.firebase.ScheduleUpdateUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by emezias on 2/10/18 to read static data and create a local cache in memory
 * Got a lot of code from the script and data model by Ken Yee
 * com.mentalmachines.droidcon_boston.preprocess.ConferenceDataMode.kt
 * first name of the speaker is the key for getting the photo resource packaged in the apk
 */

public class ConferenceData {
    public static final String TAG = ConferenceData.class.getSimpleName();
    private static SimpleDateFormat sTimeFormat = new SimpleDateFormat("h:mm a", Locale.US);
    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("M/d/yyyy", Locale.US);
    private static SimpleDateFormat sMakeDateObject = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    public static ConferenceDataModel sConfData;

    public static class EventData {
        public String name;
        String description;
        String duration;
        Boolean isGeneralEvent;
        Boolean isPublished;
        Date startTime;
        Map<String, Boolean> roomIds;
        Map<String, Boolean> speakerIds;
        String trackId;
        Map<String, Boolean> roomNames;
        Map<String, Boolean> speakerNames;
        String trackName;
        Long updatedAt;
        String updatedBy;
    }

    /**
     * Context is needed to get the json file from assets
     * @param ctx
     * @param date - which tab will be shown
     * @return a list of all of the agenda events
     */
    public static List<ScheduleRow> fetchScheduleList(Context ctx, String date) {
        ScheduleRow listItem;
        if (sConfData == null) {
            sConfData = ConferenceDataUtils.Companion.parseData(ctx);
            sConfData = ScheduleUpdateUtils.INSTANCE.checkForChanges(sConfData, 0l);
        }
        ArrayList<ScheduleRow> eventData = new ArrayList<>();
        for (EventModel event: sConfData.getEvents().values()) {
            listItem = setScheduleRow(event);
            if (listItem.dateString.equals(date)) {
                eventData.add(listItem);
            }
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
            listItem.date = event.getStartTime();
            listItem.time = sTimeFormat.format(listItem.date);
            listItem.dateString = sDateFormat.format(listItem.date);
            Log.d(TAG, "date string for compare " + listItem.dateString);
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

    /**
     * This switch is a way to use data from the res directory for speaker's photos
     * It may be ugly but it will help us to keep the WiFi at the conference responsive
     * @param speakerFirstName
     * @return - resource id for speaker photo
     */
    static int uglySwitch(String speakerFirstName) {
        switch (speakerFirstName) {
            case ADRIAN:
                return R.drawable.adrian_catalan;
            case ADNAN:
                return R.drawable.adnan_a_m;
            case ALICE:
                return R.drawable.alice_yuan;
            case ANA:
                return R.drawable.ana_baotic;
            case CARMEN:
                return R.drawable.carmen_gonzalez;
            case CLIVE:
                return R.drawable.clive_lee;
            case DANNY:
                return R.drawable.danny_preussler;
            case EFFIE:
                return R.drawable.effie_barak;
            case ELIZA:
                return R.drawable.eliza_camberogiannis;
            case ERIC:
                return R.drawable.eric_maxwell;
            case GARIMA:
                return R.drawable.garima_jain;
            case JEB:
                return R.drawable.jeb_ware;
            case JOE:
                return R.drawable.joe_birch;
            case JULIAN:
                return R.drawable.julian_krzeminski;
            case KAAN:
                return R.drawable.kaan_mamikoglu;
            case KELLY:
                return R.drawable.kelly_schuster;
            case LISA:
                return R.drawable.lisa_wray;
            case LORA:
                return R.drawable.lora_kulm;
            case DOUG:
                return R.drawable.doug_stevenson;
            case MANNY:
                return R.drawable.manuel_vicente_vivo;
            case MERCEDES:
                return R.drawable.mercedes_wyss;
            case MICHAEL:
                return R.drawable.michael_evans;
            case NATE:
                return R.drawable.nate_ebel;
            case NICHOLAS:
                return R.drawable.nicholas_dipatri;
            case PANAYOTIS:
                return R.drawable.panayotis_tzinis;
            case SAM:
                return R.drawable.sam_edwards;
            case SHOHEI:
                return R.drawable.shohei_kawano;
            case ZAC:
                return R.drawable.zac_sweers;
        }
        return  - 1;
    }

    public static final String FB = "facebook";
    public static final String TWITTER = "twitter";
    public static final String WEB = "web";
    public static final String GITHUB  = "github";
    public static final String LINKD = "linkedIn";
    //constants for the speaker images in res
    public static final String ADNAN = "adnan";
    public static final String ADRIAN = "adrian";
    public static final String ALICE = "alice";
    public static final String ANA = "ana";
    public static final String CARMEN = "carmen";
    public static final String CLIVE = "clive";
    public static final String DANNY = "danny";
    public static final String DOUG = "doug";
    public static final String EFFIE = "effie";
    public static final String ELIZA = "eliza";
    public static final String ERIC = "eric";
    public static final String GARIMA = "garima";
    public static final String JEB = "jeb";
    public static final String JOE = "joe";
    public static final String JULIAN = "julian";
    public static final String KAAN = "kaan";
    public static final String KELLY = "kelly";
    public static final String LISA = "lisa";
    public static final String LORA = "lora";
    public static final String MANNY = "manuel";
    public static final String MERCEDES = "mercedes";
    public static final String MICHAEL = "michael";
    public static final String NATE = "nate";
    public static final String NICHOLAS = "nicholas";
    public static final String PANAYOTIS = "panayotis";
    public static final String SAM = "sam";
    public static final String SHOHEI = "shohei";
    public static final String ZAC = "zac";
}
