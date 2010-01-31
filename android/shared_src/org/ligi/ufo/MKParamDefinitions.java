package org.ligi.ufo;

public interface MKParamDefinitions
{

    public final static int PARAMTYPE_BYTE=0;      // normal byte
    public final static int PARAMTYPE_MKBYTE=1;    // has potis @ end
    public final static int PARAMTYPE_BITSWITCH=2; // a bit aka boolean
    public final static int PARAMTYPE_STICK=3;     // a stick ( 1-12 )
    public final static int PARAMTYPE_KEY=4;       // a key
    public final static int PARAMTYPE_BITMASK=5;   // a bitmask ( byte )

    public final static int PARAMTYPE_CHOICE=6;

    //  choice must be last!

}
