package ru.app.yastrov.electicmetertarifftimealert;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NotifyIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_REGULAR_ALARM_TICK = "ru.app.yastrov.electicmetertarifftimealert.action.regularalarmtick";

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionRegularAlarmTick(Context context) {
        Intent intent = new Intent(context, NotifyIntentService.class);
        intent.setAction(ACTION_REGULAR_ALARM_TICK);
        context.startService(intent);
    }

    public NotifyIntentService() {
        super("NotifyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_REGULAR_ALARM_TICK.equals(action)) {
                handleActionRegularAlarmTick();
                return;
            }
        }
    }
    private void handleActionRegularAlarmTick() {
        final Context context = this.getApplicationContext();
        final int result = AlarmHelper.WhatTariffNowStrictPreferenceFeature(context);
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
}
