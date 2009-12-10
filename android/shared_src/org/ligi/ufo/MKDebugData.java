/*********************************************
 *                                            
 * class representing the DebugData and the names of the values 
 *                                            
 * Author:        Marcus -LiGi- Bueschleb     
 * 
 * see README for further Infos
 *
 ********************************************/

package org.ligi.ufo;

import java.util.Random;

public class MKDebugData 
{

    public int[] analog;
    public String[] names;
    public boolean[] got_name;
    //    public int motor_complete=-1;

    
    private int i;

    public int motor_val(int id) {	return analog[12+id];    }
    /*    public int nick_int() {	return analog[0];    }
    public int roll_int() {	return analog[1];    }
    public int accnick() {	return analog[2];    }
    public int accroll() {	return analog[3];    }
    */

    //    public int UBatt() {	return analog[9];    }
    //    public int SenderOkay() {	return analog[10];    }

    Random random;

    public MKDebugData() 
    {
	random = new Random(); // for fake conn

	names=new String[32];
	analog=new int[32];
	got_name=new boolean[32];
	for (i=0;i<32;i++)
	    {
	    analog[i]=-1;
	    names[i]="-#"+i+"->";
	    got_name[i]=false;
	    }

    }

    public void set_names_by_mk_data(int[] in_arr)
    {
	int id=in_arr[0];
	names[id]="";
	for (i=0;i<16;i++)
	    {
		if ((char)in_arr[i+1]!=' ')
		    names[id]+=(char)in_arr[i+1];
		got_name[id]=true;
	    }
	names[id]+=":";
    }


    public void set_fake_data(){
    	for (int i=0;i<16;i++)
    		analog[i]+=(random.nextInt()%5);
	
    	// 	alt
    	analog[5]+=(random.nextInt()%2);
    }

    public void set_by_mk_data(int[] in_arr,MKVersion version)
    {

	for (i=0;i<32;i++)
	    analog[i]=MKHelper.parse_signed_int_2( in_arr[2+i*2], in_arr[3+i*2] );
    
	//	motor_complete=motor_val(0)+motor_val(1)+motor_val(2)+motor_val(3);
    }



}
