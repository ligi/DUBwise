package org.ligi.android.dubwise;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.util.Log;
import android.widget.ArrayAdapter;
import org.ligi.ufo.MKCommunicator;
import org.ligi.ufo.MKParamsParser;

public class FlightSettingsActivity extends ListActivity implements Runnable {

	String[] menu_items;
	
	private final static int DIALOG_PROGRESS=1;
	ListActivity this_ref;
	ProgressDialog progressDialog;
	MKCommunicator mk; 
	
	String[] name_strings=new String[MKParamsParser.MAX_PARAMSETS];
	ArrayAdapter<String> adapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		ActivityCalls.beforeContent(this);
		this_ref=this;		
		mk=MKProvider.getMK();
		
		for (int i=0;i<MKParamsParser.MAX_PARAMSETS;i++)
            name_strings[i]="-";
		        
		if (mk.params.last_parsed_paramset!=MKParamsParser.MAX_PARAMSETS)
		{
		    showDialog(DIALOG_PROGRESS);
//		    this.runOnUiThread(this );
                  new Thread(this).start();
           }   

	        // this.setContentView(R.layout.general_settings);
	        // progressDialog.show();
		
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

	public void run() {
		
		mk.user_intent = MKCommunicator.USER_INTENT_PARAMS;
		while(progressDialog.isShowing()) {
			
			if ((mk.params.last_parsed_paramset+1)==MKParamsParser.MAX_PARAMSETS)
				{
			    progressDialog.dismiss();
				
			    for (int i=0;i<MKParamsParser.MAX_PARAMSETS;i++)
                    name_strings[i]=mk.params.getParamName(i );
			    
			    
			    this.runOnUiThread( new Runnable() {
			  
                    public void run() {
                        adapter  =new ArrayAdapter<String>(this_ref,
                                android.R.layout.simple_list_item_1, name_strings);
                        
                        
                        this_ref.setListAdapter(adapter);
                      
                    } } );
			    
			//    adapter.notifyDataSetChanged();
			    //UIThreadUtilities.   .runOnUIThread
				}
			else {
				progressDialog.setProgress((mk.params.last_parsed_paramset+1));
				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// sleeping is not that important that we should throw something ;-)
				}
				System.out.println(" setting last:" +mk.params.last_parsed_paramset );
				System.out.println(" settings act:" +mk.params.act_paramset);
			}
		}
		
	}

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        startActivity( new Intent( this, FlightSettingsTopicListActivity.class ) );
        
        // select the one touched
        MKProvider.getMK().params.act_paramset=position;
        /*
        String res="";
        for (int i =0 ; i<MKProvider.getMK().params.field_bak[position].length;i++)
            res+="" + MKProvider.getMK().params.field_bak[position][i]+",";
        
        Log.d("DUBwise", res);
        */
    }


}
