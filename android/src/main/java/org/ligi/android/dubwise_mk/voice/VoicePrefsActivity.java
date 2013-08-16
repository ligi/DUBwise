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

package org.ligi.android.dubwise_mk.voice;


import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

import org.ligi.androidhelper.preferences.TimePreference;
import org.ligi.tracedroid.logging.Log;

public class VoicePrefsActivity extends PreferenceActivity implements OnPreferenceChangeListener {

    private CheckBoxPreference voiceEnabledCheckBoxPref;
    private PreferenceCategory valuesToSpeakPrefCat;
    private TimePreference pause_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        VoicePrefs.init(this);
        super.onCreate(savedInstanceState);
        setPreferenceScreen(createPreferenceHierarchy());
    }

    public String formatedPause() {
        return "" + VoicePrefs.getPauseTimeInMS() + "ms";
    }

    private PreferenceScreen createPreferenceHierarchy() {
        // Root
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);
        root.setPersistent(true);

        // general settings

        PreferenceCategory voceGeneralPrefCat = new PreferenceCategory(this);
        voceGeneralPrefCat.setTitle("general voice settings");
        root.addPreference(voceGeneralPrefCat);

        voiceEnabledCheckBoxPref = new CheckBoxPreference(this);
        voiceEnabledCheckBoxPref.setKey(VoicePrefs.KEY_VOICE_ENABLED);
        voiceEnabledCheckBoxPref.setTitle("Voice Enabled");
        voiceEnabledCheckBoxPref.setSummary("DUBwise should speak at all?");
        voiceEnabledCheckBoxPref.setOnPreferenceChangeListener(this);
        voceGeneralPrefCat.addPreference(voiceEnabledCheckBoxPref);


        pause_pref = new TimePreference(this, null);
        pause_pref.setTitle("pause between blocks");
        pause_pref.setDialogTitle("Pause Time");

        pause_pref.setDialogMessage("Please enter a time in ms to pause between the blocks:");
        pause_pref.setDefaultValue(VoicePrefs.getPauseTimeAsString());
        pause_pref.setKey(VoicePrefs.KEY_VOICE_PAUSE);
        pause_pref.setSummary(formatedPause());
        pause_pref.setOnPreferenceChangeListener(this);
        voceGeneralPrefCat.addPreference(pause_pref);

        ListPreference streamPref = new ListPreference(this);
        streamPref.setEntries(VoicePrefs.getAllStreamNames());
        streamPref.setEntryValues(VoicePrefs.getAllStreamNames());
        streamPref.setDialogTitle("Audio Stream");
        streamPref.setKey(VoicePrefs.KEY_VOICE_STREAM);

        streamPref.setSummary("where to play the voice");
        streamPref.setOnPreferenceChangeListener(this);
        streamPref.setDefaultValue(VoicePrefs.getStreamName());
        voceGeneralPrefCat.addPreference(streamPref);

        PreferenceScreen intentPref = getPreferenceManager().createPreferenceScreen(this);
        intentPref.setIntent(new Intent(this, VoiceDebugActivity.class));
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
        root.addPreference(valuesToSpeakPrefCat);

        //valuesToSpeakPrefCat.addPreference(this.addPreferencesFromResource(R.xml.map_ufo_radius));
        CheckBoxPreference connectionInfoCheckBoxPref = new CheckBoxPreference(this);
        connectionInfoCheckBoxPref.setKey(VoicePrefs.KEY_DO_VOICE_CONNINFO);
        connectionInfoCheckBoxPref.setTitle("Connection Info");
        connectionInfoCheckBoxPref.setSummary("connection info on start");
        connectionInfoCheckBoxPref.setOnPreferenceChangeListener(this);
        valuesToSpeakPrefCat.addPreference(connectionInfoCheckBoxPref);

        CheckBoxPreference rclostCheckBoxPref = new CheckBoxPreference(this);
        rclostCheckBoxPref.setKey(VoicePrefs.KEY_DO_VOICE_RCLOST);
        rclostCheckBoxPref.setTitle("RC Lost");
        rclostCheckBoxPref.setSummary("when RC Signal lost");
        rclostCheckBoxPref.setOnPreferenceChangeListener(this);
        valuesToSpeakPrefCat.addPreference(rclostCheckBoxPref);

        CheckBoxPreference doAltCheckBoxPref = new CheckBoxPreference(this);
        doAltCheckBoxPref.setKey(VoicePrefs.KEY_DO_VOICE_ALT);
        doAltCheckBoxPref.setTitle("Altitude");
        doAltCheckBoxPref.setSummary("altitude in meters");
        doAltCheckBoxPref.setOnPreferenceChangeListener(this);
        valuesToSpeakPrefCat.addPreference(doAltCheckBoxPref);

        CheckBoxPreference doMaxAltCheckBoxPref = new CheckBoxPreference(this);
        doMaxAltCheckBoxPref.setKey(VoicePrefs.KEY_DO_VOICE_MAX_ALT);
        doMaxAltCheckBoxPref.setTitle("max altitude");
        doMaxAltCheckBoxPref.setSummary("Speak max Altitude in meters");
        doMaxAltCheckBoxPref.setOnPreferenceChangeListener(this);

        valuesToSpeakPrefCat.addPreference(doMaxAltCheckBoxPref);


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

        CheckBoxPreference doDistance2HomeCheckBoxPref = new CheckBoxPreference(this);
        doDistance2HomeCheckBoxPref.setKey(VoicePrefs.KEY_DO_VOICE_DISTANCE2HOME);
        doDistance2HomeCheckBoxPref.setTitle("Distance2Home");
        doDistance2HomeCheckBoxPref.setSummary("speak Distance to Home");
        doDistance2HomeCheckBoxPref.setOnPreferenceChangeListener(this);
        valuesToSpeakPrefCat.addPreference(doDistance2HomeCheckBoxPref);


        CheckBoxPreference doDistance2TargetCheckBoxPref = new CheckBoxPreference(this);
        doDistance2TargetCheckBoxPref.setKey(VoicePrefs.KEY_DO_VOICE_DISTANCE2TARGET);
        doDistance2TargetCheckBoxPref.setTitle("Distance2Target");
        doDistance2TargetCheckBoxPref.setSummary("speak Distance to Target");
        doDistance2TargetCheckBoxPref.setOnPreferenceChangeListener(this);
        valuesToSpeakPrefCat.addPreference(doDistance2TargetCheckBoxPref);

        CheckBoxPreference doSpeedCheckBoxPref = new CheckBoxPreference(this);
        doSpeedCheckBoxPref.setKey(VoicePrefs.KEY_DO_VOICE_SPEED);
        doSpeedCheckBoxPref.setTitle("Speed");
        doSpeedCheckBoxPref.setSummary("speak act speed");
        doSpeedCheckBoxPref.setOnPreferenceChangeListener(this);
        valuesToSpeakPrefCat.addPreference(doSpeedCheckBoxPref);

        CheckBoxPreference doSpeedMaxCheckBoxPref = new CheckBoxPreference(this);
        doSpeedMaxCheckBoxPref.setKey(VoicePrefs.KEY_DO_VOICE_MAX_SPEED);
        doSpeedMaxCheckBoxPref.setTitle("Max Speed");
        doSpeedMaxCheckBoxPref.setSummary("speak maximal speed");
        doSpeedMaxCheckBoxPref.setOnPreferenceChangeListener(this);
        valuesToSpeakPrefCat.addPreference(doSpeedMaxCheckBoxPref);

        valuesToSpeakPrefCat.setEnabled(VoicePrefs.isVoiceEnabled());

        return root;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        Log.i("pref change " + preference + " ->" + newValue);
        if (preference == voiceEnabledCheckBoxPref) {
            valuesToSpeakPrefCat.setEnabled((Boolean) newValue);
            if ((Boolean) newValue)
                StatusVoice.getInstance().init(this);
        } else if (preference == pause_pref) {
            StatusVoice.getInstance().setPauseTimeout(0);
            pause_pref.setSummary(formatedPause());
        }
        return true; // return that we are OK with preferences
    }

}

