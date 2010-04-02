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

import org.ligi.android.dubwise.helper.ActivityCalls;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;

public class VoicePrefsActivity extends PreferenceActivity implements OnPreferenceChangeListener {

	private CheckBoxPreference voiceEnabledCheckBoxPref;
	private PreferenceCategory valuesToSpeakPrefCat;
	
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
	
    private PreferenceScreen createPreferenceHierarchy() {
        // Root
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
        
        root.setPersistent(true);


        PreferenceCategory voceGeneralPrefCat = new PreferenceCategory(this);
        voceGeneralPrefCat.setTitle("general voice Settings");
        root.addPreference( voceGeneralPrefCat);

        
        voiceEnabledCheckBoxPref = new CheckBoxPreference(this);
        voiceEnabledCheckBoxPref.setKey(VoicePrefs.KEY_VOICE_ENABLED);
        voiceEnabledCheckBoxPref.setTitle("Voice Enabled");
        voiceEnabledCheckBoxPref.setSummary("DUBwise should speak at all?");
        voiceEnabledCheckBoxPref.setOnPreferenceChangeListener(this);
       	voceGeneralPrefCat.addPreference(voiceEnabledCheckBoxPref);


        valuesToSpeakPrefCat = new PreferenceCategory(this);
        valuesToSpeakPrefCat.setTitle("values to speak out");
        root.addPreference( valuesToSpeakPrefCat);

        
        
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

    	if (preference==voiceEnabledCheckBoxPref)
    		{
    		valuesToSpeakPrefCat.setEnabled((Boolean) newValue);
    		if ((Boolean)newValue)
    			StatusVoice.getInstance().init(this);
    		}
    	return true; // return that we are OK with preferences
	}


}
