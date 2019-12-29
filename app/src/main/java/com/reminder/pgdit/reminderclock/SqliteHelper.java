package com.reminder.pgdit.reminderclock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by sunipa on 29/11/2019.
 */

public class SqliteHelper extends SQLiteOpenHelper {

    //DATABASE NAME
    public static final String DATABASE_NAME = "reminderclock";

    //DATABASE VERSION
    public static final int DATABASE_VERSION = 3;

    //TABLE NAME
    public static final String TABLE_USERS = "users";
    public static final String TABLE_EVENTS = "events";

    //TABLE USERS COLUMNS
    //ID COLUMN @primaryKey
    public static final String KEY_ID = "user_id";

    //COLUMN user name
    public static final String KEY_USER_NAME = "username";

    //COLUMN email
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_CONTACTNO = "contactNo";

    //COLUMN password
    public static final String KEY_PASSWORD = "password";

    public static final String EVENT_KEY_ID = "event_id";
    public static final String event_title = "event_title";
    public static final String event_description = "event_description";
    public static final String event_datetime = "event_datetime";
    public static final String event_ringtone = "event_ringtone";
    public static final String interval_time = "interval_time";


    //SQL for creating users table
    public static final String SQL_TABLE_USERS = " CREATE TABLE " + TABLE_USERS
            + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_FIRST_NAME + " TEXT, "
            + KEY_LAST_NAME + " TEXT, "
            + KEY_GENDER + " TEXT, "
            + KEY_CONTACTNO + " INTEGER, "
            + KEY_EMAIL + " TEXT, "
            + KEY_PASSWORD + " TEXT"
            + " ) ";

    //SQL for creating  SQL_TABLE_EVENT_CREATE
    public static final String SQL_TABLE_EVENT_CREATE = " CREATE TABLE " + TABLE_EVENTS
            + " ( "
            + EVENT_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ID + " INTEGER, "
            + event_title + " TEXT, "
            + event_description + " TEXT, "
            + event_ringtone + " TEXT, "
            + event_datetime + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + interval_time + " INTEGER, "
            + "CONSTRAINT USER_EVENT_FK FOREIGN KEY (" + KEY_ID + ")  REFERENCES "  + TABLE_USERS + "("+ KEY_ID + ") "

            + " ) ";


    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_TABLE_USERS);
        sqLiteDatabase.execSQL(SQL_TABLE_EVENT_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_USERS);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_EVENTS);

    }


    public void addUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, user.id);

        values.put(KEY_EMAIL, user.email);

        values.put(KEY_PASSWORD, user.password);

        values.put(KEY_FIRST_NAME, user.firstName);

        values.put(KEY_LAST_NAME, user.lastName);

        values.put(KEY_GENDER, user.gender);

        values.put(KEY_CONTACTNO, user.contactNo);

        long todo_id = db.insert(TABLE_USERS, null, values);
    }


    public User retreiveUserByEmail(String email){
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(TABLE_USERS,null,KEY_EMAIL+"=?",new String[]{email},null,null,null);
        if(cursor!=null&&cursor.getCount()>0){
            cursor.moveToFirst();
            String userId =cursor.getString(cursor.getColumnIndex(KEY_ID));
            String mail=cursor.getString(cursor.getColumnIndex(KEY_EMAIL));
            String password=cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));
            String firstName = cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME));
            String gender = cursor.getString(cursor.getColumnIndex(KEY_GENDER));
            String contactNo = cursor.getString(cursor.getColumnIndex(KEY_CONTACTNO));
            User retreivedUser = new User(userId, mail, password, firstName, lastName, gender, contactNo);

            return retreivedUser;
        }
        return null;
    }


    public void addEvent(ReminderEventsServices reminderEventsServices) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, reminderEventsServices.user_id);

        values.put(event_title, reminderEventsServices.event_title);

        values.put(event_description, reminderEventsServices.event_description);

        values.put(event_ringtone, reminderEventsServices.event_ringtone);

        values.put(event_datetime, reminderEventsServices.event_datetime);

        values.put(interval_time, reminderEventsServices.interval_time);

        long todo_id = db.insert(TABLE_EVENTS, null, values);
    }

    /*public Cursor retriveAllEventsByUserCursor(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS,// Selecting Table
                null,
                KEY_EMAIL + "=?",
                 new String[]{email},
                null, null, null);

        return cursor;
    }*/

    public  ArrayList<ReminderEventsServices> retreiveEventByUser(String userId) {
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(TABLE_EVENTS,null,KEY_ID+"=?", new String[]{userId},null,null,"event_datetime DESC");

        ArrayList<ReminderEventsServices> eventList = new ArrayList<ReminderEventsServices>();

        if (cursor!=null&&cursor.getCount()>0) {
            cursor.moveToFirst();

            while(cursor.isAfterLast() == false) {

                //String userId =cursor.getString(cursor.getColumnIndex(KEY_ID));
                String eventTtitle =cursor.getString(cursor.getColumnIndex(event_title));
                String eventDescription = cursor.getString(cursor.getColumnIndex(event_description));
                String eventDateTime = cursor.getString(cursor.getColumnIndex(event_datetime));
                String eventRingtone = cursor.getString(cursor.getColumnIndex(event_ringtone));
                Integer intervalTime = cursor.getInt(cursor.getColumnIndex(interval_time));
                ReminderEventsServices eventData =
                        new ReminderEventsServices(userId, eventTtitle, eventDescription, eventDateTime, eventRingtone, intervalTime);
                if (!eventTtitle.isEmpty()) {
                    eventList.add(eventData);
                }
                cursor.moveToNext();

            }

        }

        return eventList;
    }


    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,// Selecting Table
                null,
                KEY_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            return true;
        }
        return false;
    }

    public boolean deleteEventItem(String rowId) {

        String table = TABLE_EVENTS;
        SQLiteDatabase database = this.getReadableDatabase();
        int result = database.delete(table, EVENT_KEY_ID + "=?", new String[]{rowId});
        if (result > 0)
            return true;
        else
            return false;
    }

    public boolean deleteEventList() {

        String table = TABLE_EVENTS;
        SQLiteDatabase database = this.getReadableDatabase();
        int result = database.delete(table, null, null);
        if (result > 0)
            return true;
        else
            return false;
    }


}
