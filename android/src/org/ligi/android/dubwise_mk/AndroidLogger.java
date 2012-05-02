/**************************************************************************
 *                                                                             
 * Author:  Marcus -LiGi- Bueschleb   
 *  http://ligi.de
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

import org.ligi.tracedroid.Log;
import org.ligi.ufo.logging.LoggingInterface;

/**
 * implement the DUBwise LoggingInterface 
 * for Android with tracedroid
 * 
 * @author ligi
 */
public class AndroidLogger implements LoggingInterface {

	@Override
	public void e(String msg) {
		Log.e(msg);
	}

	@Override
	public void i(String msg) {
		Log.i(msg);
	}

	@Override
	public void w(String msg) {
		Log.w(msg);
	}

}
