/********************************************************************************
 *
 * part of DUBwise
 *                                                                               
 * Author:        Marcus -LiGi- Bueschleb                                        
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
********************************************************************************/

package org.ligi.ufo;

/**
 * statistics from MK-Connection 
 * 
 * @author ligi
 *
 */
public class MKStatistics {
    public int bytes_in=0;
    public int bytes_out=0;

    public int resend_count=0;
    public int crc_fail=0;

    public int debug_data_count=0;
    public int debug_names_count=0;
    public int angle_data_count=0;
    public int version_data_count=0;
    public int other_data_count=0;
    public int lcd_data_count=0;
    public int params_data_count=0;
    public int navi_data_count=0;
    public int stick_data_count=0;
    public int threeD_data_count=0;
    public int external_control_confirm_frame_count=0;

    public int debug_data_request_count=0;
    public int debug_name_request_count=0;
    public int version_data_request_count=0;
    public int lcd_data_request_count=0;
    public int params_data_request_count=0;
    //    public int navi_data_count=0;
    public int stick_data_request_count=0;
    public int motortest_request_count=0;
    public int external_control_request_count=0;
    public int follow_me_request_count=0;
    
    public long flying_start=-1;
    
    public int heading_start=0;
    public int last_heading=0;
    public int max_speed=0;

    public long speed_sum=-1;
    public int speed_cnt=1;

    public int max_alt=-1;

    public MKStatistics() {
    	reset();
    }
    
    public int avg_speed() {
    	return (int)(speed_sum/speed_cnt);
    }
    public void process_speed(int speed) {
		if (speed>max_speed)
		    max_speed=speed;
		speed_sum+=speed;
		speed_cnt++;
    }

    public void process_alt(int alt) {
		if (alt>max_alt)
		    max_alt=alt;
    }
    
    public void process_mkflags(int flags) {
    	boolean flying=(flags!=0);
		if (!flying)
			flying_start=flying_start+flying_time();
		else
		    if (flying_start==-1) {
			    flying_start=System.currentTimeMillis();
			    heading_start=last_heading;
			}

    }

    public void process_compas(int heading) {
    	last_heading=heading;
    }

    /**
     * @return the time in seconds the UFO is Flying
     */
    public int flying_time() {
		if (flying_start!=-1)
		    return (int)((System.currentTimeMillis()-flying_start)/1000);
		else
		    return 0;
    }

    /**
     * reset/initialize all values
     */
    public void reset() {
		flying_start=-1;
		crc_fail=0;
		debug_data_count=0;
		debug_names_count=0;
		angle_data_count=0;
		version_data_count=0;
		other_data_count=0;
		lcd_data_count=0;
		params_data_count=0;
		navi_data_count=0;
		bytes_in=0;
		bytes_out=0;
		stick_data_count=0;
	
		debug_data_request_count=0;
		debug_name_request_count=0;
		version_data_request_count=0;
		lcd_data_request_count=0; //
		params_data_request_count=0; //
		stick_data_request_count=0;
		motortest_request_count=0; //
		follow_me_request_count=0;
		threeD_data_count=0;
    }
    
}
