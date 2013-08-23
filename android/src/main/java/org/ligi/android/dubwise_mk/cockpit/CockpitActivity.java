/**************************************************************************
 *
 * Activity to show a Cockpit
 *
 * Project URL:
 *  https://github.com/ligi/DUBwise
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
import org.ligi.android.dubwise_mk.R;
import org.ligi.android.dubwise_mk.app.App;
import org.ligi.ufo.MKCommunicator;

/**
 * Activity tp show a Cockpit ( PFD )
 */
public class CockpitActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getMK().user_intent = MKCommunicator.USER_INTENT_3DDATA;

        setContentView(new CockpitView(this));
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportMenuInflater().inflate(R.menu.cockpit,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_settings:
                startActivity(new Intent(this, CockpitPrefsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}