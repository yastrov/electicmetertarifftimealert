package ru.app.yastrov.electicmetertarifftimealert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

public class AlarmManagerReceiver extends BroadcastReceiver {
    //final public static String MY_TAG = "tag";

    public AlarmManagerReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        /*Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();

        if (extras != null && extras.getBoolean(MY_TAG, Boolean.FALSE)) {

        }
    */
        int result = AlarmHelper.WhatTariffNow(context);
        if (result == AlarmHelper.CODE_LOW_TARIFF) {
            MyNotificationHelper.MakeNotify(context,
                    context.getResources().getString(R.string.tariff_low_price));
            return;
        }
        if (result == AlarmHelper.CODE_MEDIUM_TARIFF) {
            MyNotificationHelper.MakeNotify(context,
                    context.getResources().getString(R.string.tariff_medium_price));
            return;
        }
        if (result == AlarmHelper.CODE_HIGH_TARIFF) {
            MyNotificationHelper.MakeNotify(context,
                    context.getResources().getString(R.string.tariff_high_price));
            return;
        }
    }

    public static void SetAlarm(Context context) {
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Set the alarm to start at 1:00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_HOUR /*1000 * 60 * 20*/, alarmIntent);
    }

    public static void CancelAlarm(Context context) {
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
    }
}
