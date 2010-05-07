/**************************************************************************
 *                                          
 * Connection Adapter for Bluetooth Connections
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

package org.ligi.android.dubwise.con.bluetooth;

import it.gerdavax.easybluetooth.BtSocket;
import it.gerdavax.easybluetooth.LocalDevice;
import it.gerdavax.easybluetooth.RemoteDevice;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.ligi.tracedroid.logging.Log;
import org.ligi.ufo.CommunicationAdapterInterface;

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
			//LocalDevice bta = BluetoothAdapter.getDefaultAdapter();
			// LocalBluetoothDevice.initLocalDevice(context);
			
			Log.i("getting device" + mac);
			remote_device = LocalDevice.getInstance().getRemoteForAddr(mac);
			//BluetoothDevice bd = bta.getRemoteDevice(mac);
					
			remote_device.ensurePaired();
			
			// Thread.sleep(5000 );
			/*
			 * while (bd.getBondState()!=bd.BOND_BONDED) { log("waiting for bond");
			 * Thread.sleep(200 ); }
			 */
			Log.i("create method");
			Log.i("waiting for bond");
			
			/*Method m = bd.getClass().getMethod("createRfcommSocket",
					new Class[] { int.class });
			 */
			Log.i("create connection");
			//bt_connection = (BluetoothSocket) m.invoke(bd, 1);
			// bt_connection.getRemoteDevice().
			// localBluetoothDevice.initLocalDevice(context );
	
			// localBluetoothDevice.
	
			// BluetoothDevice bt =
			// bta.getRemoteDevice(mk_url.replaceAll("btspp://","" ));
	
			// connection = (new BluetoothSocket(mk_url.replaceAll("btssp://",""
			// ).split(":")[0])));
			// bt_connection=LocalBluetoothDevice.getLocalDevice().getRemoteBluetoothDevice(mk_url.replaceAll("btspp://",""
			// )).openSocket(1 );
			// bt.createRfcommSocketToServiceRecord((UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666")));
			
			Log.i("connect ");
			
			bt_connection=remote_device.openSocket(1);
			
			Log.i("getting streams");
			
			
		}
		catch(Exception e) { Log.w(""+e); }
	}

	@Override
	public void disconnect() {
		try {
			getInputStream().close();
	    } catch (IOException e) {	    }
	    
	    try {
	    	getOutputStream().close();
	    } catch (IOException e) {	    }
	    
	    try {
	    	bt_connection.close();
	    } catch (IOException e) {	    }
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
