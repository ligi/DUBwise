/****************************************************
 *                                            
 * class representing the MK Navi OSD Data Structure 
 *                                            
 * Author:        Marcus -LiGi- Bueschleb     
 * 
 * see README for further Infos
 *
 * Some code taken from here:
 * http://www.koders.com/java/fidFC75A641A87B51BB757E9CD3136C7886C491487F.aspx
 * 
 * and
 * http://www.movable-type.co.uk/scripts/latlong.html
 *
 * thanx a lot for sharing!
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
 *
 *
 *****************************************************/

package org.ligi.ufo;


import java.lang.Math;
public class MKGPSPosition 
    implements DUBwiseDefinitions
{

	public final static int STATUS_INVALID=0;
	public final static int STATUS_NEWDATA=1;
	
    public byte act_gps_format=GPS_FORMAT_DECIMAL;
    public byte act_speed_format=SPEED_FORMAT_KMH;

    public final static int MAX_WAYPOINTS=100;

    public int[] LongWP;
    public int[] LatWP;
    public String[] NameWP;

    public int last_wp=0;

    public int Longitude=123269000;
    public int Latitude= 513662670;
        
    public int Altitude;

    public int TargetLongitude;
    public int TargetLatitude;
    public int TargetAltitude;

    public int HomeLongitude=123268230;
    public int HomeLatitude= 513661730;
    public int HomeAltitude;

    public int Distance2Target;
    public int Angle2Target;

    public int Distance2Home;
    public int Angle2Home;

    public byte SatsInUse=-1;
    public byte WayPointNumber=-1;
    public byte WayPointIndex=-1;
	
    public int AngleNick = -1;
    public int AngleRoll = -1;
    public int SenderOkay = -1;
    public int FCFlags= -1;
    public int NCFlags= -1;
    public int ErrorCode= 0;

    public int UsedCapacity =-1;
    public int Current =-1;

    public int Altimeter=-1; // hight according to air pressure
    public int Variometer=-1; // climb(+) and sink(-) rate
    public int FlyingTime=-1;

    public int GroundSpeed=-1;
    public int Heading=-1;
    public int CompasHeading=-1;

    // Constructor
    public MKGPSPosition() {
		LongWP=new int[MAX_WAYPOINTS];
		LatWP=new int[MAX_WAYPOINTS];
		NameWP=new String[MAX_WAYPOINTS];
		last_wp=0;
	    }

//#if cldc11=="on"
    public static final double PI = Math.PI;
    public static final double PI_div2 = PI / 2.0;
    public static final double PI_div4 = PI / 4.0;
    public static final double RADIANS = PI / 180.0;
    public static final double DEGREES = 180.0 / PI;

    private static final double p4 = 0.161536412982230228262e2;
    private static final double p3 = 0.26842548195503973794141e3;
    private static final double p2 = 0.11530293515404850115428136e4;
    private static final double p1 = 0.178040631643319697105464587e4;
    private static final double p0 = 0.89678597403663861959987488e3;
    private static final double q4 = 0.5895697050844462222791e2;
    private static final double q3 = 0.536265374031215315104235e3;
    private static final double q2 = 0.16667838148816337184521798e4;
    private static final double q1 = 0.207933497444540981287275926e4;
    private static final double q0 = 0.89678597403663861962481162e3;

    private static double _ATAN(double X) {
	if (X < 0.414213562373095048802)
	    return _ATANX(X);
        else if (X > 2.414213562373095048802)
	    return PI_div2 - _ATANX(1.0 / X);
        else 
	    return PI_div4 + _ATANX((X - 1.0) / (X + 1.0));
    }

    private static double _ATANX(double X){
    	double XX = X * X;
    	return X * ((((p4 * XX + p3) * XX + p2) * XX + p1) * XX + p0)/ (((((XX + q4) * XX + q3) * XX + q2) * XX + q1) * XX + q0);
    }

    public double aTan2(double Y, double X) {

		if (X == 0.0) {
		    if (Y > 0.0) 
			return PI_div2;
	      
		    else if (Y < 0.0) 
			return -PI_div2;
		    else 
			return 0.0;
		}
	
		// X<0
		else if (X < 0.0) {
		    if (Y >= 0.0) 
		    	return (PI - _ATAN(Y / -X)); // Y>=0,X<0 |Y/X|
		    else 
		    	return -(PI - _ATAN(Y / X)); // Y<0,X<0 |Y/X|
		
		}
	
		// X>0
		else if (X > 0.0) {
		    if (Y > 0.0) 
		    	return _ATAN(Y / X);
		    else 
		    	return -_ATAN(-Y / X);
		    }

    return 0.0;
  }

    public int distance(int _lat1,int _lon1,int _lat2,int _lon2) {
		double lat1=(_lat1/10000000.0)*RADIANS;
		double long1=(_lon1/10000000.0)*RADIANS;
	
		double lat2=(_lat2/10000000.0)*RADIANS;
		double long2=(_lon2/10000000.0)*RADIANS;
	
		double dLat= (lat2-lat1);
		double dLon= (long2-long1);
	
		double a = Math.sin(dLat/2.0) * Math.sin(dLat/2.0) +
	        Math.cos(lat1) * Math.cos(lat2) * 
	        Math.sin(dLon/2.0) * Math.sin(dLon/2.0); 
	
		return (int)(( 2.0 * aTan2(Math.sqrt(a), Math.sqrt(1.0-a)) )*6371008.8);
    }

    public int distance2wp(int id) {
    	return distance(Latitude,Longitude,LatWP[id],LongWP[id]);
    }

    public int angle2wp(int id) {
    	
		// TODO reuse from distance
		double lat1=(Latitude/10000000.0)*RADIANS;
		double long1=(Longitude/10000000.0)*RADIANS;
	
		double lat2=(LatWP[id]/10000000.0)*RADIANS;
		double long2=(LongWP[id]/10000000.0)*RADIANS;
	
		double dLat= (lat2-lat1);
		double dLon= (long2-long1);
	
		double y = Math.sin(dLon) * Math.cos(lat2);
		double x = Math.cos(lat1)*Math.sin(lat2) -   Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon);
		return ((int)(aTan2(y, x)*DEGREES)+360)%360;
    }


//#endif

//#if cldc11!="on"
//#    public int distance2wp(int id)
//#    {
//#	return -1;
//#    }

//#    public int angle2wp(int id)
//#    {
//#	return -1;
//#    }
//#endif

    public void push_wp() {
		LongWP[last_wp]=Longitude;
		LatWP[last_wp]=Latitude;
		last_wp++;
    }

    /*    public void next_gps_format()
    {
	act_gps_format=(byte)((act_gps_format+1)%GPS_FORMAT_COUNT);
	}*/

    public String gps_format_str(int val,int format) {
    	switch(format) {
	    case GPS_FORMAT_DECIMAL:
	    	return "" + val/10000000 + "." +val%10000000  ;
	    
	    case GPS_FORMAT_MINSEC:
	    	return "" +  val/10000000 + "^" +  ((val%10000000)*60)/10000000 + "'" + ((((val%10000000)*60)%10000000)*60)/10000000 +  "." + ((((val%10000000)*60)%10000000)*60)%10000000;
	    
	    default: 
	    	return "invalid format" + act_gps_format;
	    }
    }

        
    public String act_gps_format_str(int val) {
    	return gps_format_str(val,act_gps_format);
    }

    public String act_speed_format_str(int val) {
		switch(act_speed_format) {
		    case SPEED_FORMAT_KMH:
			return "" +  ((((val*60)/100)*60)/1000) + " km/h";
	
		    case SPEED_FORMAT_MPH:
			return "" +  (((((val*60)/100)*60)/1000)*10)/16 + " m/h";
	
		    case SPEED_FORMAT_CMS:
			return "" + val+ " cm/s";
			
		    default: 
			return "invalid speed format";
		    }
    }

    public String GroundSpeed_str() {
    	return act_speed_format_str(GroundSpeed);
    }

    public String WP_Latitude_str(int id) {
    	return act_gps_format_str(LatWP[id]); //+ "''N"  ;
    }

    public String WP_Longitude_str(int id) {
		return act_gps_format_str(LongWP[id]); //+ "''E"  ;
	}

    public String Latitude_str() {
		return act_gps_format_str(Latitude) ;
	}

    public String Longitude_str() { 
    	return act_gps_format_str(Longitude);   
    }

    public String TargetLatitude_str() { 
    	return act_gps_format_str(TargetLatitude);   
    }

    public String TargetLongitude_str() {
    	return act_gps_format_str(TargetLongitude);  
    }

    public String HomeLatitude_str() { 
    	return act_gps_format_str(HomeLatitude);   
    }

    public String HomeLongitude_str() { 
    	return act_gps_format_str(HomeLongitude);    
    }

    public boolean areEnginesOn() {
    	return MKHelper.isBitSet(FCFlags, 0); 
    }

    public boolean isFlying() {
    	return MKHelper.isBitSet(FCFlags, 1); 
    }

    public boolean isCalibrating() {
    	return MKHelper.isBitSet(FCFlags, 2); 
    }


    public boolean isStarting() {
    	return MKHelper.isBitSet(FCFlags, 3); 
    }
    

    public boolean isEmergencyLanding()	{
   		return MKHelper.isBitSet(FCFlags, 4); 
    }
    
    public boolean isLowBat() {
		return MKHelper.isBitSet(FCFlags, 5); 
    }


    public boolean isSPIRcErr()	{
		return MKHelper.isBitSet(FCFlags, 6); 
    }	
    
    public boolean isFreeModeEnabled() {
    	return MKHelper.isBitSet(NCFlags, 0); 
    }

    public boolean isPositionHoldEnabled() {
    	return MKHelper.isBitSet(NCFlags, 1); 
    }
    

    public boolean isComingHomeEnabled() {
    	return MKHelper.isBitSet(NCFlags, 2); 
    }
    
    public boolean isRangeLimitReached() {
    	return MKHelper.isBitSet(NCFlags, 3); 
    }

    public boolean isTargetReached(){
		return MKHelper.isBitSet(NCFlags, 5); 
	}

    public boolean isManualControlEnabled() {
    	return MKHelper.isBitSet(NCFlags, 6); 
	}

    public void set_by_mk_data(int[] in_arr,MKVersion version){
    	int off=0;
    	if ((version.proto_major>10)||(version.proto_minor>0)) // TODO fixme
    		off++;
	
    	Longitude=MKHelper.parse_arr_4(off+0,in_arr);
    	Latitude=MKHelper.parse_arr_4(off+4,in_arr);
    	Altitude=MKHelper.parse_arr_4(off+8,in_arr);
    	//	status=in_arr[12];

    	TargetLongitude=MKHelper.parse_arr_4(off+13,in_arr);
    	TargetLatitude=MKHelper.parse_arr_4(off+17,in_arr);
    	TargetAltitude=MKHelper.parse_arr_4(off+21,in_arr);
    	//	Targetstatus=in_arr[25];

    	Distance2Target=MKHelper.parse_arr_2(off+26,in_arr);
    	Angle2Target=MKHelper.parse_arr_2(off+28,in_arr);

    	HomeLongitude=MKHelper.parse_arr_4(off+30,in_arr);
    	HomeLatitude=MKHelper.parse_arr_4(off+34,in_arr);
    	HomeAltitude=MKHelper.parse_arr_4(off+38,in_arr);
    	//	Targetstatus=in_arr[42];

    	Distance2Home=MKHelper.parse_arr_2(off+43,in_arr);
    	Angle2Home=MKHelper.parse_arr_2(off+45,in_arr);

    	WayPointIndex=(byte)in_arr[off+47];
    	WayPointNumber=(byte)in_arr[off+48];

    	SatsInUse=(byte)in_arr[off+49];
	
    	Altimeter=MKHelper.parse_arr_2(off+50,in_arr); // hight according to air pressure
    	Variometer=MKHelper.parse_arr_2(off+52,in_arr);; // climb(+) and sink(-) rate
    	FlyingTime=MKHelper.parse_arr_2(off+54,in_arr);;
	
    	VesselData.battery.setUBatt(in_arr[off+56]);

		GroundSpeed= MKHelper.parse_arr_2(off+57,in_arr);
		Heading= MKHelper.parse_arr_2(off+59,in_arr);
		CompasHeading= MKHelper.parse_arr_2(off+61,in_arr); 
		
		AngleNick = in_arr[off+63];
		AngleRoll = in_arr[off+64];
		SenderOkay = in_arr[off+65];
	
		FCFlags=in_arr[off+66];
		NCFlags=in_arr[off+67];
	
		ErrorCode=in_arr[off+68];
	    
		
		// 69 op radius
		// 70-71 top speed
		// 72 targetHoldTime
		// 73 rc rssi
		// 74-75 set alt
		// 76 gas
		// 77/78 -current
		// 79/80 - used capacity
	
		// had no info about Current/Capacity in NC version <0.18
    	if (version.compare(0, 18)==MKVersion.VERSION_PREVIOUS) {
		
    		if (in_arr.length>off+77)
    			VesselData.battery.setCurrent(MKHelper.parse_arr_2(off+77,in_arr));
		
    		if (in_arr.length>off+79)
    			VesselData.battery.setUsedCapacity(MKHelper.parse_arr_2(off+79,in_arr));
    	}
			
    	} // end of set_by_mk_data
}