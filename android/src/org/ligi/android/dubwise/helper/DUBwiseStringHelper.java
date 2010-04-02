/**************************************************************************
 *                                          
 * Author:  Marcus -LiGi- Bueschleb   
 *
 * Project URL:
 *  http://mikrokopter.de/ucwiki/en/DUBwise
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
 **************************************************************************/

package org.ligi.android.dubwise.helper;

import org.ligi.android.dubwise.R;


public class DUBwiseStringHelper {


    public final static int[] table= {
    	R.string.LANG0,R.string.LANG1,R.string.LANG2,R.string.LANG3,R.string.CONN,R.string.SETTINGS,R.string.SELECT_FIRMWARE,R.string.DEBUG,R.string.MOTORTEST,R.string.ABOUT,R.string.EDIT_SETTINGS,R.string.COPY_TO_MOBILE,R.string.LOAD_FROM_MOBILE,R.string.REINITIALIZE_ALL,R.string.BACK,R.string.QUIT,R.string.REMOTE_CAM,R.string.FLASH_FIRMWARE,R.string.SWITCH_NAVI,R.string.SWITCH_MK3MAG,R.string.SWITCH_FC,R.string.VIEW_GPS,R.string.LCD,R.string.GRAPH,R.string.DEBUG_VALUES,R.string.KEYCONTROL,R.string.RCDATA,R.string.FLIGHTSETTINGS,R.string.HORIZON,R.string.VIEW_ERRORS,R.string.USERINTERFACE,R.string.GPS,R.string.SPECIALKEYS,R.string.VOICE,R.string.OTHER,R.string.ALTITUDE,R.string.CAMERA,R.string.CHANNELS,R.string.CONFIGURATION,R.string.GYRO,R.string.LOOP,R.string.NAVI,R.string.OUTPUT,R.string.STICK,R.string.USERPARAMS,R.string.EDIT_CONTENT,R.string.RENAME,R.string.SAVE_AS,R.string.SAVE,R.string.REREAD,R.string.PACKET_TRAFFIC,R.string.VIEW_DATA,R.string.CONNECT_BT,R.string.CONNECT_TCP,R.string.CONNECT_COM,R.string.CONNECT_RECENT,R.string.SET_PROXY,R.string.ON,R.string.OFF,R.string.DARK,R.string.LIGHT,R.string.KMH,R.string.MPH,R.string.CMS,R.string.DECIMAL,R.string.MINSEC,R.string.NONE_ASSIGNED,R.string.LANGUAGE,R.string.SKIN,R.string.FULLSCREEN,R.string.SCROLLBG,R.string.PERMALIGHT,R.string.GPSFORMAT,R.string.SPEEDFORMAT,R.string.NICK,R.string.ROLL,R.string.GIER,R.string.GASINCREASE,R.string.HEIGHTINCREASE,R.string.BACKTOMAINMENU,R.string.CLEAR,R.string.LEGEND,R.string.SCALEGRID,R.string.VOLTS,R.string.DELAYINS,R.string.VOLUME,R.string.SOUND,R.string.VIBRA,R.string.REMOTECAMSTICK,R.string.ALWAYSRELOADPARAMS,R.string.EXPERTMODE,R.string.MINACCELERATE,R.string.BAROD,R.string.SETPOINT,R.string.ALTITUDEP,R.string.GAIN,R.string.ZACC,R.string.SWITCH3,R.string.SERVOCONTROL,R.string.SERVONICKCONTROL,R.string.SERVOROLLCONTROL,R.string.NICKCOMP,R.string.ROLLCOMP,R.string.SERVONICKMIN,R.string.SERVONICKMAX,R.string.SERVOROLLMIN,R.string.SERVOROLLMAX,R.string.REFRESHRATE,R.string.INVERTDIRECTION,R.string.INVERTDIRECTIONROLL,R.string.INVERTDIRECTIONNICK,R.string.ACCELERATE,R.string.POTI1,R.string.POTI2,R.string.POTI3,R.string.POTI4,R.string.POTI5,R.string.POTI6,R.string.POTI7,R.string.POTI8,R.string.ALTITUDECONTROL,R.string.SWITCHFORSETPOINT,R.string.HEADINGHOLD,R.string.COMPASACTIVE,R.string.COMPASFIX,R.string.COUPLING,R.string.COUPLING2,R.string.COUPLINGYAWCORRECT,R.string.YAWRATELIMITER,R.string.YAWPOSFEEDBACK,R.string.YAWNEGFEEDBACK,R.string.ACCGYROFACTOR,R.string.PRATE,R.string.IRATE,R.string.ACCGYROCOMP,R.string.DRIFTCOMP,R.string.DYNAMICSTABILITY,R.string.GASLIMIT,R.string.THRESHOLD,R.string.HYSTERESE,R.string.TURNOVERNICK,R.string.TURNOVERROLL,R.string.UP,R.string.DOWN,R.string.LEFT,R.string.RIGHT,R.string.MODECONTROL,R.string.GPSGAIN,R.string.GPSP,R.string.GPSPLIMIT,R.string.GPSI,R.string.GPSILIMIT,R.string.GPSD,R.string.GPSDLIMIT,R.string.GPSACC,R.string.DRATE,R.string.SATMIN,R.string.STICKTHRESHOLD,R.string.WINDCORRECT,R.string.SPEEDCOMP,R.string.OPERATIONRADIUS,R.string.ANGLELIMIT,R.string.MINGAS,R.string.MAXGAS,R.string.COMPASEFFECT,R.string.VOLTAGEWARNING,R.string.DISTRESSGAS,R.string.DISTRESSGASTIME,R.string.J16BITMASK,R.string.J17BITMASK,R.string.WARN_J16BITMASK,R.string.WARN_J17BITMASK,R.string.J16TIMING,R.string.J17TIMING,R.string.NICKROLLP,R.string.NICKROLLD,R.string.GIERP,R.string.GIERI,R.string.STICKNEUTRALPOINT,R.string.EXTERNCONTROL,R.string.PARAM1,R.string.PARAM2,R.string.PARAM3,R.string.PARAM4,R.string.PARAM5,R.string.PARAM6,R.string.PARAM7,R.string.PARAM8,R.string.NOTREADYET,R.string.PHLOGINTIME,R.string.PARAMSINCOMPATIBLE,R.string.NOPARAMSONMOBILE,R.string.PARAMWRITEOK,R.string.SAVEDSETTINGS,R.string.SETTINGSUNDOOK,R.string.INCOMPATIBLEDEVICE,R.string.PARAMRESETOK,R.string.WRITINGPARAMS,R.string.GRAPHINTERVAL,R.string.ALTIMETER,R.string.COCKPIT,R.string.INVERTNICK,R.string.INVERTROLL,R.string.SHOWFLIGHTTIME,R.string.SHOWALTITUDE,R.string.UNITS,R.string.DEVMODE,R.string.DISCONNECT,R.string.INSTANTERRORSHOW,R.string.FORCERECONNECT,R.string.EDIT,R.string.ALTSTEPS,R.string.LOADPLAIN,R.string.LOADFANCY,R.string.TIMING,R.string.PRIMARYABO,R.string.SECONDARYABO,R.string.DEFAULTABO,R.string.BIGFONTS,R.string.CONFIRMMOTORTEST,R.string.DENYMOTORTEST,R.string.PARAMSAVE,R.string.WEBREPORTER,R.string.INTERVAL,R.string.CONNECT_FAKE,R.string.CONNECT_URL,R.string.INTRO,R.string.MINIMAL,R.string.VOICE_NOPOINT,R.string.SATELITES,R.string.FLIGHTTIME,R.string.ALTERNATIVE_FIRE,R.string.HOVERBAND,R.string.GPSZ,R.string.SENSITIVERC,R.string.VARIOBEEP,R.string.ALTITUDELIMIT,R.string.SERVO3,R.string.SERVO4,R.string.SERVO5,R.string.RECEIVER,R.string.CURRENT
    	
    	
    };
    
}