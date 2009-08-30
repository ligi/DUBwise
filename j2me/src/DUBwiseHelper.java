/***************************************************************
 *
 * Helper functions for DUBwise
 *                                                           
 * Author:        Marcus -LiGi- Bueschleb
 * Mailto:        LiGi @at@ LiGi DOTT de                    
 * 
 ***************************************************************/

import java.util.Vector;
import java.io.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;

public final class DUBwiseHelper 
{
    public final static String ip_digit_zeroes(int digit)
    {	return "" + digit/100 + "" +   (digit/10)%10 + "" + (digit)%10;   }


    public final static String[] combine_str_arr(String[] arr1 , String[] arr2)
    {
	String[] res=new String[arr1.length+arr2.length];
	for (int pos=0;pos<res.length;pos++)
	    if (pos<arr1.length)
		res[pos]=arr1[pos];
	    else
		res[pos]=arr2[pos-arr1.length];
	return res;
    }

    public final static String pound_progress(int val,int max)
    {
	String res="" + (val+1) + "/" + max + " |";
	for (int i=0;i<max;i++)
		res+=(i<(val+1))?"#":"_";
	res+="|";
	return res;
    }
		


    public final static  String ip_str(int[] ip,boolean with_zeroes)
    {
	if(with_zeroes)
	    return ip_digit_zeroes(ip[0]) + "." +ip_digit_zeroes(ip[1]) + "."+ip_digit_zeroes(ip[2]) + "."+ip_digit_zeroes(ip[3]) + ":"+ip_digit_zeroes(ip[4]) ;
	else
	    return ip[0]+"."+ip[1]+"."+ip[2]+"."+ip[3]+":"+ip[4];
	    
    }




    
    public final static String get_http_string(String url) 
    {
	
	try {

	    InputStream stream = null;
	    StringBuffer buff = new StringBuffer();
	    StreamConnection conn=null;
	    
	    System.out.println("starting conn");
	    conn = (StreamConnection)Connector.open(url);
	    stream = conn.openInputStream();
	    int ch;
	    
	    while((ch = stream.read()) != -1) 
		    buff.append((char) ch);
	
	    if(stream != null) 
		stream.close();
	    
	    if(conn != null) 
		conn.close();
	    
	    
	    return buff.toString();
	    
	}
	catch ( Exception e)
	    {
		return "err";
	    }
	
    }
    
    



    public final static  int pow(int val,int pow)
    {
	int res=1;

	for (int p=0;p<pow;p++)
	    res*=val;

	return res;
    }





    public final static  String[] split_str(String str,String sep)
    {

	Vector nodes = new Vector();

	// Parse nodes into vector
	int index = str.indexOf(sep);
	while(index>=0) {
	    nodes.addElement( str.substring(0, index) );
	    str = str.substring(index+sep.length());
	    index = str.indexOf(sep);
	}
	// add  last element
	nodes.addElement( str );
	
	// Create splitted string array
	String[] result = new String[ nodes.size() ];
	if( nodes.size()>0 ) {
	    for(int loop=0; loop<nodes.size(); loop++)
		{
		    result[loop] = (String)nodes.elementAt(loop);
		    System.out.println(result[loop]);
		}
	    
	}

	return result;
    }



    public final static  int mod_decimal(int val,int mod_power,int modder,int setter,int clipper)
    {

	int res=0;

	for (int power=0;power<4;power++)
	    {

		int act_digit=(val/pow(10,power))%10;

		int new_digit=act_digit;
		if (power==mod_power)
		    {
			if (setter!=-1)
			    new_digit=setter;
			
			new_digit+=modder;
			
			if(new_digit<0)
			    new_digit=0;

			if(new_digit>clipper)
			    new_digit=clipper;

		    }

		//		new_digit=1;
		res+=new_digit*pow(10,power);
	    }
	
	
	return res;


    }


    public static Image scaleImage(Image original, int newWidth)
	// this function is from http://willperone.net/Code/codescaling.php
    {        
    
	int[] rawInput = new int[original.getHeight() * original.getWidth()];
        original.getRGB(rawInput, 0, original.getWidth(), 0, 0, original.getWidth(), original.getHeight());
        
	int newHeight=((original.getHeight()* (newWidth/10)) / (original.getWidth()/10));
	int[] rawOutput = new int[newWidth*newHeight];        
		

        // YD compensates for the x loop by subtracting the width back out
	int YD = (original.getHeight() / newHeight) * original.getWidth() - original.getWidth(); 
        int YR = original.getHeight() % newHeight; 
        int XD = original.getWidth() / newWidth;
        int XR = original.getWidth() % newWidth;     


	
        int outOffset= 0;
        int inOffset=  0;
        
        for (int y= newHeight, YE= 0; y > 0; y--) {            
            for (int x= newWidth, XE= 0; x > 0; x--) {
                rawOutput[outOffset++]= rawInput[inOffset];
                inOffset+=XD;
                XE+=XR;
                if (XE >= newWidth) {
                    XE-= newWidth;
                    inOffset++;
                }
            }            
            inOffset+= YD;
            YE+= YR;
            if (YE >= newHeight) {
                YE -= newHeight;     
                inOffset+=original.getWidth();
            }
        }               
        return Image.createRGBImage(rawOutput, newWidth, newHeight, false);        
    }



}
