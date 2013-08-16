/**************************************************************************
 *
 * Activity to show a Cockpit
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

package org.ligi.android.dubwise_mk.cockpit;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import org.ligi.android.dubwise_mk.BaseActivity;
import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.ufo.MKCommunicator;

/**
 * Activity tp show a Cockpit ( PFD )
 */
public class CockpitActivity extends BaseActivity {

    private static final int MENU_SETTINGS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MKProvider.getMK().user_intent = MKCommunicator.USER_INTENT_3DDATA;

        setContentView(new CockpitView(this));
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem settings_menu = menu.add(0, MENU_SETTINGS, 0, "Settings");
        settings_menu.setIcon(android.R.drawable.ic_menu_preferences);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case MENU_SETTINGS:
                startActivity(new Intent(this, CockpitPrefsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}