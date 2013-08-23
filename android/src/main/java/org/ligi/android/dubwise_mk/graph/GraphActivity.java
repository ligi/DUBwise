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
import org.ligi.android.dubwise_mk.app.App;
import org.ligi.tracedroid.logging.Log;

public class GraphActivity extends BaseActivity implements OnTouchListener {

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

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_pause:
                App.getMK().freeze_debug_buff = !App.getMK().freeze_debug_buff;
                break;

            case R.id.menu_settings:
                //quit();
                startActivity(new Intent(this, GraphSettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onTouch(View v, MotionEvent event) {
        Log.i("touch graph" + event.getAction() + " " + MotionEvent.ACTION_UP);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            App.getMK().freeze_debug_buff = !App.getMK().freeze_debug_buff;
        }
        return true;
    }

}