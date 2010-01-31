/*********************************************
 *                                            
 * class representing the StickData Structure 
 *                                            
 * Author:        Marcus -LiGi- Bueschleb     
 * 
 * see README for further Infos
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
