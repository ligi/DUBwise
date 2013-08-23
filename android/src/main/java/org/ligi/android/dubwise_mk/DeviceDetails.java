/**************************************************************************
 *
 * Activity to show Device-Details
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

package org.ligi.android.dubwise_mk;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import org.ligi.android.dubwise_mk.app.App;
import org.ligi.ufo.MKCommunicator;

public class DeviceDetails extends BaseListActivity {

    private String[] menu_items;
    private ArrayAdapter<String> adapter;
    private final static int VALUE_COUNT = 3;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        menu_items = new String[VALUE_COUNT];
        refresh_values();

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, menu_items);

        setListAdapter(adapter);
    }


    private void refresh_values() {
        MKCommunicator mk = App.getMK();
        menu_items[0] = "Type: " + mk.getExtendedConnectionName();

        menu_items[1] = "Version: " + mk.version.version_str;
        menu_items[2] = "Protocol: " + mk.version.proto_str;

    }
}
