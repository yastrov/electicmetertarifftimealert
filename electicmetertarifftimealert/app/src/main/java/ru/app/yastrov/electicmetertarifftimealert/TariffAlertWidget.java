package ru.app.yastrov.electicmetertarifftimealert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class TariffAlertWidget extends AppWidgetProvider {
    static final String UPDATE_ALL_WIDGETS = "update_all_widgets";
    static  final String LOG_TAG = "TariffAlertWidget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "onUpdate");
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");
        SetAlarm(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
        CancelAlarm(context);
    }

    /**
     * Here we receive any Intent, and if we receive UPDATE_ALL_WIDGETS,
     *  we update our widget.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(LOG_TAG, "onReceive");
        if (intent.getAction().equalsIgnoreCase(UPDATE_ALL_WIDGETS)) {
            ComponentName thisAppWidget = new ComponentName(
                    context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            // It's reason if widgets 1 or 2.
            for (int appWidgetID : ids) {
                updateWidget(context, appWidgetManager, appWidgetID);
            }
        }
    }

    /**
     * Update visual elements for Widget.
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d(LOG_TAG, "updateWidget");
        int color = Color.WHITE;
        int result = AlarmHelper.WhatTariffNow(context);
        CharSequence widgetText = context.getString(R.string.app_name);
        if (result == AlarmHelper.CODE_LOW_TARIFF) {
            widgetText = context.getString(R.string.tariff_low_price);
            color = Color.GREEN;
        }
        if (result == AlarmHelper.CODE_MEDIUM_TARIFF) {
            widgetText = context.getString(R.string.tariff_medium_price);
            color = Color.YELLOW;
        }
        if (result == AlarmHelper.CODE_HIGH_TARIFF) {
            widgetText = context.getString(R.string.tariff_high_price);
            color = Color.RED;
        }
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.tariff_alert_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setInt(R.id.appwidget_text, "setBackgroundColor", color);
        // Open MainActivity
        Intent configIntent = new Intent(context, MainActivity.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        /* PendingIntent.getActivity() for call Activity
            or PendingIntent.getBroadcast() for Widget or BroadcastReceiver
         */
        PendingIntent pIntent = PendingIntent.getActivity(context, appWidgetId, configIntent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_text, pIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void SetAlarm(Context context) {
        Log.d(LOG_TAG, "SetAlarm");
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TariffAlertWidget.class);
        intent.setAction(UPDATE_ALL_WIDGETS);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Set the alarm to start at 1:00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_HOUR /*1000 * 60 * 20*/, alarmIntent);
    }

    private static void CancelAlarm(Context context) {
        Log.d(LOG_TAG, "CancelAlarm");
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TariffAlertWidget.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
    }
}

