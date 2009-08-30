
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
import android.widget.*;
import android.content.DialogInterface; 
import android.content.Context.*;
import android.widget.EditText;
import android.text.method.NumberKeyListener;


import com.google.android.maps.MapView;

import  	android.app.AlertDialog.*;
import  	android.app.AlertDialog;

import android.content.SharedPreferences;


import android.view.*;
import android.content.*;
import android.view.ViewGroup.*;

import org.ligi.ufo.*;

public class RCDataActivity extends Activity
    implements DUBwiseDefinitions,Runnable
{



    AndroidMKCommunicator mk;

    ProgressBar[] bars;
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   


	bars=new ProgressBar[10];

	mk= new AndroidMKCommunicator(this);
	mk.user_intent=USER_INTENT_RCDATA;
	setContentView(R.layout.rcdata);

	bars[0]=	(ProgressBar)findViewById( R.id.stick_progress_0);
	bars[1]= 	(ProgressBar)findViewById( R.id.stick_progress_1);
	bars[2]=	(ProgressBar)findViewById( R.id.stick_progress_2);
	bars[3]=	(ProgressBar)findViewById( R.id.stick_progress_3);
	bars[4]=	(ProgressBar)findViewById( R.id.stick_progress_4);
	bars[5]=	(ProgressBar)findViewById( R.id.stick_progress_5);
	bars[6]=	(ProgressBar)findViewById( R.id.stick_progress_6);
	bars[7]=	(ProgressBar)findViewById( R.id.stick_progress_7);
	bars[8]=	(ProgressBar)findViewById( R.id.stick_progress_8);
	bars[9]=	(ProgressBar)findViewById( R.id.stick_progress_9);

	new Thread( this ).start(); // fire up main Thread 	
    }

    public void run()
    {
	while(true)
	    {
		try{
		Thread.sleep(50);
		for (int i=0;i<10;i++)
		    bars[i].setProgress(mk.stick_data.stick[i]+127);		
		}
		catch(Exception e)
		    {

			mk.log(e.toString());
		    }

    }



    }
   @Override
    protected void onDestroy()
    {
	mk.close_connections(true);
	mk=null;
	super.onDestroy();

    }


}
