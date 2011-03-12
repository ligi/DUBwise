/**************************************************************************
 *                                          
 * Author:  Marcus -LiGi- Bueschleb   
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

package org.ligi.android.dubwise.conn.bluetooth;

import it.gerdavax.easybluetooth.BtSocket;
import it.gerdavax.easybluetooth.LocalDevice;
import it.gerdavax.easybluetooth.RemoteDevice;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.ligi.tracedroid.logging.Log;
import org.ligi.ufo.CommunicationAdapterInterface;

/**
 * Connection Adapter for Android Bluetooth Connections
 * uses lib http://android-bluetooth.googlecode.com to work with 
 * Android <2.0 and have the openSocket method without the need for SDP 
 * But that uses non standard calls - so might not work everywhere ..                                    
 *             
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class BluetoothCommunicationAdapter implements
		CommunicationAdapterInterface {
	
	private BtSocket bt_connection;
	private RemoteDevice remote_device;
	private String mac="";
	
	public int getRSSI() {
		return remote_device.getRSSI();	 		
	}
	
	public BluetoothCommunicationAdapter(String mac) {
		this.mac=mac;
	}

	@Override
	public void connect() {
		
		try {
			Log.i("getting device: " + mac);
			remote_device = LocalDevice.getInstance().getRemoteForAddr(mac);
			Log.i("ensure Paired");
			remote_device.ensurePaired();
			Log.i("open Socket");
			bt_connection=remote_device.openSocket(1);
		}
		catch(Exception e) { Log.w(""+e); }
	}

	@Override
	public void disconnect() {
		try {
			getInputStream().close();
	    } catch (Exception e) {	    }
	    
	    try {
	    	getOutputStream().close();
	    } catch (Exception e) {	    }
	    
	    try {
	    	bt_connection.close();
	    } catch (Exception e) {	    }
	}

	public InputStream getInputStream() {
		try {
			return bt_connection.getInputStream();
		} catch (Exception e) {
			Log.i("DUBwise","getInputstream problem" + e);
			return null;
		}
	}

	public OutputStream getOutputStream() {
		try {
			return bt_connection.getOutputStream();
		} catch (Exception e) {
			return null;
		}
	}
	

	@Override
	public int available() {
		try {
			return getInputStream().available();
		} catch (Exception e) {
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
