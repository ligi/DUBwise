package org.ligi.ufo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.xml.sax.InputSource;

public class FakeCommunicationAdapter implements
		CommunicationAdapterInterface {
	
	private InputStream input_stream;
	private OutputStream output_stream;

	public void log(String what) {
	 Log.d("DUBwise Fake Communication Adapter",what);	
	}
	
	public FakeCommunicationAdapter() {
		input_stream =new ByteArrayInputStream("fake".getBytes());
		output_stream=new ByteArrayOutputStream();
	}

	@Override
	public void connect() {
	}

	@Override
	public void disconnect() {
		
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
