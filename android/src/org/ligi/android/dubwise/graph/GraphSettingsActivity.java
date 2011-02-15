/**************************************************************************
 *                                          
 * Settings Activity for the Graph
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

package org.ligi.android.dubwise.graph;

import org.ligi.android.dubwise.R;
import org.ligi.android.dubwise.helper.ActivityCalls;
import android.os.Bundle;
import android.widget.CheckBox;
import android.app.Activity;
import android.content.SharedPreferences;

public class GraphSettingsActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCalls.beforeContent(this);
		
		SharedPreferences settings = getSharedPreferences("DUBWISE", 0);

		setContentView(R.layout.graph_settings);
		
		((CheckBox) findViewById(R.id.LegendCheckBox)).setChecked(settings.getBoolean("do_legend",true));
		((CheckBox) findViewById(R.id.GridCheckBox)).setChecked(settings.getBoolean("do_grid",true));
		System.out.println("do grid" + settings.getBoolean("do_grid",true));

	}

	@Override
	protected void onResume() {
		ActivityCalls.afterContent(this);
		super.onResume();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();

		SharedPreferences settings = getSharedPreferences("DUBWISE", 0);
		SharedPreferences.Editor editor = settings.edit();

		editor.putBoolean("do_grid",
				(((CheckBox) findViewById(R.id.GridCheckBox)).isChecked()));
		editor.putBoolean("do_legend",
				(((CheckBox) findViewById(R.id.LegendCheckBox)).isChecked()));

		editor.commit();
	}

}