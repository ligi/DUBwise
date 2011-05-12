package org.ligi.android.dubwise.map;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.ligi.tracedroid.logging.Log;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

public class GPXHelper {

	public static void show_save_dlg(Context ctx) {
		final EditText input = new EditText(ctx);   
		input.setText("default");

		new AlertDialog.Builder(ctx).setTitle("Save GPX").setMessage("How should the file I will write to " +MapPrefs.getGPXPath() + " be named?").setView(input)
		.setPositiveButton("OK" , new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			String value = input.getText().toString(); 
				
			File f = new File(MapPrefs.getGPXPath());
			
			if (!f.isDirectory())
				f.mkdirs();
			
			try {
				f=new File(MapPrefs.getGPXPath() + "/"+value+".gpx");
				f.createNewFile();
				
				FileWriter gpx_writer = new FileWriter(f);
				
				BufferedWriter out = new BufferedWriter(gpx_writer);
				
				out.write(FlightPlanProvider.toGPX());
				out.close();
				gpx_writer.close();
			} catch (IOException e) {
				Log.i(""+e);
			}

		}
		}).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		// Do nothing.
		}
		}).show();

	}
}
