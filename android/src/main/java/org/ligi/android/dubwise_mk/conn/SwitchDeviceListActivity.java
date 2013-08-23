/**************************************************************************
 *
 * Activity to switch the Device
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

import android.R;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.ligi.android.dubwise_mk.BaseListActivity;
import org.ligi.android.dubwise_mk.app.App;

public class SwitchDeviceListActivity extends BaseListActivity {

    private String[] menu_items = new String[]{"to Navi", "to FC", "to MK3MAG"};
    private int[] menu_actions = new int[]{ACTIONID_SWITCH_NAVI, ACTIONID_SWITCH_FC, ACTIONID_SWITCH_MK3MAG};

    public final static int ACTIONID_SWITCH_NAVI = 0;
    public final static int ACTIONID_SWITCH_FC = 1;
    public final static int ACTIONID_SWITCH_MK3MAG = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListAdapter adapter = new ArrayAdapter<String>(this,R.layout.simple_list_item_1, menu_items);
        this.setListAdapter(adapter);
    }

    public void quit() {
        this.setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, menu_items));
        // setContentView(this);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        try {

            switch (menu_actions[position]) {

                case ACTIONID_SWITCH_NAVI:
                    App.getMK().switch_to_navi();
                    break;

                case ACTIONID_SWITCH_FC:
                    App.getMK().switch_to_fc();
                    break;

                case ACTIONID_SWITCH_MK3MAG:
                    App.getMK().switch_to_mk3mag();
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        finish();
    }

}
