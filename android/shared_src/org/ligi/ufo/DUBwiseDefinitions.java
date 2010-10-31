/**************************************************************************
 *                                          
 * DUBwise Definitions 
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

package org.ligi.ufo;

public interface DUBwiseDefinitions
{

    public final static byte USER_INTENT_NONE             =0;
    public final static byte USER_INTENT_RAWDEBUG         =1;
    public final static byte USER_INTENT_PARAMS           =2;
    public final static byte USER_INTENT_GRAPH            =3;
    public final static byte USER_INTENT_RCDATA           =4;
    public final static byte USER_INTENT_LCD              =5;
    public final static byte USER_INTENT_EXTERNAL_CONTROL =6;
    public final static byte USER_INTENT_GPSOSD           =7;
    public final static byte USER_INTENT_FOLLOWME         =8;
    public final static byte USER_INTENT_3DDATA           =9;

    public final static byte GPS_FORMAT_DECIMAL           =0;
    public final static byte GPS_FORMAT_MINSEC            =1;


    public final static byte SPEED_FORMAT_KMH             =0; // km/h
    public final static byte SPEED_FORMAT_MPH             =1; // miles/h
    public final static byte SPEED_FORMAT_CMS             =2; // cm/s

    public final static byte BOOTLOADER_STAGE_NONE=0;
    public final static byte BOOTLOADER_STAGE_GOT_MKBL=1;

    /* from uart.h
       unsigned char Digital[2];
       unsigned char RemoteTasten;
       signed char   Nick;
       signed char   Roll;
       signed char   Gier;
       unsigned char Gas;
       signed char   Hight;
       unsigned char free;
       unsigned char Frame;
       unsigned char Config;
    */

    public final static byte EXTERN_CONTROL_NICK        =3;
    public final static byte EXTERN_CONTROL_ROLL        =4;
    public final static byte EXTERN_CONTROL_GIER        =5;
    public final static byte EXTERN_CONTROL_GAS         =6;
    public final static byte EXTERN_CONTROL_HIGHT       =7;
    public final static byte EXTERN_CONTROL_FRAME       =9;
    public final static byte EXTERN_CONTROL_CONFIG      =10;

    public final static byte EXTERN_CONTROL_LENGTH       =11;

    public final static byte EXTERN_CONTROL_DEFAULT      =42;

    public final static byte[] default_extern_keycontrol = {  (byte)0, (byte)0, (byte)0,  (byte)EXTERN_CONTROL_DEFAULT,  (byte)EXTERN_CONTROL_DEFAULT,  (byte)EXTERN_CONTROL_DEFAULT,  (byte)255 , (byte)0, (byte)0, (byte)1, (byte)1 };
}
