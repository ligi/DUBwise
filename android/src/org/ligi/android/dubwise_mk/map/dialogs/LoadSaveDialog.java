/**************************************************************************                                         
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
package org.ligi.android.dubwise_mk.map.dialogs;

import org.ligi.android.dubwise_mk.map.DUBwiseMap;
import org.ligi.android.dubwise_mk.map.GPXHelper;
import org.ligi.android.dubwise_mk.map.GPXListActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;


/**
 * AlertDialog to show a dialog to allow loading and saving GPX files
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class LoadSaveDialog {

		public static void show(final DUBwiseMap ctx) {

			AlertDialog.Builder alert=new AlertDialog.Builder(ctx);

			LinearLayout lin=new LinearLayout(ctx);
			LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT,1);
			
			lin.setOrientation(LinearLayout.HORIZONTAL);
						
			Button loadButton=new Button(ctx);
			loadButton.setText("Load");
			loadButton.setLayoutParams(lp);
			
			lin.addView(loadButton);

			Button saveButton=new Button(ctx);
			saveButton.setText("Save");
			
			saveButton.setLayoutParams(lp);
			lin.addView(saveButton);
			
			alert.setView(lin);
			alert.setTitle("Persist Direction").setMessage("");
			
			final AlertDialog alert_dlg=alert.show();

			loadButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					ctx.startActivity(new Intent(ctx,GPXListActivity.class));
					alert_dlg.hide();
				}}  );

			saveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					GPXHelper.show_save_dlg(ctx);		
					alert_dlg.hide();
				}}  );
			
		}
}
