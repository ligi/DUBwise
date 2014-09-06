/**************************************************************************
 *
 * Activity to show a Connection Details
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

import org.ligi.android.dubwise_mk.app.App;
import org.ligi.android.io.BluetoothCommunicationAdapter;
import org.ligi.axt.base_activities.RefreshingStringBaseListActivity;
import org.ligi.ufo.MKCommunicator;

public class ConnectionDetails extends RefreshingStringBaseListActivity {

    public String getStringByPosition(int pos) {
        MKCommunicator mk = App.getMK(); // often used here

        try {
            switch (pos) {
                case 0:
                    return "Time: " + mk.getConnectionTime() + " s";
                case 1:
                    return "Bytes in:" + mk.stats.bytes_in;
                case 2:
                    return "Bytes out:" + mk.stats.bytes_out;
                case 3:
                    return "Debug Data: " + mk.stats.debug_data_count + "/" + mk.stats.debug_data_request_count;
                case 4:
                    return "Stick: " + mk.stats.stick_data_count + "/" + mk.stats.stick_data_request_count;
                case 5:
                    return "Analog Names: " + mk.stats.debug_names_count + "/" + mk.stats.debug_name_request_count;
                case 6:
                    return "LCD Data: " + mk.stats.lcd_data_count + "/" + mk.stats.lcd_data_request_count;
                case 7:
                    return "ExternalControl: " + mk.stats.external_control_confirm_frame_count + "/" + mk.stats.external_control_request_count;
                case 8:
                    return "Version: " + mk.stats.version_data_count + "/" + mk.stats.version_data_request_count;
                case 9:
                    return "OSD: " + mk.stats.navi_data_count;
                case 10:
                    return "Angles: " + mk.stats.angle_data_count;
                case 11:
                    return "Motortest: " + mk.stats.motortest_request_count;
                case 12:
                    return "Params: " + mk.stats.params_data_count + "/" + mk.stats.params_data_request_count;
                case 13:
                    return "3D Data: " + mk.stats.threeD_data_count;
                case 14:
                    return "Other: " + mk.stats.other_data_count;
                case 15:
                    if (mk.getCommunicationAdapter() instanceof BluetoothCommunicationAdapter)
                        return "BTRssi" + ((BluetoothCommunicationAdapter) (mk.getCommunicationAdapter())).getRSSI();
                    else
                        return "NO BT RSSI";
            }
        } /*switch*/ catch (Exception e) {
            return "NA"; // ignore the failed one
        }
        ;
        return null;
    }

    @Override
    public int getRefreshSleep() {
        return 100;
    }

}
