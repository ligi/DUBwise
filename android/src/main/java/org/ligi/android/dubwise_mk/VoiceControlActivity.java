/**************************************************************************
 *
 * Activity to show Device-Details
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

package org.ligi.android.dubwise_mk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import org.ligi.tracedroid.logging.Log;

import java.util.ArrayList;

/**
 * some testing around voice control, but stalled until found something that works offline
 */
public class VoiceControlActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    /*
		ArrayList<String> potentialResults = new ArrayList<String>();
		potentialResults.add("open map");
		potentialResults.add("open cockpit");
		potentialResults.add("enable altitude hold");
		potentialResults.add("enable position hold");
		potentialResults.add("enable comming home");

		 // Create Intent
		 Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
		                 new Intent("android.speech.action.RECOGNIZE_SPEECH");  
		 // Settings
		 intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
		 intent.putExtra("android.speech.extra.PROMPT", "Speak now");
		 intent.putExtra("android.speech.extra.RESULTS", potentialResults);
		
		 
		 startActivityForResult(intent,0);
		 */

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Intent intent = new Intent("android.speech.RecognizerIntent.RECOGNIZE_SPEECH");
            startActivityForResult(intent, 0);
        } catch (Exception e) {
            new AlertDialog.Builder(this).setTitle("Problem").setMessage("cannot find RecognizerIntent on your Android Phone.").show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // (())  ...code for checking resultCode and data intent...
        if ((data == null) || (data.getExtras() == null))
            return;
        ArrayList<String> results = data.getExtras().getStringArrayList("results");

        for (String result : results) {
            Log.i("voice result " + result);
        }
    }
}
