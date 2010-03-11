/*********************************************
 *                                            
 * class representing the StickData Structure 
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

public class MKStickData 
{
	public final static int MAX_STICKS=12;
    // holing stick data
    public int[] stick;

    // general counter
    private int i;

    public MKStickData() 
    {

	stick=new int[MAX_STICKS];
	for (i=0;i<MAX_STICKS;i++)
	    stick[i]=-1;

    }

    public void set_by_mk_data(int[] in_arr)
    {

	for (i=0;i<MAX_STICKS;i++)
	    {			
			stick[i]=(int)((in_arr[1+i*2]<<8) | in_arr[i*2]);
			if ((stick[i]&(1<<15))!=0)
			    stick[i]=-(stick[i]&(0xFFFF-1))^(0xFFFF-1);
	    }

    }



}
