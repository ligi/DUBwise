package org.ligi.android.dubwise.helper.preferences;

/**************************************************************************
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

import java.util.Vector;

import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;

/**
 * Class to enable or disable preferences depending on the state of a CheckBoxPrefenece
 * If somebody has a idea for a better name for this class just let me know ;-) 
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class SetPreferenceEnabledByCheckBoxPreferenceState implements OnPreferenceChangeListener {

	private Vector<Preference> preference_vector;
	private CheckBoxPreference pref;
	
	public SetPreferenceEnabledByCheckBoxPreferenceState(CheckBoxPreference pref) {
		this.pref=pref;
		pref.setOnPreferenceChangeListener(this);
		preference_vector=new Vector<Preference>();
	}
	
	public SetPreferenceEnabledByCheckBoxPreferenceState addPreference2SetEnable(Preference new_p) {
		preference_vector.add(new_p);
		new_p.setEnabled(pref.isChecked());
		return this;
	}
	
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		for (Preference p : preference_vector)
			p.setEnabled((Boolean)newValue);
		return true;
	}
	
}
