package org.ligi.android;

public interface DUBwiseUIDefinitions
{
    // id for each state - must just be uniq - order isnt important
    public final static byte STATEID_SCANNING         =0;
    public final static byte STATEID_DEVICESELECT     =1;
    public final static byte STATEID_MAINMENU         =2;
    public final static byte STATEID_MOTORTEST        =3;
    public final static byte STATEID_SELECT_PARAMSET  =4;
    public final static byte STATEID_EDIT_PARAMS      =5;
    public final static byte STATEID_HANDLE_PARAMS    =6;
    public final static byte STATEID_FLIGHTVIEW       =7;
    public final static byte STATEID_RAWDEBUG         =8;
    public final static byte STATEID_KEYCONTROL       =9;
    public final static byte STATEID_SETTINGSMENU     =10;
    public final static byte STATEID_STICKVIEW        =11;
    public final static byte STATEID_CAMMODE          =12;
    public final static byte STATEID_READ_PARAMS      =13;
    public final static byte STATEID_GPSVIEW          =14;
    public final static byte STATEID_FILEOPEN         =15;
    public final static byte STATEID_GRAPH            =16;
    public final static byte STATEID_CONN_DETAILS     =17;
    public final static byte STATEID_IPINPUT          =18;
    public final static byte STATEID_PROXY            =19;
    public final static byte STATEID_TRAFFIC          =20;
    public final static byte STATEID_SELECT_COMPORT   =21;
    public final static byte STATEID_ABOUT            =22;
    public final static byte STATEID_NC_ERRORS        =23;
    public final static byte STATEID_FLASHING         =24;
    public final static byte STATEID_NAMEINPUT        =25;
    public final static byte STATEID_DATABUFF         =26;



    public final static int MAINMENU_TELEMETRY     =0;
    public final static int MAINMENU_RAWDEBUG      =1+MAINMENU_TELEMETRY;
    public final static int MAINMENU_STICKS        =1+MAINMENU_RAWDEBUG;
    public final static int MAINMENU_KEYCONTROL    =1+MAINMENU_STICKS;
    public final static int MAINMENU_MOTORTEST     =1+MAINMENU_KEYCONTROL;
    public final static int MAINMENU_PARAMS        =1+MAINMENU_MOTORTEST;
    public final static int MAINMENU_SETTINGSMENU  =1+MAINMENU_PARAMS;
    public final static int MAINMENU_CAMMODE       =1+MAINMENU_SETTINGSMENU;
    public final static int MAINMENU_PROXY         =1+MAINMENU_CAMMODE;
    public final static int MAINMENU_DEVICESELECT  =1+MAINMENU_PROXY;
    public final static int MAINMENU_QUIT          =1+MAINMENU_DEVICESELECT;

    public String[] main_menu_items={"Telemetry","Raw Debug-Values", "RC-Data", "pilot UFO", "Motor Test" , "Flight Settings","(NA)Tool Settings","(NA)Remote Camera","(NA)Relay","(NA)Change Device" , "Quit " };


    public final static  String[] settings_menu_items={"Skin ","Sound ","Vibra " ,"Scrolling BG ","FullScreen " , "Back" };



    //    public String[] settings_menu_items={"Skin ","Sound ","Vibra " ,"Graph ","FullScreen " ,"Keep BGLight " ,"Back" };
    public final static int SETTINGSMENU_CHANGESKIN   =0;
    public final static int SETTINGSMENU_SOUNDTOGGLE  =1;
    public final static int SETTINGSMENU_VIBRATOGGLE  =2;
    public final static int SETTINGSMENU_GRAPHTOGGLE  =3;
    public final static int SETTINGSMENU_FULLSCREENTOGGLE  =4;
    public final static int SETTINGSMENU_LIGHTTOGGLE  =5;
    public final static int SETTINGSMENU_BACK      =6;








}
