/**************************************************************************
 *
 * Activity to show Analog Values
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
import android.os.Handler;
import android.widget.ArrayAdapter;

import org.ligi.android.dubwise_mk.app.App;
import org.ligi.ufo.MKCommunicator;
import org.ligi.ufo.MKDebugData;

public class AnalogValuesActivity extends BaseListActivity implements Runnable {

    private String[] menu_items;
    private ArrayAdapter<String> adapter;
    private boolean running = true;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set user intent to rawdebug so that analog values get resolved
        App.getMK().user_intent = MKCommunicator.USER_INTENT_RAWDEBUG;
        menu_items = new String[MKDebugData.MAX_VALUES];
        refresh_values();

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, menu_items);


        this.setListAdapter(adapter);
        new Thread(this).start();
    }


    private void refresh_values() {
        for (int i = 0; i < MKDebugData.MAX_VALUES; i++)
            menu_items[i] = App.getMK().debug_data.names[i] + " " + App.getMK().debug_data.analog[i];
    }

    @Override
    protected void onDestroy() {
        running = false;
        super.onDestroy();
    }

    final Handler mHandler = new Handler();

    // Create runnable for posting
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void run() {
        while (running) {
            refresh_values();
            mHandler.post(mUpdateResults);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
