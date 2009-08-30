package org.ligi.android;
import android.os.Bundle; 
import com.google.android.maps.*;

import android.content.Context;
import android.graphics.Paint; 
import android.util.AttributeSet; 
import java.util.Map; 
import android.view.Menu; 
import android.view.MenuItem; 

import android.view.*;
import android.content.SharedPreferences;

public class DUBwiseMapActivity extends MapActivity implements Runnable,MapKey
{

    //MapView mMapView; 
   

    MapView 	map;
    MapController mc ;
 @Override
     public boolean onCreateOptionsMenu(Menu menu) {
          boolean supRetVal = super.onCreateOptionsMenu(menu);

          menu.add(0, 0,0, getString(R.string.map_menu_zoom_in));
          menu.add(0, 1,0, getString(R.string.map_menu_zoom_out));
          menu.add(0, 2,0, getString(R.string.map_menu_toggle_street_satellite));
          menu.add(0, 3,0, R.string.map_menu_back_to_list); 
          return supRetVal;
     } 




 @Override
     public boolean onOptionsItemSelected(MenuItem item){
         switch (item.getItemId()) {
              case 0:
               // Zoom not closer than possible
		   this.mc.setZoom(Math.min(21, map.getZoomLevel() + 1));
		  //		  map.displayZoomControls(true);
                  return true;
              case 1:
               // Zoom not farer than possible
		  this.mc.setZoom(Math.max(1, map.getZoomLevel() - 1));
                  return true;
              case 2:
               // Switch to satellite view
		  map.setSatellite(!map.isSatellite());
                  return true;
              case 3:
               this.finish();
                  return true;
         }
         return false;
     } 


    AndroidMKCommunicator mk;


    public void run()
    {
	/*
	int i=0;
        while(true)
            {
		try { 

		
		this.setTitle("UBATT:no");
		    this.setTitle("UBATT:" + mk.UBatt() + " stats:" + mk.stats.debug_data_count + "--" + i);
		    i++;
		    //		    Thread.sleep(1000);

		    this.setTitle("UBATT2:" + mk.UBatt() + " stats:" + mk.stats.debug_data_count + "--" + i);
	
		}

		catch (Exception e)
		    {
		    		    this.setTitle(e.toString());
}
	    }

*/
    }

   @Override
       public boolean onKeyDown(int keyCode, KeyEvent event)
    {

	this.setTitle("UBATT2:" + mk.UBatt() + " stats:" + mk.stats.version_data_count + "--" );
	return true;
    }

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	map=new MapView(this,MapKey); 
	map.setClickable(true); 


	//////////////////////////////////////////////////////////////////////////////this.overlay = this.map.createOverlayController(); 



	mc = map.getController();
        //mc.setZoom(20);
	setContentView(map);
 

	mk=new AndroidMKCommunicator(this
				);
	

	//	this.setTitle("test");
	//
	new Thread( this ).start(); // fire up main Thread 	
   } 

   @Override
    protected void onDestroy()
    {
	mk.close_connections(true);
	mk=null;
	super.onDestroy();

    }

        @Override
        protected boolean isRouteDisplayed() {
                return false;
        }



}
