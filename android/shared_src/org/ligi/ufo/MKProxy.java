/**************************************************************************
 * 
 * Class for Proxying the Connection
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
***************************************************************************/
package org.ligi.ufo;


import java.io.*;
//import javax.microedition.io.*;

//#ifdef j2me
//# import javax.microedition.io.*;
//#endif

public class MKProxy
 implements  Runnable
{

    public boolean connected;
    public String url;
    
    public String err_str="none";

//#ifdef j2me
//#    StreamConnection connection;
//#endif

//#ifdef android
    java.net.Socket connection;
//#endif

    public java.io.InputStream	reader;    
     public java.io.OutputStream writer;    

    MKCommunicator mk;


    public MKProxy(MKCommunicator _mk)
    {
	mk=_mk;
	new Thread( this ).start(); // fire up main Thread 
    }

    public void connect(String url_)
    {
	url=url_;

	try 
	    {
		//

//#ifdef android
		connection = new java.net.Socket(url_,9876); 

		reader=connection.getInputStream();
		writer=connection.getOutputStream();
//#endif

//#ifdef j2me
//#		connection = (StreamConnection) Connector.open(url, Connector.READ_WRITE);
//#		reader=connection.openInputStream();
//#		writer=connection.openOutputStream();

//#endif

		String init="new:foo bar\r\n";
		writer.write(init.getBytes());
		writer.flush();
		connected=true;
	    }
	
	catch (Exception e)
	    {
		//		err_str=e.toString();
		//		this=null;

		connected=false;
	    }

    }

    public void write(byte[] input,int off,int len)
    {
	if (connected)
	    try{ writer.write(input,off,len);
	    writer.write(13);
	    writer.flush();
	    //	    if (input==13) writer.flush();

	    }
	    catch(Exception e) { connected=false; }
    }
    
    public void sleep(int time) {
    	try { Thread.sleep(time); }
    	catch (Exception e)  {   }
    }

    public void run() {
    	while(true) {

		try {
			if (connected) {
				byte[] data_in_buff=new byte[reader.available()];
				int read_count =reader.read(data_in_buff,0,reader.available());
				if (read_count>0) mk.write_raw(data_in_buff);
				sleep(30);
			    }
			else
				sleep(300);
		}
		catch ( Exception e){}
	    } // while

    }
}