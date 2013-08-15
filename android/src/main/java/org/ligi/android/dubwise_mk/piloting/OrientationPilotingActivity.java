/**************************************************************************
 *
 * Activity to Pilot the UFO via ACC / Orientation sensor
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

package org.ligi.android.dubwise_mk.piloting;

import android.app.Activity;
import android.os.Bundle;

import org.ligi.android.dubwise_mk.helper.ActivityCalls;

public class OrientationPilotingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCalls.beforeContent(this);

        setContentView(new OrientationPilotingView(this));

    }

    @Override
    protected void onResume() {
        ActivityCalls.afterContent(this);
        super.onResume();
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
}