package org.ligi.android.dubwise;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import android.util.Log;

import android.net.Uri;

import android.widget.ArrayAdapter;

//import com.google.android.maps.MapView;

import android.content.SharedPreferences;

import org.ligi.ufo.MKCommunicator;


public class DUBwise extends ListActivity {

	// DUBwiseView canvas;
	boolean do_sound;
	boolean fullscreen;
	// MKCommunicator mk;
	String[] menu_items = new String[] { "Settings", "Cockpit" , "Graph", "Connection",
			"Old Interface", "View On Maps", "Flight Settings", "RCData",
			"Motor Test", "About", "Quit" };

	int[] menu_icons = new int[] { android.R.drawable.ic_menu_preferences ,
									android.R.drawable.ic_menu_view,
									android.R.drawable.ic_menu_view,
									android.R.drawable.ic_menu_share,
									android.R.drawable.ic_menu_view,
									android.R.drawable.ic_menu_mapmode,
									android.R.drawable.ic_menu_edit,
									android.R.drawable.ic_menu_view,
									android.R.drawable.ic_menu_rotate,
									android.R.drawable.ic_menu_info_details,
									android.R.drawable.ic_menu_close_clear_cancel
	};

	int[] menu_actions = new int[] { ACTIONID_SETTINGS, ACTIONID_COCKPIT, ACTIONID_GRAPH, ACTIONID_CONN,
			ACTIONID_OLDINTERFACE, ACTIONID_MAPS, ACTIONID_FLIGHTSETTINGS,
			ACTIONID_RCDATA, ACTIONID_MOTORTEST, ACTIONID_ABOUT, ACTIONID_QUIT };

	public final static int ACTIONID_CONN = 0;
	public final static int ACTIONID_MAPS = 1;
	public final static int ACTIONID_ABOUT = 2;
	public final static int ACTIONID_OLDINTERFACE = 3;
	public final static int ACTIONID_FLIGHTSETTINGS = 4;
	public final static int ACTIONID_MOTORTEST = 5;
	public final static int ACTIONID_RCDATA = 6;
	public final static int ACTIONID_SETTINGS = 7;
	public final static int ACTIONID_GRAPH = 8;
	public final static int ACTIONID_COCKPIT = 9;
	public final static int ACTIONID_QUIT = 100;

	SharedPreferences settings;

	// public MapView map;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCalls.beforeContent(this);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		 
		 settings = getSharedPreferences("DUBWISE", 0);
		
//		getWindow().setFeatureInt(featureId, value)
		
		//settings.
		
		// menu_items[0]=settings.getString("conn_host","--");
		MKCommunicator mk=new MKCommunicator();
		/*this.setListAdapter(new ArrayAdapter<String>(this,
		 android.R.layout.simple_list_item_1, menu_items). );
		*/
		this.setListAdapter(new IconicAdapter(this,menu_items,menu_icons));
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

	public void quit() {
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, menu_items));
		// setContentView(this);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		// Get the item that was clicked
		Object o = this.getListAdapter().getItem(position);
		// String keyword = o.toString();

		// Create an VIEW intent
		Intent myIntent = null;

		try {

			switch (menu_actions[position]) {

			case ACTIONID_SETTINGS:
				// setContentView(new ConnectionView(this));

				startActivity(new Intent(this, SettingsActivity.class));
				break;

			case ACTIONID_CONN:
				// setContentView(new ConnectionView(this));

				startActivity(new Intent(this, ConnectionListActivity.class));
				break;
				
			case ACTIONID_COCKPIT:
				// setContentView(new ConnectionView(this));

				startActivity(new Intent(this, CockpitActivity.class));
				break;
				
			case ACTIONID_MOTORTEST:
				// setContentView(new ConnectionView(this));
				startActivity(new Intent(this, MotorTestaActivity.class));

				break;

			case ACTIONID_RCDATA:
				// setContentView(new ConnectionView(this));
				startActivity(new Intent(this, RCDataActivity.class));

				break;
				
			case ACTIONID_GRAPH:
				//setContentView(new GraphView(this));
				startActivity(new Intent(this, GraphActivity.class));
				
				break;
			case ACTIONID_FLIGHTSETTINGS:
				startActivity(new Intent(this, FlightSettingsActivity.class));
				// setContentView(new ConnectionView(this));
				// startActivity(new Intent(this,
				// FlightSettingsActivity.class));

				/*
				 * EditText edit_host=(EditText)findViewById( R.id.edit_host);
				 * edit_host.setKeyListener(new NumberKeyListener(){
				 * 
				 * @Override protected char[] getAcceptedChars() { char[]
				 * numberChars = {'1','2','3'}; return numberChars; } });
				 */

				// edit_host.setText("foobar");
				
				Log.i("DUBwise","UBatt" + MKProvider.getMK().UBatt());
				break;
			case ACTIONID_OLDINTERFACE:
				// mk.connect_to(settings.getString("conn_host","10.0.2.2")+":"+(settings.getString("conn_port","9876")),"unnamed");
				// canvas=new DUBwiseView(this);
				// setContentView(canvas);
				break;
			case ACTIONID_MAPS:
				// setActivity(new DUBwiseMapActivity(this));
				// startActivity(new Intent(this, DUBwiseMapActivity.class));

				// new
				// AlertDialog.Builder(this).setTitle("foo").setMessage("bar").setPositiveButton("OK",null).create().show();

				// showAlert("A funny title", "MessageBoxes rule extremely!",
				// "Hit Me!", false);
				/*
				 * // The intent will open our anddev.org-board and search for
				 * the keyword clicked. myIntent = new
				 * Intent("android.intent.action.DUBWISEMAP",
				 * Uri.parse("http://www.ligi.de/")); startActivity(myIntent);
				 */
				break;

			case ACTIONID_ABOUT:
				startActivity(new Intent("android.intent.action.VIEW", Uri
						.parse("http://www.ligi.de/")));
				break;

			case ACTIONID_QUIT:
				finish();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Start the activity
	
	}

	
	

    class IconicAdapter extends ArrayAdapter { 
        Activity context; 
 
        private String[] text_items;
        private int[] drawable_items;

        IconicAdapter(Activity context,String[] text_items,int[] drawable_items) { 
        	super(context, R.layout.icon_and_text, text_items);
        	this.drawable_items=drawable_items;
        	this.text_items=text_items;
            this.context=context; 
        } 
 
        public View getView(int position, View convertView, ViewGroup parent) { 
        	 LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	 
            //ViewInflate inflater=context.get .getViewInflate(); 
            View row=vi.inflate(R.layout.icon_and_text, null); 
            TextView label=(TextView)row.findViewById(R.id.TextView01); 
 
            label.setText(text_items[position]); 

            
            if ((drawable_items.length>position)&&(drawable_items[position]!=-1)) { 
                ImageView icon=(ImageView)row.findViewById(R.id.ImageView01); 
                icon.setImageResource(drawable_items[position]); 
            }    
 
            return(row); 
        } 
    }
}
