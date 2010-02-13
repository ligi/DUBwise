package org.ligi.ufo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FakeCommunicationAdapter implements
		CommunicationAdapterInterface {
	
	private InputStream input_stream;
	private OutputStream output_stream;

	public void log(String what) {
	
		input_stream=new ByteArrayInputStream("fake".getBytes());
		output_stream=new ByteArrayOutputStream();
	
	}

	public void connect() {
		// nothing to do here in the fake adapter
	}

	public void disconnect() {
		// nothing to do here in the fake adapter
	}

	public InputStream getInputStream() {
		return input_stream;
	}

	public OutputStream getOutputStream() {
		return output_stream;
	}

	/*
	public void run() {
		while (true) {
			try {
				piped_output_stream.write((byte)'#');
				piped_output_stream.write((byte)'0');
				piped_output_stream.write((byte)'=');
				piped_output_stream.write((byte)'=');
				piped_output_stream.write((byte)'=');
				piped_output_stream.write((byte)'=');
				piped_output_stream.write((byte)'\n');
				piped_output_stream.write((byte)'\r');
				Thread.sleep(100);
			} catch (Exception e) {}
		}
	}
	*/
}
