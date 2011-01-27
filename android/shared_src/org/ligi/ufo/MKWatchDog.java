/**************************************
 *                      
 * Author:        Marcus -LiGi- Bueschleb
 *  http://ligi.de
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
 **************************************/

package org.ligi.ufo;

/**
 * WatchDog/TrafficShaping for MK-Connection
 * 
 * @author ligi
 */
public class MKWatchDog
        implements Runnable, DUBwiseDefinitions {

    private MKCommunicator mk                  = null;
    private int            bytes_in_count_buff = -123;
    public  int            act_paramset        = 0;
    private int            conn_check_timeout  = 0;
    public  byte           resend_timeout      = 0;
    private int            last_count          = 0;
    private int            last_fm_send        = -1;

    // TODO make base sleep adjustable
    public final static int BASE_SLEEP           = 50;
    private int            intitial_paramset_try = 0;
    private int            abo_timeout           =0;

    public MKWatchDog( MKCommunicator _mk ) {
        mk = _mk;
        new Thread( this ).start(); // fire up main Thread
    }

    public boolean resend_check( int ref_count ) {
        if ((last_count != ref_count) || (resend_timeout < 0)) {
            if (resend_timeout < 0)
                mk.stats.resend_count++;
            last_count = ref_count;
            resend_timeout = 20;
            return true;
        }
        else
            resend_timeout--;

        return false;
    }

    
    
    public void run() {
        mk.log( "starting Watchdog" );
        // get all params
        int act_debug_name = 0;

        while (true) {
            try {
                Thread.sleep( BASE_SLEEP );

                if (mk.connected && (!mk.force_disconnect))// &&(mk.bootloader_stage==BOOTLOADER_STAGE_NONE))
                {
                	abo_timeout+=BASE_SLEEP;
                   
                    if (mk.version.major == -1)
                        mk.get_version();
                    else if (mk.is_navi() && (mk.error_str == null))
                        mk.get_error_str();

                    else if (mk.is_mk() && (mk.params.last_parsed_paramset == -1)
                            && (intitial_paramset_try < 7)) {
                        mk.get_params( 0xFF - 1 );
                        Thread.sleep( 150 );
                        intitial_paramset_try++;
                        act_paramset = 0; // warning - if dropped problem
                    }
                    else if (mk.is_mk() && (mk.mixer_manager.state==MixerManager.STATE_NO_DATA))
                    		mk.trigger_mixer_read();
                	
                 // fetch the debug names
                    else if (act_debug_name < 32) {

                       if (resend_timeout == 0) {
                           mk.requestDebugName( act_debug_name );
                           resend_timeout = 50;
                       }

                       if (mk.debug_data.got_name[act_debug_name]) {
                       	act_debug_name++;
                       	mk.requestDebugName(act_debug_name );
                           resend_timeout = 10;
                       }
                       else
                           resend_timeout--;

                   }
                   else if (!(mk.debug_data.got_name[0]))
                   		act_debug_name = 0;
                    
                    else
                    	// set abos every 2 seconds
                    	if (abo_timeout>1000)
                    	{
                    		mk.log("regenerating abos");
                    		switch (mk.user_intent) {
                    			case USER_INTENT_RAWDEBUG:
                    			case USER_INTENT_GRAPH:
	                    			mk.set_debug_interval( mk.primary_abo );
	                    			mk.set_gpsosd_interval( mk.secondary_abo );
	                    			break;

	                            case USER_INTENT_GPSOSD:
	                    			mk.set_gpsosd_interval( mk.primary_abo );
	                                mk.set_debug_interval( mk.secondary_abo );
	                                break;
	
	                            case USER_INTENT_3DDATA:
	                    			mk.set_3ddata_interval( mk.primary_abo );
	                                mk.set_debug_interval( mk.secondary_abo );
	                                break;
	
	                            default:
	                            	mk.set_gpsosd_interval( mk.secondary_abo );
	                                mk.set_debug_interval( mk.secondary_abo );
	                            	break;
                    		}
                    		abo_timeout=0;
                    	}                   	
                    else
                    	switch (mk.user_intent) {
                            case USER_INTENT_PARAMS:
                            	if ((act_paramset < 5)) {
                            		if (resend_timeout == 0) {
                            			mk.get_params( act_paramset );
                            			resend_timeout = 120;
                                        	}

                                    if (mk.params.field[act_paramset] != null) {
                                    	mk.get_params( ++act_paramset );
                                    	resend_timeout = 120;
                                     }
                                    else
                                    	resend_timeout--;
                                    
                                }
                                break;
                            
                            case USER_INTENT_RAWDEBUG:
                            
                                 break;

                            case USER_INTENT_RCDATA:
                                if (resend_check( mk.stats.stick_data_count ))
                                    mk.trigger_rcdata();
                                break;

                            case USER_INTENT_EXTERNAL_CONTROL:
                                if (resend_check( mk.stats.external_control_confirm_frame_count )) 
                                    mk.send_extern_control();
                                break;

                            case USER_INTENT_LCD:
                                if (resend_check( mk.stats.lcd_data_count ))
                                    mk.LCD.trigger_LCD();
                                break;

                            case USER_INTENT_FOLLOWME:
                                // once a second
                                if (last_fm_send != (System.currentTimeMillis() / 1000)) {
                                    last_fm_send = (int)(System.currentTimeMillis() / 1000);
                                    mk.send_follow_me( (byte)60,mk.follow_me_lat,mk.follow_me_lon );
                                    mk.stats.follow_me_request_count++;
                                }
                                break;

                            default:
                                mk.log("uncactched intent " +mk.user_intent );
                                break;
                        }

                    if (bytes_in_count_buff == mk.stats.bytes_in)
                        if ((conn_check_timeout++) * BASE_SLEEP > 3000) {
                            conn_check_timeout = 0;
                            mk.close_connections( false );
                        }
                        else
                            conn_check_timeout = 0;
                    bytes_in_count_buff = mk.stats.debug_data_count;
                }

            } // 3000
            catch (Exception e) {
                mk.log( "err in watchdog:" );
                mk.log( e.toString() );
            }
        }

    }

}
