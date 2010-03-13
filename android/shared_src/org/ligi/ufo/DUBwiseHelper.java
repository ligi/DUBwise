package org.ligi.ufo;

public class DUBwiseHelper {

	
	public static String make2charsStr(int val) {
		if (val<10)
			return "0"+val;
		else
			return ""+val;
	}
	
	public static String seconds2str(int seconds) {
		return "" + make2charsStr(seconds/60) + ":" + make2charsStr(seconds%60);
	}
	
	
}
