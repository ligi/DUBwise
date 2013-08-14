/**************************************************************************
 *                                          
 * Connection Adapter for TCP Connections
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

package org.ligi.android.dubwise_mk.conn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.ligi.java.io.CommunicationAdapterInterface;

public class TCPConnectionAdapter implements CommunicationAdapterInterface {

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(byte[] data, int offset, int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(byte[] data) {
		// TODO Auto-generated method stub
		
	}

	public void write(char data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(int data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int available() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int read(byte[] data, int offset, int length) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int read() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
