package org.ligi.android.dubwise;

import java.util.Vector;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import android.util.Log;

import android.net.Uri;

import android.widget.ArrayAdapter;

import android.content.SharedPreferences;

public class DUBwise extends ListActivity {

	// DUBwiseView canvas;
	boolean do_sound;
	boolean fullscreen;
	SharedPreferences settings;

	public final static int ACTIONID_QUIT=1;
		 
	 /** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        ActivityCalls.beforeContent(this);
        
	    Vector<MenuItem> menu_items_vector=new Vector<MenuItem>();
	    
	    menu_items_vector.add(new MenuItem("Connection",android.R.drawable.ic_menu_share,new Intent(this, ConnectionListActivity.class) ) );
	    menu_items_vector.add(new MenuItem("Settings",android.R.drawable.ic_menu_preferences ,new Intent(this, SettingsActivity.class) ) );
	    menu_items_vector.add(new MenuItem("Graph",android.R.drawable.ic_menu_view ,new Intent(this, GraphActivity.class) ) );
	    menu_items_vector.add(new MenuItem("Cockpit",android.R.drawable.ic_menu_view ,new Intent(this, CockpitActivity.class) ) );
	    
	    menu_items_vector.add(new MenuItem("Motor Test",android.R.drawable.ic_menu_rotate ,new Intent(this, MotorTestActivity.class) ) );
        menu_items_vector.add(new MenuItem("RCData",android.R.drawable.ic_menu_view ,new Intent(this, RCDataActivity.class) ) );
        
        menu_items_vector.add(new MenuItem("View on Map",android.R.drawable.ic_menu_mapmode,new Intent(this, RCDataActivity.class) ) );
        
	    menu_items_vector.add(new MenuItem("Flight Settings",android.R.drawable.ic_menu_edit ,new Intent(this, FlightSettingsActivity.class) ) );

	    menu_items_vector.add(new MenuItem("About" , android.R.drawable.ic_menu_info_details, new Intent( "android.intent.action.VIEW", Uri.parse( "http://www.ligi.de/" ))));
	    menu_items_vector.add(new MenuItem("Quit" , android.R.drawable.ic_menu_close_clear_cancel,ACTIONID_QUIT));
	    
		
		 
		 settings = getSharedPreferences("DUBWISE", 0);
		
		 //		getWindow().setFeatureInt(featureId, value)
		
		//settings.
		
		// menu_items[0]=settings.getString("conn_host","--");
		/*this.setListAdapter(new ArrayAdapter<String>(this,
		 android.R.layout.simple_list_item_1, menu_items). );
		*/
		//		menu_items_=(menu_items_vector.toArray());
		this.setListAdapter(new IconicAdapter(this,(menu_items_vector.toArray())));
		Log.d("DUWISE", "create");
		//		this.setTitle("DUBwise Main Menu");
	
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
		//		finish();
		//		startActivity(new Intent(this, DUBwise.class));
		Log.d("DUBWISE", "resume");
		
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void log(String msg) {
		Log.d("DUWISE", msg);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		System.out.println("->!!!!!" + this.getListAdapter().getItem(position));
		
		MenuItem item  = ((MenuItem)(this.getListAdapter().getItem(position) )) ;

		switch (item.action) {
		    case ACTIONID_QUIT:
		        finish();
		        break;
		}
		
		if (item.intent!=null)
		    startActivity(item.intent);
		
	}

	
	class MenuItem {
	    
	    int drawable;
	    String label;
	    Intent intent=null;
	    int action=-1;
	    
	    public MenuItem( String label , int drawable,Intent intent) {
	        this.drawable=drawable;
	        this.label=label;
	        this.intent=intent;
	    }
	    
	    public MenuItem( String label , int drawable,int action) {
            this.drawable=drawable;
            this.label=label;
            this.action=action;
        }
        
	    
	}
	

    class IconicAdapter extends ArrayAdapter { 
        Activity context; 
 
        Object[] items;

        
        IconicAdapter(Activity context,Object[] items) {
        
            
        	super(context, R.layout.icon_and_text, items);
        	this.items=items;
        	
            this.context=context; 
        } 
 
        public View getView(int position, View convertView, ViewGroup parent) { 
        	 LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	 
            //ViewInflate inflater=context.get .getViewInflate(); 
            View row=vi.inflate(R.layout.icon_and_text, null); 
            TextView label=(TextView)row.findViewById(R.id.TextView01); 
 
            label.setText(((MenuItem)items[position]).label); 

            
            if ((items.length>position)&&(((MenuItem)items[position]).drawable!=-1)) { 
                ImageView icon=(ImageView)row.findViewById(R.id.ImageView01); 
                icon.setImageResource(((MenuItem)items[position]).drawable ); 
            }    
 
            return(row); 
        } 
    }
}
