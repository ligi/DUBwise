/**************************************************************************
 *                                          
 * Interface for Communication Adapters
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

package org.ligi.ufo;

import java.io.InputStream;
import java.io.OutputStream;


public interface CommunicationAdapterInterface
{
	
	
	public void connect();
	
	public void disconnect();
	
	public InputStream getInputStream();
	
	public OutputStream getOutputStream();
	

}
