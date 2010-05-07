package org.ligi.android.dubwise.map.dialogs;

import org.ligi.android.dubwise.map.DUBwiseMap;

import android.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

public class AddWPDialog {

		public static void show(final DUBwiseMap ctx) {

			AlertDialog.Builder alert=new AlertDialog.Builder(ctx);

			TableRow row=new TableRow(ctx);
			ImageButton myLocation=new ImageButton(ctx);
			
			myLocation.setImageBitmap(ctx.getOverlay().getPhoneIcon());
			row.addView(myLocation);
			ImageButton ufoLocation=new ImageButton(ctx);
			ufoLocation.setImageBitmap(ctx.getOverlay().getKopterIcon());
			row.addView(ufoLocation);
			ImageButton homeLocation=new ImageButton(ctx);
			homeLocation.setImageBitmap(ctx.getOverlay().getHomeIcon());
			
			row.addView(homeLocation);
			
			TableLayout table=new TableLayout(ctx);
			table.addView(row);
			table.setColumnStretchable(0, true);
			table.setColumnStretchable(2, true);
			table.setColumnStretchable(1, true);
			
			alert.setView(table);
			alert.setTitle("Place WP").setMessage("Where should I Place the Waypoint?");
			
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
