/*******************************************************************
 * DUBwise
 * by Marcus -Ligi- Bueschleb 
 * http://ligi.de
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

/**
 * class with helper-Functions for communicating with MikroKopter Hardware
 
 * @author Marcus -ligi- Bueschleb
 *
 */
public final class MKHelper
{

	public final static int parse_signed_int_2(int i1,int i2) {
		int res=(int)((i2<<8)|i1);
		if ((res&(1<<15))!=0)
		    return -(res&(0xFFFF-1))^(0xFFFF-1); // TODO check!
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
   
   public final static void int32ToByteArr(int val,byte[] arr,int offset) {
	   arr[offset]=(byte)((0xFF)&(val));	   
	   arr[offset+1]=(byte)((0xFF)&(val>>8));	   
	   arr[offset+2]=(byte)((0xFF)&(val>>16));	   
	   arr[offset+3]=(byte)((0xFF)&(val>>24));	   
   }
   
   /**
    * test if a bit is set in a integer
    * 
    * @param val - the value
    * @param pos - position in the value
    * @return
    */
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

   public static byte[] encodeCommand(byte modul,char cmd,byte[] params) {
	   
	   	byte[] res=new byte[3 + (params.length/3 + (params.length%3==0?0:1) )*4 +3]; // 5=1*start_char+1*addr+1*cmd+2*crc  + line break
		res[0]='#';
		res[1]=(byte)(modul+'a');
		res[2]=(byte)cmd;
		
		for(int param_pos=0;param_pos<(params.length/3 + (params.length%3==0?0:1)) ;param_pos++) {
			byte a = (param_pos*3<params.length)?params[param_pos*3]:0;
			byte b = ((param_pos*3+1)<params.length)?params[param_pos*3+1]:0;
			byte c = ((param_pos*3+2)<params.length)?params[param_pos*3+2]:0;

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

   /**
    * @return the magic sequence which swithes the MK to navi mode
    */
   public final static byte[] getNaviSwitchMagicSequence() {
	   return new byte[] {27,27,0x55,(byte)0xAA,0,(byte)'\r'};
   }

   /**
    * decode the pseudo Base64 which is used by MikroKopter Hardware
    * 
    * @param in_arr - the array to process
    * @param offset - skip bytes in the arr
    * @param len - count of bytes to process
    * @return
    */
   public static int[] Decode64(byte[] in_arr, int offset,int len) {
		int ptrIn=offset;	
		int a,b,c,d,x,y,z;
		int ptr=0;
		
		int[] out_arr=new int[len];
	
		while(len!=0) {
			a=0;
			b=0;
			c=0;
			d=0;
			try {
				a = in_arr[ptrIn++] - '=';
				b = in_arr[ptrIn++] - '=';
				c = in_arr[ptrIn++] - '=';
				d = in_arr[ptrIn++] - '=';
			}
			catch (Exception e) { }

			x = ((a << 2) | (b >> 4))&0xFF;
			y = ((b & 0x0f) << 4) | (c >> 2);
			z = ((c & 0x03) << 6) | d;
	
			if((len--)!=0) out_arr[ptr++] = x; else break;
			if((len--)!=0) out_arr[ptr++] = y; else break;
			if((len--)!=0) out_arr[ptr++] = z; else break;
		    }
		
		return out_arr;
   }

}
