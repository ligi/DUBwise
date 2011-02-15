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

package org.ligi.android.dubwise.cockpit;

import org.ligi.android.dubwise.DUBwisePrefs;
import org.ligi.android.dubwise.helper.ActivityCalls;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;

public class CockpitPrefsActivity extends PreferenceActivity implements OnPreferenceChangeListener {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
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
        
        /* UI section */
        PreferenceCategory artificialHorizonPrefCat = new PreferenceCategory(this);
        artificialHorizonPrefCat.setTitle("Artificial Horizon");
        root.addPreference( artificialHorizonPrefCat);

        CheckBoxPreference invertArtificialHorizonCheckBoxPref = new CheckBoxPreference(this);
        invertArtificialHorizonCheckBoxPref.setKey(DUBwisePrefs.KEY_INVERT_ARTIFICIAL_HORIZON);
        invertArtificialHorizonCheckBoxPref.setTitle("Invert");
        invertArtificialHorizonCheckBoxPref.setSummary("Invert Artificial Horizon");
        invertArtificialHorizonCheckBoxPref.setOnPreferenceChangeListener(this);
       	artificialHorizonPrefCat.addPreference(invertArtificialHorizonCheckBoxPref);
       	
       	PreferenceCategory valuesPrefCat = new PreferenceCategory(this);
       	valuesPrefCat.setTitle("Shown Values");
        root.addPreference( valuesPrefCat);

        CheckBoxPreference drawAltCheckBoxPref = new CheckBoxPreference(this);
        drawAltCheckBoxPref.setKey(DUBwisePrefs.KEY_COCKPIT_SHOW_ALT);
        drawAltCheckBoxPref.setTitle("Altitude");
        drawAltCheckBoxPref.setSummary("Show Altitude in m");
        drawAltCheckBoxPref.setOnPreferenceChangeListener(this);
        drawAltCheckBoxPref.setDefaultValue(DUBwisePrefs.showAlt());
       	valuesPrefCat.addPreference(drawAltCheckBoxPref);
       	
       	
       	CheckBoxPreference drawFlightTimeCheckBoxPref = new CheckBoxPreference(this);
        drawFlightTimeCheckBoxPref.setKey(DUBwisePrefs.KEY_COCKPIT_SHOW_FLIGHTTIME);
        drawFlightTimeCheckBoxPref.setTitle("Flight Time");
        drawFlightTimeCheckBoxPref.setSummary("Show Altitude in mm:ss");
        drawFlightTimeCheckBoxPref.setOnPreferenceChangeListener(this);
        drawFlightTimeCheckBoxPref.setDefaultValue(DUBwisePrefs.showFlightTime());
       	valuesPrefCat.addPreference(drawFlightTimeCheckBoxPref);


        CheckBoxPreference drawCurrentCheckBoxPref = new CheckBoxPreference(this);
        drawCurrentCheckBoxPref.setKey(DUBwisePrefs.KEY_COCKPIT_SHOW_CURRENT);
        drawCurrentCheckBoxPref.setTitle("Current");
        drawCurrentCheckBoxPref.setSummary("Show Current in A");
        drawCurrentCheckBoxPref.setOnPreferenceChangeListener(this);
        drawCurrentCheckBoxPref.setDefaultValue(DUBwisePrefs.showCurrent());
       	valuesPrefCat.addPreference(drawCurrentCheckBoxPref);


        CheckBoxPreference drawUsedCapacityCheckBoxBoxPref = new CheckBoxPreference(this);
        drawUsedCapacityCheckBoxBoxPref.setKey(DUBwisePrefs.KEY_COCKPIT_SHOW_USEDCAPACITY);
        drawUsedCapacityCheckBoxBoxPref.setTitle("Used Capacity");
        drawUsedCapacityCheckBoxBoxPref.setSummary("Show Used Capacity in mAh");
        drawUsedCapacityCheckBoxBoxPref.setOnPreferenceChangeListener(this);
        drawUsedCapacityCheckBoxBoxPref.setDefaultValue(DUBwisePrefs.showUsedCapacity());
        valuesPrefCat.addPreference(drawUsedCapacityCheckBoxBoxPref);

        return root;
 
    }
    
    @Override
 	public boolean onPreferenceChange(Preference preference, Object newValue) {
	  	return true; // return that we are OK with preferences
	}


}
