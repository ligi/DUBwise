/**************************************************************************
 *                                          
 * Communication Adapter for a Fake Connection
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

public class FakeCommunicationAdapter implements
		CommunicationAdapterInterface {
	

	public void connect() {
		// nothing to do here in the fake adapter
	}

	public void disconnect() {
		// nothing to do here in the fake adapter
	}

	public int available() {
		return 0;
	}

	public void flush() {
	}

	public int read(byte[] b, int offset, int length) {
		return 0;
	}

	public void write(byte[] buffer, int offset, int count) {
	}

	public void write(byte[] buffer) {
	}

	public void write(int oneByte) {
	}
	
	public int read() {
		return 0;
	}

}
