
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

public class FlightSettingsActivity extends Activity
					    // extends ListActivity

{

    String[] menu_items = new String[]{"Stick","Navi"};



    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	Intent i=new Intent(this, FlightSettingsSettingSelect.class);
	i.putExtra("items",menu_items);

	startActivityForResult(i,0);
    }
    @Override
	
    protected void onActivityResult(int requestCode,int  resultCode, Intent intent)
    {
	if ( resultCode!=RESULT_CANCELED)
	    
	    {
	Bundle extras = intent.getExtras();
	if (extras!=null)
	    {
		LinearLayout lin = new LinearLayout(this); 
		
		lin.setOrientation(LinearLayout.VERTICAL);
	
		LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		TextView t1 = new TextView(this); 
		
		t1.setText("Here is the first textbox" + extras.getInt("pos")); 
		lin.addView(t1, params);
		
		setContentView(lin); 
	    }
	    }
	else
	    finish();
    }





}
