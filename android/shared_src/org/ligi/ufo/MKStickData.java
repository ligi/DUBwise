/*********************************************
 *                                                                                      
 * Author:        Marcus -LiGi- Bueschleb     
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
 *
 ********************************************/

package org.ligi.ufo;

/**
 * class representing the StickData Structure  
 */
public class MKStickData {
	
    public final static int MAX_STICKS=12;
    public final static byte POTI_COUNT=8;

    // holing stick data
    private int[] stick_value=null;

    /**
     * set data with decoded payload of MK Package
     *  
     * @param in_arr
     */
    public void set_by_mk_data(int[] in_arr) {
    	stick_value=new int[MAX_STICKS];
    	for (int i=0;i<MAX_STICKS;i++) 
    		stick_value[i]=MKHelper.parse_signed_int_2(in_arr[i*2], in_arr[1+i*2]);
    }

    /**
     * return the value for a given stick
     * or -1 for no data avialable
     * 
     * @param stick_id
     * @return
     */
    public int getStickValue(byte stick_id) {
    	if (stick_value==null)
    		return -1;
    	
    	return stick_value[stick_id];
    }
}
