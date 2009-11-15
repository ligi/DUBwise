/************************************
 *                                   
 * class representing the MK-Version 
 * Author:        Marcus -LiGi- Bueschleb
 * Project-Start: 9/2007                                                                                                          *
 * 
 * see README for further Infos
 *
 ****************************************/
package org.ligi.ufo;

public class MKVersion

{
    public int major=-1;
    public int minor=-1;
    public int proto_major=-1;
    public int proto_minor=-1;
    public int patch=-1;

    public String version_str="";
    public String proto_str="";

    public void reset()
    {
	major=-1;
	minor=-1;
	proto_major=-1;
	proto_minor=-1;
	patch=-1;

	version_str="";
	proto_str="";
    }
    

    // version known?
    public boolean known=false;

    public final byte VERSION_AFTER=0;
    public final byte VERSION_EQUAL=1;
    public final byte VERSION_PREVIOUS=2;


    public void set_fake_data()
    {
	int[] fake_data={0,23,0,5,5};
	set_by_mk_data(fake_data);
    }

    public void set_by_mk_data(int[] data)
    {
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
	// TODO - compare major - PC-COMPATIBLE
	else if (minor_c>minor) return VERSION_AFTER;
	return VERSION_PREVIOUS;
	
    }

}
