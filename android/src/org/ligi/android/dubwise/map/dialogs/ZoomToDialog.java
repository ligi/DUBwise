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
package org.ligi.android.dubwise.map.dialogs;

import org.ligi.android.dubwise.map.DUBwiseMap;
import org.ligi.android.dubwise.map.MapPrefs;

import com.google.android.maps.MapController;

import android.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.LinearLayout.LayoutParams;

/**
 * AlertDialog to show a Dialog to zomm and center a POI
 *  
 * @author ligi
 *
 */
public class ZoomToDialog {

		public final static int ZOOM2OFFSET=10;
		public static void show(final DUBwiseMap ctx) {

			AlertDialog.Builder alert=new AlertDialog.Builder(ctx);

			LinearLayout lin=new LinearLayout(ctx);
			LinearLayout outer_lin=new LinearLayout(ctx);
			LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT,1);
			
			lin.setOrientation(LinearLayout.HORIZONTAL);
			outer_lin.setOrientation(LinearLayout.VERTICAL);

			final SeekBar seek=new SeekBar(ctx);
			seek.setMax(23-ZOOM2OFFSET);
			seek.setProgress(MapPrefs.getZoom2level()-ZOOM2OFFSET);
			seek.setPadding(7, 0, 7, 10);
			seek.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			outer_lin.addView(seek);
			outer_lin.addView(lin);
			
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
			
			alert.setView(outer_lin);
			alert.setTitle("ZoomTo").setMessage("How close and to what POI should I&I zoom?");
			
			final AlertDialog alert_dlg=alert.show();
			final MapController map_controler= ctx.getMapView().getController();
			
			ufoLocation.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					MapPrefs.setZoom2level(seek.getProgress()+ZOOM2OFFSET);
					map_controler.setZoom(MapPrefs.getZoom2level());
					map_controler.setCenter(ctx.getOverlay().getUFOPos());
					alert_dlg.hide();
				}}  );
			
			

			homeLocation.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					MapPrefs.setZoom2level(seek.getProgress()+ZOOM2OFFSET);
					map_controler.setZoom(MapPrefs.getZoom2level());
					map_controler.setCenter(ctx.getOverlay().getHomePos());
					alert_dlg.hide();
				}}  );
			

			myLocation.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					MapPrefs.setZoom2level(seek.getProgress()+ZOOM2OFFSET);
					map_controler.setZoom(MapPrefs.getZoom2level());
					map_controler.setCenter(ctx.getOverlay().getPhonePos());
					alert_dlg.hide();
				}}  );
			
			
		
		}
}
