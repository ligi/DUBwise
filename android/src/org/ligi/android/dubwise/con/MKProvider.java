/**************************************************************************
 *                                          
 * Singleton Class to hold the connection 
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

package org.ligi.android.dubwise.con;

import org.ligi.ufo.MKCommunicator;

public final class MKProvider  {

	static MKCommunicator mk=null;
	
	public static MKCommunicator getMK() {
		if (mk==null)
			mk=new MKCommunicator();
		
		return mk;
	}
	public static void disposeMK() {
     mk=null;
    }
}
