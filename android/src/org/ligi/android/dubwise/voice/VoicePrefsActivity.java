/**************************************************************************
 *                                          
 * Activity to edit the Cockpit Preferences
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

package org.ligi.android.dubwise.voice;


import org.ligi.android.dubwise.TimePreference;
import org.ligi.android.dubwise.helper.ActivityCalls;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;

public class VoicePrefsActivity extends PreferenceActivity implements OnPreferenceChangeListener {

	private CheckBoxPreference voiceEnabledCheckBoxPref;
	private PreferenceCategory valuesToSpeakPrefCat;
	private TimePreference pause_pref;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		VoicePrefs.init(this);
		ActivityCalls.beforeContent(this);		
		super.onCreate(savedInstanceState);
        setPreferenceScreen(createPreferenceHierarchy());
		
    }
	
	
	@Override 
	public void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);		
	}
	
	public String formatedPause() {
		return ""+VoicePrefs.getPauseTimeInMS() +"ms";
	}
    private PreferenceScreen createPreferenceHierarchy() {
        // Root
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
        
        root.setPersistent(true);

        
        // general settings

        PreferenceCategory voceGeneralPrefCat = new PreferenceCategory(this);
        voceGeneralPrefCat.setTitle("general voice settings");
        root.addPreference( voceGeneralPrefCat);

        
        voiceEnabledCheckBoxPref = new CheckBoxPreference(this);
        voiceEnabledCheckBoxPref.setKey(VoicePrefs.KEY_VOICE_ENABLED);
        voiceEnabledCheckBoxPref.setTitle("Voice Enabled");
        voiceEnabledCheckBoxPref.setSummary("DUBwise should speak at all?");
        voiceEnabledCheckBoxPref.setOnPreferenceChangeListener(this);
       	voceGeneralPrefCat.addPreference(voiceEnabledCheckBoxPref);

       	
       	
       	pause_pref=new TimePreference(this,null);
       	pause_pref.setTitle("pause between blocks");
       	pause_pref.setDialogTitle("Pause Time");
       	
       	pause_pref.setDialogMessage("Please enter a time in ms to pause between the blocks:");
       	pause_pref.setDefaultValue(VoicePrefs.getPauseTimeAsString());
       	pause_pref.setKey(VoicePrefs.KEY_VOICE_PAUSE);
       	pause_pref.setSummary(formatedPause());
       	pause_pref.setOnPreferenceChangeListener(this);
       	voceGeneralPrefCat.addPreference(pause_pref);
       	
       	
        
        PreferenceScreen intentPref = getPreferenceManager().createPreferenceScreen(this);
        intentPref.setIntent(new Intent(this,VoiceDebugActivity.class));
        intentPref.setTitle("voice info");
        intentPref.setSummary("show voice information");
        voceGeneralPrefCat.addPreference(intentPref);
  //     	voceGeneralPrefCat.addPreference(this.findPreference("map_ufo_radius"));
        //this.addPreferencesFromResource(R.xml.map_ufo_radius) ;
        //this.findViewById(R.xml.map_ufo_radius);
       //	Log.i("DUBwise prefs","" + this.findPreference("map_ufo_radius") );
       	//EditTextPreference pause_time_pref=new EditTextPreference(this);
       	//pause_time_pref.
       	//pause_time_pref.set
       	 
       	
        valuesToSpeakPrefCat = new PreferenceCategory(this);
        valuesToSpeakPrefCat.setTitle("values to speak out");
        root.addPreference( valuesToSpeakPrefCat);

        
        //valuesToSpeakPrefCat.addPreference(this.addPreferencesFromResource(R.xml.map_ufo_radius));
        CheckBoxPreference connectionInfoCheckBoxPref = new CheckBoxPreference(this);
        connectionInfoCheckBoxPref.setKey(VoicePrefs.KEY_DO_VOICE_CONNINFO);
        connectionInfoCheckBoxPref.setTitle("Connection Info");
        connectionInfoCheckBoxPref.setSummary("Speak Connection info on start");
        connectionInfoCheckBoxPref.setOnPreferenceChangeListener(this);
       	valuesToSpeakPrefCat.addPreference(connectionInfoCheckBoxPref);


        CheckBoxPreference invertArtificialHorizonCheckBoxPref = new CheckBoxPreference(this);
        invertArtificialHorizonCheckBoxPref.setKey(VoicePrefs.KEY_DO_VOICE_ALT);
        invertArtificialHorizonCheckBoxPref.setTitle("Altitude");
        invertArtificialHorizonCheckBoxPref.setSummary("Speak Altitude in meters");
        invertArtificialHorizonCheckBoxPref.setOnPreferenceChangeListener(this);
       	valuesToSpeakPrefCat.addPreference(invertArtificialHorizonCheckBoxPref);

        CheckBoxPreference doSatelitesCheckBoxPref = new CheckBoxPreference(this);
        doSatelitesCheckBoxPref.setKey(VoicePrefs.KEY_DO_VOICE_SATELITES);
        doSatelitesCheckBoxPref.setTitle("Satelites");
        doSatelitesCheckBoxPref.setSummary("Speak used Satelites for GPS");
        doSatelitesCheckBoxPref.setOnPreferenceChangeListener(this);
       	valuesToSpeakPrefCat.addPreference(doSatelitesCheckBoxPref);

        CheckBoxPreference doCurrentCheckBoxPref = new CheckBoxPreference(this);
        doCurrentCheckBoxPref.setKey(VoicePrefs.KEY_DO_VOICE_CURRENT);
        doCurrentCheckBoxPref.setTitle("Current");
        doCurrentCheckBoxPref.setSummary("Speak Current in Ampere");
        doCurrentCheckBoxPref.setOnPreferenceChangeListener(this);
       	valuesToSpeakPrefCat.addPreference(doCurrentCheckBoxPref);

        CheckBoxPreference doUsedCapacityCheckBoxPref = new CheckBoxPreference(this);
        doUsedCapacityCheckBoxPref.setKey(VoicePrefs.KEY_DO_VOICE_USEDCAPACITY);
        doUsedCapacityCheckBoxPref.setTitle("Used Capacity");
        doUsedCapacityCheckBoxPref.setSummary("Speak used Capacity in mAh");
        doUsedCapacityCheckBoxPref.setOnPreferenceChangeListener(this);
       	valuesToSpeakPrefCat.addPreference(doUsedCapacityCheckBoxPref);

        CheckBoxPreference doVoltageCheckBoxPref = new CheckBoxPreference(this);
        doVoltageCheckBoxPref.setKey(VoicePrefs.KEY_DO_VOICE_VOLTS);
        doVoltageCheckBoxPref.setTitle("Potential");
        doVoltageCheckBoxPref.setSummary("Speak potential in Volts");
        doVoltageCheckBoxPref.setOnPreferenceChangeListener(this);
       	valuesToSpeakPrefCat.addPreference(doVoltageCheckBoxPref);

        CheckBoxPreference doNaviErrorCheckBoxPref = new CheckBoxPreference(this);
        doNaviErrorCheckBoxPref.setKey(VoicePrefs.KEY_DO_VOICE_NCERR);
        doNaviErrorCheckBoxPref.setTitle("Navi Errors");
        doNaviErrorCheckBoxPref.setSummary("speak navi errors");
        doNaviErrorCheckBoxPref.setOnPreferenceChangeListener(this);
       	valuesToSpeakPrefCat.addPreference(doNaviErrorCheckBoxPref);

        CheckBoxPreference doFlightTimeCheckBoxPref = new CheckBoxPreference(this);
        doFlightTimeCheckBoxPref.setKey(VoicePrefs.KEY_DO_VOICE_FLIGHTTIME);
        doFlightTimeCheckBoxPref.setTitle("Flight Time");
        doFlightTimeCheckBoxPref.setSummary("speak Flight Time");
        doFlightTimeCheckBoxPref.setOnPreferenceChangeListener(this);
       	valuesToSpeakPrefCat.addPreference(doFlightTimeCheckBoxPref);

       	valuesToSpeakPrefCat.setEnabled(VoicePrefs.isVoiceEnabled());
       	
       	return root;
    }
    
    @Override
 	public boolean onPreferenceChange(Preference preference, Object newValue) {

    	Log.i("gobandroid","pref change " + preference + " ->" + newValue);
    	if (preference==voiceEnabledCheckBoxPref)
    		{
    		valuesToSpeakPrefCat.setEnabled((Boolean) newValue);
    		if ((Boolean)newValue)
    			StatusVoice.getInstance().init(this);
    		}
    	else if (preference==pause_pref)
    	{
    		StatusVoice.getInstance().setPauseTimeout(0);
    		pause_pref.setSummary(formatedPause());
    	}
    	return true; // return that we are OK with preferences
	}


}
