/**************************************************************************
 *                                          
 * Class to handle the MK-Mixer
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

public class MixerManager
{

	public final static String[] default_names={"Hexa","Hexa2","Octo","Octo-U","Octo2","Octo3","Q4Y6V2","Quadro","Quadro-X","Y6_VOYAGER"};
	public final static byte[][] default_arrays={{64,64,0,64,64,64,-64,-64,64,-64,-64,64,64,-64,0,-64,64,-64,64,64,64,64,64,-64,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{64,64,0,64,64,64,-64,-64,64,-64,-64,64,64,-64,0,-64,64,-64,64,64,64,64,64,-64,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{64,64,64,64,64,64,-64,-64,64,64,-64,64,64,-64,-64,-64,64,-64,-64,64,64,-64,64,-64,64,-64,64,64,64,64,64,-64,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{64,64,0,64,64,64,-64,-64,64,0,-64,64,64,-64,-64,-64,64,-64,0,64,64,-64,64,-64,64,0,64,64,64,64,64,-64,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{64,64,0,64,64,64,-64,-64,64,0,-64,64,64,-64,-64,-64,64,-64,0,64,64,-64,64,-64,64,0,64,64,64,64,64,-64,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{64,64,0,64,64,64,0,-64,64,0,-64,64,64,0,-64,-64,64,-64,0,64,64,-64,0,-64,64,0,64,64,64,0,64,-64,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{64,32,64,64,64,32,-64,64,64,-64,0,-64,76,32,64,-64,76,32,-64,-64,76,-64,0,64,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{64,64,0,64,64,-64,0,64,64,0,-64,-64,64,0,64,-64,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{64,64,64,64,64,-64,-64,64,64,64,-64,-64,64,-64,64,-64,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{76,32,-64,64,64,32,-64,-64,76,-64,0,-64,64,-64,0,64,76,32,64,64,64,32,64,-64,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};

        public final static byte VERSION_LENGTH=1;
	public final static byte NAME_LENGTH=12;
	public final static byte DATA_LENGTH=12*4;
	public final static byte COMPLETE_LENGTH=VERSION_LENGTH + NAME_LENGTH + DATA_LENGTH ;

	public final static byte TYPE_GAS=0;
	public final static byte TYPE_NICK=1;
	public final static byte TYPE_ROLL=2;
	public final static byte TYPE_YAW=3;

	public final static byte STATE_NO_DATA=0;
	public final static byte STATE_VALID_DATA=1;
	public final static byte STATE_INVALID_DATA=2;
	
	public byte state=STATE_NO_DATA;
	
	private String name;
	private byte[] data_array;
		
	public MixerManager() {
		name="unread";
		data_array=new byte[COMPLETE_LENGTH];
	}
	public static String[] getDefaultNames() {
		return default_names;
	}
	
	public static byte[][] getDefaultArrays() {
		return default_arrays;
	}
	
	public void setDefaultValues(byte pos) {
		name=default_names[pos];
		data_array=default_arrays[pos];
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name=name;
	}

	public byte[] getValuesByType(byte type) {
		byte[] res=new byte[12];
		for ( int i=0;i<12;i++)
			res[i]=data_array[i*4+type];
		return res;
	}
	
	public void setValuesByType(byte type,byte[] arr) {
		for ( int i=0;i<12;i++)
			data_array[i*4+type]=arr[i];
	}

	int mixer_version;
	public void setByMKData(int[] data)
	{
		name="";
		mixer_version=data[0];
		
		// TODO check for validity
		
		for (int i=0;i<NAME_LENGTH;i++)
			name+=(char)data[i+VERSION_LENGTH];
		
		for (int i=0;i<DATA_LENGTH;i++)
			data_array[i]=(byte)data[i+VERSION_LENGTH+NAME_LENGTH];
		
		state=STATE_VALID_DATA;
	}
	
	/*
	 * the byte arr we could write to the mk
	 */
    public int[] getFCArr()
    {
	
	int[] res= new int[COMPLETE_LENGTH ];

	res[0]=1;
	for ( int i=1;i<13;i++)
	    try 
		{
		    res[i]=(byte)name.charAt(i-1);
		}
	    catch (Exception e)
		{
		    res[i] =0;
		}

	for ( int i=0;i<12*4;i++)
	    res[1+12+i]=data_array[i];
	
	return res;
    }
    
    public byte getLastUsedEngine() {
    	byte[] gas_values=getValuesByType(TYPE_GAS);
    	byte last_used=0;
    	for (byte i=0;i<12;i++)
    		if (gas_values[i]!=0)
    			last_used=i;
    	return last_used;
    }
    
}
