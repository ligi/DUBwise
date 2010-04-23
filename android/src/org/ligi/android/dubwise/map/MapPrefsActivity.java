/**************************************************************************
 *                                          
 * Activity to edit the Map Preferences
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

package org.ligi.android.dubwise.map;


import org.ligi.android.dubwise.helper.ActivityCalls;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;


public class MapPrefsActivity extends PreferenceActivity implements OnPreferenceChangeListener {


	@Override
    protected void onCreate(Bundle savedInstanceState) {
		MapPrefs.init(this);
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

        
        // general settings

    /*
        PreferenceCategory mapGeneralPrefCat = new PreferenceCategory(this);
        mapGeneralPrefCat.setTitle("UFO Settings");
        root.addPreference( mapGeneralPrefCat);

        
        CheckBoxPreference zoom2LevelCheckBoxPref = new CheckBoxPreference(this);
        zoom2LevelCheckBoxPref.setKey(MapPrefs.KEY_ZOOM2LEVEL);
        zoom2LevelCheckBoxPref.setTitle("Voice Enabled");
        zoom2LevelCheckBoxPref.setSummary("DUBwise should speak at all?");
        zoom2LevelCheckBoxPref.setOnPreferenceChangeListener(this);
       	mapGeneralPrefCat.addPreference(zoom2LevelCheckBoxPref);
 	*/

        PreferenceCategory ufoPrefCat = new PreferenceCategory(this);
        ufoPrefCat.setTitle("UFO Settings");
        root.addPreference( ufoPrefCat);

        
        CheckBoxPreference showUFOCheckBoxPref = new CheckBoxPreference(this);
        showUFOCheckBoxPref.setKey(MapPrefs.KEY_SHOW_UFO);
        showUFOCheckBoxPref.setTitle("Show UFO");
        showUFOCheckBoxPref.setSummary("show icon for UFO?");
        showUFOCheckBoxPref.setOnPreferenceChangeListener(this);
        showUFOCheckBoxPref.setDefaultValue(MapPrefs.showUFO());
       	ufoPrefCat.addPreference(showUFOCheckBoxPref);


        CheckBoxPreference showHomeCheckBoxPref = new CheckBoxPreference(this);
        showHomeCheckBoxPref.setKey(MapPrefs.KEY_SHOW_HOME);
        showHomeCheckBoxPref.setTitle("Show Home");
        showHomeCheckBoxPref.setSummary("show icon for Home?");
        showHomeCheckBoxPref.setOnPreferenceChangeListener(this);
        showHomeCheckBoxPref.setDefaultValue(MapPrefs.showHome());
       	ufoPrefCat.addPreference(showHomeCheckBoxPref);
       	       	

        CheckBoxPreference showPhoneCheckBoxPref = new CheckBoxPreference(this);
        showPhoneCheckBoxPref.setKey(MapPrefs.KEY_SHOW_PHONE);
        showPhoneCheckBoxPref.setTitle("Show Phone");
        showPhoneCheckBoxPref.setSummary("show icon for Phone?");
        showPhoneCheckBoxPref.setOnPreferenceChangeListener(this);
        showPhoneCheckBoxPref.setDefaultValue(MapPrefs.showPhone());
       	ufoPrefCat.addPreference(showPhoneCheckBoxPref);
       	
       	return root;
    }
    
    @Override
 	public boolean onPreferenceChange(Preference preference, Object newValue) {

 
    	return true; // return that we are OK with preferences
	}


}
