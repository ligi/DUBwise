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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
