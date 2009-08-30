
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

import org.ligi.ufo.*;

public class MotorTestActivity extends Activity
 implements SeekBar.OnSeekBarChangeListener 
{

    SeekBar seek_right,seek_left,seek_front,seek_back,seek_all;
    CheckBox full_speed;
    boolean toasted=false;

    AndroidMKCommunicator mk;
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   

	mk= new AndroidMKCommunicator(this);
	
	setContentView(R.layout.motortest);

	seek_right=(SeekBar)findViewById( R.id.seek_right);
	seek_left=(SeekBar)findViewById( R.id.seek_left);
	seek_front=(SeekBar)findViewById( R.id.seek_front);
	seek_back=(SeekBar)findViewById( R.id.seek_back);
	seek_all=(SeekBar)findViewById( R.id.seek_all);
	full_speed=(CheckBox)findViewById( R.id.check_fullspeed);
	seek_right.setOnSeekBarChangeListener(this);
	seek_left.setOnSeekBarChangeListener(this);
	seek_front.setOnSeekBarChangeListener(this);
	seek_back.setOnSeekBarChangeListener(this);
	seek_all.setOnSeekBarChangeListener(this);
	//	toast=Toast;
	toast=toast.makeText(this, "Value too Dangerous - Clipping! Activate 'Allow Full Speed' to Override" + mk.stats.debug_data_count,Toast.LENGTH_LONG ); 
    }
   @Override
    protected void onDestroy()
    {
	mk.close_connections(true);
	mk=null;
	super.onDestroy();

    }

    Toast toast;
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
	//        mProgressText.setText(progress + " " + 
	//      getString(R.string.seekbar_from_touch) + "=" + fromTouch);
	if (( progress>50)&&(!full_speed.isChecked()))
	    {
		seekBar.setProgress(50);
		progress=50;
		toast.show ();
	    }
	else
	    {
		toast.cancel();
	    }
	if (seekBar==seek_all)
	    {
	    seek_right.setProgress(progress);
	    seek_left.setProgress(progress);
	    seek_front.setProgress(progress);
	    seek_back.setProgress(progress);
	    }


	int[] params=new int[4];
	params[0] = seek_right.getProgress();
	params[1] = seek_left.getProgress();
	params[2] = seek_front.getProgress();
	params[3] = seek_back.getProgress();


	params[3] = seek_right.getProgress();
	params[2] = seek_left.getProgress();
	params[0] = seek_front.getProgress();
	params[1] = seek_back.getProgress();

	mk.motor_test(params);
    }


    public void onStartTrackingTouch(SeekBar seekBar) {
        //mTrackingText.setText(getString(R.string.seekbar_tracking_on));
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
	//        mTrackingText.setText(getString(R.string.seekbar_tracking_off));
    }

}
