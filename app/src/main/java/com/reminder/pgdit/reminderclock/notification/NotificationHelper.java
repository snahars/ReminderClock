package com.reminder.pgdit.reminderclock.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.reminder.pgdit.reminderclock.EventListActivity;
import com.reminder.pgdit.reminderclock.R;

import static android.os.Build.VERSION_CODES.O;

public class NotificationHelper extends ContextWrapper {


    public static final String CHANNEL_ID = "channel1";
    public static final String CHANNEL_Name = "Notification Chanel Service";
    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();

        }
    }


    @RequiresApi(api = O)
    public void createChannel() {

        NotificationChannel notificationChannel = new NotificationChannel(
                CHANNEL_ID, CHANNEL_Name, NotificationManager.IMPORTANCE_DEFAULT
        );

        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(R.color.colorPrimary);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel);

    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }


    public NotificationCompat.Builder getChannel1Notification(String title, String message) {
        //Intent eventIntent = new Intent(this, EventListActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, eventIntent, 0);
        //.setContentIntent(pendingIntent)

        return new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_event_note_black_24dp);

    }

}