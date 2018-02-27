package com.mentalmachines.droidcon_boston.data;

/*
 * View models for schedule and schedule detail items.
 */
public class Schedule {

    public static final String SCHEDULE_ITEM_ROW = "schedule_item_row";
    public static final String MONDAY = "03/26/2018";
    public static final String TUESDAY = "03/27/2018";

    public static class ScheduleRow {

        public String talkDescription;
        public String speakerName;
        public int speakerCount;
        public String talkTitle;
        public String photo;
        public String utcStartTimeString;
        public String startTime;
        public String endTime;
        public String room;
        public String date;
        public Integer trackSortOrder;

        public String getId() {
            return talkTitle;
        }
    }

    public static class ScheduleDetail {

        public ScheduleRow listRow;
        public String speakerBio;
        public String twitter;
        public String linkedIn;
        public String facebook;

        public String getId() {
            return listRow.getId();
        }
    }
}
