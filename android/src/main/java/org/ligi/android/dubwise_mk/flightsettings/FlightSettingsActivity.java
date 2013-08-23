/**************************************************************************
 *
 * Startup Activity for the flightsettings
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
 *  Organisations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

package org.ligi.android.dubwise_mk.flightsettings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.ligi.android.dubwise_mk.BaseListActivity;
import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.androidhelper.helpers.dialog.ActivityFinishingOnClickListener;
import org.ligi.tracedroid.logging.Log;
import org.ligi.ufo.MKCommunicator;
import org.ligi.ufo.MKParamsParser;

public class FlightSettingsActivity extends BaseListActivity implements Runnable {

    private final static int DIALOG_PROGRESS = 1;
    private ProgressDialog progressDialog;
    private MKCommunicator mk;

    private String[] name_strings = new String[MKParamsParser.MAX_PARAMSETS];
    private ArrayAdapter<String> adapter;
    private AlertDialog alert;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mk = MKProvider.getMK();

        for (int i = 0; i < MKParamsParser.MAX_PARAMSETS; i++) {
            name_strings[i] = "-";
        }


        alert = new AlertDialog.Builder(this).create();
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setTitle("Error");

        alert.setButton("OK", new ActivityFinishingOnClickListener(this));

        if (mk.params.last_parsed_paramset != MKParamsParser.MAX_PARAMSETS) {
            showDialog(DIALOG_PROGRESS);
            new Thread(this).start();
        }

        // this.setContentView(R.layout.general_settings);
        // progressDialog.show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_PROGRESS:
                progressDialog = new ProgressDialog(FlightSettingsActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage("Loading Flight Settings ...");
                progressDialog.setCancelable(true);
                progressDialog.setMax(MKParamsParser.MAX_PARAMSETS);
                return progressDialog;
        }
        return null;
    }

    public void run() {
        Looper.prepare();

        mk.user_intent = MKCommunicator.USER_INTENT_PARAMS;
        while (progressDialog.isShowing()) {

            if (mk.params.compatibility != MKParamsParser.Compatibility.COMPATIBLE) {
                progressDialog.dismiss();

                this.runOnUiThread(new Runnable() {

                    public void run() {
                        String msg = "Incompatible Params (Datarevision " + mk.params.params_version + ")";
                        switch (mk.params.compatibility) {
                            case TOO_OLD:
                                msg += " Please Update your FC!";
                                break;
                            case TOO_NEW:
                                msg += " Please Update DUBwise!";
                                break;
                            case INCOMPATIBLE:
                                msg += " You have a strange version!";
                                break;
                        }

                        alert.setMessage(msg);
                        alert.show();
                    }
                });
            } else if ((mk.params.allParamsetsKnown())) {
                progressDialog.dismiss();

                for (int i = 0; i < MKParamsParser.MAX_PARAMSETS; i++) {
                    name_strings[i] = mk.params.getParamName(i);

					/*if (i==mk.params.act_paramset)
                        menu_items[i]+="(Active)"; */
                }


                this.runOnUiThread(new Runnable() {

                    public void run() {
                        adapter = new ArrayAdapter<String>(FlightSettingsActivity.this,
                                android.R.layout.simple_list_item_1, name_strings);

                        FlightSettingsActivity.this.setListAdapter(adapter);
                    }
                });

            } else {
                progressDialog.setProgress((mk.params.last_parsed_paramset + 1));

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // sleeping is not that important that we should throw something ;-)
                }
                Log.i(" setting last:" + mk.params.last_parsed_paramset);
                Log.i(" settings act:" + mk.params.act_paramset);
            }
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // select the one touched
        MKProvider.getMK().params.act_paramset = position;
        MKProvider.getMK().params.update_backup(position);

        // Start topic list Activity
        startActivity(new Intent(this, FlightSettingsTopicListActivity.class));
    }

}
