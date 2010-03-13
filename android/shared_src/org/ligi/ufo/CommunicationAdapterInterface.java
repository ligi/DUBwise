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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public interface CommunicationAdapterInterface
{
	
	
	public void connect();
	
	public void disconnect();
	
	public void flush()  throws IOException ;
	
	public void  write  (byte[] buffer, int offset, int count)  throws IOException ;
		
	public void  write  (byte[] buffer)  throws IOException ;
	
	public void  write  (int oneByte)  throws IOException ;
	
	public int  available  () throws IOException;
	
	public int  read  (byte[] b, int offset, int length) throws IOException;
	
	public int  read  () throws IOException;

	/*
	public InputStream getInputStream();
	
	public OutputStream getOutputStream();
	*/

}
