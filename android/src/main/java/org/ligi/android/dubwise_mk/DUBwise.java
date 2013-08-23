/**************************************************************************
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
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.ligi.android.dubwise_mk.app.App;
import org.ligi.android.dubwise_mk.cockpit.CockpitActivity;
import org.ligi.android.dubwise_mk.cockpit.VarioSound;
import org.ligi.androidhelper.AndroidHelper;
import org.ligi.tracedroid.TraceDroid;
import org.ligi.tracedroid.logging.Log;
import org.ligi.tracedroid.sending.TraceDroidEmailSender;
import org.ligi.ufo.logging.NotLogger;

/**
 * Main Menu ( & startup ) Activity for DUBwise
 */
public class DUBwise extends BaseActivity {

    private VarioSound vs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TraceDroid.init(this);
        Log.setTAG("DUBwise"); // It's all about DUBwise from here ;-)

        TraceDroidEmailSender.sendStackTraces("ligi@ligi.de", this);

        vs = new VarioSound((App) this.getApplicationContext());

        AndroidHelper.at(this).startActivityForClass(CockpitActivity.class);
        finish();
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
                shutdownDUBwise();
                finish();
            }
        });


        Button rmbt_btn = new Button(this);
        rmbt_btn.setText("Yes and disable BT");

        rmbt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shutdownDUBwise();
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
        updateMKLogging();
    }

    /**
     * enable/disable MK Protocol logging depending on user settings
     */
    public void updateMKLogging() {
        if (DUBwisePrefs.isVerboseLoggingEnabled()) {
            App.getMK().setLoggingInterface(new AndroidLogger());
        } else {
            App.getMK().setLoggingInterface(new NotLogger());
        }
    }

}
