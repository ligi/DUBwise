package org.ligi.ufo;

import java.io.InputStream;
import java.io.OutputStream;


public interface CommunicationAdapterInterface
{
	
	
	public void connect();
	
	public void disconnect();
	
	public InputStream getInputStream();
	
	public OutputStream getOutputStream();
	

}
