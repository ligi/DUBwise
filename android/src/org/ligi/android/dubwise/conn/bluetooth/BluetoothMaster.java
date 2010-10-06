/**************************************************************************
 *                                          
 * this file is a part of DUBwise
 *                                          
 * Author:  Marcus -LiGi- Bueschleb   
 * http://ligi.de
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

package org.ligi.android.dubwise.conn.bluetooth;

import it.gerdavax.easybluetooth.BtSocket;
import it.gerdavax.easybluetooth.ConnectionListener;
import it.gerdavax.easybluetooth.LocalDevice;
import it.gerdavax.easybluetooth.ReadyListener;

import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.tracedroid.Log;
import org.ligi.ufo.MKHelper;
import org.ligi.ufo.MKProtocolDefinitions;

import android.content.Context;
import android.os.Looper;

/**
 * class to let DUBwise be a Bluetooth Master so that another phone 
 * can get UFO info too
 * -> so we have multiple screens
 * 
 * @author ligi
 *
 */
public class BluetoothMaster implements Runnable {

	private static BluetoothMaster singleton=null;
	private Context ctx=null;
	
	public BluetoothMaster(Context ctx) {
		this.ctx=ctx;
		
		 class myReadyListener extends ReadyListener {

				private Thread threat2start;
				public myReadyListener(Thread thread) {
					this.threat2start=thread;
				}
				
			@Override
			public void ready() {
//				MyConnectionListener my_conn_listener=new MyConnectionListener();
//				LocalDevice.getInstance().listenForConnection(my_conn_listener, 22);

				threat2start.start();
				}
			}
		
		 Log.i("BluetoothMaster initing BT");
		 LocalDevice.getInstance().init(ctx, new myReadyListener(new Thread(this)));
		
	}

	public static void restart() {
		new Thread(singleton).start();
	}
	
	public static void init(Context ctx) {
		if (singleton==null)
			singleton=new BluetoothMaster(ctx);
	}
	
	 class MyConnectionListener extends ConnectionListener {
		 
			private boolean closed=false;
			
			@Override
			public void connectionError() {
				// TODO Auto-generated method stub
				Log.i("BluetoothMaster accepted conn err");	
				closed=true;
			}
			
			public boolean isClosed() {
				return closed;
			}

			@Override
			public void connectionWaiting(BtSocket arg0) {
				// TODO Auto-generated method stub
				Log.i("BluetoothMaster accepted conn");
			while(!closed) {
				try {
					//arg0.getOutputStream().write(("Nick" + MKProvider.getMK().debug_data.analog[0] + "\n").getBytes());
					
					arg0.getOutputStream().write(MKHelper.encodeCommand(MKProtocolDefinitions.NAVI_SLAVE_ADDR, 'V', new int[]{0,79,0,5,5}));
					int[] debug_params = new int[66];
					for (int i=0;i<32;i++) {
						debug_params[i*2+2]=MKProvider.getMK().debug_data.analog[i]&0xFF;
						debug_params[i*2+3]=MKProvider.getMK().debug_data.analog[i]>>8;
					}
					arg0.getOutputStream().write(MKHelper.encodeCommand(MKProtocolDefinitions.NAVI_SLAVE_ADDR, 'D', debug_params));
					try {
					//arg0.getOutputStream().write((MKProvider.getMK().data_buff[0]+"\r\n").getBytes());
					}
					catch (Exception e) {
						
					//x	MKHelper.encodeCommand(MKProtocolDefinitions.FAKE_SLAVE_ADDR, 'D', e.toString().getBytes());
					}
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
				closed=true;
				try {
					Thread.sleep(333);
					arg0.close();
				
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				BluetoothMaster.restart();
			}
			}
			}
			 
		 }


	@Override
	public void run() {
		Looper.prepare();
		Log.i("BluetoothMaster ready");
		
		MyConnectionListener my_conn_listener=new MyConnectionListener();
		new Thread(new ConnStarter(my_conn_listener)).start();
		Looper.loop();
		
		/*
		while(true) {
			
			MyConnectionListener my_conn_listener=new MyConnectionListener();

			new Thread(new ConnStarter(my_conn_listener)).start();
			
			while(!my_conn_listener.isClosed()) {
				Log.i("BluetoothMaster conn listener not closed");
				
				Looper.loop();
				
				try {
					Thread.sleep(1001);
				} catch (Exception e) {	} 

				}
			
		}
*/
	}
	
	
	class ConnStarter implements Runnable {
	
		private MyConnectionListener my_conn_listener;
		
		public ConnStarter(MyConnectionListener my_conn_listener) {
			this.my_conn_listener=my_conn_listener;
		}
		
		@Override
		public void run() {
			Looper.prepare();
			try {
				LocalDevice.getInstance().listenForConnection(my_conn_listener, 22);
				Looper.loop();
			} catch (Exception e) {} 
		}
	}
}