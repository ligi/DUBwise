/**************************************************************************
 *                                          
 * Activity to Select possible Piloting Activitys
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

import org.ligi.android.dubwise_mk.helper.ActivityCalls;
import org.ligi.android.dubwise_mk.helper.DUBwiseBaseListActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ArrayAdapter;

public class PilotingListActivity extends DUBwiseBaseListActivity {

	private String[] menu_items = new String[] { "via ACC","Via Multitouch"};
	private int[] menu_actions = new int[] { ACTIONID_ACC , ACTIONID_MT };

	public final static int ACTIONID_ACC = 0;
	public final static int ACTIONID_MT = 1;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityCalls.beforeContent(this);
		// menu_items[0]=settings.getString("conn_host","--");
		
	    this.setListAdapter(new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1, menu_items));
	       
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
	}

	public void quit() {
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, menu_items));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		try {
            switch (menu_actions[position]) {
                case ACTIONID_ACC:
                	startActivity( new Intent( this, OrientationPilotingActivity.class ) );
                	break;
                case ACTIONID_MT:
                	startActivity( new Intent( this, MultiTouchPilotingActivity.class ) );
                    break;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Start the activity
	}

}
