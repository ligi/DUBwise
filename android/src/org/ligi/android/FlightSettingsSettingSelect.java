
package org.ligi.android;

import android.app.ListActivity; 
import android.content.Intent; 
import android.content.pm.PackageManager; 
import android.content.pm.ResolveInfo; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.ListView; 
import android.widget.SimpleAdapter;


import android.util.Log;
import android.view.ViewGroup.*;
import android.app.Activity;
import android.os.Bundle;


import android.view.*;
import android.widget.*;


import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.net.Uri; 

import java.net.URISyntaxException; 
import android.widget.ArrayAdapter;


import android.text.method.NumberKeyListener;


import com.google.android.maps.MapView;

import  	android.app.AlertDialog.*;
import  	android.app.AlertDialog;

import android.content.*;

import org.ligi.ufo.*;

public class FlightSettingsSettingSelect  extends ListActivity
{

    //    String[] menu_items = new String[]{"Stick","Navi"};


    @Override
	protected void onCreate(Bundle savedInstanceState) {
      
Bundle extras = getIntent().getExtras();

//	Bundle extras = intent.getExtras();

	String[] menu_items=extras.getStringArray("items");
	super.onCreate(savedInstanceState);
        //setContentView(R.layout.motortest);

	this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu_items)); 
	
	//	setContentView(list_act); 


    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id){
	super.onListItemClick(l, v, position, id);
	
	// Get the item that was clicked
	Object o = this.getListAdapter().getItem(position);
	//String keyword = o.toString();
	

	Intent mIntent = new Intent();
	mIntent.putExtra("pos",position);
	setResult(RESULT_OK, mIntent);
	finish();



	// Create an VIEW intent
	try {
	    switch(position)
		{
		    
		case 0:
		    /*    
		    //		 ListActivity Lis= new ListActivity(this);
		    is_list=false;
		    LinearLayout lin = new LinearLayout(this); 
		    lin.setOrientation(LinearLayout.VERTICAL);
		    
		    LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		    TextView t1 = new TextView(this); 
		    
		    t1.setText("Here is the first textbox"); 
		    lin.addView(t1, params);
		    
		    setContentView(lin); 
		    
		    */
		    break;
		    
		    
		}
	} catch (Exception e) {
	    e.printStackTrace();
	}
	// Start the activity
	
    }


}
