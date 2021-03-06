/******************************************
 * 
 * Author:        Marcus -LiGi- Bueschleb
 * 
 *******************************************/

import javax.microedition.media.*;
import javax.microedition.media.control.*;
import java.util.*;
import java.io.*;
import org.ligi.ufo.VesselData;

/**
 * report UFO Data to the network via HTTP Post
 *
 **/
public class UFOWebReporter
    implements Runnable
{


    private DUBwiseCanvas canvas=null;
    private int last_send_id=10;

    public UFOWebReporter(DUBwiseCanvas _canvas)   {
	canvas=_canvas;
	new Thread( this ).start(); // fire up main Thread 
    }


    public void run() {
        while(true) {
	
		if ((canvas.mk!=null)&&(canvas.mk.connected)&&(canvas.settings.webreporter)&&(last_send_id--<0))  {
			last_send_id=canvas.settings.webreporter_interval;
			String request_str="http://ligi-tec.appspot.com/update_ufo?ubatt="+VesselData.battery.getVoltage()+"&ufo_id="+canvas.settings.dubwise_id + "&sender=" + canvas.mk.SenderOkay() + "&alt=" + canvas.mk.getAlt();
			if (canvas.mk.connected&&canvas.mk.is_navi())
				request_str+="&type=navi&lat="+canvas.mk.gps_position.Latitude + "&lon=" +canvas.mk.gps_position.Longitude + "&sats=" +canvas.mk.gps_position.SatsInUse + "&error_code=" +canvas.mk.gps_position.ErrorCode + "&compas_heading="+ +canvas.mk.gps_position.CompasHeading;

			DUBwiseHelper.get_http_string(request_str);
		    }

		try { 
		    Thread.sleep(100); 
		}
		catch (Exception e)  {   }
		
	    }
    }
    
}

