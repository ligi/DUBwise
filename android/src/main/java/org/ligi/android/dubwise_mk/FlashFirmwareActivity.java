/**************************************************************************
 *                                          
 * Activity Frontend for the MKFirmwareFlasher to flash Firmwares
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
 *  Organisations (e.g. Army & Police) are not allowed to use this Project!
 *
 **************************************************************************/

package org.ligi.android.dubwise_mk;

import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.android.dubwise_mk.helper.ActivityCalls;
import org.ligi.java.io.CommunicationAdapterInterface;
import org.ligi.tracedroid.logging.Log;
import org.ligi.ufo.MKFirmwareFlasher;
import org.ligi.ufo.MKFirmwareHelper;
import org.ligi.ufo.MKParamsParser;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;

public class FlashFirmwareActivity extends Activity implements Runnable, OnCancelListener {

	private static final int DIALOG_PROGRESS = 0;
	private ProgressDialog progressDialog;
	
	private MKFirmwareFlasher firmware_flasher;
	private CommunicationAdapterInterface comm_passer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		ActivityCalls.beforeContent(this);

		
		// pass the communication-adapter
		comm_passer=MKProvider.getMK().getCommunicationAdapter();
		MKProvider.getMK().setCommunicationAdapter(null);
		firmware_flasher=new MKFirmwareFlasher(comm_passer,MKFirmwareHelper.BOOTLOADER_INTENSION_RESET_PARAMS);
		firmware_flasher.start();
		showDialog(DIALOG_PROGRESS);

		new Thread(this).start();
		//this.runOnUiThread(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
	
	
	}

	@Override
	protected void onDestroy() {
		ActivityCalls.onDestroy(this);
		super.onDestroy();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_PROGRESS:
			
			progressDialog = new ProgressDialog(FlashFirmwareActivity.this);
			
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMessage("Connecting to bootloader");
			progressDialog.setCancelable(true);
			progressDialog.setMax(MKParamsParser.MAX_PARAMSETS);

			progressDialog.setOnCancelListener(this);
	//		progressDialog.setButton("cancel", new OnClickListener {});
			return progressDialog;
		}
		return null;
	  }

	boolean flashing_in_progress=true;
	
	@Override
	public void run() {
		
		while(flashing_in_progress) {
			try {
			
				this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						
						progressDialog.setMessage(firmware_flasher.getCompleteLog());		
					}});
				
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// we dont need no sleep
			}
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {

		if (dialog==progressDialog) {
			firmware_flasher.setCommunicationAdapter(null);
			
			MKProvider.getMK().setCommunicationAdapter(comm_passer);
			flashing_in_progress=false;
			finish();
		}
		else 
			Log.w( "Dialog canceled which i did not have opened");
	}
		
}
