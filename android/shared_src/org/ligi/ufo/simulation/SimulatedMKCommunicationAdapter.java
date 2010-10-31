/**************************************************************************
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

package org.ligi.ufo.simulation;

import java.util.Vector;

import org.ligi.tracedroid.logging.Log;
import org.ligi.ufo.CommunicationAdapterInterface;
import org.ligi.ufo.MKHelper;
import org.ligi.ufo.MKParamsParser;
import org.ligi.ufo.MKProtocolDefinitions;
import org.ligi.ufo.MKStickData;

/**                                          
* CommunicationAdapter for a fake connection - can be used as a test too
**/
public class SimulatedMKCommunicationAdapter implements
		CommunicationAdapterInterface,Runnable {
	
	private Vector send_stack;
	private int[] debug_data;
	private int[] navi_osd_data;
	private int[] stick_data;
	
	private int[] lcd_lines;
	private int lcd_pagecount=1;
	
	public void string2lcdlines(String str,int line) {
		str2arr(str,lcd_lines,line*20+2);
	}
	
	public SimulatedMKCommunicationAdapter()   {
		
		lcd_lines=new int[2+4*20];
		
		send_stack=new Vector();
    	
		send_stack.addElement(MKHelper.encodeCommand(MKProtocolDefinitions.NAVI_SLAVE_ADDR, 'V', new int[] { 0,80,0,10,2  }));
		
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

	public void str2arr(String str,int[] arr,int offset) {

		for (int i=0;i<20;i++)
			try {
				arr[offset+i]=str.charAt(i);
			} catch ( Exception e) {
				arr[offset+i]=' ';
			}
	}
	
	private byte[] buff=new byte[512];
	private int buff_len=0;
	private char last_cmd;
	
	public void write(byte[] buffer, int offset, int count) {
		for (int i=0;i<count;i++) 
			switch (buffer[i]) {
				case '#':
					buff_len=0;
					i+=2;
					last_cmd=(char)buffer[i];
					break;
				case '\r':
					process_cmd( last_cmd, MKHelper.Decode64(buff, 0, buff_len));
					break;
				case '\n':
					break;
				default:
					buff[buff_len]=buffer[i];
					buff_len++;
					break;
			}
	}

	
	public void process_cmd(char cmd,int[] params) {
		switch (cmd) {
			case 'a':
				int[] analog_name_arr=new int[20];
				for (int c=0;c<32;c++) {
					str2arr((char)c+"Analog Fake "+c,analog_name_arr,0);
					send_stack.addElement(MKHelper.encodeCommand(MKProtocolDefinitions.NAVI_SLAVE_ADDR, 'A', analog_name_arr));
				}
				break;
			case 'e':
				int[] err_str=new int[20];
				str2arr("No Error",err_str,0);
				send_stack.addElement(MKHelper.encodeCommand(MKProtocolDefinitions.NAVI_SLAVE_ADDR, 'E', err_str));
				err_str=null;
				break;
			case 'l':
				lcd_lines[1]=2;
				
				switch (params[0]) {
					case 0:	
						lcd_lines[0]=0;
						string2lcdlines("Simulated Mikro-",0);
						string2lcdlines("Kopter Communication ",1);
						string2lcdlines("",2);
						string2lcdlines("site 1/2",3);
						send_stack.addElement(MKHelper.encodeCommand(MKProtocolDefinitions.NAVI_SLAVE_ADDR, 'L', lcd_lines));			
						break;
					case 1:
						lcd_lines[0]=1;
						string2lcdlines("(cc) ligi@ligi.de",0);
						string2lcdlines("2010",1);
						string2lcdlines("",2);
						string2lcdlines("site 2/2",3);
						send_stack.addElement(MKHelper.encodeCommand(MKProtocolDefinitions.NAVI_SLAVE_ADDR, 'L', lcd_lines));
						break;
				}
				break;
			case 'q':
				Log.i("sending Q!!");
				send_stack.addElement(MKHelper.encodeCommand(MKProtocolDefinitions.FC_SLAVE_ADDR, 'Q', MKParamsParser.default_params[params[0]-1]));
				break;
		}	
	}
	
	public void write(byte[] buffer) {
		write(buffer,0,buffer.length);
	}

	public void write(int oneByte) {
		write ( new byte[] {(byte)oneByte});
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
