public interface DUBwiseUIDefinitions

{


    // colors
    public final static int BG_COLOR_SKIN_DARK  = 0x000000;	
    public final static int BG_COLOR_SKIN_LIGHT = 0xFFFFFF;	
    public final static int FG_COLOR_SKIN_DARK  = 0xFFFFFF;	
    public final static int FG_COLOR_SKIN_LIGHT = 0x000000;	



    // id for each state - must just be uniq - order isnt important
    public final static byte STATEID_INITIAL          =0;
    public final static byte STATEID_DEVICESELECT     =1;
    public final static byte STATEID_MAINMENU         =2;
    public final static byte STATEID_MOTORTEST        =3;
    public final static byte STATEID_SELECT_PARAMSET  =4;
    public final static byte STATEID_EDIT_PARAMS      =5;
    public final static byte STATEID_HANDLE_PARAMS    =6;
    public final static byte STATEID_LCD              =7;
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
    public final static byte STATEID_ERROR_MSG        =23;
    public final static byte STATEID_FLASHING         =24;
    public final static byte STATEID_NAMEINPUT        =25;
    public final static byte STATEID_DATABUFF         =26;
    public final static byte STATEID_HORIZON          =27;
    public final static byte STATEID_SUCCESS_MSG      =28;
    public final static byte STATEID_STRINGINPUT      =29;
    public final static byte STATEID_SCANNING         =30;
    public final static byte STATEID_RESET_PARAMS     =31;
    public final static byte STATEID_PARAM_MENU       =32;
    public final static byte STATEID_PARAM_MASSWRITE  =33;
    public final static byte STATEID_SELECT_FIRMWARE  =34;
    public final static byte STATEID_SHOWPHONEGPS     =35;
    public final static byte STATEID_SETTING_OPTIONS  =36;
    public final static byte STATEID_MAP              =37;
    public final static byte STATEID_GET_AVRSIG       =38;
    public final static byte STATEID_SELECT_MIXER     =39;
    public final static byte STATEID_DUBWISE_VALUES   =40;
    public final static byte STATEID_BIG_SYMBOLS      =41;
    public final static byte STATEID_CONFIRM_MOTORTEST=42;
    public final static byte STATEID_CONNECT_RECENT   =43;

    public final static byte STATEID_COUNT            =44;



    public final static byte ACTIONID_SETTINGS              = 0; 
    public final static byte ACTIONID_DEVICESELECT          = 1; 
    public final static byte ACTIONID_DEBUG                 = 2;
    public final static byte ACTIONID_CONN_DETAILS          = 3;
    public final static byte ACTIONID_SWITCH_NC             = 4; 
    public final static byte ACTIONID_SWITCH_FC             = 5; 
    public final static byte ACTIONID_GRAPH                 = 6; 
    public final static byte ACTIONID_LCD                   = 7; 
    public final static byte ACTIONID_RAWDEBUG              = 8; 
    public final static byte ACTIONID_RCDATA                = 9; 
    public final static byte ACTIONID_KEYCONTROL            = 10;
    public final static byte ACTIONID_MOTORTEST             = 11;
    public final static byte ACTIONID_EDIT_PARAMS           = 12;
    public final static byte ACTIONID_CAM                   = 13; 
    public final static byte ACTIONID_PROXY                 = 14; 
    public final static byte ACTIONID_GPSDATA               = 15;
    public final static byte ACTIONID_TRAFFIC               = 16;
    public final static byte ACTIONID_ABOUT                 = 17;
    public final static byte ACTIONID_NC_ERRORS             = 18;
    public final static byte ACTIONID_WRITE_PARAMS          = 19;
    public final static byte ACTIONID_UNDO_PARAMS           = 20;
    public final static byte ACTIONID_MAINMENU              = 21;
    public final static byte ACTIONID_HORIZON               = 22;
    public final static byte ACTIONID_WRITE_PARAM_AS        = 23;
    public final static byte ACTIONID_PARAM_WRITE_OK        = 24;
    public final static byte ACTIONID_PARAM_MENU            = 25;
    public final static byte ACTIONID_PARAM_LOAD_MOBILE     = 26;
    public final static byte ACTIONID_PARAM_COPY_MOBILE     = 27;
    public final static byte ACTIONID_DATABUFF              = 28;
    public final static byte ACTIONID_SWITCH_MK3MAG         = 29; 
    public final static byte ACTIONID_CONNECT_TCP           = 30;
    public final static byte ACTIONID_SCAN_BT               = 31;
    public final static byte ACTIONID_SELECT_COM            = 32;
    public final static byte ACTIONID_PROXY_INPUT           = 33;
    public final static byte ACTIONID_FLASH                 = 34;
    public final static byte ACTIONID_RESET_PARAMS          = 35;
    public final static byte ACTIONID_BACK_TO_CONNDETAILS   = 36;    
    public final static byte ACTIONID_SELECT_PARAMS         = 37;    
    public final static byte ACTIONID_RENAME_PARAMS         = 38;
    public final static byte ACTIONID_SELECT_FIRMWARE       = 39;
    public final static byte ACTIONID_UPDATE_DUBWISE        = 40;
    public final static byte ACTIONID_SHOWPHONEGPS          = 41;
    public final static byte ACTIONID_RECONNECT             = 42;
    public final static byte ACTIONID_SETTING_OPTIONS       = 43;
    public final static byte ACTIONID_SETTING_LOADPLAIN     = 44;
    public final static byte ACTIONID_SETTING_LOADFANCY     = 45;
    public final static byte ACTIONID_MAP                   = 46;
    public final static byte ACTIONID_SELECT_DUBWISEDIR     = 47;
    public final static byte ACTIONID_SELECT_MIXER          = 48;
    public final static byte ACTIONID_BIG_SYMBOLS           = 49;
    public final static byte ACTIONID_START_ENGINES         = 50;

    public final static byte ACTIONID_CONFIRM_MOTORTEST     = 51;
    public final static byte ACTIONID_DISCARD_MOTORTEST     = 52;
    public final static byte ACTIONID_CONNECT_RECENT        = 53;

    public final static byte ACTIONID_QUIT                  = 100; 




    //    public  final static String[] param_menu_items={"Edit Settings","Copy all to Mobile","Load from Mobile","Reinitialize all","back"};








    /*
    public final static String[] conn_details_menu_items={ "packet Traffic","view Data","connect via TCP/IP","connect via BT","connect via COM","set Proxy","back" };
    public  final static byte[]  conn_details_menu_actions={ ACTIONID_TRAFFIC,ACTIONID_DATABUFF,ACTIONID_CONNECT_TCP,ACTIONID_SCAN_BT, ACTIONID_SELECT_COM,ACTIONID_PROXY_INPUT,ACTIONID_MAINMENU};
    */



    final static byte SKINID_DARK= 0;
    final static byte SKINID_LIGHT = 1;


    final static byte CREDITS_VERSION=7;
    final static String[] credits= {
	"##### DUBwise ######",
	"",
	"Digital UFO",
	"Broadcasting With ",
	"Intelligent Service",
	"Equipment",
	"",
	"",
	"",
	"",
	"(cc) 2007-2009 by ",
	"Marcus LiGi B"+(char)(252)+"schleb",
	"mailto:ligi"+"@"+"ligi.de",
	"",
	"Licence:",
	"Creative Commons(CC)",
	" -Attribution",
	" -Noncommercial",
	" -Share Alike",
	" -No Violence",
	"",
	"Links:",
	" www.ligi.de",
	" www.mikrokopter.com",
	"",
	"Credits: ",
	" -HolgerB&IngoB",
	"  (MikroKopter)",
	" -CaScAdE",
	"  (Graphics/Testing)",
	" -Orion8",
	"  (Inspiration)",
	" -Joko",
	"  (Testing)",
	" -Speedy",
	"  (Graphics/Sounds)",
	" -Jamiro",
	"  (Icon)",
        " -SelectaT",
	"  (Sounds)",
	" -JiPsi",
	"  (french translat.)",
	" -kmpec",
	"  (Test-Device)",
	"####################"

    };
		

    public final static char[] idle_seq={'.','_','-','='};


    public final static int[] default_ip={192,168,1,42,4242};




}
