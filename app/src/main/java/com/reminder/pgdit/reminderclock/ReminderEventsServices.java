package com.reminder.pgdit.reminderclock;

public class ReminderEventsServices {

    public String user_id;
    public String event_title;
    public String event_description;
    public String event_datetime;
    public String event_ringtone;

    public int interval_time;

    public ReminderEventsServices() {

    }

    public ReminderEventsServices(String userId, String eventTitle1, String eventDescription1, String date_time1, String ringtone1, int intervalTime1) {
        this.user_id = userId;
        this.event_title = eventTitle1;
        this.event_description = eventDescription1;
        this.event_datetime = date_time1;
        this.event_ringtone = ringtone1;
        this.interval_time = intervalTime1;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getEvent_title() {
        return event_title;
    }

    public String getEvent_description() {
        return event_description;
    }

    public String getEvent_datetime() {
        return event_datetime;
    }

    public String getEvent_ringtone() {
        return event_ringtone;
    }

    public int getInterval_time() {
        return interval_time;
    }
}
