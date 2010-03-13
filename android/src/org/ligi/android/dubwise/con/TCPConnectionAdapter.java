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

package org.ligi.android.dubwise.con;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.ligi.ufo.CommunicationAdapterInterface;

public class TCPConnectionAdapter implements CommunicationAdapterInterface {

	private String host;
	private int port;
	private boolean qmk;
	
	private Socket socket;
	private String qmk_pwd;
	
	public TCPConnectionAdapter(String host,int port) {
		this.host=host;
		this.port=port;
		this.qmk=false;
	}
	
	public TCPConnectionAdapter(String host,int port,String username,String pwd) {
		this.host=host;
		this.port=port;
		this.qmk=true;
		this.qmk_pwd=pwd;
	}
	
	/**
	 * function to calculate a md5sum 
	 * taken from: http://www.androidsnippets.org/snippets/52/
	 * @param s
	 * @return
	 */
	public String md5(String s) {  
	    try {  
	        // Create MD5 Hash  
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");  
	        digest.update(s.getBytes());  
	        byte messageDigest[] = digest.digest();  
	          
	        // Create Hex String  
	        StringBuffer hexString = new StringBuffer();  
	        for (int i=0; i<messageDigest.length; i++)  
	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));  
	        return hexString.toString();  
	          
	    } catch (NoSuchAlgorithmException e) {  
	        e.printStackTrace();  
	    }  
	    return "";  
	}  
	
	@Override
	public void connect() {
	 try {
		socket=new Socket(host,port);

		if (qmk) {
			socket.getOutputStream().write("$:10:101:DUBwise for Android:0\n\r".getBytes());
			socket.getOutputStream().flush();
			socket.getOutputStream().write(("$:10:105:"+md5(qmk_pwd)+":0\n\r").getBytes());
			socket.getOutputStream().flush();
		}
	
	 } catch (UnknownHostException e) {
	} catch (IOException e) {
	}
	
	if (qmk);;
	// TODO handle QMK protocol
	}

	@Override
	public void disconnect() {
		try {
			getInputStream().close();
			getOutputStream().close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public InputStream getInputStream() {
		try {
			return socket.getInputStream();
		} catch (IOException e) {
			return null;
		}
	}

	public OutputStream getOutputStream() {
		try {
			return socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public int available() {
		try {
			return getInputStream().available();
		} catch (IOException e) {
			return 0;
		}
	}

	@Override
	public void flush() throws IOException {
			getOutputStream().flush();
	}

	@Override
	public int read(byte[] b, int offset, int length) throws IOException {
			return getInputStream().read(b,offset,length);
	}

	@Override
	public void write(byte[] buffer, int offset, int count) throws IOException {
		getOutputStream().write(buffer, offset, count);
		
	}

	@Override
	public void write(byte[] buffer)  throws IOException  {
		getOutputStream().write(buffer);
	}

	@Override
	public void write(int oneByte) throws IOException  {
		getOutputStream().write(oneByte);
	}

	@Override
	public int read() throws IOException {
		return getInputStream().read();
	}
}
