package ru.app.yastrov.electicmetertarifftimealert;

import android.app.Notification.Builder;
import android.content.Context;
import android.app.NotificationManager;

/**
 * Created by Yuriy Astrov on 28.07.2015.
 */
public class MyNotificationHelper {
    private static final int NOTIFY_ID = 1;

    public static void MakeNotify(Context context, String title, String content)
    {
        Builder builder = new Builder(context)
                //.setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_stat_name)
                //.setVisibility(Notification.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(content);
        //.setContentIntent(getPendingIntent())
        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFY_ID, builder.build());
    }

    public static void MakeNotify(Context context, String content)
    {
        MakeNotify(context,context.getResources().getString(R.string.app_name), content);
    }
}
