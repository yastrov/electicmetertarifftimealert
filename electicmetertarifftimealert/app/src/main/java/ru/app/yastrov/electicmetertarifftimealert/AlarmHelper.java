package ru.app.yastrov.electicmetertarifftimealert;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Yuriy Astrov on 31.07.2015.
 */
public class AlarmHelper {
    public static final int CODE_PASS = 0;
    public static final int CODE_LOW_TARIFF = 1;
    public static final int CODE_MEDIUM_TARIFF = 2;
    public static final int CODE_HIGH_TARIFF = 3;

    public static final int WORK_DAY_START = 7;
    public static final int WORK_DAY_END = 18;

    public static int WhatTariffNowStrictPreferenceFeature(Context context) {
        Calendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int dow = calendar.get (Calendar.DAY_OF_WEEK);

        SharedPreferences sPref = MyPreferencesHelper.getSharedPreferences(context);
        if (sPref.getBoolean(MyPreferencesHelper.OPTIONS_WORK_WEEK_ACTIVATE, false)) {
            if ((dow >= Calendar.MONDAY) && (dow <= Calendar.FRIDAY) && (hour < WORK_DAY_END) && (hour >= WORK_DAY_START)) {
                return CODE_PASS;
            }
        }

        if(sPref.getBoolean(MyPreferencesHelper.OPTIONS_EVERY_HOUR_ACTIVATE, false)) {
            if ((hour < 7) || (hour >= 23)) {
                return CODE_LOW_TARIFF;
            }
            if ((hour >= 7 && hour < 10) || (hour >= 17 && hour < 21)) {
                return CODE_HIGH_TARIFF;
            }
            if ((hour >= 10 && hour < 17) || (hour >= 21 && hour < 23)) {
                return CODE_MEDIUM_TARIFF;
            }
        } else {
            if ((hour == 6) || (hour == 23)) {
                return CODE_LOW_TARIFF;
            }
            if ((hour == 7 ) || (hour == 17)) {
                return CODE_HIGH_TARIFF;
            }
            if ((hour == 10 ) || (hour == 21)) {
                return CODE_MEDIUM_TARIFF;
            }
        }
        return CODE_PASS;
    }

    public static int WhatTariffNow(Context context) {
        Calendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int dow = calendar.get (Calendar.DAY_OF_WEEK);

        SharedPreferences sPref = MyPreferencesHelper.getSharedPreferences(context);
        if(sPref.getBoolean(MyPreferencesHelper.OPTIONS_WORK_WEEK_ACTIVATE, false)) {
            if ((dow >= Calendar.MONDAY) && (dow <= Calendar.FRIDAY) && (hour < WORK_DAY_END) && (hour >= WORK_DAY_START)) {
                return CODE_PASS;
            }
        }

        if((hour < 7) || (hour >= 23)) {
            return CODE_LOW_TARIFF;
        }
        if ((hour >= 7 && hour < 10) || (hour >= 17 && hour < 21)) {
            return CODE_HIGH_TARIFF;
        }
        if ((hour >= 10 && hour < 17) || (hour >= 21 && hour < 23)) {
            return CODE_MEDIUM_TARIFF;
        }
        return CODE_PASS;
    }
}
