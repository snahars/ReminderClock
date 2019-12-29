package com.reminder.pgdit.reminderclock.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.reminder.pgdit.reminderclock.EventListActivity;

import static com.reminder.pgdit.reminderclock.notification.NotificationHelper.CHANNEL_ID;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("eventTitle");
        String message = intent.getStringExtra("eventDescription");
        String KEY_TONE_URL = intent.getStringExtra("KEY_TONE_URL");

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification(title, message);

        if (KEY_TONE_URL != null) {
            Uri uri = Uri.parse(KEY_TONE_URL);
            nb.setSound(uri);
        }

        notificationHelper.getManager().notify(1, nb.build());
    }
}
