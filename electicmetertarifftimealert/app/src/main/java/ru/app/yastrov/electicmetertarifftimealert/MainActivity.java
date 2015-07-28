package ru.app.yastrov.electicmetertarifftimealert;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;


public class MainActivity extends Activity implements
        //View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    final static String OPTIONS_BOOT_ACTIVATE = "activated_boot";
    final static String OPTIONS_ALARM_ACTIVATE = "activated_alarm";
    final static String OPTIONS_WORK_WEEK_ACTIVATE = "activated_work_week";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);*/
        LoadPreferencesAndShowOnActivity();
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
        SharedPreferences sPref;
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();

        switch (buttonView.getId()) {
            case R.id.switchEnableBoot:
                ed.putBoolean(OPTIONS_BOOT_ACTIVATE, isChecked);
                ed.commit();
                if(isChecked) {
                    EnableBootIntentReceiver();
                } else {
                    DisableBootIntentReceiver();
                }
                break;
            case R.id.switchEnableAlarm:
                ed.putBoolean(OPTIONS_ALARM_ACTIVATE, isChecked);
                ed.commit();
                Context context = this.getApplicationContext();
                if(isChecked) {
                    AlarmManagerReceiver.SetAlarm(context);
                } else {
                    AlarmManagerReceiver.CancelAlarm(context);
                }
                break;
            default:
                break;
        }

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
        sPref = getPreferences(MODE_PRIVATE);

        checked = sPref.getBoolean(OPTIONS_ALARM_ACTIVATE, false);
        sw = (Switch)findViewById(R.id.switchEnableAlarm);
        sw.setChecked(checked);

        checked = sPref.getBoolean(OPTIONS_BOOT_ACTIVATE, false);
        sw = (Switch)findViewById(R.id.switchEnableBoot);
        sw.setChecked(checked);
    }
}
