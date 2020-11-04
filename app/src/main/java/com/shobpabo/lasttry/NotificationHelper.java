package com.shobpabo.lasttry;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    private NotificationManager mManager;
    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("reminder1", Context.MODE_PRIVATE);
        String adrs1 = sharedPreferences1.getString("address1","");
        String timedate = sharedPreferences1.getString("timer1","");
        String notes = sharedPreferences1.getString("notes1","");

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(adrs1+" at "+ timedate)
                .setContentText(notes+"\n Click Here to see your schedule")

                .setContentIntent(pi)

                .setSmallIcon(R.drawable.ic_date);
    }
}