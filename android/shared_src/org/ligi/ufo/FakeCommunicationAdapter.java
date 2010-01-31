package org.ligi.ufo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import android.util.Log;


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
		// nothing to do here in the fake adapter
	}

	@Override
	public void disconnect() {
		// nothing to do here in the fake adapter
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
