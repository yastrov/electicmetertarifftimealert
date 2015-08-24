package ru.app.yastrov.electicmetertarifftimealert;

import android.app.Activity;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;


public class MainActivity extends Activity implements
        //View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);*/
        LoadPreferencesAndShowOnActivity();

        TabHost tabs = (TabHost) findViewById(R.id.tabHost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("tag1");
        spec.setContent(R.id.tab1);
        spec.setIndicator(getResources().getString(R.string.action_settings));
        //spec.setIndicator("Settings", getResources().getDrawable(R.drawable.icon_videos_tab, null));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Info");
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("About");
        tabs.addTab(spec);
        // Set current Tab
        tabs.setCurrentTab(0);

        TextView tv = (TextView) findViewById(R.id.InfoTextView);
        tv.setText(R.string.instruction_text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ProgressDialog mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(getResources().getString(R.string.please_wait));
        mProgressDialog.show();

        SharedPreferences.Editor ed = MyPreferencesHelper.getEditor(this.getApplicationContext());

        switch (buttonView.getId()) {
            case R.id.switchEnableBoot:
                ed.putBoolean(MyPreferencesHelper.OPTIONS_BOOT_ACTIVATE, isChecked);
                ed.commit();
                if(isChecked) {
                    EnableBootIntentReceiver();
                } else {
                    DisableBootIntentReceiver();
                }
                break;
            case R.id.switchEnableAlarm:
                ed.putBoolean(MyPreferencesHelper.OPTIONS_ALARM_ACTIVATE, isChecked);
                ed.commit();
                Context context = this.getApplicationContext();
                if(isChecked) {
                    AlarmManagerReceiver.SetAlarm(context);
                } else {
                    AlarmManagerReceiver.CancelAlarm(context);
                }
                break;
            case R.id.switchEnableWorkWeek:
                ed.putBoolean(MyPreferencesHelper.OPTIONS_WORK_WEEK_ACTIVATE, isChecked);
                ed.commit();
                updateWidgets(this);
                break;
            case R.id.switchEnableEveryHour:
                ed.putBoolean(MyPreferencesHelper.OPTIONS_EVERY_HOUR_ACTIVATE, isChecked);
                ed.commit();
                break;
            default:
                break;
        }
        mProgressDialog.dismiss();
    }

    /*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                ;
                break;
            default:
                break;
        }
    }
    */

    private void EnableBootIntentReceiver()
    {
        Context context = this.getApplicationContext();
        ComponentName receiver = new ComponentName(context, MyBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

    }

    private void DisableBootIntentReceiver()
    {
        Context context = this.getApplicationContext();
        ComponentName receiver = new ComponentName(context, MyBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void LoadPreferencesAndShowOnActivity()
    {
        Switch sw;
        SharedPreferences sPref;
        boolean checked;
        sPref = MyPreferencesHelper.getSharedPreferences(this.getApplicationContext());

        checked = sPref.getBoolean(MyPreferencesHelper.OPTIONS_ALARM_ACTIVATE, false);
        sw = (Switch)findViewById(R.id.switchEnableAlarm);
        sw.setChecked(checked);
        sw.setOnCheckedChangeListener(this);

        checked = sPref.getBoolean(MyPreferencesHelper.OPTIONS_BOOT_ACTIVATE, false);
        sw = (Switch)findViewById(R.id.switchEnableBoot);
        sw.setChecked(checked);
        sw.setOnCheckedChangeListener(this);

        checked = sPref.getBoolean(MyPreferencesHelper.OPTIONS_WORK_WEEK_ACTIVATE, false);
        sw = (Switch)findViewById(R.id.switchEnableWorkWeek);
        sw.setChecked(checked);
        sw.setOnCheckedChangeListener(this);

        checked = sPref.getBoolean(MyPreferencesHelper.OPTIONS_EVERY_HOUR_ACTIVATE, false);
        sw = (Switch)findViewById(R.id.switchEnableEveryHour);
        sw.setChecked(checked);
        sw.setOnCheckedChangeListener(this);
    }

    private static void updateWidgets(Context context) {
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] ids = widgetManager.getAppWidgetIds(new ComponentName(context, TariffAlertWidget.class));
        if(ids.length != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                widgetManager.notifyAppWidgetViewDataChanged(ids, android.R.id.list);
            Intent intent = new Intent(context.getApplicationContext(), TariffAlertWidget.class);
            intent.setAction(TariffAlertWidget.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(intent);
        }
    }
}
