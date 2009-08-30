
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

import android.app.Activity;
import android.os.Bundle;


import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
import android.content.DialogInterface; 
import android.content.Context.*;
import android.widget.EditText;
import android.text.method.NumberKeyListener;


import com.google.android.maps.MapView;

import  	android.app.AlertDialog.*;
import  	android.app.AlertDialog;

import android.content.SharedPreferences;

import org.ligi.ufo.*;

public class DUBwise extends ListActivity
{


    DUBwiseView canvas;
    boolean do_sound;
    boolean fullscreen;
    MKCommunicator mk;
    String[] menu_items = new String[]{"Settings","Connection", "Old Interface" , "View On Maps", "Flight Settings","RCData","Motor Test", "About","Quit"};
    int[] menu_actions= new int[]{ACTIONID_SETTINGS,ACTIONID_CONN , ACTIONID_OLDINTERFACE , ACTIONID_MAPS ,ACTIONID_FLIGHTSETTINGS,ACTIONID_RCDATA,ACTIONID_MOTORTEST, ACTIONID_ABOUT , ACTIONID_QUIT };

    public final static int ACTIONID_CONN=0;
    public final static int ACTIONID_MAPS=1;
    public final static int ACTIONID_ABOUT=2;
    public final static int ACTIONID_OLDINTERFACE=3;
    public final static int ACTIONID_FLIGHTSETTINGS=4;
    public final static int ACTIONID_MOTORTEST=5;
    public final static int ACTIONID_RCDATA=6;
    public final static int ACTIONID_SETTINGS=7;
    public final static int ACTIONID_QUIT=100;

    SharedPreferences settings;
    //    public MapView map;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

	settings = getSharedPreferences("DUBwise", 0);
	//	menu_items[0]=settings.getString("conn_host","--");
	//	mk=new MKCommunicator();	
	this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu_items)); 
    }




    public void log(String msg)
    {
	Log.d("DUWISE",msg);
    }

    public void quit()
    {
	this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu_items)); 
	//	setContentView(this);
    }

@Override
    protected void onListItemClick(ListView l, View v, int position, long id){
     super.onListItemClick(l, v, position, id);
      
     // Get the item that was clicked
     Object o = this.getListAdapter().getItem(position);
     //String keyword = o.toString();

     // Create an VIEW intent
     Intent myIntent = null;

     try {
	 

	 switch(menu_actions[position])
	     {

	     case ACTIONID_SETTINGS:
		 //		 setContentView(new ConnectionView(this));

		 startActivity(new Intent(this, SettingsActivity.class));
		 break;

	     case ACTIONID_CONN:
		 //		 setContentView(new ConnectionView(this));

		 startActivity(new Intent(this, ConnectionActivity.class));
		 break;
	     case ACTIONID_MOTORTEST:
		 //		 setContentView(new ConnectionView(this));
		 startActivity(new Intent(this, MotorTestActivity.class));

		 break;

	     case ACTIONID_RCDATA:
		 //		 setContentView(new ConnectionView(this));
		 startActivity(new Intent(this, RCDataActivity.class));

		 break;
	     case ACTIONID_FLIGHTSETTINGS:
		 //		 setContentView(new ConnectionView(this));
		 startActivity(new Intent(this, FlightSettingsActivity.class));



		 /*		 EditText edit_host=(EditText)findViewById( R.id.edit_host);
		 edit_host.setKeyListener(new NumberKeyListener(){
   @Override
   protected char[] getAcceptedChars() {
      char[] numberChars = {'1','2','3'};
      return numberChars;
   }
   });*/
	

		 //		 edit_host.setText("foobar");
		 break;
	     case ACTIONID_OLDINTERFACE:
		 mk.connect_to(settings.getString("conn_host","10.0.2.2")+":"+(settings.getString("conn_port","9876")),"unnamed");
		 canvas=new DUBwiseView(this);
		 setContentView(canvas);
		 break;
	     case ACTIONID_MAPS:
		 //		 setActivity(new DUBwiseMapActivity(this));
		 startActivity(new Intent(this, DUBwiseMapActivity.class));


		 // new AlertDialog.Builder(this).setTitle("foo").setMessage("bar").setPositiveButton("OK",null).create().show();

		 //		 showAlert("A funny title", "MessageBoxes rule extremely!", "Hit Me!", false);
		 /*
               // The intent will open our anddev.org-board and search for the keyword clicked.
               myIntent = new Intent("android.intent.action.DUBWISEMAP",
                    Uri.parse("http://www.ligi.de/"));
		    startActivity(myIntent);*/
		 break;

	     case ACTIONID_ABOUT:
		 startActivity( new Intent("android.intent.action.VIEW", Uri.parse("http://www.ligi.de/")));
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



}
