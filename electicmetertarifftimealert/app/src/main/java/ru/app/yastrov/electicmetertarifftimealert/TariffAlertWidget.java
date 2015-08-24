package ru.app.yastrov.electicmetertarifftimealert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class TariffAlertWidget extends AppWidgetProvider {
    static  final String LOG_TAG = "TariffAlertWidget";
    public static final String ACTION_APPWIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "onUpdate");

        final int background_resource;
        final CharSequence widgetText;
        final int result = AlarmHelper.WhatTariffNow(context);
        switch (result)
        {
            case AlarmHelper.CODE_LOW_TARIFF:
                widgetText = context.getString(R.string.tariff_low_price);
                background_resource = R.drawable.widget_background_gradient_low;
                break;
            case AlarmHelper.CODE_MEDIUM_TARIFF:
                widgetText = context.getString(R.string.tariff_medium_price);
                background_resource = R.drawable.widget_background_gradient_medium;
                break;
            case AlarmHelper.CODE_HIGH_TARIFF:
                widgetText = context.getString(R.string.tariff_high_price);
                background_resource = R.drawable.widget_background_gradient_hight;
                break;
            default:
                background_resource = R.drawable.widget_background_gradient;
                widgetText = context.getString(R.string.app_name);
                break;
        }

        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateWidget(context, appWidgetManager, appWidgetIds[i], background_resource, widgetText);
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
        Log.d(LOG_TAG, "onReceive");
        super.onReceive(context, intent); // It process default ACTION_APPWIDGET_UPDATE
    }

    /**
     * Update visual elements for Widget.
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int background_resource, CharSequence widgetText) {
        Log.d(LOG_TAG, "updateWidget");
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.tariff_alert_widget);
        views.setTextViewText(R.id.information_widget_text, widgetText);
        views.setInt(R.id.information_widget, "setBackgroundResource", background_resource);
        views.setImageViewResource(R.id.information_widget_image_view, R.drawable.ic_stat_name);
        // Open MainActivity
        Intent configIntent = new Intent(context, MainActivity.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        /* PendingIntent.getActivity() for call Activity
            or PendingIntent.getBroadcast() for Widget or BroadcastReceiver
         */
        PendingIntent pIntent = PendingIntent.getActivity(context, appWidgetId, configIntent, 0);
        views.setOnClickPendingIntent(R.id.information_widget, pIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void SetAlarm(Context context) {
        Log.d(LOG_TAG, "SetAlarm");
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TariffAlertWidget.class);
        intent.setAction(ACTION_APPWIDGET_UPDATE);
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

