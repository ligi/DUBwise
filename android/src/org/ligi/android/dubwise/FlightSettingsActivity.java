package org.ligi.android.dubwise;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import android.util.Log;

import android.net.Uri;

import android.widget.ArrayAdapter;

//import com.google.android.maps.MapView;

import android.content.SharedPreferences;

import org.ligi.ufo.MKCommunicator;
import org.ligi.ufo.MKParamsParser;

public class FlightSettingsActivity extends Activity implements Runnable {

	String[] menu_items;
	
	private final static int DIALOG_PROGRESS=1;
	
	ProgressDialog progressDialog;
	MKCommunicator mk; 
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		ActivityCalls.beforeContent(this);
		
		mk=MKProvider.getMK();
		
		if (mk.params.last_parsed_paramset!=MKParamsParser.MAX_PARAMSETS)
				{
					showDialog(DIALOG_PROGRESS);
					new Thread(this).start();
				}
				
		
		//this.setContentView(R.layout.general_settings);
//		progressDialog.show();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
	}
	  @Override
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case DIALOG_PROGRESS:
	    		
	    		progressDialog = new ProgressDialog(FlightSettingsActivity.this);
	    		
	    		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    		progressDialog.setMessage("Loading Flight Settings ...");
	    		progressDialog.setCancelable(true);
	    		progressDialog.setMax(MKParamsParser.MAX_PARAMSETS);
	
	    		return progressDialog;
	        	
	        }
	        return null;
	  }

	@Override
	public void run() {
		
		mk.user_intent = MKCommunicator.USER_INTENT_PARAMS;
		while(progressDialog.isShowing()) {
			
			if (mk.params.last_parsed_paramset==MKParamsParser.MAX_PARAMSETS)
				progressDialog.dismiss();
			else {
				progressDialog.setProgress(mk.params.last_parsed_paramset);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// sleeping is not that important that we should throw something ;-)
				}
				System.out.println(" setting last:" +mk.params.last_parsed_paramset );
			}
		}
		
	}
}
