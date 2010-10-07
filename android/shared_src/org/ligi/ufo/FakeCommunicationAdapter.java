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

import java.util.Vector;

import org.ligi.ufo.CommunicationAdapterInterface;
import org.ligi.ufo.MKHelper;

public class FakeCommunicationAdapter implements
		CommunicationAdapterInterface,Runnable {
	
	private Vector send_stack;
	private int[] debug_data;
	private int[] navi_osd_data;
	private int[] stick_data;
	
	private String[][] lcd_lines;
	
	
	public FakeCommunicationAdapter()   {
		
		lcd_lines=new String[2][4];
		
		send_stack=new Vector();
    	
		send_stack.addElement(MKHelper.encodeCommand(MKProtocolDefinitions.NAVI_SLAVE_ADDR, 'V', new int[] { 0,80,0,10,2  }));
		send_stack.addElement(MKHelper.encodeCommand(MKProtocolDefinitions.NAVI_SLAVE_ADDR, 'L', "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest1234".getBytes()));
		
		debug_data=new int[64];
		for ( int i = 0 ; i < 32 ; i++ ) {
			debug_data[i*2]=23;
			debug_data[i*2+1]=0;
			}

		navi_osd_data=new int[81];
		navi_osd_data[57]=162; // UBatt
		navi_osd_data[50]=9; // SatsInUse
			
		stick_data=new int[MKStickData.MAX_STICKS];
		
		new Thread(this).start();
	}
	
	public void connect() {
		// nothing to do here in the fake adapter
	}

	public void disconnect() {
		// nothing to do here in the fake adapter
	}

	public int available() {
		if (send_stack.size()==0)
			return 0;
		return ((byte[])send_stack.elementAt(0)).length;
	}

	public void flush() {
	}

	public int read(byte[] b, int offset, int length) {
		if (send_stack.size()==0)
			return 0;
		
		byte[] _b=((byte[])send_stack.elementAt(0));
		for (int i=0;i<_b.length;i++)
			b[i]=_b[i];

		send_stack.removeElementAt(0);
		return _b.length;
	}

	public void write(byte[] buffer, int offset, int count) {
	}

	public void write(byte[] buffer) {
	}

	public void write(int oneByte) {
	}
	
	public int read() {
		return 0;
	}

	public void run() {
		while ( true ) {
			try {
				send_stack.addElement(MKHelper.encodeCommand(MKProtocolDefinitions.NAVI_SLAVE_ADDR, 'D', debug_data));
				send_stack.addElement(MKHelper.encodeCommand(MKProtocolDefinitions.NAVI_SLAVE_ADDR, 'O', navi_osd_data));
				send_stack.addElement(MKHelper.encodeCommand(MKProtocolDefinitions.NAVI_SLAVE_ADDR, 'P', stick_data));
				Thread.sleep(300);
			} catch (InterruptedException e) {	}
		}
	}
}
