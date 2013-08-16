/**************************************************************************
 *
 * Activity to edit the DUBwise Preferences
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

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

/**
 * Activity to edit the DUBwise Preferences
 *
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 */
public class DUBwisePrefsActivity extends PreferenceActivity implements
        OnPreferenceChangeListener, OnSharedPreferenceChangeListener {

    private ListPreference stayAwakePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPreferenceScreen(createPreferenceHierarchy());
    }

    private PreferenceScreen createPreferenceHierarchy() {
        // Root
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(
                this);

        root.setPersistent(true);

		/* UI section */
        PreferenceCategory uiPrefCat = new PreferenceCategory(this);
        uiPrefCat.setTitle("User Interface");
        root.addPreference(uiPrefCat);

        CheckBoxPreference fullscreenCheckBoxPref = new CheckBoxPreference(this);
        fullscreenCheckBoxPref.setKey(DUBwisePrefs.KEY_FULLSCREEN);
        fullscreenCheckBoxPref.setTitle("Fullscreen");
        fullscreenCheckBoxPref.setSummary("see more DUBwise & less Device");
        fullscreenCheckBoxPref.setOnPreferenceChangeListener(this);
        uiPrefCat.addPreference(fullscreenCheckBoxPref);

        CheckBoxPreference statusBarCheckBoxPref = new CheckBoxPreference(this);
        statusBarCheckBoxPref.setKey(DUBwisePrefs.KEY_STATUSBAR);
        statusBarCheckBoxPref.setTitle("Status Bar");
        statusBarCheckBoxPref.setSummary("see important info on top");
        statusBarCheckBoxPref.setOnPreferenceChangeListener(this);

        uiPrefCat.addPreference(statusBarCheckBoxPref);

        stayAwakePref = new ListPreference(this);
        stayAwakePref.setEntries(DUBwisePrefs.getKeepLightOptionStrings());
        stayAwakePref.setEntryValues(DUBwisePrefs.getKeepLightOptionStrings());
        stayAwakePref.setDialogTitle("Stay Awake");
        stayAwakePref.setKey(DUBwisePrefs.KEY_KEEPLIGHT);
        stayAwakePref.setTitle("Stay Awake");
        stayAwakePref.setSummary(DUBwisePrefs.getKeepLightString());
        stayAwakePref.setOnPreferenceChangeListener(this);
        stayAwakePref.setDefaultValue(DUBwisePrefs.getKeepLightString());
        uiPrefCat.addPreference(stayAwakePref);

		/* UI section */
        PreferenceCategory miscPrefCat = new PreferenceCategory(this);
        miscPrefCat.setTitle("Misc");
        root.addPreference(miscPrefCat);

        CheckBoxPreference expertModeCheckBoxPref = new CheckBoxPreference(this);
        expertModeCheckBoxPref.setKey(DUBwisePrefs.KEY_EXPERTMODE);
        expertModeCheckBoxPref.setTitle("Expert Mode");
        expertModeCheckBoxPref
                .setSummary("experimental functions - might crash more often");
        miscPrefCat.addPreference(expertModeCheckBoxPref);

        CheckBoxPreference keepScreenAwakeCheckBoxPref = new CheckBoxPreference(
                this);
        keepScreenAwakeCheckBoxPref.setKey(DUBwisePrefs.KEY_VERBOSELOG);
        keepScreenAwakeCheckBoxPref.setTitle("Verbose Logging");
        keepScreenAwakeCheckBoxPref.setSummary("might help find problems");
        miscPrefCat.addPreference(keepScreenAwakeCheckBoxPref);

        this.getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        return root;

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference == stayAwakePref) {
            preference.setSummary((String) newValue);
        }

        return true; // return that we are OK with preferences
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        // for fullscreen and statusbar instant change
        //ActivityCalls.afterContent(this);
    }

}
