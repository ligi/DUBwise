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

import android.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * AlertDialog to show a 
 *  
 * @author ligi
 *
 */
public class AddWPDialog {

		public static void show(final DUBwiseMap ctx) {

			AlertDialog.Builder alert=new AlertDialog.Builder(ctx);

			LinearLayout lin=new LinearLayout(ctx);
			LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT,1);
			
			lin.setOrientation(LinearLayout.HORIZONTAL);
			ImageButton myLocation=new ImageButton(ctx);
			
			myLocation.setImageBitmap(ctx.getOverlay().getPhoneIcon());
			myLocation.setLayoutParams(lp);
			myLocation.setEnabled(ctx.getOverlay().hasPhonePos());
			lin.addView(myLocation);
			ImageButton ufoLocation=new ImageButton(ctx);
			ufoLocation.setImageBitmap(ctx.getOverlay().getKopterIcon());
			ufoLocation.setLayoutParams(lp);
			
			lin.addView(ufoLocation);
			ImageButton homeLocation=new ImageButton(ctx);
			homeLocation.setImageBitmap(ctx.getOverlay().getHomeIcon());
			
			homeLocation.setLayoutParams(lp);
			lin.addView(homeLocation);
			
			alert.setView(lin);
			alert.setTitle("Place WayPoint").setMessage("Where should I place the Waypoint?");
			
			final AlertDialog alert_dlg=alert.show();

			ufoLocation.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					ctx.getOverlay().ufopos2wp();
					alert_dlg.hide();
				}}  );
			
			

			homeLocation.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					ctx.getOverlay().homepos2wp();
					alert_dlg.hide();
				}}  );
			

			myLocation.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					ctx.getOverlay().phonepos2wp();
					alert_dlg.hide();
				}}  );
			
			
		
		}
}
