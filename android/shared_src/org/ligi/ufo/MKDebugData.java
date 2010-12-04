/**************************************************************************
 *                                          
 * class representing the DebugData and the names of the values 
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

public class MKDebugData {
	public final static int MAX_VALUES=32;
    public int[] analog;
    public String[] names;
    public boolean[] got_name;
    
    private int i;

    public int motor_val(int id) {	return analog[12+id];    }
    
    public MKDebugData()  {
		names=new String[32];
		analog=new int[32];
		got_name=new boolean[32];
		for (i=0;i<32;i++) {
		    analog[i]=-1;
		    names[i]="-#"+i+"->";
		    got_name[i]=false;
		    }
    }

    public void set_names_by_mk_data(int[] in_arr) {
		int id=in_arr[0];
		names[id]="";
		for (i=0;i<16;i++) {
			if ((char)in_arr[i+1]!=' ')
			    names[id]+=(char)in_arr[i+1];
			got_name[id]=true;
		    }
		names[id]+=":";
    }


    public void set_by_mk_data(int[] in_arr,MKVersion version)  {
    	for (i=0;i<32;i++)
    		analog[i]=MKHelper.parse_signed_int_2( in_arr[2+i*2], in_arr[3+i*2] );

    	switch(version.getSlaveAddr()) {
    		case MKCommunicator.FC_SLAVE_ADDR:
    	
    			if (version.compare(0, 78)==MKVersion.VERSION_PREVIOUS) {
    	    		VesselData.battery.setCurrent(analog[22]);
    	    		VesselData.battery.setUsedCapacity(analog[23]);
    			}
    		case MKCommunicator.RIDDIM_SLAVE_ADDR:
    			VesselData.battery.setUBatt(analog[9]);
    			break;
    		case MKCommunicator.FOLLOWME_SLAVE_ADDR:
    			VesselData.battery.setUBatt(analog[8]);
    			break;
    	}


    	
    }
}
