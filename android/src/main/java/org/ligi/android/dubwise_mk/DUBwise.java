/**************************************************************************
 *
 * Main Menu ( & startup ) Activity for DUBwise 
 *
 * Author:  Marcus -LiGi- Bueschleb   
 *
 * Project URL:
 *  http://mikrokopter.de/ucwiki/en/DUBwise
 *
 * License:
 *  http://creativecommons.org/licenses/by-nc-sa/2.0/de/ 
 *  (Creative Commons / Non Commercial / Share Alike)
 *  Additionally to the Creative Commons terms it is not allowed
 *  to use this project in _any_ violent manner! 
 *  This explicitly includes that lethal Weapon owning "People" and 
 *  Organizations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

package org.ligi.android.dubwise_mk;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.ligi.android.dubwise_mk.app.ApplicationContext;
import org.ligi.android.dubwise_mk.balance.BalanceActivity;
import org.ligi.android.dubwise_mk.cockpit.CockpitActivity;
import org.ligi.android.dubwise_mk.cockpit.VarioSound;
import org.ligi.android.dubwise_mk.conn.ConnectionListActivity;
import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.android.dubwise_mk.flightsettings.FlightSettingsActivity;
import org.ligi.android.dubwise_mk.graph.GraphActivity;
import org.ligi.android.dubwise_mk.helper.ActivityCalls;
import org.ligi.android.dubwise_mk.helper.DUBwiseStringHelper;
import org.ligi.android.dubwise_mk.helper.IconicMenuItem;
import org.ligi.android.dubwise_mk.lcd.LCDActivity;
import org.ligi.android.dubwise_mk.piloting.PilotingListActivity;
import org.ligi.androidhelper.AndroidHelper;
import org.ligi.tracedroid.TraceDroid;
import org.ligi.tracedroid.logging.Log;
import org.ligi.tracedroid.sending.TraceDroidEmailSender;
import org.ligi.ufo.DUBwiseNotificationListenerInterface;
import org.ligi.ufo.MKCommunicator;
import org.ligi.ufo.logging.NotLogger;
import java.util.ArrayList;
import java.util.List;

public class DUBwise extends ActionBarActivity implements DUBwiseNotificationListenerInterface, Runnable {

    private VarioSound vs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("TableLen" + DUBwiseStringHelper.table.length);
        TraceDroid.init(this);
        Log.setTAG("DUBwise"); // It's all about DUBwise from here ;-)

        TraceDroidEmailSender.sendStackTraces("ligi@ligi.de", this);

        ActivityCalls.beforeContent(this);

        vs = new VarioSound((ApplicationContext) this.getApplicationContext());

        AndroidHelper.at(this).startActivityForClass(MixerEditActivity.class);
    }


    @Override
    public void onBackPressed() {

        LinearLayout lin = new LinearLayout(this);
        lin.setOrientation(LinearLayout.VERTICAL);

        Button kidding_btn = new Button(this);
        kidding_btn.setText("No - just kidding");

        kidding_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AlertDialog) view.getTag()).hide();
            }
        });

        lin.addView(kidding_btn);

        Button yes_btn = new Button(this);
        yes_btn.setText("Yes");
        lin.addView(yes_btn);

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCalls.shutdownDUBwise();
                finish();
            }
        });


        Button rmbt_btn = new Button(this);
        rmbt_btn.setText("Yes and disable BT");

        rmbt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCalls.shutdownDUBwise();
                BluetoothAdapter.getDefaultAdapter().disable();
                finish();
            }
        });

        lin.addView(rmbt_btn);

        Button stay_awake_btn = new Button(this);
        stay_awake_btn.setText("Yes - but stay awake");
        lin.addView(stay_awake_btn);

        stay_awake_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        kidding_btn.setTag((new AlertDialog.Builder(this)).setView(lin).setTitle("Exit DUBwise " + getVersionCode() + " ?").show());

    }

    private String getVersionCode() {
        try {
            return "v" + getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return "v?";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityCalls.afterContent(this);
        updateMKLogging();
        Log.d("onResume DUBwise.java" + this);
        //refresh_list();
        MKProvider.getMK().addNotificationListener(this);
    }

    /**
     * enable/disable MK Protocol logging depending on user settings
     */
    public void updateMKLogging() {
        if (DUBwisePrefs.isVerboseLoggingEnabled())
            MKProvider.getMK().setLoggingInterface(new AndroidLogger());
        else
            MKProvider.getMK().setLoggingInterface(new NotLogger());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        ActivityCalls.onDestroy(this);
        super.onDestroy();
    }

    /*
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        IconicMenuItem item = ((IconicMenuItem) (this.getListAdapter().getItem(position)));

        if (item.intent != null) {
            startActivity(item.intent);
        }

    }
    */

    public void processNotification(byte notification) {
        switch (notification) {
            case NOTIFY_CONNECTION_CHANGED:
                this.runOnUiThread(this);
                break;
        }
    }


    @Override
    public void run() {
        //refresh_list();
    }
}
