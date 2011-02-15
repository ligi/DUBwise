/**
 * gobandroid 
 * by Marcus -Ligi- Bueschleb 
 * http://ligi.de
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as 
 * published by the Free Software Foundation; 
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 **/

package org.ligi.android.dubwise.blackbox;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.EditTextPreference;

public class BlackBoxPrefsActivity extends PreferenceActivity  {

	
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
        Intent i=new Intent(this,BlackBoxWatchActivity.class);
        
        intentPref.setIntent(i);
        intentPref.setTitle("Watch BlackBox");
        intentPref.setSummary("check BlackBox status");
        root.addPreference(intentPref);

        
        return root;
    }
     	
}
