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
    public final static int parse_signed_int_2(int i1,int i2) {
	int res=(int)((i2<<8)|i1);
	if ((res&(1<<15))!=0)
	    return -(res&(0xFFFF-1))^(0xFFFF-1);
	else
	    return res;

    }


   public final static int parse_unsigned_int_2(int i1,int i2) {
	return (int)((i2<<8)|i1);
    }

   
   public final static int parse_arr_4(int offset,int[] in_arr) {
	return ((in_arr[offset+3]<<24) |
		(in_arr[offset+2]<<16) |
		(in_arr[offset+1]<<8)  |
		(in_arr[offset+0]));
   }

   public final static int parse_arr_2(int offset,int[] in_arr) {
	return (((in_arr[offset+1]&0xFF)<<8)  |
		(in_arr[offset+0]&0xFF ));
   }
   
   public final static boolean isBitSet(int val,int pos) {
	   return (val&(1<<pos))!=0;
   }

   
   /**
    * encode a command in the mikrokopter style
    *
    * @param modul  - to which module ( FC / NC / ... )
    * @param cmd    - which command
    * @param params - parameters for the command
    * @return
    */
   public static byte[] encodeCommand(byte modul,char cmd,int[] params) {
	   	byte[] res=new byte[3 + (params.length/3 + (params.length%3==0?0:1) )*4 +3]; // 5=1*start_char+1*addr+1*cmd+2*crc  + line break
		res[0]='#';
		res[1]=(byte)(modul+'a');
		res[2]=(byte)cmd;
		
		for(int param_pos=0;param_pos<(params.length/3 + (params.length%3==0?0:1)) ;param_pos++)
		    {
			int a = (param_pos*3<params.length)?params[param_pos*3]:0;
			int b = ((param_pos*3+1)<params.length)?params[param_pos*3+1]:0;
			int c = ((param_pos*3+2)<params.length)?params[param_pos*3+2]:0;

			res[3+param_pos*4] =  (byte)((a >> 2)+'=' );
			res[3+param_pos*4+1] = (byte)('=' + (((a & 0x03) << 4) | ((b & 0xf0) >> 4)));
			res[3+param_pos*4+2] = (byte)('=' + (((b & 0x0f) << 2) | ((c & 0xc0) >> 6)));
			res[3+param_pos*4+3] = (byte)('=' + ( c & 0x3f));
		    }
		int tmp_crc=0;
		
		for ( int tmp_i=0; tmp_i<res.length-3;tmp_i++)
		    tmp_crc+=(int)res[tmp_i];
			
		tmp_crc%=4096;
		
		res[res.length-3]=(byte)((char)(tmp_crc/64 +'='));
		res[res.length-2]=(byte)((char)(tmp_crc%64+'='));
		res[res.length-1]=(byte)('\r');
		return res;
   }

}
