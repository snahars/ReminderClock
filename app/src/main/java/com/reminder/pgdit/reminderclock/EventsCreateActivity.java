package com.reminder.pgdit.reminderclock;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.reminder.pgdit.reminderclock.notification.AlarmReceiver;
import com.reminder.pgdit.reminderclock.notification.NotificationHelper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EventsCreateActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextInputLayout textInputLayoutEventDatePicker;
    private TextInputLayout textInputLayoutEventTitle;
    private TextInputLayout textInputLayoutEventDescription;
    private TextInputLayout textInputLayoutSnoozeTime;

    private EditText editTextEventDatePicker;
    private EditText editTextEventTitle;
    private EditText editTextEventDescription;
    private EditText editTextSnoozeTime;
    //private EditText selectedRingtone;

    private TextView selectRingtone;
    private TextView selectedRingtone;

    private String eventTitle;
    private String eventDescription;
    private String eventDatetime;
    private String ringtone;

    private String userId;

    private Button buttonSetAlarm;
    private FloatingActionButton eventFlistbutton;

    SqliteHelper eventsCreateSqliteHelper;

    int year, month, day, hour, minute;
    int yearFinal, monthFinal, dayFinal, hourFinal, minuteFinal;
    String amPm;

    private NotificationHelper mNotification;
    private String uriStr;

    // https://www.youtube.com/watch?v=FbpD5RZtbCc follow this link to build notification.
    //https://www.youtube.com/watch?v=1fV9NmvxXJo


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_create);

        mNotification = new NotificationHelper(this);

        eventsCreateSqliteHelper = new SqliteHelper(this);
        initViewsEventCreateActivity();

        editTextEventDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimePickerDialog();
            }
        });

        selectRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickRingtoneAndSet();
            }
        });

        eventFlistbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTaskList();
            }
        });


        buttonSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getAllFieldsValue();

                userId = getIntent().getExtras().getString("userId");

                eventsCreateSqliteHelper.addEvent(new ReminderEventsServices(
                        userId, eventTitle, eventDescription, eventDatetime, uriStr, 0));

                setAlarmAndNotification();

                refreshEventCreateUi();
                Snackbar.make(buttonSetAlarm, "Successfully Event Created!", Snackbar.LENGTH_LONG).show();

            }
        });

    }

    private void initViewsEventCreateActivity() {

        buttonSetAlarm = (Button) findViewById(R.id.buttonSetAlarm);
        eventFlistbutton = (FloatingActionButton) findViewById(R.id.floatingListEvent);

        editTextEventDatePicker = (EditText) findViewById(R.id.editTextEventDatePicker);
        editTextEventTitle = (EditText) findViewById(R.id.editTextEventTitle);
        editTextEventDescription = (EditText) findViewById(R.id.editTextEventDescription);
        // editTextSnoozeTime = (EditText) findViewById(R.id.editTextSnoozeTime);

        textInputLayoutEventDatePicker = (TextInputLayout) findViewById(R.id.textInputLayoutEventDatePicker);
        textInputLayoutEventTitle = (TextInputLayout) findViewById(R.id.textInputLayoutEventTitle);
        textInputLayoutEventDescription = (TextInputLayout) findViewById(R.id.textInputLayoutEventDescription);

        selectRingtone = (TextView) findViewById(R.id.selectRingtone);
        selectedRingtone = (TextView) findViewById(R.id.selectedRingtone);

    }


    private void getAllFieldsValue() {

        eventTitle = editTextEventTitle.getText().toString();
        eventDescription = editTextEventDescription.getText().toString();
        eventDatetime = editTextEventDatePicker.getText().toString();
        ringtone = selectedRingtone.getText().toString();
        //intervalTime = editTextSnoozeTime.getText().toString();
    }

    private void refreshEventCreateUi() {
        finish();
        startActivity(getIntent());
    }

    private void pickRingtoneAndSet() {
        final Intent ringtone = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        startActivityForResult(ringtone, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 0 && resultCode == RESULT_OK) {
            final Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            final Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
            //display Ringtone title
            uriStr = uri.toString();
            selectedRingtone.setText(ringtone.getTitle(EventsCreateActivity.this));

            /*r = RingtoneManager.getRingtone(getApplicationContext(),Uri.parse(sound));*/
            /*+" and " + "Current Ringtone:"+ringtone.getTitle(EventsCreateActivity.this)*/
            // Get your title here `ringtone.getTitle(this)`
            //RingtoneManager.setActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_RINGTONE, uri);
        }
    }


    @Override
    public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth) {

        yearFinal = year1;
        monthFinal = month1 + 1;
        dayFinal = dayOfMonth;

        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(EventsCreateActivity.this,
                EventsCreateActivity.this, hour, minute, false);

        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        hourFinal = hourOfDay;
        minuteFinal = minute;

        if (hourOfDay >= 12) {
            amPm = "PM";
        } else {
            amPm = "AM";
        }

        String selectedDate = dayFinal + "/" + monthFinal + "/" + yearFinal;
        // parse the String selectedDate to a java.util.Date object
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(selectedDate);
            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
            editTextEventDatePicker.setText(formattedDate + " " + String.format("%02d:%02d %s", hourFinal, minuteFinal, amPm));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setDateTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EventsCreateActivity.this,
                EventsCreateActivity.this, year, month, day);
        datePickerDialog.show();

    }

    public long stringTolongTime(String stringDate) {
        Long longTime = null;
        if (!stringDate.isEmpty()) {
            Timestamp ts2 = stringToTimestamp(stringDate);
            longTime = ts2.getTime();
        }

        return longTime;
    }

    public Timestamp stringToTimestamp(String stringDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        Timestamp dateTimestamp = null;
        try {

            if (!stringDate.isEmpty()) {
                Date parsedDate = dateFormat.parse(stringDate);
                dateTimestamp = new Timestamp(parsedDate.getTime());
            }
        } catch (Exception e) {
        }
        return dateTimestamp;
    }

    private void showTaskList() {
        String userId = getIntent().getExtras().getString("userId");

        Intent callingIntent = new Intent(EventsCreateActivity.this, EventListActivity.class);
        callingIntent.putExtra("userId", userId);
        startActivity(callingIntent);

    }

    private void setAlarmAndNotification() {

        long timeInLong = stringTolongTime(eventDatetime);
        userTaskTimeWiseAlarmSet(timeInLong);
        //AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //Intent intent = new Intent(this, AlarmReceiver.class);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInLong, pendingIntent);
    }

   /* private void sendNotificationChannel(String title, String message) {

        NotificationCompat.Builder builder = mNotification.getChannel1Notification(title, message);
        mNotification.getManager().notify(1, builder.build());

    }*/

    private void userTaskTimeWiseAlarmSet(long timeInLong) {

        int requestCode = (int) timeInLong;
        if (!eventDatetime.isEmpty()) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("userId", userId);
            intent.putExtra("KEY_TONE_URL", uriStr);
            intent.putExtra("eventTitle", eventTitle);
            intent.putExtra("eventDescription", eventDescription);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getBaseContext(),
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInLong, pendingIntent);
        }
    }


}
