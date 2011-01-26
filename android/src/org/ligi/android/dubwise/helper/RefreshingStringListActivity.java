/**************************************************************************
 *                                          
 * Activity to show a changing strings in a list
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

package org.ligi.android.dubwise.helper;

import org.ligi.android.dubwise.R;
import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.android.dubwise.helper.DUBwiseBaseListActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public abstract class RefreshingStringListActivity extends DUBwiseBaseListActivity implements Runnable{

	private myArrayAdapter adapter;
	private boolean running=true;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityCalls.beforeContent(this);
		adapter=new myArrayAdapter(this);
	    this.setListAdapter(adapter);
	
	    new Thread(this).start();
	}


	public abstract String getStringByPosition(int pos);
	
	class myArrayAdapter extends ArrayAdapter<Object> { 
		 
		private Activity context; 
        
        public myArrayAdapter(Activity context) {
        	super(context, R.layout.listitem_w_text);
        	
            this.context=context;
            int id=0;
            while (getStringByPosition(id++)!=null)
            	this.add(new Object());
            
        } 
 
        public View getView(int position, View convertView, ViewGroup parent) { 
        	LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	 
            View row=vi.inflate(R.layout.listitem_w_text, null); 
            TextView label=(TextView)row.findViewById(R.id.Label); 
            label.setText(getStringByPosition(position));
            return(row); 
        }

    }

	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		running=false;
	}
	 
	final Handler mHandler = new Handler();
	// Create runnable for posting
	final Runnable mUpdateResults = new Runnable() {
		public void run() {
	    	   adapter.notifyDataSetChanged();
		}
	};

	public int getRefreshSleep() {
		return 100;
	}
	
	@Override
	public void run() {
		while (running) {
			mHandler.post(mUpdateResults);
			try {
			Thread.sleep(getRefreshSleep());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
	}

}
