/**************************************************************************
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


package org.ligi.android.dubwise_mk.blackbox;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceScreen;

import org.ligi.android.dubwise_mk.BasePrefsActivity;

/**
 * show Preferences for the BlackBox
 */
public class BlackBoxPrefsActivity extends BasePrefsActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPreferenceScreen(createPreferenceHierarchy());
    }

    private PreferenceScreen createPreferenceHierarchy() {
        // Root
        PreferenceScreen root = getPreferenceManager().createPreferenceScreen(this);

        root.setPersistent(true);

        CheckBoxPreference nextScreenCheckBoxPref = new CheckBoxPreference(this);
        nextScreenCheckBoxPref.setKey(BlackBoxPrefs.KEY_ENABLED);
        nextScreenCheckBoxPref.setTitle("enabled");
        nextScreenCheckBoxPref.setSummary("is BlackBox enabled?");
        root.addPreference(nextScreenCheckBoxPref);

        EditTextPreference sgf_fname_pref = new EditTextPreference(this);
        sgf_fname_pref.setTitle("Path");
        root.addPreference(sgf_fname_pref);

        sgf_fname_pref.setDialogTitle("Enter BlackBox Path");
        sgf_fname_pref.setDialogMessage("Please enter the BlackBox Path.");
        sgf_fname_pref.setKey(BlackBoxPrefs.KEY_PATH);
        sgf_fname_pref.setText(BlackBoxPrefs.getPath());
        sgf_fname_pref.setSummary(BlackBoxPrefs.getPath());

        PreferenceScreen intentPref = getPreferenceManager().createPreferenceScreen(this);
        Intent i = new Intent(this, BlackBoxWatchActivity.class);

        intentPref.setIntent(i);
        intentPref.setTitle("Watch BlackBox");
        intentPref.setSummary("check BlackBox status");
        root.addPreference(intentPref);


        return root;
    }

}
