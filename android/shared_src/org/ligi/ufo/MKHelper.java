/*******************************************************************
 *
 * Helper Functions for Communicating with MK Hardware
 * 
 * License:
 *
 *  http://creativecommons.org/licenses/by-nc-sa/2.0/de/ 
 *  (Creative Commons / Non Commercial / Share Alike)
 *  Additionally to the Creative Commons terms it is not allowed
 *  to use this project in _any_ violent manner! 
 *  This explicitly includes that lethal Weapon owning "People" and 
 *  Organisations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **********************************************************************/

package org.ligi.ufo;

public final class MKHelper
{
    public final static int parse_signed_int_2(int i1,int i2)
    {
	int res=(int)((i2<<8)|i1);
	if ((res&(1<<15))!=0)
	    return -(res&(0xFFFF-1))^(0xFFFF-1);
	else
	    return res;

    }


   public final static int parse_unsigned_int_2(int i1,int i2)
    {
	return (int)((i2<<8)|i1);
    }

   
   public final static int parse_arr_4(int offset,int[] in_arr)
   {
	return ((in_arr[offset+3]<<24) |
		(in_arr[offset+2]<<16) |
		(in_arr[offset+1]<<8)  |
		(in_arr[offset+0]));
   }

   public final static int parse_arr_2(int offset,int[] in_arr)
   {
	return (((in_arr[offset+1]&0xFF)<<8)  |
		(in_arr[offset+0]&0xFF ));
   }
   
   public final static boolean isBitSet(int val,int pos) {
	   return (val&(1<<pos))!=0;
   }
}
