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

}
