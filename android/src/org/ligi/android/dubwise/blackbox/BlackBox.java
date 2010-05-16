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
 *  Organizations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

package org.ligi.android.dubwise.blackbox;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.tracedroid.logging.Log;
import org.ligi.ufo.MKCommunicator;

/**
 * class to persist telemetry Data
 * 
 * @author ligi
 *
 */
public class BlackBox implements Runnable {

	private static BlackBox singleton=null;
	private String act_fname="none";
	private int act_records=0;
	public static void init() {
		if (singleton==null)
		{
			singleton=new BlackBox();
			new Thread(singleton).start();
		}
	}
	
	public static BlackBox getInstance() {
		init();
		return singleton;
	}
	
	public String getActFileName() {
		return act_fname;
	}

	@Override
	public void run() {
		while (true)
		{
			MKCommunicator mk=MKProvider.getMK();
			if (mk.isConnected()&&mk.isFlying())
			{
				
				 DateFormat path_dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				 DateFormat fname_dateFormat = new SimpleDateFormat("HH_mm_ss");
			     Date date = new Date();
			     
			     // create the path
			     new File(BlackBoxPrefs.getPath()+"/"+path_dateFormat.format(date)).mkdirs();
			     
			     act_fname=BlackBoxPrefs.getPath()+"/"+path_dateFormat.format(date)+"/"+fname_dateFormat.format(date)+".csv";
			     File outFile=new File(act_fname);
			     act_records=0;
			     try {
					FileWriter writer=new FileWriter(outFile);
					
					
					String header="";
					
					for (int i =0 ; i<mk.debug_data.names.length;i++)
						header+=((i==0)?"":";") + mk.debug_data.names[i];
					header+="\n";
					writer.write(header);
					while (mk.isConnected()&&mk.isFlying())
					{
						String act_line="";
						
						
						for (int i =0 ; i<mk.debug_data.names.length;i++)
							act_line+=((i==0)?"":";") + mk.debug_data.analog[i];
						act_line+="\n";
						writer.write(act_line);
						act_records++;
						

						try {
							Thread.sleep(50); 
						} catch (InterruptedException e) {}
						
					}
					
					
					writer.close();
					
				} catch (IOException e) {
					Log.w("problem writing the BlackBox File" + e);
				}
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
	
		
			
	}
}
