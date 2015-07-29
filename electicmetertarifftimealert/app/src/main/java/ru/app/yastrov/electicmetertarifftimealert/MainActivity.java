package ru.app.yastrov.electicmetertarifftimealert;

import android.app.Activity;
import android.app.ProgressDialog;
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
    }
}
