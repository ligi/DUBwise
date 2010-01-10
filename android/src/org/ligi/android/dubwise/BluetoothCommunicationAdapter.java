package org.ligi.android.dubwise;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.ligi.ufo.CommunicationAdapterInterface;

public class BluetoothCommunicationAdapter implements
		CommunicationAdapterInterface {
	
	private InputStream input_stream;
	private OutputStream output_stream;
	private BluetoothSocket bt_connection;
	

	public void log(String what) {
	 Log.d("DUBwise Bluetooth Communication Adapter",what);	
	}
	
	public BluetoothCommunicationAdapter(String mac) {
		try {
		BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();

		// LocalBluetoothDevice.initLocalDevice(context);

		log("getting device");
		BluetoothDevice bd = bta.getRemoteDevice(mac);
		log("waiting for bond");

		// Thread.sleep(5000 );
		/*
		 * while (bd.getBondState()!=bd.BOND_BONDED) { log("waiting for bond");
		 * Thread.sleep(200 ); }
		 */
		log("create method");
		log("waiting for bond");
		Method m = bd.getClass().getMethod("createRfcommSocket",
				new Class[] { int.class });

		log("create connection");
		bt_connection = (BluetoothSocket) m.invoke(bd, 1);
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
		log("connect ");
		// Thread.sleep(5000 );
		bt_connection.connect();

		log("getting streams ");
		// Thread.sleep(5000 );
		input_stream = bt_connection.getInputStream();
		output_stream = bt_connection.getOutputStream();
		}
		catch(Exception e) { log(""+e); }
	}

	@Override
	public void connect() {

	}

	@Override
	public void disconnect() {
		try {
	        bt_connection.close();
	    }
	    catch (IOException e) {
	        // XXX Auto-generated catch block
	        
	    }
	}

	@Override
	public InputStream getInputStream() {
		return input_stream;
	}

	@Override
	public OutputStream getOutputStream() {
		return output_stream;
	}

}
