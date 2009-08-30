
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

public class SettingsActivity extends Activity

{


    CheckBox fullscreen;


    AndroidMKCommunicator mk;
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   

	setContentView(R.layout.settings);


	fullscreen=(CheckBox)findViewById( R.id.check_fullscreen);

    }
   @Override
    protected void onDestroy()
    {

	super.onDestroy();

    }


}
