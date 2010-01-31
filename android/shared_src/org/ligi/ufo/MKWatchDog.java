/**************************************
 * 
 * WatchDog for MK-Connection
 *                      
 * Author:        Marcus -LiGi- Bueschleb
 * 
 * see README for further Infos
 *
 *

 **************************************/
package org.ligi.ufo;

public class MKWatchDog
        implements Runnable, DUBwiseDefinitions {

    MKCommunicator mk                  = null;

    int            bytes_in_count_buff = -123;


    public MKWatchDog( MKCommunicator _mk ) {

        mk = _mk;
        new Thread( this ).start(); // fire up main Thread
    }

    public int  act_paramset       = 0;

    int         conn_check_timeout = 0;

    public byte resend_timeout     = 0;

    int         last_count         = 0;

    int         last_fm_send       = -1;


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

    // #ifdef android
    // public final static int BASE_SLEEP=50;
    // #else
    public final static int BASE_SLEEP            = 10;

    // #endif

    int                     intitial_paramset_try = 0;


    public void run() {
        mk.log( "starting Watchdog" );
        // get all params
        int act_debug_name = 0;
        // int sleeper=BASE_SLEEP;
        while (true) {
            try {
                Thread.sleep( BASE_SLEEP );
                // sleeper=BASE_SLEEP;
                if (mk.connected && (!mk.force_disconnect))// &&(mk.bootloader_stage==BOOTLOADER_STAGE_NONE))
                {
                    // // mk.log("watchdog pre main loop");
                    // if (mk.init_bootloader)
                    // {
                    // mk.jump_bootloader();
                    // mk.init_bootloader=false;
                    // }
                    // else

                    if (mk.is_fake()) { // fake some stuff
                        mk.stats.bytes_in++;
                    }
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
                    else
                        switch (mk.user_intent) {
                            case USER_INTENT_PARAMS:

                                // do fake parsing
                                if (mk.is_fake()) {
                                    if ((mk.params.last_parsed_paramset < 4)) {
                                        if (resend_timeout == 0) {
                                            mk.params.set_by_mk_data(mk.params.default_params[mk.params.last_parsed_paramset+1]);
                                            resend_timeout = 20;
                                        }
                                        resend_timeout--;
                                    }
                                    int test=0;
                                }
                                else {
                                    // do real parsing
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
                                    /*
                                     * // act_paramset++;
                                     * else
                                     * mk.get_params(act_paramset);
                                     * 
                                     * sleeper+=1200;
                                     */
                                }
                                break;
                            case USER_INTENT_RAWDEBUG:
                                if (act_debug_name < 32) {

                                    if (resend_timeout == 0) {
                                        mk.get_debug_name( act_debug_name );
                                        resend_timeout = 50;
                                    }

                                    // sleeper+=100;
                                    if (mk.debug_data.got_name[act_debug_name]) {
                                        mk.get_debug_name( ++act_debug_name );
                                        resend_timeout = 50;
                                    }
                                    else
                                        resend_timeout--;

                                }
                                else if (!(mk.debug_data.got_name[0]))
                                    act_debug_name = 0;

                                break;

                            case USER_INTENT_RCDATA:
                                if (mk.is_fake()) {
                                    // fake some data ;-)
                                    for (int i = 0; i < MKStickData.MAX_STICKS; i++)
                                        mk.stick_data.stick[i] = mk.debug_data.analog[i] % 127;
                                }
                                else if (resend_check( mk.stats.stick_data_count ))
                                    mk.trigger_rcdata();
                                break;

                            case USER_INTENT_EXTERNAL_CONTROL:
                                if (resend_check( mk.stats.external_control_confirm_frame_count )) {
                                    mk.send_extern_control();
                                    }

                                break;

                            case USER_INTENT_LCD:
                                if (resend_check( mk.stats.lcd_data_count ))
                                    mk.LCD.trigger_LCD();

                                break;

                            case USER_INTENT_GPSOSD:
                                mk.set_gpsosd_interval( mk.primary_abo );
                                break;
                            case USER_INTENT_GRAPH:
                                mk.set_debug_interval( mk.primary_abo );
                                break;

                            case USER_INTENT_FOLLOWME:

                                // once a second
                                if (last_fm_send != (System.currentTimeMillis() / 1000)) {
                                    last_fm_send = (int)(System.currentTimeMillis() / 1000);
                                    mk.send_follow_me( 60 );
                                }

                                break;

                            default:
                                // mk.log("uncactched intent "
                                // +mk.root.canvas.user_intent )
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

        // mk.log("watchdog quit");
    }

}
