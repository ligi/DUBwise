/**************************************************************************
 *
 * Activity to Display the Graph
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

package org.ligi.android.dubwise_mk.graph;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import org.ligi.android.dubwise_mk.BaseActivity;
import org.ligi.android.dubwise_mk.R;
import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.tracedroid.logging.Log;

public class GraphActivity extends BaseActivity implements OnTouchListener {

    public final static int MENU_GRID = 0;
    public final static int MENU_FREEZE = 1;
    public final static int MENU_SETTINGS = 2;

    private GraphView graph_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        graph_view = new GraphView(this);
        graph_view.setOnTouchListener(this);

        setContentView(graph_view);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.graph,menu);

        menu.add(0, MENU_SETTINGS, 0, "Settings").setIcon(android.R.drawable.ic_menu_preferences);

        menu.add(0, MENU_FREEZE, 0, (MKProvider.getMK().freeze_debug_buff ? "unfreeze" : "freeze"))
                .setIcon(android.R.drawable.ic_media_pause);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case MENU_FREEZE:
                MKProvider.getMK().freeze_debug_buff = !MKProvider.getMK().freeze_debug_buff;
                break;

            case MENU_SETTINGS:
                //quit();
                startActivity(new Intent(this, GraphSettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onTouch(View v, MotionEvent event) {
        Log.i("touch graph" + event.getAction() + " " + MotionEvent.ACTION_UP);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            MKProvider.getMK().freeze_debug_buff = !MKProvider.getMK().freeze_debug_buff;
        }
        return true;
    }

}