/**************************************************************************
 *                                          
 * This code is part of DUBwise 
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
 *  Organizations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

package org.ligi.ufo.simulation;

/**
 * Simple implementation of AttitudeProvider for use with e.g. J2ME
 * at the moment only return 0's 
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class SimpleAttitudeProvider implements AttitudeProvider {

	public int getNick() {
		return 0;
	}

	public int getRoll() {
		return 0;
	}

	public int getYaw() {
		return 0;
	}

}
