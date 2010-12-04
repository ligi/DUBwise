/*********************************************************************
 *                                   
 * class representing the MK-Version 
 * Author:        Marcus -LiGi- Bueschleb
 * Project-Start: 9/2007                                                                                                          *
 * 
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
 **********************************************************************/

package org.ligi.ufo;

public class MKVersion

{
    public int major=-1;
    public int minor=-1;
    public int proto_major=-1;
    public int proto_minor=-1;
    public int patch=-1;
    private int slave_addr;
    
    public String version_str="";
    public String proto_str="";

    public boolean known=false;    // version known?

    public final static byte VERSION_AFTER=0;
    public final static byte VERSION_EQUAL=1;
    public final static byte VERSION_PREVIOUS=2;

    
    public void reset()    {
		major=-1;
		minor=-1;
		proto_major=-1;
		proto_minor=-1;
		patch=-1;
	
		version_str="";
		proto_str="";
    }
    
    public void set_by_mk_data(int[] data,int slave_addr) {
    	this.slave_addr=slave_addr;
    	major=data[0];
    	minor=data[1];
    	proto_major=data[2];
    	proto_minor=data[3];
    	patch=data[4];

    	version_str="v"+major+"."+minor + (char)('a'+patch);

    	proto_str="v"+proto_major+"."+proto_minor ;
    	known=true;
    }
    
    public byte compare(int major_c,int minor_c)
    	{
    	if ((major_c==major)&&(minor_c==minor))
    		return VERSION_EQUAL;
    	
    	else if ((major_c*1000+minor_c)>(minor + major*1000)) 
    		return VERSION_AFTER;
    	
    	return VERSION_PREVIOUS;
    	}	

    public boolean isEqualOrNewerThan(int major_c,int minor_c) {
    	return compare(major_c,minor_c)!=VERSION_PREVIOUS;
    }


	public int getSlaveAddr() {
		return slave_addr;
	}
}
