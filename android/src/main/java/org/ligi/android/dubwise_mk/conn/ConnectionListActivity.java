/**************************************************************************
 *
 * Activity to show menu with possible connection types and options
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

package org.ligi.android.dubwise_mk.conn;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.ligi.android.common.intents.IntentHelper;
import org.ligi.android.dubwise_mk.BaseListActivity;
import org.ligi.android.dubwise_mk.helper.ActivityCalls;
import org.ligi.android.dubwise_mk.helper.DUBwiseBaseListActivity;
import org.ligi.android.dubwise_mk.simulation.AndroidAttitudeProvider;
import org.ligi.android.io.BluetoothCommunicationAdapter;
import org.ligi.androidhelper.helpers.dialog.DialogDiscardingOnClickListener;
import org.ligi.tracedroid.logging.Log;
import org.ligi.ufo.simulation.SimulatedMKCommunicationAdapter;

public class ConnectionListActivity extends BaseListActivity {
    private String[] menu_items = new String[]{"Simulate a connection", "Connect via bluetooth", "Connect via TCP/IP", "Disconnect", "Reconnect", "Switch device", "Connection details"};
    private int[] menu_actions = new int[]{ACTIONID_SIMULATED, ACTIONID_BT, ACTIONID_TCP, ACTIONID_DISCONN, ACTIONID_RECONNECT, ACTIONID_SWITCH, ACTIONID_CONDETAILS};

    public final static int ACTIONID_SIMULATED = 0;
    public final static int ACTIONID_BT = 1;
    public final static int ACTIONID_TCP = 2;
    public final static int ACTIONID_DISCONN = 3;
    public final static int ACTIONID_SWITCH = 4;
    public final static int ACTIONID_CONDETAILS = 5;
    public final static int ACTIONID_RECONNECT = 6;
    private static final int INTENT_REQUEST_CODE_BT_CONN = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCalls.beforeContent(this);
        this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu_items));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityCalls.afterContent(this);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        try {
            switch (menu_actions[position]) {
                case ACTIONID_SIMULATED:
                    SimulatedMKCommunicationAdapter sim = new SimulatedMKCommunicationAdapter();
                    sim.setAttitudeProvider(new AndroidAttitudeProvider(this));
                    MKProvider.getMK().setCommunicationAdapter(sim);
                    finish();
                    break;
                case ACTIONID_BT:
                    if (BluetoothAdapter.getDefaultAdapter() == null)
                        new AlertDialog.Builder(this)
                                .setMessage("Bluetooth is not available on this Device!")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("OK", new DialogDiscardingOnClickListener())
                                .setTitle("BT Error")
                                .show();
                    else {

                        final String picker_package = "org.ligi.android.bluetooth.device_picker";
                        PackageInfo info = null;
                        try {
                            android.content.pm.PackageManager mPm = getPackageManager();  // 1
                            info = mPm.getPackageInfo(picker_package, 0);  // 2,3
                        } catch (Exception e) {

                        }
                        if (info == null) {
                            new AlertDialog.Builder(this)
                                    .setMessage("Please install the Bluetooth Device Picker ")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("Install", new OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            IntentHelper.goToMarketPackage(ConnectionListActivity.this, picker_package);
                                            dialog.dismiss();
                                        }

                                    })
                                    .setNegativeButton(android.R.string.cancel, new DialogDiscardingOnClickListener())
                                    .setTitle("Missing APP")
                                    .show();
                        } else {
                            IntentHelper.action4result(this, "PICK_BLUETOOTH_DEVICE", INTENT_REQUEST_CODE_BT_CONN);
                        }

                    }
                    break;
                case ACTIONID_TCP:
                    startActivity(new Intent(this, ConnectViaTCPActivity.class));
                    break;
                case ACTIONID_DISCONN:

                    MKProvider.getMK().close_connections(true);

                    break;

                case ACTIONID_SWITCH:
                    startActivity(new Intent(this, SwitchDeviceListActivity.class));
                    break;

                case ACTIONID_CONDETAILS:
                    startActivity(new Intent(this, ConnectionDetails.class));
                    break;

                case ACTIONID_RECONNECT:
                    MKProvider.getMK().close_connections(false);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        ActivityCalls.onDestroy(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENT_REQUEST_CODE_BT_CONN:
                if (resultCode == Activity.RESULT_OK) {
                    String addr = data.getStringExtra("ADDR");
                    String friendly_name = data.getStringExtra("FRIENDLYNAME");
                    Log.i("connecting to bt addr:" + addr);


                    BluetoothCommunicationAdapter bt_com = new BluetoothCommunicationAdapter(addr);
                    Log.i("connecting got adapter");

                    MKProvider.getMK().setCommunicationAdapter(bt_com);


                    Log.i("connecting to " + friendly_name + "(" + addr + ")");
                    Log.i(" force disconn " + MKProvider.getMK().force_disconnect);
                    MKProvider.getMK().connect_to("btspp://" + addr, friendly_name);
                    Log.i("finishing BluetoothDeviceListActivity");


                    ConnectionStatusAlertDialog.show(this);
                }
                break;

            default:
                Log.w("unknown code in onActivityResult code=" + requestCode);
        }

    }


}
