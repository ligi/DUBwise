package org.ligi.android.dubwise.con;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.ligi.ufo.CommunicationAdapterInterface;

public class TCPConnectionAdapter implements CommunicationAdapterInterface {

	private String host;
	private int port;
	private boolean qmk;
	
	private Socket socket;
	
	public TCPConnectionAdapter(String host,int port,boolean qmk) {
		this.host=host;
		this.port=port;
		this.qmk=qmk;
	}
	
	@Override
	public void connect() {
	 try {
		socket=new Socket(host,port);
	} catch (UnknownHostException e) {
	} catch (IOException e) {
	}
	
	if (qmk);;
	// TODO handle QMK protocol
	}

	@Override
	public void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public InputStream getInputStream() {
		try {
			return socket.getInputStream();
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public OutputStream getOutputStream() {
		try {
			return socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
