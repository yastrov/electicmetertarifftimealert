package ru.app.yastrov.electicmetertarifftimealert;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by astro on 29.07.2015.
 */
public class MyPreferencesHelper {
    private final static String SETTINGS_NAME = "my_settings";
    public final static String OPTIONS_BOOT_ACTIVATE = "activated_boot";
    public final static String OPTIONS_ALARM_ACTIVATE = "activated_alarm";
    public final static String OPTIONS_WORK_WEEK_ACTIVATE = "activated_work_week";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SETTINGS_NAME, context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }
}
