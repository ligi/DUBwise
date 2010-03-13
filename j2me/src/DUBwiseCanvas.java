/***************************************************************
 *
 * User Interface ( Canvas ) of DUBwise
 *                                                           
 * Author:        Marcus -LiGi- Bueschleb
 * Mailto:        LiGi @at@ LiGi DOTT de                    
 * 
 ***************************************************************/

import javax.microedition.lcdui.*;

//#if j2memap=="on"
import com.eightmotions.map.*;
import com.eightmotions.util.UtilMidp;
//#endif

//#if jsr179=="on"
import javax.microedition.location.*;
//#endif

//#if openlapi=="on"
import com.openlapi.*;
//#endif

import javax.microedition.rms.*;
import java.io.*;


import org.ligi.ufo.MKFirmwareFlasher;
import org.ligi.ufo.DUBwiseDefinitions;
import org.ligi.ufo.DUBwiseLangDefs;
import org.ligi.ufo.MKCommunicator;
import org.ligi.ufo.MKFirmwareHelper;
import org.ligi.ufo.MixerManager;
import org.ligi.ufo.FakeCommunicationAdapter;
import org.ligi.ufo.MKParamsParser;

public class DUBwiseCanvas
    extends Canvas
    implements Runnable,DUBwiseDefinitions , DUBwiseUIDefinitions,DUBwiseLangDefs

//#if (location=="on")&&(cldc11=="on")
	       ,LocationListener
//#endif
{

//#expand    public final static byte version_patch=%VERSION_PATCH%;
//#expand    public final static byte version_minor=%VERSION_MINOR%;
//#expand    public final static byte version_major=%VERSION_MAJOR%;
    
    public final static String version_str="" +version_major + "." + version_minor +"" +(char)('a'+version_patch) ;

//#if  (location=="on")&&(cldc11=="on")
    public void providerStateChanged(LocationProvider p ,int i )
    {
    }
    
    public void locationUpdated(LocationProvider lp, Location loc)
    {
	try
	    {
		Coordinates coords = loc.getQualifiedCoordinates();
		if (coords != null)
		    {
			phone_long = coords.getLongitude();
			phone_lat = coords.getLatitude();
			
			/*
			if (mk!=null)
			    {
				mk.lat=(long)phone_lat*10000000;
				mk.lon=(long)phone_long*10000000;

			    }
			*/
			// Start thread for handling the new coordinates
                                // (...)
			}
			else
			{
			    phone_lat=-1.0;
			    phone_long=-1.0;


			    
			}
		}
		catch (Exception e)
		{
			// handle the exception
                        // (...)
		}
	}

//#endif


    // MENU Definitions

    public final static int[] param_menu_items={STRINGID_EDIT_SETTINGS,STRINGID_COPY_TO_MOBILE,STRINGID_LOAD_FROM_MOBILE,STRINGID_REINITIALIZE_ALL,STRINGID_BACK};
    public  final static byte[] param_menu_actions={ACTIONID_SELECT_PARAMS,ACTIONID_PARAM_COPY_MOBILE,ACTIONID_PARAM_LOAD_MOBILE,ACTIONID_RESET_PARAMS,ACTIONID_MAINMENU};

    public  final static int[] handle_params_menu_items={STRINGID_EDIT_CONTENT,STRINGID_RENAME,STRINGID_SAVE_AS,STRINGID_SAVE,STRINGID_REREAD,STRINGID_BACK};
    public  final static byte[] handle_params_menu_actions={ACTIONID_EDIT_PARAMS,ACTIONID_RENAME_PARAMS,ACTIONID_WRITE_PARAM_AS,ACTIONID_WRITE_PARAMS,ACTIONID_UNDO_PARAMS,ACTIONID_SELECT_PARAMS };


    public  final static int[] handle_motortest_items={STRINGID_DENYMOTORTEST,STRINGID_CONFIRMMOTORTEST};
    public  final static byte[] handle_motortest_actions={ACTIONID_DISCARD_MOTORTEST,ACTIONID_CONFIRM_MOTORTEST };



    public  final static int[] setting_options_menu_items={STRINGID_EDIT,STRINGID_LOADPLAIN,STRINGID_LOADFANCY,STRINGID_BACK};
    public  final static byte[]setting_options_menu_actions={ACTIONID_SETTINGS,ACTIONID_SETTING_LOADPLAIN,ACTIONID_SETTING_LOADFANCY,ACTIONID_MAINMENU };

    public final static int[] onlyback_menu_items={STRINGID_BACK };
    public final static byte[]    back_to_conndetails_actions={ACTIONID_BACK_TO_CONNDETAILS};



    public int[] beta_unlock_code={Canvas.KEY_NUM2,Canvas.KEY_NUM3,Canvas.KEY_NUM8,Canvas.KEY_NUM2};

    int beta_unlock_pos=0;

//#if fileapi=="on"
    DUBwiseFileAccess file_access;
//#endif

    MixerManager mixer_manager;
    MotorTester motor_tester;
    

    String act_input_str=" ";
    // for dual use of states
    boolean select_paramset4edit;

//#if (location=="on")&&(cldc11=="on")
    Criteria cr= null;

    LocationProvider lp= null;
    Coordinates c=null;				


//#endif

    String act_msg="";
            
    boolean read_paramset_intension_save;
    boolean ipinput4proxy;


    byte ipinput_pos=0;
    int[] act_edit_ip;

    int canvas_width=100;
    int canvas_height=100;

    int heading_offset=0;
    int act_wp;

    long key_bitfield=0;
    boolean debug_overlay=false;

    public long keyCode2mask(int keyCode)
    {
	if ((keyCode>=KEY_NUM0)&&(keyCode<=KEY_NUM9))
	    return 1<<(keyCode-KEY_NUM0);
	
	switch (getGameAction( keyCode ))
	    {	    
	    case UP:
		return 1<<10;
	    case DOWN:
		return 1<<11;
	    case LEFT:
		return 1<<12;
	    case RIGHT:
		return 1<<13;
	    case FIRE:
		return 1<<14;
		
	    }
	
	switch ( keyCode )
	    {	    
	    case KEY_STAR:
		return 1<<15;
	    }
	//	log("unmapped key");
	return 0; // cant translate
    }

//#if bluetooth=="on"
    private BTSearcher bt_scanner;
//#endif

    public MKCommunicator mk=null;


    public UFOWebReporter web_reporter=null;
    //    private MKStatistics mk_stat=null;
    
    private MKParamsEditor act_editor=null;

    public MKFirmwareFlasher firmware_flasher=null;
    public DUBwiseDebug debug=null;
    public FirmwareLoader fw_loader=null;

    public byte last_navi_error=0;



//#if voice_mode!="no_voice"
    public MKStatusVoice status_voice; 
//#endif



    public DUBwise root;
    public DUBwiseSettings settings;


    private Image bg_img;
    private Image lcd_img;
    private Image symbols_img;

    private Image big_symbols_img;
    
    private int symbols_img_tile_height;
    private int symbols_img_tile_width;


    private Image err_img;


    private Image icon_img;


    byte max_lines;


    /*
    public byte act_motor=0;
    public byte act_motor_increase=0;
    public boolean motor_test_sel_all=false;
    */

    /* Graph Vars */
    public final static int GRAPH_COUNT=4;
    //    public final static int[] graph_colors={0x156315,0xCC1315,0xf8ef02,0x19194d};
    public final static int[] graph_colors={0x0aff15,0xCC1315,0xf8ef02,0x1638ff};
    public int[] graph_sources={0,1,2,3};
    public String[] graph_names={"nick int","roll int","nick acc","roll acc"};
    //    public int[][] graph_data;
    //public int graph_offset=0;
    
    //    public int lcd_char_width=0;
    public int lcd_char_height=0;

    public int frame_pos=0;

    //    public byte mk.user_intent=USER_INTENT_NONE;
    
    int line_scaler=20;

    int rawdebug_cursor_y=0;    
    int rawdebug_off_y=0;

    public int line_middle_y; 
    public int graph_height; 


    boolean quit=false;

    int     bg_offset=0;

    // variable to hold the current state
    public byte state=STATEID_INITIAL;
    public byte nextstate=STATEID_INITIAL;
 
    //    int local_max=-1;

    int y_off=0; 
    int spacer_small=0;
    int spacer_medium=0;
    int spacer_large=0;

    String[] menu_items;
    byte[]    menu_actions;
    byte act_menu_select=0;
    byte[] act_menu_select_bak;
    String[] lcd_lines =null;


    // to check if 2 keys are pressed 
    byte keycontrol_exit=0;


    byte     setup_pos;
    byte[]   tmp_actions;
    String[] tmp_items;
    //    boolean expert_mode=false;


    byte act_lang=0;

    public String[] localized_strings;

    public String l(int str_id)
    {
	    return localized_strings[str_id];

    }
    public void tmp_menu_init(int max_items)
    {
	setup_pos=0;
	tmp_actions=new byte[max_items];
	tmp_items=new String[max_items];
    }

    public void tmp_menu_add(String label,byte action)
    {
	tmp_actions[setup_pos] = action;
	tmp_items[setup_pos] = label;
	setup_pos++;
    }

    public void tmp_menu_use()
    {
	byte[]   tmp_actions_fin=new byte[setup_pos];
	String[] tmp_items_fin=new String[setup_pos];

	if (setup_pos<act_menu_select)
	    act_menu_select=0;
	
	for ( int tmp_p=0;tmp_p<setup_pos;tmp_p++)
	    {
		tmp_actions_fin[tmp_p]=tmp_actions[tmp_p];
		tmp_items_fin[tmp_p]  =tmp_items[tmp_p];
	    }

	setup_menu(tmp_items_fin,tmp_actions_fin);
    }
    
    public void setup_conn_menu()
    {
	tmp_menu_init(10);


	if (mk.connected)
	    {
		tmp_menu_add(l(STRINGID_PACKET_TRAFFIC),ACTIONID_TRAFFIC);
		tmp_menu_add(l(STRINGID_VIEW_DATA),ACTIONID_DATABUFF);
		tmp_menu_add(l(STRINGID_FORCERECONNECT),ACTIONID_RECONNECT);


	    }


	if(settings.recent_connection_count()>0)
	    tmp_menu_add(l(STRINGID_CONNECT_RECENT),ACTIONID_CONNECT_RECENT);


//#if bluetooth=="on"
	tmp_menu_add(l(STRINGID_CONNECT_BT),ACTIONID_SCAN_BT);
//#endif

	tmp_menu_add(l(STRINGID_CONNECT_TCP),ACTIONID_CONNECT_TCP);

	tmp_menu_add(l(STRINGID_CONNECT_URL),ACTIONID_CONNECT_URL);


	tmp_menu_add(l(STRINGID_CONNECT_FAKE),ACTIONID_CONNECT_FAKE);



	if ((System.getProperty("microedition.commports")!=null)&&(!System.getProperty("microedition.commports").equals("")))
	    tmp_menu_add(l(STRINGID_CONNECT_COM),ACTIONID_SELECT_COM);
	tmp_menu_add(l(STRINGID_SET_PROXY),ACTIONID_PROXY_INPUT);
	tmp_menu_add(l(STRINGID_BACK),ACTIONID_MAINMENU);
	tmp_menu_use();

    }


    public void setup_main_menu()
    {
	tmp_menu_init(30);
	

	tmp_menu_add(l(STRINGID_SETTINGS),ACTIONID_SETTING_OPTIONS);
	tmp_menu_add(l(STRINGID_CONN),ACTIONID_CONN_DETAILS);


	if (settings.dev_mode)
	    {
		//		tmp_menu_add(l(STRINGID_SELECT_FIRMWARE),ACTIONID_SELECT_FIRMWARE);
		//		tmp_menu_add("UPDATE_DUBWISE",ACTIONID_UPDATE_DUBWISE);
	    
		//		tmp_menu_add("Follow Me",ACTIONID_SHOWPHONEGPS);

		tmp_menu_add("File Open",ACTIONID_SELECT_DUBWISEDIR);
		tmp_menu_add(l(STRINGID_DEBUG),ACTIONID_DEBUG);
	    }





	// only mk	    
	if (mk.is_mk())
	    {
		//		tmp_menu_add("Start Engines",ACTIONID_START_ENGINES);


		tmp_menu_add(l(STRINGID_MOTORTEST),ACTIONID_MOTORTEST);
		tmp_menu_add(l(STRINGID_KEYCONTROL),ACTIONID_KEYCONTROL);
		tmp_menu_add(l(STRINGID_RCDATA),ACTIONID_RCDATA);
		tmp_menu_add(l(STRINGID_FLIGHTSETTINGS),ACTIONID_PARAM_MENU);
		tmp_menu_add(l(STRINGID_GRAPH),ACTIONID_GRAPH);

		tmp_menu_add("Mixer",ACTIONID_SELECT_MIXER);
	    }

	if (mk.is_fake())
	    {
		tmp_menu_add(l(STRINGID_GRAPH),ACTIONID_GRAPH);
		tmp_menu_add("Big Symbols",ACTIONID_BIG_SYMBOLS);
		tmp_menu_add(l(STRINGID_COCKPIT),ACTIONID_HORIZON);
	    }

	if ( mk.is_rangeextender()|| mk.is_followme() )
	    {
		tmp_menu_add(l(STRINGID_LCD),ACTIONID_LCD);
		tmp_menu_add(l(STRINGID_DEBUG_VALUES),ACTIONID_RAWDEBUG);
	    }

	// mk & navi

	if ( mk.is_navi()||mk.is_mk() || mk.is_riddim() )
	    {
		tmp_menu_add("Big Symbols",ACTIONID_BIG_SYMBOLS);
		tmp_menu_add(l(STRINGID_COCKPIT),ACTIONID_HORIZON);
		tmp_menu_add(l(STRINGID_LCD),ACTIONID_LCD);
	    }



	if (( mk.is_navi()||mk.is_mk()||mk.is_mk3mag() ))
	    tmp_menu_add(l(STRINGID_DEBUG_VALUES),ACTIONID_RAWDEBUG);

	
//#if j2memap=="on"
	if (mk.is_fake())tmp_menu_add("Map",ACTIONID_MAP);
//#endif

	// only navi
	if ( mk.is_navi())
	    {

//#if j2memap=="on"
		tmp_menu_add("Map",ACTIONID_MAP);
//#endif


//#if (location=="on")&&(cldc11=="on")
		tmp_menu_add("Follow Me",ACTIONID_SHOWPHONEGPS);
//#endif

		tmp_menu_add(l(STRINGID_VIEW_GPS),ACTIONID_GPSDATA);
		if (mk.gps_position.ErrorCode!=0) tmp_menu_add(l(STRINGID_VIEW_ERRORS),ACTIONID_NC_ERRORS);
		tmp_menu_add(l(STRINGID_SWITCH_FC),ACTIONID_SWITCH_FC);
		tmp_menu_add(l(STRINGID_SWITCH_MK3MAG),ACTIONID_SWITCH_MK3MAG);

	    }

	if (mk.is_mk()||mk.is_mk3mag() )
	    tmp_menu_add(l(STRINGID_SWITCH_NAVI),ACTIONID_SWITCH_NC);


	//if ((settings.expert_mode)&& ( mk.ufo_prober.is_navi()||mk.ufo_prober.is_mk()||mk.ufo_prober.is_mk3mag()||mk.ufo_prober.is_incompatible()   ))
	  
	if ((settings.expert_mode)||mk.is_mk())
	    tmp_menu_add(l(STRINGID_FLASH_FIRMWARE),ACTIONID_FLASH);
	    
	//if (settings.dev_mode&& mk.is_mk()   )
	//    tmp_menu_add(l(STRINGID_REMOTE_CAM),ACTIONID_CAM);

	tmp_menu_add(l(STRINGID_REMOTE_CAM),ACTIONID_CAM);

	tmp_menu_add(l(STRINGID_ABOUT),ACTIONID_ABOUT);

	tmp_menu_add(l(STRINGID_QUIT),ACTIONID_QUIT);

	tmp_menu_use();
    }

    public void setup_menu(String[] items , byte[] actions)
    {
	if ((menu_items==null)||(act_menu_select>=menu_items.length)) 
	    act_menu_select=0;
	menu_items=items;
	menu_actions=actions;
	lcd_lines=new String[menu_items.length];
       
    }

    public void setup_menu(int[] items , byte[] actions)
    {
	menu_items=new String[items.length];
	for(int i=0;i<items.length;i++)
	    menu_items[i]=l(items[i]);

	menu_actions=actions;
	lcd_lines=new String[menu_items.length];
    }

    public void paint_menu(Graphics g)
    {
	if (menu_items==null) return;
	for ( int i=0;i<menu_items.length;i++)
	    {
		if ((frame_pos%3)!=0)
		    {
			lcd_lines[i]=(act_menu_select==i?">":" ") + menu_items[i];	
			for ( int ii=0;ii<(18-menu_items[i].length());ii++)
			    lcd_lines[i]+=" ";
			if (act_menu_select==i)
			    lcd_lines[i]+="<";
		    }
		else
		    lcd_lines[i]=" " + menu_items[i];	
			

	    }
	paint_lcd(g);
    }


    //    int selected_fw=

    public void menu_keypress(int keyCode)
    {
	debug.log("Menu with KeyCode:"+keyCode);
	boolean fire_key=false;
	switch (getGameAction (keyCode)) 
	    {
	    case UP:
		if (act_menu_select!=0) 
		    act_menu_select--;
		else
		    act_menu_select=(byte)(menu_items.length-1);
		break;
		
	    case DOWN:
		if (act_menu_select<(menu_items.length-1)) 
		    act_menu_select++;
		else
		    act_menu_select=0;
		break;

	    case FIRE:
		fire_key=true;
	    }
	if (keyCode==settings.key_alternative_fire) 
	    fire_key=true;
	if (fire_key)
	    switch(state)
		    {

		    case STATEID_SELECT_MIXER:
			if (act_menu_select<menu_items.length-1)
			    // mk.set_mixer_table(mixer_manager.bytes(act_menu_select));
			    {
				mixer_manager.setDefaultValues(act_menu_select);
				mk.set_mixer_table(mixer_manager.getFCArr());
			    }
			else
			    chg_state(STATEID_MAINMENU);

			break;

		    case STATEID_SELECT_FIRMWARE:	

			fw_loader.menu_fire(act_menu_select);
						    
			break;
		    case STATEID_SELECT_COMPORT:	
			if (act_menu_select<menu_items.length)
			    connect_mk("comm:"+menu_items[act_menu_select]+";baudrate=57600",menu_items[act_menu_select]);
		        
			chg_state(STATEID_CONN_DETAILS);			   
			break;

//#if fileapi=="on"
		    case STATEID_FILEOPEN:
			
			file_access.fire();
		    
			break;

//#endif

		    case STATEID_SELECT_PARAMSET:

			if (act_menu_select==5) // back
			    {
				if (select_paramset4edit)
					chg_state(STATEID_PARAM_MENU);
				else
				    chg_state(STATEID_HANDLE_PARAMS); // from save as
				
			    }
			else
			    {
				if (select_paramset4edit)
				    //			if ( mk.params.field[act_menu_select]!=null) 
				    { 
					mk.params.act_paramset=act_menu_select;
					chg_state(STATEID_HANDLE_PARAMS);
					//act_msg="Params saved";
				    }
				else
				    { 
					mk.write_params(act_menu_select);
					nextstate=STATEID_HANDLE_PARAMS;
					act_msg="saved in slot " + (act_menu_select+1) ;
					chg_state(STATEID_SUCCESS_MSG);  // TODO - ground too optimistic way ;-)
				    }

			    }


			break;

		    case STATEID_CONNECT_RECENT:
			if (act_menu_select!=settings.recent_connection_count())
			    {
				mk.close_connections(true);
				connect_mk(settings.recent_conn_url(act_menu_select),settings.recent_conn_name(act_menu_select));
			    }
			chg_state(STATEID_CONN_DETAILS);
			break;
			
//#if bluetooth=="on"
		    case STATEID_DEVICESELECT:
			

			if (bt_scanner.remote_device_count > act_menu_select)
			    {
				
				connect_mk("btspp://"+bt_scanner.remote_device_mac[act_menu_select] + ":1",bt_scanner.remote_device_name[act_menu_select]);
				chg_state(STATEID_CONN_DETAILS);
			    }
			else
			    {
				if (bt_scanner.remote_device_count == act_menu_select)
				    chg_state(STATEID_SCANNING);
				else
				    chg_state(STATEID_CONN_DETAILS);
			    }
				    
			break;
//#endif


		    default:
			if (menu_actions!=null)
			    process_action(menu_actions[act_menu_select]);
		    }
    

    }

    public boolean cam_condition()
    {
	return true;
	//	return (mk.stick_data.stick[settings.remote_cam_stick]>100);
    }

    //    int lcd_top=25;
    int lcd_off;
    public void paint_lcd(Graphics g)
    {

	int y;
	//	int lcd_top= (state==STATEID_EDIT_PARAMS?0:25);
	//int lcd_top= 25;
	max_lines=(byte)((canvas_height-25/*lcd_top*/)/lcd_char_height);
	int spacer_left_right=(canvas_width-(20*(lcd_img.getWidth()/222)))/2;
	//	for(int i=0;i<lcd_lines.length;i++)

	int display_lines=(lcd_lines.length>max_lines?max_lines:lcd_lines.length);



	lcd_off=act_menu_select-display_lines+1;
	if (((act_editor!=null)&&(!act_editor.select_mode)&&((state!=STATEID_ERROR_MSG)&&(state!=STATEID_SUCCESS_MSG))))
	    lcd_off=act_editor.act_y-display_lines+1;

	if ( lcd_off<0)  lcd_off=0;

	for(int i=0;i<display_lines;i++)
	    for (int pos=0;pos<20;pos++)
	    {
		/*if (!bottomup)
		    y=i*lcd_char_height;
		    else*/
		y=canvas_height-(display_lines-i)*lcd_char_height;

		g.setClip((lcd_img.getWidth()/222)*pos+spacer_left_right,y,(lcd_img.getWidth()/222),lcd_img.getHeight());
		g.drawImage(lcd_img,spacer_left_right+(lcd_img.getWidth()/222)*pos-((pos<lcd_lines[i+lcd_off].length()?lcd_lines[i+lcd_off].charAt(pos):' ')-' ')*(lcd_img.getWidth()/222),y,g.TOP | g.LEFT);
	    }

    }

    public void load_skin_images()
    {
	try
	    {
		bg_img=null;
		lcd_img=null;

		// load all needed images
		switch (settings.act_skin)
		    {
		    case SKINID_DARK:
			lcd_img=Image.createImage("/lcd_green.png");
			if (settings.do_scrollbg) bg_img=Image.createImage("/starfield.jpg"); 
			break;
			
			
		    case SKINID_LIGHT:
			lcd_img=Image.createImage("/lcd_blue.png");
			if (settings.do_scrollbg) bg_img=Image.createImage("/clouds.jpg"); 
			break;
		    }
		//		lcd_char_width=lcd_img.getWidth()/222;
		lcd_char_height=lcd_img.getHeight();
	    }

	catch (Exception e)
	    {
		debug.err(e.toString());
	    }

    }
    public void load_global_images()
    {
	try
	    {
		icon_img=Image.createImage("/i.png");
		symbols_img=Image.createImage("/symbols.png");
		symbols_img_tile_height=symbols_img.getHeight()/3;
		symbols_img_tile_width=symbols_img.getWidth()/10;

		/*		if (bg_img!=null)
		    graph_data=new int[GRAPH_COUNT][bg_img.getWidth()];
		else
		*/

	    }
	
	catch (Exception e)
	    {
		debug.err(e.toString());
	    }
	
    }


    public void load_strings()
    {
	//	System.out.println("loading strings");
	localized_strings=new String[STRING_COUNT];
	
	try {
	    InputStream in=this.getClass().getResourceAsStream("/l");	    
	    
	    for (int i=0;i<STRING_COUNT;i++)
		{
		    String tmp_s="";  // temp string to use
		    String tmp_b="";  // backup string ( if string isnt present in selected lang - e.g. same as in english )
		    char ch=0;

		    int act_l=0;
		    while ( (ch=(char)in.read())!='\n')
			{
			    if (ch==';')
				act_l++;
			    else
				{
				    // "backup" str
				    if (act_l==0)
					tmp_b+=ch;
				    
				    
				    // normal str
				    if (act_l==act_lang)
					tmp_s+=ch;
				}
  
			}


		    if (tmp_s.equals(""))
			localized_strings[i]=tmp_b;	    
		    else
			localized_strings[i]=tmp_s;	    

		}
	}
	catch (Exception e) {		    System.out.println("str_load err"+e);}
    }


    // construct
    public DUBwiseCanvas(DUBwise _root)
    {

	int foo=0;

	//       
	load_strings();

	
	act_menu_select_bak=new byte[STATEID_COUNT];

	for (int i=0;i<STATEID_COUNT;i++)
	    act_menu_select_bak[i]=0;

//#if fileapi=="on"
	file_access=new DUBwiseFileAccess(this);
//#endif


	root=_root;

	mixer_manager = new MixerManager();
	settings = new DUBwiseSettings(this);

	try {
	   	settings.load();
	}
	catch ( Exception e) {}

	load_global_images();

	debug   = new DUBwiseDebug(this);



//#if voice_mode!="no_voice"
	status_voice=new MKStatusVoice(this);
//#endif

	web_reporter=new UFOWebReporter(this);
	
//	mk.gps_position.act_speed_format=settings.speed_format;
///	mk.gps_position.act_gps_format=	settings.gps_format;

	
	chg_state(STATEID_MAINMENU);
	
	new Thread(this).start();

    }

    public void refresh_dynamic_menues()
    {
	if (state==STATEID_MAINMENU)
	    chg_state(STATEID_MAINMENU); // reload mainmenu ( changed content )
	if (state==STATEID_CONN_DETAILS)
	    chg_state(STATEID_CONN_DETAILS); // reload con details ( changed content )
    }



    
    /****************************** Thread ******************/
    // ticking runnable Section
    public void run()
    {
        while(true)
            {
		if ((firmware_flasher!=null))
		    {
			if (mk!=null)
			    {
				mk.close_connections(true);
				mk=null;
			    }
			if (!firmware_flasher.started)
			    firmware_flasher.start();
		    }

		if ( key_bitfield==(keyCode2mask(KEY_NUM1)|keyCode2mask(KEY_NUM3)))
		    debug_overlay=!debug_overlay;

		try {


                repaint();
                serviceRepaints();

		System.gc();
                long loopStartTime = System.currentTimeMillis();
                long sleeptime=0;
                // ticked thing
	
		frame_pos++;

		if (mk!=null)
		    {
			if (mk.mixer_change_notify)
			    {
				nextstate=STATEID_MAINMENU;
				if (mk.mixer_change_success)
				    {
					act_msg="Mixer Write OK";
					chg_state(STATEID_SUCCESS_MSG);
				    }
				    else
					
				    {
					act_msg="Mixer Write Fail";
					chg_state(STATEID_ERROR_MSG);
				    }
				mk.mixer_change_notify=false;
			    }

			// handle Device Change
			if (mk.change_notify)
			    {
				mk.change_notify=false; // handling of event done

				refresh_dynamic_menues();
				if ((mk.is_incompatible())&&(settings.instant_error_show))
				    {
					act_msg=l(STRINGID_INCOMPATIBLEDEVICE);
					chg_state(STATEID_ERROR_MSG);
				    }
				else
				    {
				    }

				if (mk.connected)
				    settings.add_recent(mk.mk_url,mk.name);
				
				if (state==STATEID_ERROR_MSG)
				    chg_state(STATEID_MAINMENU);
				
			    }
			
			if (mk.disconnect_notify)
			    {
				mk.disconnect_notify=false;				
				
				if (!mk.mk_url.equals("")&&(settings.instant_error_show))
				    {
					if (settings.instant_error_show)
					    {
						nextstate=STATEID_MAINMENU;
						act_msg=l(STRINGID_DISCONNECT);
						chg_state( STATEID_ERROR_MSG);
					    }
					if (settings.do_vibra)
					    root.vibrate(100);
				    }
			    }
			
			if (mk.is_navi())
			    {
				if ((last_navi_error==0)&&(mk.gps_position.ErrorCode!=0))
				    {
					if (settings.instant_error_show)
					    {
						nextstate=state;
						
						act_msg=null;
						chg_state( STATEID_ERROR_MSG);
					    }
					if (settings.do_vibra)
					    root.vibrate(100);
				    }
				else
				    if ((last_navi_error!=0)&&(mk.gps_position.ErrorCode==0)&&(state== STATEID_ERROR_MSG))
					chg_state(nextstate);
				    else if (last_navi_error!=mk.gps_position.ErrorCode)
					act_msg=null;
				last_navi_error=(byte)mk.gps_position.ErrorCode;
			    }
			
		    }

		switch(state)
		    {

//#if fileapi=="on"
		    case STATEID_FILEOPEN:
			file_access.refresh_if_dirty();
			break;
//#endif


		    case STATEID_GET_AVRSIG:
			if (firmware_flasher.job_done)
			    {
				fw_loader=new FirmwareLoader(firmware_flasher.avr_sig,this);
				firmware_flasher.exit_bootloader();
				firmware_flasher=null;
				chg_state(STATEID_SELECT_FIRMWARE);
			    }
			break;

		    case STATEID_FLASHING:
			if (firmware_flasher.job_done)
			    {
				
				if (firmware_flasher.success)
				    {
					nextstate=STATEID_MAINMENU;
					act_msg="Flashing OK";
					chg_state(STATEID_SUCCESS_MSG);
				    }
				else
				    {
					nextstate=STATEID_MAINMENU;
					act_msg="Flashing Fail";
					chg_state(STATEID_ERROR_MSG);
				    }
				
			    }
			
		       
			break;


//#if (location=="on")&&(cldc11=="on")

		    case STATEID_SHOWPHONEGPS:

			try
			    {
				//				Class.forName("javax.microedition.location.LocationProvider");

				if ((lp==null))
				    //				if ((cr==null)||(lp==null))
				    {
					//Criteria cr= new Criteria();
					//cr.setHorizontalAccuracy(500);
				
					// Get an instance of the provider
					//					LocationProvider lp= LocationProvider.getInstance(cr);
					lp= LocationProvider.getInstance(null);
					lp.setLocationListener(this, 1, -1, -1);

				    }
				// Request the location, setting a one-minute timeout

				else if (false){

				    Location l = lp.getLocation(10);
				    c = l.getQualifiedCoordinates();



				/* low crit

Criteria crit2 = new Criteria();
crit2.setHorizontalAccuracy(Criteria.NO_REQUIREMENT);
crit2.setVerticalAccuracy(Criteria.NO_REQUIREMENT);
crit2.setPreferredResponseTime(Criteria.NO_REQUIREMENT);
crit2.setPreferredPowerConsumption(Criteria.POWER_USAGE_HIGH);
crit2.setCostAllowed(false);
crit2.setSpeedAndCourseRequired(false);
crit2.setAltitudeRequired(false);
lp= LocationProvider.getInstance(crit2);

				*/

				if(c != null ) {
				    // Use coordinate information
//#if cldc11=="on"

				    phone_lat= c.getLatitude();
				    phone_long = c.getLongitude();
//#endif
				}
				}
			    }
			catch (Exception e)
			    {
					debug.log("Follow ME Err" );
					debug.log("-" + e );
					debug.log("-" + e.getMessage() );
				//    act_msg="Phone GPS Problem";
				//    chg_state(STATEID_ERROR_MSG);
				
			    }
		
			break;
	
//#endif	
		    case STATEID_SELECT_FIRMWARE:


			//			if ((menu_items.length==0)&&(fw_loader.got_list))
			//   setup_menu(fw_loader.names,null);
			
			
			break;
			
		    case STATEID_RESET_PARAMS:
			//			if (mk.bootloader_finish_ok)
			if (firmware_flasher.job_done)
			    {
				//				mk.params.reset();
				//mk.watchdog.act_paramset=0;
				nextstate=STATEID_PARAM_MENU;
				act_msg=l(STRINGID_PARAMRESETOK);
				firmware_flasher.exit_bootloader();
				firmware_flasher=null;
				chg_state(STATEID_SUCCESS_MSG);
			    }
			break;

			/*
		    case STATEID_GRAPH:

		       
			graph_offset--;
			if (graph_offset==-graph_data[0].length)
			    graph_offset=0;
			break;
			*/
                    case STATEID_CAMMODE:
			
			try
			    {
				if(cam_condition())
				    {
					cam_img=null;
					debug.log("get snap\n");
					//xda					cam_raw = mVideoControl.getSnapshot(null);
					
					
					try { Thread.sleep(4000); }
					catch (Exception e)
					    {
						debug.log("Problem Sleeping ");
						
					    }
					cam_img = Image.createImage(cam_raw, 0, cam_raw.length);
				    }
				else
				    {
					if (cam_img==null)
					    ;;
				    }
			    }
			catch ( Exception e)
			    {
				debug.log(e.toString());
			    }
			break;
		    case STATEID_KEYCONTROL:
			//mk.send_keys(keycontrol_bitfield);	
			break;
			

		    case STATEID_ERROR_MSG:
			lcd_lines[0]=""+act_msg;
			break;
			

		    case STATEID_PARAM_MASSWRITE:

			if (param_masswrite_write_pos==5)
			    {
				nextstate=STATEID_PARAM_MENU;
				act_msg=l(STRINGID_PARAMWRITEOK);
				chg_state(STATEID_SUCCESS_MSG);
			    }
			else
			    {
				if (param_masswrite_write_pos!=0) 
				    try { Thread.sleep(2000); }
				    catch (Exception e)
					{debug.log("Problem Sleeping ");   }
				
				lcd_lines[0]=l(STRINGID_WRITINGPARAMS);
				lcd_lines[1]=DUBwiseHelper.pound_progress(param_masswrite_write_pos,5);

			
				mk.params.set_by_mk_data(params2masswrite[param_masswrite_write_pos]);
				mk.params.act_paramset=param_masswrite_write_pos;
				mk.write_params(mk.params.act_paramset);
				
				param_masswrite_write_pos++;
			    }
			break;
		    case STATEID_READ_PARAMS:
			if (mk.watchdog.act_paramset==5)
			    {
				if (read_paramset_intension_save) 
				    {
		
					try
					    {
						RecordStore.deleteRecordStore( PARAM_SAVE_STORE_NAME);
					    }
					catch (Exception e)
					    { }
					
					try {
					    RecordStore recStore = RecordStore.openRecordStore( PARAM_SAVE_STORE_NAME, true );
					    
					    ByteArrayOutputStream bout = new ByteArrayOutputStream();
					    DataOutputStream      dout = new   DataOutputStream( bout );
					    
					    // params_version
					    dout.writeInt(mk.params.params_version);
					    
					    // params_length
					    dout.writeInt(mk.params.field_bak[0].length);
					    
					    for (int p=0;p<5;p++)
						for (int i=0;i<mk.params.field_bak[0].length;i++)
						    dout.writeInt(mk.params.field_bak[p][i]);

					    recStore.addRecord(bout.toByteArray(),0,bout.size());
					    recStore.closeRecordStore();
					    
					    
					}
					catch (Exception e)
					    {  	    }
					//					chg_state(STATEID_COPY_PARAMS);

					nextstate=STATEID_PARAM_MENU;
					act_msg="Parameter Copy OK";
					chg_state(STATEID_SUCCESS_MSG);
					
				    }
				else
				    {
					select_paramset4edit=true;
					chg_state(STATEID_SELECT_PARAMSET);

				    }

			    }
			else
			    {
				lcd_lines[0]="Reading Settings    ";
				lcd_lines[1]=DUBwiseHelper.pound_progress(mk.watchdog.act_paramset,5);
				
				
				if (mk.params.incompatible_flag!=MKParamsParser.INCOMPATIBLE_FLAG_NOT)
				    {
					act_msg="incompatible params";
					chg_state(STATEID_ERROR_MSG);
				    }
				
			    }
			break;
			

		    case STATEID_MOTORTEST:
			/*
			if (motor_test_sel_all)
			    for (int m=0;m<4;m++)
				{
				    motor_test[m]+=act_motor_increase;
				    if (motor_test[m]<0)motor_test[m]=0;
				    if (motor_test[m]>255)motor_test[m]=255;
				}
			else
			    {
				motor_test[act_motor]+=act_motor_increase;
				if (motor_test[act_motor]<0) motor_test[act_motor]=0;
				if (motor_test[act_motor]>255) motor_test[act_motor]=255;
			    }

			    mk.motor_test(motor_test);*/
			
			if (motor_tester.new_is_dangerous())
			    {
				nextstate=STATEID_CONFIRM_MOTORTEST;
				act_msg="Warning Dangerous";
				chg_state(STATEID_ERROR_MSG);
			    }
			else
			    {
				motor_tester.backup_engine_field();
				mk.motor_test(motor_tester.engine_field);
			    }
			break;

		    case STATEID_STRINGINPUT:
			
			lcd_lines[0]=act_input_str;
			for(int tmp_i=act_input_str.length();tmp_i<20;tmp_i++)
			    lcd_lines[0]+=(char)(0);

			lcd_lines[1]="";
			for(int foo=0;foo<20;foo++)
			    {
				if (foo==ipinput_pos)
				    lcd_lines[1]+="^";
				else
				    lcd_lines[1]+=" ";
			    }
			break;


		    case STATEID_IPINPUT:
			if (ipinput4proxy)
			    act_edit_ip=settings.act_proxy_ip;
			else
			    act_edit_ip=settings.act_conn_ip;			    
			    
			lcd_lines[1]=DUBwiseHelper.ip_str(act_edit_ip,true);
			

			lcd_lines[2]="";
			for(int foo=0;foo<20;foo++)
			    {
				if (foo==ipinput_pos)
				    lcd_lines[2]+="^";
				else
				    lcd_lines[2]+=" ";
			    }
			break;

		    case STATEID_MAINMENU:

			break;

//#if bluetooth=="on"
		    case STATEID_SCANNING:

			lcd_lines[1]="Bluetooth Devices " + idle_seq[(((frame_pos/5)%idle_seq.length))]; 
			lcd_lines[2]="found " + bt_scanner.remote_device_count;
			
			if (!bt_scanner.searching)
				chg_state(STATEID_DEVICESELECT);
			
			break;
//#endif

			
		    }

		if (quit) 
		    {
			//			settings.speed_format=mk.gps_position.act_speed_format;
			//settings.gps_format=mk.gps_position.act_gps_format;
			settings.save();
			root.quit();
		    }


		    //rescan=false;

		if (bg_img!=null)
		    {
			bg_offset--;
			if (bg_offset==-bg_img.getWidth())
			    bg_offset=0;
		    }



		
//#if devicecontrol=="on"
		try {
		    if (mk.connected&&settings.keep_lighton) com.nokia.mid.ui.DeviceControl.setLights(0,100);

		}
		catch (Exception e)   
		    {
			// cannot do anything helpfull here 
		    }
//#endif       	
			
                sleeptime=1000/ 15 - (int) (System.currentTimeMillis()- loopStartTime);

		if (sleeptime<0)
		    sleeptime=100; // everyone has fi sleep
		
		try { Thread.sleep(sleeptime); }
                catch (Exception e)
                    {
			debug.log("Problem Sleeping ");
                    }

		}
		catch (Exception e)
		    {
			debug.log("E!:"+e.getMessage());

		    }
            }
    }

      

    boolean firstrun=true;

    public int skin_bg_color()
    {
	
	switch (settings.act_skin)
	    {
	    case SKINID_DARK:
		return BG_COLOR_SKIN_DARK; 

	    default:		
	    case SKINID_LIGHT:
		return BG_COLOR_SKIN_LIGHT; 
	    }
    }


    public int skin_fg_color()
    {
	
	switch (settings.act_skin)
	    {
	    case SKINID_DARK:
		return FG_COLOR_SKIN_DARK;
		
	    default:
	    case SKINID_LIGHT:
		return FG_COLOR_SKIN_LIGHT;
	    }
    }

    /*
    public int check_local_max(int val)
    {
	if ( val>local_max)
	    local_max=val;
	else if (-val>local_max)
	    local_max=-val;
	return val;
    }
    */


    public byte big_symbol_by_pos(byte pos)
    {
	switch(pos)
	    {

	    case 0:
		if (mk.connected)
		    return (byte)((((mk.stats.bytes_in>>3)&1)==1)?2:3);

		else
		    {
			if (mk.mk_url.startsWith("btspp://"))
			    return 1;
			else return 29;
		
			    
		    }
	    case 1:
		if (mk.SenderOkay()>0)
		    return 6;
		else
		    return 29;


	    case 5:
		return 4;

	    case 6:
		if ( mk.UBatt()==-1)
		    return 29;
		else
		    return (byte)(mk.UBatt()/100+10);
		

	    case 7:
		if ( mk.UBatt()==-1)
		    return 29;
		else
		    return (byte)((mk.UBatt()/10)%10+10); 
		
	    case 8:
		if ( mk.UBatt()==-1)
		    return 29;
		else
		    return 9;

	    case 9:
		if ( mk.UBatt()==-1)
		    return 29;
		else
		    return (byte)((mk.UBatt())%10+10); 
		    

	    case 10:
		return (byte)(((mk.stats.flying_time()/60)/10)%10+10);
	    case 11:
		return (byte)(((mk.stats.flying_time()/60))%10+10);

	    case 12:
		return 23;

	    case 13:
		return (byte)(((mk.stats.flying_time()%60)/10)%10+10);
	    case 14:
		return (byte)(((mk.stats.flying_time()%60))%10+10);

	    case 15:
		return (byte)((mk.getAlt()/10000)%10+10);

	    case 16:
		return (byte)((mk.getAlt()/1000)%10+10);

	    case 17:
		return (byte)((mk.getAlt()/100)%10+10);
	    case 18:
		return (byte)((mk.getAlt()/10)%10+10);

	    case 19:
		return (byte)((mk.getAlt())%10+10);

	    default:
		return 29;

	    }
    }
    public void big_symbol_paint(Graphics g,byte x,byte y,byte tile)
    {
	int big_symbols_tile_width=big_symbols_img.getWidth()/10;
	int big_symbols_tile_height=big_symbols_img.getHeight()/3;

	byte tile_y=(byte)(tile/10);
	tile%=10;

	g.setClip(x*big_symbols_tile_width,y*big_symbols_tile_height,(big_symbols_tile_width),big_symbols_tile_height);
	g.drawImage(big_symbols_img,x*big_symbols_tile_width+(-tile)*(big_symbols_tile_width),(y-tile_y)*(big_symbols_tile_height), g.TOP | g.LEFT);	   		    
    }

    public void symbol_paint(Graphics g,int x,int tile,int row)
    {
	g.setClip(x,0,(symbols_img_tile_width),symbols_img_tile_height);;
	g.drawImage(symbols_img,x+(-tile)*(symbols_img_tile_width),row*(-symbols_img_tile_height), g.TOP | g.LEFT);	   		    
    }


    Font font_medium,font_small,font_large;



    boolean regenerate_fonts=true;
    // drawing sections
    public void paint(Graphics g) {
	
	canvas_width=this.getWidth();
	canvas_height=this.getHeight();

	if (debug.showing) 
	    {
		debug.paint(g);
		return;
	    }


	if (firstrun)
	    {
		if (settings.fullscreen) setFullScreenMode(settings.fullscreen);
		firstrun=false;
	    }
		
	if (regenerate_fonts)
	    {
		if (settings.big_fonts)
		    {
			font_small = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);  
			font_medium = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
			font_large = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);  

		    }
		else
		    {
			font_small = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);  
			font_medium = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
			font_large = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);  
		    }
		    
		spacer_medium=(font_medium.getHeight());
		spacer_small=(font_small.getHeight());
		spacer_large=(font_large.getHeight());
		regenerate_fonts=false;
	    }

	y_off=0; 
	
	try {


	    //default Font
	    g.setFont(font_medium);


	    //draw background
	    if ((settings.do_scrollbg)&&(bg_img!=null))
		{
		    g.setColor(0xFFFFFF);
		    g.fillRect(0,0,canvas_width,canvas_height);
		    g.drawImage(bg_img,bg_offset,0, g.TOP | g.LEFT);
		    
		    if (bg_offset+bg_img.getWidth()<canvas_width)
			g.drawImage(bg_img,bg_offset+bg_img.getWidth(),0, g.TOP | g.LEFT);		

		}
	    else
		{
		    g.setColor(0xdedfff);
		    g.fillRect(0,0,canvas_width,symbols_img_tile_height);

		    g.setColor(skin_bg_color());
		    g.fillRect(0,symbols_img_tile_height,canvas_width,canvas_height-symbols_img_tile_height);
		}


	    //int bar=0;
	    //	    for ( int bar=0;bar<3;bar++)
	    if (settings.do_scrollbg)
		for ( int bar=0;bar<canvas_width/(symbols_img.getWidth()/10)+1;bar++)
		    {
			g.setClip(bar*(symbols_img_tile_width),0,(symbols_img_tile_width),symbols_img_tile_height);;
			g.drawImage(symbols_img,bar*(symbols_img_tile_width),0, g.TOP | g.LEFT);

		    }

	    if (mk!=null)
		{
		    int symbol_left=0;
		    int symbol_spacer=5;

		    
		    
		    //		    g.setClip(symbol_left,0,symbols_img_tile_width,symbols_img_tile_height);;
		    if (mk.connected)
			symbol_paint(g,symbol_left,(((mk.stats.bytes_in>>3)&1)==1)?2:3,0);

		    else
			{
			    if (mk.mk_url.startsWith("btspp://"))
				symbol_paint(g,symbol_left,1,0);
		
			    
			}
		    //		g.drawImage(symbols_img,(-1)*(symbols_img.getWidth()/10),0, g.TOP | g.LEFT);	   		
		    
		    
		    
		    symbol_left+=6*(symbols_img_tile_width)/4;
		    
		    
		    if (mk.UBatt()!=-1)
			{
			    
			    symbol_paint(g,symbol_left,4,0);
			    symbol_left+=5 *(symbols_img_tile_width)/4;
			    
			    if ((mk.UBatt()/100)!=0)
				{
				    symbol_paint(g,symbol_left,(mk.UBatt()/100),1);
				    symbol_left+=(symbols_img_tile_width);
				}
			    
			    symbol_paint(g,symbol_left,(mk.UBatt()/10)%10,1);
			    symbol_left+=(2*symbols_img_tile_width/3);
			    symbol_paint(g,symbol_left,9,0);
			    symbol_left+=(2*symbols_img_tile_width/3);
			    symbol_paint(g,symbol_left,(mk.UBatt())%10,1);
			    symbol_left+=6*(symbols_img_tile_width)/4;
			    
			    
			    //		    g.drawString("" + (mk.UBatt()/10) + "," +(mk.UBatt()%10)+"V" , symbol_left,y_off,Graphics.TOP | Graphics.LEFT);
			    
			    
			    //symbol_left+= 	    g.getFont().stringWidth("88,8V");//;
			    /*
			      symbol_paint(g,symbol_left,6,0);
			      symbol_left+=5*(symbols_img_tile_width)/4;
			    */
			}
		    if (mk.SenderOkay()>0)
			{
			    symbol_paint(g,symbol_left,6,0);
			    symbol_left+=5*(symbols_img_tile_width)/4;
				    
				    /*symbol_paint(g,symbol_left,(mk.SenderOkay())/100,1);
				      symbol_left+=(symbols_img_tile_width);
				      
				      symbol_paint(g,symbol_left,(mk.SenderOkay()/10)%10,1);
				      symbol_left+=(symbols_img_tile_width);*/
			}
			    //		    symbol_paint(g,symbol_left,(mk.SenderOkay())%10,1);
			    //symbol_left+=6*(symbols_img_tile_width)/4;
			    
			    //		    g.drawString(""+mk.SenderOkay() ,symbol_left,y_off,Graphics.TOP | Graphics.LEFT);
			    
			    
			    //symbol_left+= 	    g.getFont().stringWidth("8")+symbol_spacer_medium; //,0,(symbols_img.getWidth()/10)+2;
			    



		    if (mk.is_navi())
			{
			    if (mk.gps_position.ErrorCode!=0)
				{
				    symbol_paint(g,symbol_left,7,0);
				    symbol_left+=5*(symbols_img_tile_width)/4;
				}
			    
			    if ((mk.gps_position.NCFlags&2)!=0)
				{
				    symbol_paint(g,symbol_left,8,0);
				    symbol_left+=5*(symbols_img_tile_width)/4;
				}
			    
			    
			    if ((mk.gps_position.NCFlags&4)!=0)
				{
				    symbol_paint(g,symbol_left,0,2);
				    symbol_left+=5*(symbols_img_tile_width)/4;
				    
				}
			    
			    if ((mk.gps_position.NCFlags&8)!=0)
				{
				    symbol_paint(g,symbol_left,0,2);
				    symbol_left+=5*(symbols_img_tile_width)/4;
				    
				}
			    
			    
			}

			    
		    if (mk.SatsInUse()!=-1)
			{
			    symbol_paint(g,symbol_left,5,0);
			    symbol_left+=5*(symbols_img_tile_width)/4;
			    
			    symbol_paint(g,symbol_left,(mk.SatsInUse())%10,1);
			    symbol_left+=6*(symbols_img_tile_width)/4;
			    
			}


		} // if mk!=null
		    // unclip
	    g.setClip(0,0,canvas_width,canvas_height);
	    
	    y_off+=symbols_img_tile_height; 
	    graph_height=(canvas_height-y_off)/2;
	    line_middle_y=graph_height+y_off;
	    g.setColor(skin_fg_color());
	  
	    switch(state)
		{
//#if j2memap=="on"
		case STATEID_MAP:

		    root.m_map.paint(g);

		    break;
//#endif
		case STATEID_HORIZON:

		    g.setStrokeStyle(Graphics.SOLID);  
		    g.setColor(0x6e4e1d);  

		    int horizon_height=(canvas_height-y_off)/2;
		    int horizon_middle=y_off+horizon_height;

		    
		    int horizon_roll_pixels= ((mk.AngleRoll()*horizon_height)/600) * (settings.horizon_invert_roll?-1:1);

		    int nick_bar_width=canvas_width/4;
		    int nick_bar_height= nick_bar_width/2;

		    
		    int nick_size=canvas_width/4;
		    int nick_pixels_y=((mk.AngleNick()*horizon_height)/900) * (settings.horizon_invert_nick?-1:1);;
		
		    int nick_pixels_x;
		    if ((mk.AngleRoll()*mk.AngleNick())>1)
			nick_pixels_x=-((int)Math.sqrt(mk.AngleRoll()*mk.AngleNick())*(canvas_width/2))/(900);
		    else
			nick_pixels_x=((int)Math.sqrt(-mk.AngleRoll()*mk.AngleNick())*(canvas_width/2))/(900);
			

		    if (horizon_roll_pixels>0)

			{

			    g.fillTriangle(0,horizon_middle+horizon_roll_pixels,canvas_width,horizon_middle+horizon_roll_pixels,canvas_width,horizon_middle-horizon_roll_pixels);
			    g.fillRect(0,horizon_middle+horizon_roll_pixels,canvas_width,canvas_height-(horizon_middle+horizon_roll_pixels));
			}
		    else
			{
			    
			    g.fillTriangle(0,horizon_middle+horizon_roll_pixels,0,horizon_middle-horizon_roll_pixels,canvas_width,horizon_middle-horizon_roll_pixels);
			    g.fillRect(0,horizon_middle-horizon_roll_pixels,canvas_width,canvas_height-(horizon_middle-horizon_roll_pixels));
			}


		    

		    g.setColor(0x254d9e);  
		    

		    g.fillArc((canvas_width-nick_size)/2- nick_pixels_x, horizon_middle-nick_size/2+ nick_pixels_y, nick_size, nick_size, 0,360); 


		    if (mk.is_navi())
			{
			    g.setColor(0xCC2233);  
			    g.fillArc((canvas_width-nick_size)/2- nick_pixels_x, horizon_middle-nick_size/2+ nick_pixels_y, nick_size, nick_size, -mk.gps_position.CompasHeading-45/2 +90 +mk.stats.heading_start,45); 
			}



		    //		    for (i=0;i<horizon_roll_pixels
		    //		     g.fillArc(0, 0, canvas_width, canvas_width, 0,45); 
		    
/*
  int start_angle=(360+mk.gps_position.angle2wp(act_wp) - ((360+mk.debug_data.analog[26]-heading_offset)%360))%360;
		    //		    start_angle=0;
		    start_angle=(360-start_angle +90 -(45/2))%360;

		    g.fillArc(0, 0, canvas_width, canvas_width, start_angle,45); 
*/
		    //		    g.drawArc(1, 1, canvas_width-2, canvas_width-2, start_angle,45); 
		    // g.drawArc(2, 2, canvas_width-4, canvas_width-4, start_angle  ,45); 


		    g.setColor(skin_fg_color());


		    if (settings.expert_mode)
			{
			    g.drawString("nick => " + mk.AngleNick(),0,y_off,Graphics.TOP | Graphics.LEFT);
			    y_off+=spacer_medium;

			    g.drawString("roll => " + mk.AngleRoll(),0,y_off,Graphics.TOP | Graphics.LEFT);
			    y_off+=spacer_medium;
			}



		    for (int c=0;c<2;c++)
			{
			    if (c==0)
				g.setColor(0);
			    else
				g.setColor(0xFFFFFF);
			    
			    y_off=canvas_height-5;



			    g.setFont(font_small);
			    if (mk.is_navi())
				g.drawString("avg:" + mk.stats.avg_speed() + " max:"+  mk.stats.max_speed,10-c,y_off-c,Graphics.BOTTOM | Graphics.LEFT);
			    
			    g.drawString(" max:"+  mk.stats.max_alt/10,canvas_width-10-c,y_off-c,Graphics.BOTTOM | Graphics.RIGHT);

			    y_off-=spacer_small;

			    g.setFont(font_large);
			    
			    
			    if (mk.is_navi())
				g.drawString("" + mk.gps_position.GroundSpeed_str() ,10-c*3,y_off-c*3,Graphics.BOTTOM | Graphics.LEFT);
			    else
				if (settings.horizon_display_flytime) g.drawString("" + mk.stats.flying_time()+"s" ,10-c*3,y_off-c*3,Graphics.BOTTOM | Graphics.LEFT);
			    
			    if (settings.horizon_display_altitude)			    g.drawString(  mk.Alt_formated()/* + "/"+status_voice.last_alt*/ ,canvas_width-10-c*3,y_off-c*3,Graphics.BOTTOM | Graphics.RIGHT);
			    
			    y_off-=spacer_large;
			    if (mk.is_navi()&&(settings.horizon_display_flytime))
				{
				    g.setFont(font_medium);
				    g.drawString("" + mk.stats.flying_time()+"s" ,10-c*2,y_off-c*2,Graphics.BOTTOM | Graphics.LEFT);
				}

			}
		    
		    break;

		case STATEID_DUBWISE_VALUES:

		    g.setFont(font_small);

		    g.drawString("debug values" ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;
//#if voice_mode=="no_voice"
		
		    g.drawString("no voice" ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;
//#endif
//#if voice_mode!="no_voice"
		    g.drawString("voice props" ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    g.drawString("voice_loop_cnt: "+status_voice.loop_cnt ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    g.drawString("voice_volts_cnt: "+ status_voice.volts_play_cnt ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    g.drawString("player_state: "+ status_voice.act_player_state ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    g.drawString("sound method: "+ status_voice.sound_method ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    g.drawString("do volts sound"+ settings.do_volts_voice ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;


//#endif		    


		    g.drawString("DUBwise ID"+ settings.dubwise_id ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    break;
		case STATEID_DATABUFF:

		    g.setFont(font_small);

		    int lines2paint=(((canvas_height-y_off)/spacer_small));
		    y_off=canvas_height-spacer_small;

		    for (int pos_y=0;pos_y<lines2paint;pos_y++)
			{
			    
			    g.drawString(mk.get_buff(pos_y) ,0,y_off,Graphics.TOP | Graphics.LEFT);
			    y_off-=spacer_small;
			}
		    break;

		case STATEID_BIG_SYMBOLS:
		    int act_tile=0;
		    for (int sx=0;sx<5;sx++)
			for (int sy=0;sy<4;sy++)
			    big_symbol_paint(g,(byte)sx,(byte)sy,big_symbol_by_pos((byte)(sx+sy*5) ));
			    
		    
		    //		    g.drawImage(big_symbols_img,0,0, g.TOP | g.LEFT);
		    break;
		case STATEID_SUCCESS_MSG:
		case STATEID_ERROR_MSG:
		    if (err_img!=null)
			{
			    int err_img_left=(canvas_width-err_img.getWidth()/2)/2;
			    int err_img_top=(canvas_height-err_img.getHeight())/2;
			    if (err_img_top<0)err_img_top=0;
			    g.setClip(err_img_left,err_img_top,err_img.getWidth()/2,err_img.getHeight());
			    g.drawImage(err_img,err_img_left-((state==STATEID_SUCCESS_MSG)?0:(err_img.getWidth()/2)),err_img_top, g.TOP | g.LEFT);
			}
		    paint_lcd(g);

		     break;

		case STATEID_GPSVIEW:

		    g.setFont(font_small);

		    g.setStrokeStyle(Graphics.SOLID);  
		    g.setColor(0x0000ff);  


		    //		    g.fillArc(0, 0, canvas_width, canvas_width, 0,45); 

/*
  int start_angle=(360+mk.gps_position.angle2wp(act_wp) - ((360+mk.debug_data.analog[26]-heading_offset)%360))%360;
		    //		    start_angle=0;
		    start_angle=(360-start_angle +90 -(45/2))%360;

		    g.fillArc(0, 0, canvas_width, canvas_width, start_angle,45); 
*/
		    //		    g.drawArc(1, 1, canvas_width-2, canvas_width-2, start_angle,45); 
		    // g.drawArc(2, 2, canvas_width-4, canvas_width-4, start_angle  ,45); 


		    g.setColor(skin_fg_color());

		    g.drawString("Used Sats: " + mk.gps_position.SatsInUse + " | Packages: " + mk.stats.navi_data_count ,0,y_off,Graphics.TOP | Graphics.LEFT);   
		    y_off+=spacer_small;

		    g.drawString("Err: " + mk.gps_position.ErrorCode + " NC-Flags:" + mk.gps_position.NCFlags + " MK-Flags:" + mk.gps_position.MKFlags ,0,y_off,Graphics.TOP | Graphics.LEFT);   
		    y_off+=spacer_small;

		    g.drawString("Lat: " + mk.gps_position.Latitude_str() ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    g.drawString("Long: " + mk.gps_position.Longitude_str() ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    g.drawString("Altitude: " + mk.gps_position.Altitude ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    g.drawString("GrSpeed: " + mk.gps_position.GroundSpeed_str() ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    g.drawString("Heading: " + mk.gps_position.Heading ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    g.drawString("CompasHeading: " + mk.gps_position.CompasHeading ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;
		    

		    g.drawString("Target-Lat: " + mk.gps_position.TargetLatitude_str() ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;
		    g.drawString("Target-Long: " + mk.gps_position.TargetLongitude_str() ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    g.drawString("Target-Alt: " + mk.gps_position.TargetAltitude ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;


		    g.drawString("Home-Lat: " + mk.gps_position.HomeLatitude_str() ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    g.drawString("Home-Long: " + mk.gps_position.HomeLongitude_str() ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    g.drawString("Home-Alt: " + mk.gps_position.HomeAltitude ,0,y_off,Graphics.TOP | Graphics.LEFT);

		    y_off+=spacer_small;
		    g.drawString("Distance : " + mk.gps_position.Distance2Target ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;
		    g.drawString("Angle: " + mk.gps_position.Angle2Target ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    g.drawString("WayPoints: " + mk.gps_position.WayPointNumber + "/" +  mk.gps_position.WayPointIndex ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;



		    /*
		      g.drawString("" +  mk.gps_position.NameWP[act_wp] ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;

    		    g.drawString("Lat: " +  mk.gps_position.WP_Latitude_str(act_wp) ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;

    		    g.drawString("Long: " +  mk.gps_position.WP_Longitude_str(act_wp) ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;


    		    g.drawString("Distance: " +  mk.gps_position.distance2wp(act_wp) ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;

    		    g.drawString("Angle: " +  mk.gps_position.angle2wp(act_wp) ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;

    		    g.drawString("Compas Heading: " +  (360+mk.debug_data.analog[26]-heading_offset)%360) + "("  +mk.debug_data.analog[26] +")" ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;
		    */
		   

		    break;

		case STATEID_GET_AVRSIG:
		case STATEID_RESET_PARAMS:
		case STATEID_FLASHING:
		    g.setFont(font_small);
		    int msg_pos=0;
		    g.drawString("Initializing Bootloader" ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    if ((frame_pos%3)!=0)    g.drawString("Please Wait!" ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    while (firmware_flasher.flash_msgs[msg_pos]!=null)
			{
			    g.drawString(firmware_flasher.flash_msgs[msg_pos] ,0,y_off,Graphics.TOP | Graphics.LEFT);
			    y_off+=spacer_small;
			    msg_pos++;
			}

			    g.drawString("mk:" + (mk==null) ,0,y_off,Graphics.TOP | Graphics.LEFT);
			    y_off+=spacer_small;

		    break;

		case STATEID_CAMMODE:

		    if (cam_img!=null)
			g.drawImage(cam_img,0,0,g.TOP | g.LEFT);
		    g.drawString("condition: stick" +settings.remote_cam_stick + " (act:" +mk.stick_data.stick[settings.remote_cam_stick] +",thres:100) =>" + cam_condition() ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;



		    y_off+=spacer_medium;
		    g.drawString("width " + cam_img.getWidth(),0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;
		    g.drawString("height " + cam_img.getHeight(),0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;
		    break;

		case STATEID_STICKVIEW:

		    for(int tmp_y=0;tmp_y<10;tmp_y++)
			{
			    g.drawString(""+tmp_y+"(" + l(mk.params.stick_stringids[tmp_y]) + ")=>"+mk.stick_data.stick[tmp_y],0,y_off,Graphics.TOP | Graphics.LEFT);
			    y_off+=spacer_medium;
			}
		    break;

		case STATEID_KEYCONTROL:

		    y_off+=spacer_medium;
		    g.drawString("UP or DOWN =>nick " + mk.extern_control[EXTERN_CONTROL_NICK],0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;
		    g.drawString("LEFT or RIGHT =>roll " + mk.extern_control[EXTERN_CONTROL_ROLL],0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;
		    g.drawString("1 or 4 =>altitude  " + mk.extern_control[EXTERN_CONTROL_HIGHT],0,y_off,Graphics.TOP | Graphics.LEFT);

		    y_off+=spacer_medium;
		    g.drawString("2 or 3 =>gier " + mk.extern_control[EXTERN_CONTROL_GIER],0,y_off,Graphics.TOP | Graphics.LEFT);

		    y_off+=spacer_medium;
		    g.drawString("6 or 9 =>gas " + mk.extern_control[EXTERN_CONTROL_GAS],0,y_off,Graphics.TOP | Graphics.LEFT);

		    y_off+=spacer_medium;
		    g.drawString("5 => start engines ",0,y_off,Graphics.TOP | Graphics.LEFT);

		    //		    y_off+=spacer_medium;
		    //g.drawString("5 => start engines ",0,y_off,Graphics.TOP | Graphics.LEFT);


		    y_off+=spacer_medium;
		    g.drawString("* and # =>exit",0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;
		    /*		    g.drawString("* and Fire =>Start Engines",0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;
		    g.drawString("* and 0 =>Stop Engines",0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;
		    */
		    g.drawString("sent:" + mk.stats.external_control_request_count   +"confirm:" + mk.stats.external_control_confirm_frame_count,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;
		    //		    g.drawString("bfont_medium:"+ keycontrol_bitfield[0] ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    //g.drawString("bfont_small:"+ keycontrol_bitfield[1] ,canvas_width/2,y_off,Graphics.TOP | Graphics.LEFT);
		  
		    break;

		    //		case STATEID_MOTORTEST:
		    
		    //		    motor_tester.paint(g);


		    /*

		    for (int bar=0;bar<4;bar++)

			{
			    g.setColor(((bar==act_motor)|motor_test_sel_all)?0x44CC44:0x4444DD);   
			    g.fillRect(canvas_width/(8*2)+bar*2*canvas_width/8,y_off+10,canvas_width/8,y_off+20+motor_test[bar]);
			    g.setColor(0x000000);
			    g.drawString(""+motor_test[bar] ,canvas_width/8+bar*2*canvas_width/8,y_off+10,Graphics.TOP | Graphics.HCENTER);
			    g.drawString(""+mk.debug_data.motor_val(bar) ,canvas_width/8+bar*2*canvas_width/8,y_off+25,Graphics.TOP | Graphics.HCENTER);
			}
		    g.setColor(skin_fg_color());
		    g.drawString(""+mk.debug_data.analog[2]+"<->"+mk.debug_data.analog[3] ,0,canvas_height-30,Graphics.TOP | Graphics.LEFT);



		    break;
		    */
		case STATEID_EDIT_PARAMS:
		case STATEID_SETTINGSMENU:
		case STATEID_MOTORTEST:

		    act_editor.paint(g);

		    break;



//#if fileapi=="on"
		case STATEID_FILEOPEN:
		    y_off+=spacer_medium;
		    g.drawString("act_path" + file_access.act_path() ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    paint_menu(g);
		    break;
//#endif


//#if  (location=="on")&&(cldc11=="on")


		case STATEID_SHOWPHONEGPS:

		    y_off+=spacer_medium;
		    g.drawString("lat" + phone_lat ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;
		    g.drawString("long" + phone_long ,0,y_off,Graphics.TOP | Graphics.LEFT);


		    y_off+=spacer_medium;
		    g.drawString("cr"  + (cr!=null) ,0,y_off,Graphics.TOP | Graphics.LEFT);

		    y_off+=spacer_medium;
		    g.drawString("LocationProvider"  + (lp!=null) ,0,y_off,Graphics.TOP | Graphics.LEFT);

		    y_off+=spacer_medium;
		    g.drawString("Coordinates"  + (c!=null) ,0,y_off,Graphics.TOP | Graphics.LEFT);

		    break;

//#endif
		case STATEID_CONNECT_RECENT:
		case STATEID_SELECT_MIXER:
		case STATEID_SELECT_FIRMWARE:
		case STATEID_SELECT_COMPORT:
		case STATEID_PARAM_MENU:
		case STATEID_CONFIRM_MOTORTEST:
		    paint_menu(g);
		    break;
		case STATEID_STRINGINPUT:
		case STATEID_ABOUT:
		case STATEID_IPINPUT:
		case STATEID_PARAM_MASSWRITE:
		case STATEID_READ_PARAMS:
//#if bluetooth=="on"
		case STATEID_SCANNING:
//#endif
		    paint_lcd(g);
		    break;



		case STATEID_RAWDEBUG:	
		    g.setFont(font_small);
		    rawdebug_off_y=0;
		    if ((rawdebug_cursor_y+3)*spacer_small>canvas_height)
			rawdebug_off_y=((rawdebug_cursor_y+3)*spacer_small-canvas_height)/spacer_small;
		    for (int i=0;i<(canvas_height/spacer_small)-1;i++)
			{
			    if (i+rawdebug_off_y==rawdebug_cursor_y)
				{
				g.setColor(0x0000CC);
				g.fillRect(0,y_off,canvas_width,spacer_small);

				g.setColor(skin_fg_color());


				}
			    if (i+rawdebug_off_y<32) //todo better style
				g.drawString(mk.debug_data.names[i+rawdebug_off_y] + mk.debug_data.analog[i+rawdebug_off_y] ,0,y_off,Graphics.TOP | Graphics.LEFT);
			    
			    y_off+=spacer_small;
			    
			}



		    break;

		
		case STATEID_CONN_DETAILS:	
		    g.setFont(font_medium);
		    g.drawString(l(STRINGID_CONN)+":",0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;
		    g.setFont(font_small);

		    g.drawString(" URL:" + mk.mk_url,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small; 

		    g.drawString(" Name:" + mk.name,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small; 

		    g.drawString(" "+mk.extended_name()+ " (" + (mk.connected?("open"+((System.currentTimeMillis()- mk.connection_start_time)/1000)+"s"):"closed")+"):",0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;
		    g.drawString(" Software Version:" + mk.version.version_str ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		    g.drawString(" Protocol Version:" + mk.version.proto_str ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;
		    g.drawString(" Slave-Addr:" + mk.slave_addr,0,y_off,Graphics.TOP | Graphics.LEFT);

		    paint_menu(g);

		    break;

		case STATEID_TRAFFIC:
		    		   
		    g.setFont(font_medium);
		    g.drawString("Packet Traffic (over "+mk.conn_time_in_s()+"s):",0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;
		    g.setFont(font_small);
		    g.drawString( ">>in:"+mk.stats.bytes_in+ " bytes => " + mk.stats.bytes_in/mk.conn_time_in_s() + " bytes/s",0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small; 

		    g.drawString( " crcFail:"+mk.stats.crc_fail +" unknown:" +mk.stats.other_data_count,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small; 


		    g.drawString( " debug:"+mk.stats.debug_data_count+ " LCD:" + mk.stats.lcd_data_count + " vers:" + mk.stats.version_data_count,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small; 
		    g.drawString( " rc:"+mk.stats.stick_data_count+" params:"+mk.stats.params_data_count + " GPS:"+mk.stats.navi_data_count ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small; 
		    g.drawString( " debug_names:" + mk.stats.debug_names_count + " angles:" + mk.stats.angle_data_count ,0,y_off,Graphics.TOP | Graphics.LEFT);

		    //y_off+=spacer_small; 
		    //		    g.drawString( " other:"+mk.stats.other_data_count,0,y_off,Graphics.TOP | Graphics.LEFT);

		    y_off+=spacer_small+3; 
		    g.drawString( "<<out:"+mk.stats.bytes_out + " bytes =>" + mk.stats.bytes_out/mk.conn_time_in_s() + "bytes/s", 0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small; 
		    g.drawString( " LCD:" + mk.stats.lcd_data_request_count + " vers:" + mk.stats.version_data_request_count,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small; 
		    g.drawString( " params:"+mk.stats.params_data_request_count +" rc:" + mk.stats.stick_data_request_count ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small; 

		    g.drawString( " resend:"+mk.stats.resend_count ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small; 

		    paint_menu(g);
		    break;

		case STATEID_PROXY:	
		    g.setFont(font_medium);
		    g.drawString("Host:",0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;
		    g.setFont(font_small);
		    g.drawString(" " + mk.proxy.url + "("+((mk.proxy.connected)?"open":"closed") +")",0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_small;

		   

		    break;


		// falltru wanted
		case STATEID_SETTING_OPTIONS:
		case STATEID_MAINMENU:	
		case STATEID_SELECT_PARAMSET:
		case STATEID_HANDLE_PARAMS:
		case STATEID_DEVICESELECT:

		    paint_menu(g);
		    break;
		    

		case STATEID_GRAPH:
   
		    g.setClip(0,0,canvas_width,canvas_height);
		    
		    //		  
		    g.setColor(0xe1dddd);
		    g.setFont(font_small);
		    // LEGEND
		    if (settings.graph_legend) 
			{
			      g.setStrokeStyle(Graphics.DOTTED);  
			      for (int d=0;d<GRAPH_COUNT;d++)
				  {
				      
				      //			       g.setColor(graph_colors[d]);		
				      //			       g.fillRect(0,y_off +spacer_small/2-2 ,20,4);
				      //			       g.setColor(skin_fg_color());
				      //			       g.drawString(graph_names[d] + mk.debug_data.analog[graph_sources[d]],23,y_off,Graphics.TOP | Graphics.LEFT);
				      //			       y_off+=spacer_small; 
				      
				      g.setColor(graph_colors[d]);		
				      g.fillRect(canvas_width-20,canvas_height-spacer_medium*(d+1)+(spacer_medium)/2-2 ,20,4);
				      g.setColor(skin_fg_color());
				      g.drawString(graph_names[d] ,canvas_width-23,canvas_height-spacer_medium*(d+1),Graphics.TOP | Graphics.RIGHT);
				      //			    y_off+=spacer_small; 
				      
				  }

			      g.setStrokeStyle(Graphics.SOLID);  
			}

		    if (settings.graph_scale)
			{
			    
			    int scale=10;
			    //			    if ((10/line_scaler)<5)scale =1;
			    if (((10/line_scaler)*2)<(canvas_height/2) )scale =10;
			    if (((50/line_scaler)*2)<(canvas_height/2) )scale =50;
			    if (((100/line_scaler)*2)<(canvas_height/2) )scale =100;
			    if (((250/line_scaler)*2)<(canvas_height/2) )scale =250;
			    if (((500/line_scaler)*2)<(canvas_height/2) )scale =500;
			    if (((1000/line_scaler)*2)<(canvas_height/2) )scale =1000;
			    if (((10000/line_scaler)*2)<(canvas_height/2) )scale =10000;




			    //			    g.drawString("scale:"+scale + "line scaler" + line_scaler,0,y_off,Graphics.TOP | Graphics.LEFT);


			    int jump=0;

			    g.drawLine(0,line_middle_y,canvas_width,line_middle_y);
			    
			    while ((jump/line_scaler)<graph_height)
				{
				    
				    g.drawString(""+jump,0,line_middle_y - jump/line_scaler,Graphics.TOP | Graphics.LEFT);
				    if (jump!=0)g.drawString("-"+jump,0,line_middle_y + jump/line_scaler,Graphics.TOP | Graphics.LEFT);
				    g.drawLine(0,line_middle_y - jump/line_scaler,canvas_width,line_middle_y - jump/line_scaler);
				    g.drawLine(0,line_middle_y + jump/line_scaler,canvas_width,line_middle_y + jump/line_scaler);
				    jump+=scale;
				}
			}



		    for (int gr=0;gr<GRAPH_COUNT;gr++)
			{

			    // !!TODO checkme
			    g.setColor(graph_colors[gr]);		

			    //			    check_local_max(mk.debug_data.analog[graph_sources[gr]]);
		    
			    line_scaler= mk.debug_buff_max/graph_height+1;


			    for ( int x=0;x<canvas_width-1;x++)
				{
				    /*int p= (((-graph_offset+x-canvas_width-5)));
				    if (p<1)
					p+=graph_data[0].length;
				    p%=(graph_data[0].length-1);
				    
				    draw_graph_part(g,x,graph_data[gr][p]/line_scaler,graph_data[gr][p+1]/line_scaler);
				    */
				    
				       int p= (((mk.debug_buff_off+x-canvas_width)));
				    if (p<0)
					p+=mk.debug_buff_len;
				    p%=(mk.debug_buff_len-2);
				    

				     if (p<mk.debug_buff_lastset)

					 //				    draw_graph_part(g,x,mk.debug_buff[p][gr]/line_scaler,mk.debug_buff[p+1][gr]/line_scaler);

				     g.drawLine(x,line_middle_y-mk.debug_buff[p][gr]/line_scaler,x+1,line_middle_y-mk.debug_buff[p+1][gr]/line_scaler);
				     //				    System.out.println("--p"+p + "gr " + gr + " l " + mk.debug_buff.length +" ls:" + mk.debug_buff_lastset );				    
				    
				}
			}


		    /*
		    int[] pixels=new int[canvas_width*canvas_height];
		    for (int x=0;x<canvas_width;x++)
			for (int y=0;y<canvas_height;y++)
			    if (x==y) pixels[x+canvas_width*y]=0xFFFFFFFF;

		    Image test= Image.createRGBImage(pixels,canvas_width,canvas_height,true);
		    g.drawImage(test,0,0,Graphics.TOP | Graphics.LEFT );
		    */

		    break;

		case STATEID_LCD:
		    /*
		    g.setClip(canvas_width/2-load_img.getWidth()/6+1,canvas_height/2-load_img.getHeight()/8+1, load_img.getWidth()/4,load_img.getHeight()/3);;
		    if (( mk.LCD.init_state!=-1)||(mk.LCD.act_mk_page!=mk.LCD.act_user_page)) g.drawImage(load_img,canvas_width/2-load_img.getWidth()/8 - ((((frame_pos/3)%12)%4)*(load_img.getWidth()/4)) ,canvas_height/2-load_img.getHeight()/6- ((((frame_pos/3)%12)/4)*(load_img.getHeight()/3)), g.TOP | g.LEFT);		    

		    */
		    /*
		    // !!TODO!! check exactly which version those Datas where introduced
		    if (mk.version.compare(0,60)==mk.version.VERSION_PREVIOUS)
			{
			    g.drawString("Voltage: " + (mk.debug_data.UBatt()/10) + "," +(mk.debug_data.UBatt()%10)+"V" ,0,y_off,Graphics.TOP | Graphics.LEFT);
			    g.drawString("Sender: " + mk.debug_data.SenderOkay(),canvas_width/2,y_off,Graphics.TOP | Graphics.LEFT);
			    y_off+=spacer_medium;
			}

		    g.drawString(mk.version.str+"(d"+mk.debug_data_count+ "l" + mk.lcd_data_count+  "v" + mk.version_data_count+"o"+mk.other_data_count+"p"+mk.params_data_count+")",0,y_off,Graphics.TOP | Graphics.LEFT);

		    y_off+=spacer_medium;

		    g.drawString("n:"+mk.debug_data.nick_int() + " r:"+mk.debug_data.roll_int() + " an:"+mk.debug_data.accnick() + " ar:"+mk.debug_data.accroll() ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;
		    
		    
		    g.drawString("m1:"+mk.debug_data.motor_val(0) + " m2:"+mk.debug_data.motor_val(1)+" m3:"+mk.debug_data.motor_val(2) + " m4:"+mk.debug_data.motor_val(3) ,0,y_off,Graphics.TOP | Graphics.LEFT);
		    y_off+=spacer_medium;
		    
		    if (mk.connected)
			{ 
			    g.drawString("time conn:" +((System.currentTimeMillis()- mk.connection_start_time)/1000)+"s" ,0,y_off,Graphics.TOP | Graphics.LEFT);
			    y_off+=spacer_medium;
			    g.drawString("time motor>15:" +(mk_stat.motor_on_time/1000) +"s" ,0,y_off,Graphics.TOP | Graphics.LEFT);
			    y_off+=spacer_medium;
			    g.drawString("time motor=15:" +(mk_stat.motor_stand_time/1000) +"s" ,0,y_off,Graphics.TOP | Graphics.LEFT);
			    y_off+=spacer_medium;
			    g.drawString("lcd:" + mk.LCD.act_mk_page + "/" + mk.LCD.pages + " ( wanted: " + mk.LCD.act_user_page + "state:" + mk.LCD.init_state +")" ,0,y_off,Graphics.TOP | Graphics.LEFT);

			    y_off+=spacer_medium;
			    g.drawString("lcd-key:" + mk.LCD.act_key ,0,y_off,Graphics.TOP | Graphics.LEFT);

			}
*/
		    
		    int spacer_left_right=(canvas_width-(20*(lcd_img.getWidth()/222)))/2;

		    y_off=canvas_height-4*lcd_img.getHeight();
		    
		    for ( int foo=0;foo<4;foo++)
			{
			    for (int x=0;x<20;x++)
				{
				    g.setClip(spacer_left_right+(lcd_img.getWidth()/222)*x,y_off,(lcd_img.getWidth()/222),lcd_img.getHeight());
					    g.drawImage(lcd_img,spacer_left_right+(lcd_img.getWidth()/222)*x-(mk.LCD.get_act_page()[foo].charAt(x)-' ')*(lcd_img.getWidth()/222),y_off, g.TOP | g.LEFT);

					}
				    y_off+=lcd_img.getHeight();
				}

		    g.setClip(0,0,canvas_width,canvas_height);

		}

		

	    	} catch (Exception e) { System.out.println("error in paint " + e);}


	if (debug_overlay)
	    {

		for(int debug_line=0;debug_line<5;debug_line++)
		    for ( int bar=0;bar<canvas_width/(symbols_img.getWidth()/10)+1;bar++)
			{
			    g.setClip(bar*(symbols_img_tile_width),canvas_height-(debug_line+1)*symbols_img_tile_height,(symbols_img_tile_width),symbols_img_tile_height);;
			    g.drawImage(symbols_img,bar*(symbols_img_tile_width),canvas_height-(debug_line+1)*symbols_img_tile_height, g.TOP | g.LEFT);
			    
			}

		g.setColor(0);
		g.setClip(0,0,canvas_width,canvas_height);
		g.drawString("freeMemory:" + Runtime.getRuntime().freeMemory() ,0,canvas_height-spacer_small,Graphics.TOP | Graphics.LEFT);
		g.drawString("totalMemory:" + Runtime.getRuntime().totalMemory() ,0,canvas_height-2*spacer_small,Graphics.TOP | Graphics.LEFT);

		g.drawString("state:" + state ,0,canvas_height-3*spacer_small,Graphics.TOP | Graphics.LEFT);
		g.drawString("key_bitfield:" + key_bitfield ,0,canvas_height-4*spacer_small,Graphics.TOP | Graphics.LEFT);

	    }


    }
    //xda    Player mPlayer;
    //xda    VideoControl	mVideoControl;
    Image cam_img;
    Image last_cam_img;
    int cam_img_seq=0;
    byte[] cam_raw;

    private void connect_mk(String url,String name)
    {
	mk.setCommunicationAdapter(new URLCommunicationAdapter(url));
	//	mk.ufo_prober.bluetooth_probe(url);
	settings.connection_url=url;
	settings.connection_name=name;
	mk.connect_to(url,name);
    }

    /*
    public void draw_graph_part(Graphics g,int x,int y1,int y2)
    {
	g.drawLine(x,line_middle_y-y1,x+1,line_middle_y-y2);
    }
    */


    /*********************************************** input Section **********************************************/



    //    public final String intro_str="   Digital Ufo Broadcasting with intelligent service equipment by Marcus -LiGi- Bueschleb ; Big Up Holger&Ingo for the MikroKopter Project (http://www.mikrokopter.de) ";

    //    int intro_str_pos=0;
    // int intro_str_delay=3;
    //    boolean init_bootloader=false;





    public void chg_state(byte next_state)
    {
//#if bluetooth=="on"
	if (next_state!=STATEID_DEVICESELECT)
	    bt_scanner=null;
//#endif
	if (firmware_flasher!=null)
	    {
		firmware_flasher.exit_bootloader();
		firmware_flasher=null;
	    }

	if ((mk==null))
	    {
		if ((next_state!=STATEID_SELECT_FIRMWARE)&&(next_state!=STATEID_FLASHING))
		    {
			mk      = new MKCommunicator();
			settings.process_all_settings(); // TODO remove
			if (settings.connection_url!="")
			    connect_mk(settings.connection_url,settings.connection_name);


		    }

	    }
	else
	    {
		mk.watchdog.resend_timeout=0;

	    	mk.destroy_debug_buff();
		mk.user_intent=USER_INTENT_NONE;
		mk.set_debug_interval(mk.default_abo);
		mk.set_gpsosd_interval(mk.default_abo);
	    }
	

	// keep editor over popup messages
	if (((next_state!=STATEID_ERROR_MSG)&&(next_state!=STATEID_SUCCESS_MSG))&&((state!=STATEID_ERROR_MSG)&&(state!=STATEID_SUCCESS_MSG)))
	    {

		act_editor = null;
		
		if ((next_state!=STATEID_MOTORTEST)&&(next_state!=STATEID_CONFIRM_MOTORTEST))
		    motor_tester=null;
	    }

	err_img=null; 
	//	graph_data=null;

	act_menu_select_bak[state]=act_menu_select;
	act_menu_select=act_menu_select_bak[next_state];

	// prepare next state
	switch(next_state)

	    {
	    case STATEID_CONNECT_RECENT:
		setup_menu(DUBwiseHelper.combine_str_arr(settings.all_recent_connection_strings(),new String[] {l(STRINGID_BACK)}),null);
		break;

	    case STATEID_CONFIRM_MOTORTEST:
		setup_menu(handle_motortest_items,handle_motortest_actions);
		break;

	    case STATEID_BIG_SYMBOLS:
		big_symbols_img=DUBwiseHelper.scaleImage(symbols_img,(this.getWidth()/5)*10);
		break;
	    case STATEID_MOTORTEST:
		if (motor_tester==null)
		    motor_tester=new MotorTester();
		
		    
		act_editor=new MKParamsEditor(this,motor_tester,STATEID_MAINMENU);
		break;

	    case STATEID_SELECT_MIXER:
		setup_menu(DUBwiseHelper.combine_str_arr(mixer_manager.getDefaultNames(),new String[] {l(STRINGID_BACK)}),null);
		break;


	    case STATEID_SETTING_OPTIONS:
		setup_menu(setting_options_menu_items,setting_options_menu_actions);
		break;

	    case STATEID_SHOWPHONEGPS:
		mk.user_intent=USER_INTENT_FOLLOWME;
		break;

	    case STATEID_GPSVIEW:

		mk.user_intent=USER_INTENT_GPSOSD;
		break;
	    case STATEID_SELECT_FIRMWARE:
		setup_menu(fw_loader.names,null);
		break;


	    case STATEID_SUCCESS_MSG:
	    case STATEID_ERROR_MSG:
		lcd_lines=new String[1];
		lcd_lines[0]=act_msg;
		try 
		    {
			err_img=Image.createImage("/preflight.jpg");	    
		    }
		catch (Exception e)	    
		    {    
			debug.log("problem loading error image");
		    }
		
		break;
	    case STATEID_EDIT_PARAMS:
		mk.user_intent=USER_INTENT_RCDATA;
		if (act_editor==null)
		    act_editor = new MKParamsEditor(this,mk.params,STATEID_HANDLE_PARAMS);
		break;
	    case STATEID_STRINGINPUT:
		lcd_lines=new String[2];
		lcd_lines[0]=act_input_str;
		lcd_lines[1]="^";
		
		break;
	    case STATEID_LCD:
		mk.user_intent=USER_INTENT_LCD;
		break;
	    case STATEID_RESET_PARAMS:

		//		firmware_flasher=new MKFirmwareFlasher(settings.connection_url,MKFirmwareHelper.BOOTLOADER_INTENSION_RESET_PARAMS);
		firmware_flasher=new MKFirmwareFlasher(mk.getCommunicationAdapter(),MKFirmwareHelper.BOOTLOADER_INTENSION_RESET_PARAMS);


		/*		mk.bootloader_intension_flash=false;
		mk.bl_retrys=0;
		mk.init_bootloader=true;*/
		break;

	    case STATEID_FLASHING:

		firmware_flasher=new MKFirmwareFlasher(mk.getCommunicationAdapter(),MKFirmwareHelper.BOOTLOADER_INTENSION_FLASH_FIRMWARE);
		//		firmware_flasher=new MKFirmwareFlasher(settings.connection_url,MKFirmwareHelper.BOOTLOADER_INTENSION_FLASH_FIRMWARE);
		firmware_flasher.in=fw_loader.get_input_str();

		/*
		mk.bootloader_intension_flash=true;
		mk.bl_retrys=0;
		mk.init_bootloader=true;
		*/
		break;

	    case STATEID_GET_AVRSIG:
		firmware_flasher=new MKFirmwareFlasher(mk.getCommunicationAdapter(),MKFirmwareHelper.BOOTLOADER_INTENSION_FLASH_FIRMWARE);
		//		firmware_flasher=new MKFirmwareFlasher(settings.connection_url,MKFirmwareHelper.BOOTLOADER_INTENSION_GET_SIG);


		break;

 //#if fileapi=="on"
	    case STATEID_FILEOPEN:
		file_access.dirty=true;
		break;
//#endif

	    case STATEID_STICKVIEW:
		mk.user_intent=USER_INTENT_RCDATA;
		break;
	    case STATEID_SELECT_COMPORT:	
		menu_items=DUBwiseHelper.split_str(System.getProperty("microedition.commports")+",back",",");
		setup_menu(menu_items,null);
		break;


	    case STATEID_ABOUT:
		lcd_lines=credits;
		lcd_lines[CREDITS_VERSION]=  "Version:   V" + version_str;
		lcd_lines[CREDITS_VERSION+1]="Lib Vers.: " + mk.lib_version_str();
		act_menu_select=(byte)(max_lines-1);
		break;

	    case STATEID_CONN_DETAILS:	
		setup_conn_menu();//conn_details_menu_items,conn_details_menu_actions);
		break;


	    case STATEID_PARAM_MENU:
		setup_menu(param_menu_items,param_menu_actions);
		break;
	    case STATEID_TRAFFIC:	
		setup_menu(onlyback_menu_items,back_to_conndetails_actions);
		break;
	    case STATEID_CAMMODE:
		/*xda
		mk.user_intent=USER_INTENT_RCDATA;
		if (mVideoControl==null)
		    try 
			{
			    debug.log("creating player\n");
			    mPlayer = Manager.createPlayer("capture://video?encoding=png&width=2048&height=1536");
			    
			    debug.log("realizing player\n");
			    mPlayer.realize();
			    
			    debug.log("get_videocontrol\n");
			    
			    mVideoControl = (VideoControl)mPlayer.getControl("VideoControl");
			    
			    debug.log("switching Canvas\n");
			    mVideoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this); 
			
			    debug.log("get snap\n");
			    byte[] raw = mVideoControl.getSnapshot(null);    
			 
			}
		    catch ( Exception e)
			{
			    debug.log(e.toString());
			}
		*/

		break;
	    case STATEID_KEYCONTROL:
		mk.user_intent= USER_INTENT_EXTERNAL_CONTROL;
		keycontrol_exit=0;
		break;


	    case STATEID_PARAM_MASSWRITE:
		mk.user_intent=USER_INTENT_PARAMS;
		lcd_lines=new String[2];
		lcd_lines[0]="";
		lcd_lines[1]="";
		break;

	    case STATEID_READ_PARAMS:
		mk.user_intent=USER_INTENT_PARAMS;
		lcd_lines=new String[2];
		lcd_lines[0]="Reading Settings    ";
		lcd_lines[1]=DUBwiseHelper.pound_progress(mk.watchdog.act_paramset,5);

		break;

	    case STATEID_IPINPUT:
		
		lcd_lines=new String[3];
		lcd_lines[0]="Address (IP:Port):  ";
		break;

//#if bluetooth=="on"
	    case STATEID_SCANNING:
		lcd_lines=new String[3];
		lcd_lines[0]="Searching for";
		lcd_lines[1]="Bluetooth Devices";
		lcd_lines[2]="found";

		mk.close_connections(true);

		bt_scanner = new BTSearcher();

		break;
//#endif

	    case STATEID_HANDLE_PARAMS:
		setup_menu(handle_params_menu_items,handle_params_menu_actions);
		break;

	    case STATEID_SELECT_PARAMSET:
		menu_items=new String[6];
		for (int i=0;i<5;i++)
		    menu_items[i]=""+(i+1)+": " + mk.params.getParamName(i) + ((i==mk.params.active_paramset)?"*":"");

		menu_items[5]=l(STRINGID_BACK);
		lcd_lines=new String[6];
		break;

//#if bluetooth=="on"
	    case STATEID_DEVICESELECT:
		
		menu_items=new String[bt_scanner.remote_device_count+2];
		lcd_lines=new String[bt_scanner.remote_device_count+2];

		for (int i=0;i<bt_scanner.remote_device_count;i++)
		    menu_items[i]=bt_scanner.remote_device_name[i];
		menu_items[bt_scanner.remote_device_count]="scan again";
		menu_items[bt_scanner.remote_device_count+1]="cancel";

		break;
//#endif

	    case STATEID_MAINMENU:
		setup_main_menu();

		break;

	    case STATEID_SETTINGSMENU:
		//		if (settings_editor==null)
		    act_editor = new MKParamsEditor(this,settings,STATEID_MAINMENU);

		break;

	    case STATEID_RAWDEBUG:
		mk.user_intent=USER_INTENT_RAWDEBUG;
		break;

	    case STATEID_HORIZON:
		//		mk.user_intent=USER_INTENT_GRAPH;
		if (mk.is_navi())
		    mk.set_debug_interval(mk.secondary_abo);
		break;

	    case STATEID_GRAPH:
		//mk.set_debug_interval(1);

		mk.setup_debug_buff(graph_sources,this.getWidth(),settings.graph_interval);

		mk.user_intent=USER_INTENT_GRAPH;
		

		/*
		graph_data=new int[GRAPH_COUNT][this.getWidth()*2];
		
		for (int c=0;c<graph_data[0].length;c++)
		    for (int d=0;d<GRAPH_COUNT;d++)
			graph_data[d][c]=-1;
		
	

		local_max=1;
		*/
		break;

	    }



	// switch state
	state=next_state;
    } // void chg_state


    public void keyReleased(int keyCode)
    {
	
	if (state==STATEID_MAP)
	    key_bitfield=0;
	else
	    key_bitfield&=(0xFFFFFFFF)^keyCode2mask(keyCode);

	switch(state)
	    {
		/*
		  case STATEID_MOTORTEST:
		  act_motor_increase=0;
		  break;
		*/
		
	    case STATEID_KEYCONTROL:
		if (keyCode==KEY_POUND)
		    keycontrol_exit &= 255^1;
		else
		if (keyCode==KEY_STAR)
		    keycontrol_exit &= 255^2;
		else
		    mod_external_control_by_keycode(keyCode,(byte)0);

		    /*
		mk.send_keys(keycontrol_bitfield);
		    */
		break;
	    }

    }
    public final static byte INPUTSTRINGID_CONNECTION_URL=0;

    public void string_input_result(byte str_id,String str)
    {
	switch(str_id)
	    {
	    case INPUTSTRINGID_CONNECTION_URL:
		mk.connect_to(str,"URL-Connection");
		break;
	    }
    }


    public void mod_external_control_by_keycode(int keyCode,byte mul)
    {

	if (keyCode==this.KEY_NUM2)
	    	mk.extern_control[EXTERN_CONTROL_GIER]=(byte)(-mul*settings.default_extern_control[EXTERN_CONTROL_GIER]);
	else if (keyCode==this.KEY_NUM3)
	    	mk.extern_control[EXTERN_CONTROL_GIER]=(byte)(mul*settings.default_extern_control[EXTERN_CONTROL_GIER]);

	else if (keyCode==this.KEY_NUM1)
	    mk.extern_control[EXTERN_CONTROL_HIGHT]+=mul*settings.default_extern_control[EXTERN_CONTROL_HIGHT];
	else if (keyCode==this.KEY_NUM4)
	    mk.extern_control[EXTERN_CONTROL_HIGHT]-=mul*settings.default_extern_control[EXTERN_CONTROL_HIGHT];


	else if (keyCode==this.KEY_NUM6)
	    { if ( mk.extern_control[EXTERN_CONTROL_GAS]<255) mk.extern_control[EXTERN_CONTROL_GAS]+=mul*settings.default_extern_control[EXTERN_CONTROL_GAS]; }
	else if (keyCode==this.KEY_NUM9)
	    { if ( mk.extern_control[EXTERN_CONTROL_GAS]>0) mk.extern_control[EXTERN_CONTROL_GAS]-=mul*settings.default_extern_control[EXTERN_CONTROL_GAS]; }

	else switch (getGameAction (keyCode)) 
		    {
		    case UP:
			mk.extern_control[EXTERN_CONTROL_NICK]=(byte)(mul*settings.default_extern_control[EXTERN_CONTROL_NICK]);
			break;
			
		    case DOWN:
			mk.extern_control[EXTERN_CONTROL_NICK]=(byte)(-mul*settings.default_extern_control[EXTERN_CONTROL_NICK]);

			break;
			
			
		    case LEFT:
			mk.extern_control[EXTERN_CONTROL_ROLL]=(byte)(mul*settings.default_extern_control[EXTERN_CONTROL_ROLL]);
			break;
			
		    case RIGHT:
			mk.extern_control[EXTERN_CONTROL_ROLL]=(byte)(-mul*settings.default_extern_control[EXTERN_CONTROL_ROLL]);
			break;
			
		    case FIRE:

			break;
			
			
		    }

    }


    
    public final static String PARAM_SAVE_STORE_NAME="MKParamsV1";

    
    int[][] params2masswrite;
    int     param_masswrite_write_pos;
    
//#if cldc11=="on"
    double phone_lat=0.0;
    double phone_long=0.0;
//#endif

//#if j2memap=="on"

    Marker ufo_marker,home_marker;
//#endif
    public void process_action(byte actionid)
    {
	switch(actionid)
	    {
	    case ACTIONID_CONNECT_URL:
		DUBwiseStringInput string_input=new DUBwiseStringInput("URL",30,INPUTSTRINGID_CONNECTION_URL,this);
		break;

	    case ACTIONID_DISCARD_MOTORTEST:
		System.out.println("mt" + motor_tester);
		motor_tester.use_backup();
		chg_state(STATEID_MOTORTEST);
		break;

	    case ACTIONID_CONFIRM_MOTORTEST:
		motor_tester.backup_engine_field();
		chg_state(STATEID_MOTORTEST);
		break;

	    case ACTIONID_BIG_SYMBOLS:
		chg_state(STATEID_BIG_SYMBOLS);
		break;

	    case ACTIONID_SELECT_MIXER:
		chg_state(STATEID_SELECT_MIXER);
		break;

	    case ACTIONID_SELECT_DUBWISEDIR:
		chg_state(STATEID_FILEOPEN);
		break;

//#if j2memap=="on"
	    case ACTIONID_MAP:
		int default_zoom=14; // TODO calc of distance - kopter<>home
		try {

		    // ufo_marker=new Marker(UtilMidp.parseFloat(""+(mk.gps_position.Longitude/10000000d)),UtilMidp.parseFloat(""+(mk.gps_position.Latitude/10000000d)));

		    ufo_marker=new Marker(UtilMidp.parseFloat(mk.gps_position.gps_format_str(mk.gps_position.Longitude,GPS_FORMAT_DECIMAL)),UtilMidp.parseFloat(mk.gps_position.gps_format_str(mk.gps_position.Latitude,GPS_FORMAT_DECIMAL)));
		    ufo_marker.iconImage=icon_img;
		    ufo_marker.m_type=MarkerStyle.ICON_IMAGE;
		    ufo_marker.name="Kopter";
		    ufo_marker.description="This is the Kopter!";

		    root.m_map.m_listMyPlaces.addElement(ufo_marker);

		    root.m_map.gotoLonLat(UtilMidp.parseFloat(mk.gps_position.gps_format_str(mk.gps_position.Longitude,GPS_FORMAT_DECIMAL)),UtilMidp.parseFloat(mk.gps_position.gps_format_str(mk.gps_position.Latitude,GPS_FORMAT_DECIMAL)) ,default_zoom);
		}
		catch(Exception e) {}


//#if location=="on"
//#if cldc11=="on"
		if (!((phone_lat==0.0)&&(phone_long==0.0)))try {

		    home_marker=new Marker(UtilMidp.parseFloat(""+phone_long),UtilMidp.parseFloat(""+phone_lat));
		    home_marker.name="HomePos";
		    home_marker.description="This is home!";
		    root.m_map.m_listMyPlaces.addElement(home_marker);


		    root.m_map.gotoLonLat(UtilMidp.parseFloat(""+phone_long) ,UtilMidp.parseFloat(""+phone_lat),default_zoom);

		}
		catch(Exception e) {}
//#endif
//#endif

		    chg_state(STATEID_MAP);
		break;
//#endif
	    case ACTIONID_SETTING_LOADFANCY:
		settings.load_fancy_defaults();
		break;

	    case ACTIONID_SETTING_LOADPLAIN:
		settings.load_plain_defaults();
		break;

	    case ACTIONID_RECONNECT:
		mk.close_connections(false);
		break;
	    case ACTIONID_UPDATE_DUBWISE:
		try
		    {
			root.platformRequest("http://mikrocontroller.cco-ev.de/mikrosvn/Projects/DUBwise/trunk/j2me/bin/240x320/cldc11/en_speedy/bluetooth_on/fileapi_on/devicecontrol_on/DUBwise-240x320-cldc11-en_speedy-all_firmwares-bluetooth_on-fileapi_on-devicecontrol_on.jad");
		    }
		catch(Exception e) { }
		break;


	    case ACTIONID_SHOWPHONEGPS:
		chg_state(STATEID_SHOWPHONEGPS);
		break;

	    case ACTIONID_PARAM_LOAD_MOBILE:
		try
		    {
			RecordStore recStore = RecordStore.openRecordStore(PARAM_SAVE_STORE_NAME , true );
			
			if (recStore.getNumRecords()==1)
			    {
				ByteArrayInputStream bin = new ByteArrayInputStream(recStore.getRecord(1));
				DataInputStream      din = new   DataInputStream( bin );


				// params_version
				if ( mk.params.params_version!=din.readInt())
				    {
					act_msg=l(STRINGID_PARAMSINCOMPATIBLE);
					chg_state(STATEID_ERROR_MSG);
				    }
				else
				    {
					int p_length=din.readInt();
					params2masswrite=new int[5][p_length];

					for ( int p=0;p<5;p++)
					    for ( int p_pos=0;p_pos<p_length;p_pos++)
						params2masswrite[p][p_pos]=din.readInt();
					
					param_masswrite_write_pos=0;
					chg_state(STATEID_PARAM_MASSWRITE);
				    }
			    }
			else throw(new Exception("rms err"));
			recStore.closeRecordStore();
		    }
		catch (Exception e)
		    { 
			act_msg=l(STRINGID_NOPARAMSONMOBILE);
			chg_state(STATEID_ERROR_MSG);
		    }


		break;
		
	    case ACTIONID_SELECT_FIRMWARE:
		fw_loader=new FirmwareLoader(116,this);
		chg_state(STATEID_SELECT_FIRMWARE);
		break;
	    case ACTIONID_PARAM_COPY_MOBILE:
		read_paramset_intension_save=true;
		chg_state(STATEID_READ_PARAMS);
		break;

	    case ACTIONID_PARAM_MENU:
		chg_state(STATEID_PARAM_MENU);
		break;
				
	    case ACTIONID_PARAM_WRITE_OK:
		act_msg=l(STRINGID_PARAMWRITEOK);
		chg_state(STATEID_SUCCESS_MSG);
		break;
	    case ACTIONID_HORIZON:
		chg_state(STATEID_HORIZON);
		break;

	    case ACTIONID_RENAME_PARAMS:
		act_input_str=mk.params.getParamName(mk.params.act_paramset);
		ipinput_pos=0;
		chg_state(STATEID_STRINGINPUT);
		break;


	    case ACTIONID_BACK_TO_CONNDETAILS:
		chg_state(STATEID_CONN_DETAILS);
		break;

	    case ACTIONID_RESET_PARAMS:
		chg_state(STATEID_RESET_PARAMS);
		break;

	    case ACTIONID_FLASH:
		chg_state(STATEID_GET_AVRSIG);
		break;
					
	    case ACTIONID_DATABUFF:
		chg_state(STATEID_DATABUFF);
		break;
				    
	    case ACTIONID_NC_ERRORS:
		act_msg=null;
		chg_state(STATEID_ERROR_MSG);
		break;

	    case ACTIONID_ABOUT:
		chg_state(STATEID_ABOUT);
		break;

	    case ACTIONID_CONN_DETAILS:
		chg_state(STATEID_CONN_DETAILS);
		break;
				
	    case ACTIONID_QUIT:
		quit=true;
		break;

	    case ACTIONID_SWITCH_NC:
		mk.switch_to_navi();
		break;

	    case ACTIONID_SWITCH_FC:
		mk.switch_to_fc();
		break;

	    case ACTIONID_SWITCH_MK3MAG:
		mk.switch_to_mk3mag();
		break;
				  
	    case ACTIONID_GRAPH:
		chg_state(STATEID_GRAPH);
		break;
				  
	    case ACTIONID_KEYCONTROL:
		chg_state(STATEID_KEYCONTROL);
		break;
				
	    case ACTIONID_LCD :
		chg_state(STATEID_LCD);
		break;

				
	    case ACTIONID_PROXY:

		chg_state(STATEID_IPINPUT);
		break;
				
	    case ACTIONID_DEVICESELECT:
		chg_state(STATEID_SCANNING);
		break;
				
	    case ACTIONID_RAWDEBUG:
		chg_state(STATEID_RAWDEBUG);
		break;
				
	    case ACTIONID_SETTINGS:
		chg_state(STATEID_SETTINGSMENU);
		break;

	    case ACTIONID_SETTING_OPTIONS:
		chg_state(STATEID_SETTING_OPTIONS);
		break;

	    case ACTIONID_RCDATA:
		chg_state(STATEID_STICKVIEW);
		break;

	    case ACTIONID_CAM:
		chg_state(STATEID_CAMMODE);
		break;

	    case ACTIONID_GPSDATA:
		chg_state(STATEID_GPSVIEW);
		break;


	    case  ACTIONID_MOTORTEST :
		chg_state(STATEID_MOTORTEST);
		break;
				

	    case ACTIONID_EDIT_PARAMS:
		chg_state(STATEID_EDIT_PARAMS);
		break;

	    case ACTIONID_START_ENGINES:
		mk.start_engines();
		break;
	    case ACTIONID_SELECT_PARAMS:
		select_paramset4edit=true;
		read_paramset_intension_save=false;
		if (settings.reload_settings)
		    {	

			mk.watchdog.act_paramset=0;
			mk.params.reset();

			chg_state(STATEID_READ_PARAMS);
		    }
		else
		    {
			if (mk.watchdog.act_paramset!=5)
			    chg_state(STATEID_READ_PARAMS);
			else
			    chg_state(STATEID_SELECT_PARAMSET);
		    }
		break;


	    case ACTIONID_WRITE_PARAM_AS:
		select_paramset4edit=false;
		chg_state(STATEID_SELECT_PARAMSET);
		break;

	    case ACTIONID_WRITE_PARAMS:
		mk.write_params(mk.params.act_paramset);
		act_msg=l(STRINGID_SAVEDSETTINGS); // too optimistic
		
		nextstate=state;
		chg_state(STATEID_SUCCESS_MSG);
		break;

	    case ACTIONID_UNDO_PARAMS:
		mk.params.use_backup();
		act_msg=l(STRINGID_SETTINGSUNDOOK); // too optimistic
		nextstate=STATEID_HANDLE_PARAMS;
		chg_state(STATEID_SUCCESS_MSG);

		break;

	    case ACTIONID_MAINMENU:
		chg_state(STATEID_MAINMENU);
		break;

	    case ACTIONID_DEBUG:
		chg_state(STATEID_DUBWISE_VALUES);
		//		debug.showing=true;
		break;

	    case ACTIONID_TRAFFIC:
		chg_state(STATEID_TRAFFIC);
		break;

	    case ACTIONID_CONNECT_RECENT:
		chg_state(STATEID_CONNECT_RECENT);
		break;

	    case ACTIONID_CONNECT_FAKE:
		mk.setCommunicationAdapter(new FakeCommunicationAdapter());
		mk.connect_to("fake","fake");

		break;

	    case ACTIONID_CONNECT_TCP:
		ipinput4proxy=false;
		chg_state(STATEID_IPINPUT);
		break;

	    case ACTIONID_SCAN_BT:
		chg_state(STATEID_SCANNING);
		break;

	    case ACTIONID_SELECT_COM:
		chg_state(STATEID_SELECT_COMPORT);
		break;

	    case ACTIONID_PROXY_INPUT:
		ipinput4proxy=true;
		chg_state(STATEID_IPINPUT);
		break;			    

	    }
		
    }





    public void pointerDragged (int pointer_x, int pointer_y)
    {
//#if j2memap=="on"
	if (state==STATEID_MAP)
	    {
		root.m_map.pointerDragged(pointer_x,pointer_y);
		return;
	    }
//#endif

	if ((pointer_x>canvas_width) || (pointer_y>canvas_height)) return; // bugfix/workaround - pointer in offscreen - e.g. wtk when not in fullscreen

	System.out.println("p_y: " + pointer_y  + "ch:" + canvas_height);
	byte act_pointer_row;
	
	if (lcd_lines.length>max_lines)
	    act_pointer_row=(byte)((pointer_y-(canvas_height-lcd_img.getHeight()*max_lines))/lcd_img.getHeight() +lcd_off );
	else
	    act_pointer_row=(byte)((pointer_y-(canvas_height-lcd_img.getHeight()*lcd_lines.length))/lcd_img.getHeight() );

	if ((act_pointer_row>=lcd_lines.length)||(act_pointer_row<0)) return;

	switch(state)
		{
		case STATEID_PARAM_MENU:
		case STATEID_CONN_DETAILS:
		case STATEID_HANDLE_PARAMS:
		case STATEID_FILEOPEN:
		case STATEID_TRAFFIC:
		case STATEID_SELECT_COMPORT:	
		case STATEID_MAINMENU:
		case STATEID_SETTING_OPTIONS:
		case STATEID_SELECT_PARAMSET:
		case STATEID_DEVICESELECT:
		case STATEID_SELECT_MIXER:
		case STATEID_CONNECT_RECENT:
		    act_menu_select=act_pointer_row;
		}
	
    }

    public void pointerPressed (int pointer_x, int pointer_y)
    {

//#if j2memap=="on"
	if (state==STATEID_MAP)
	    {
		root.m_map.pointerPressed(pointer_x,pointer_y);
		return;
	    }
//#endif

	if ((pointer_x>canvas_width) || (pointer_y>canvas_height)) return; // bugfix - pointer in offscreen - e.g. wtk when not in fullscreen

	byte act_pointer_row;

	if (lcd_lines.length>max_lines)
	    act_pointer_row=(byte)((pointer_y-(canvas_height-lcd_img.getHeight()*max_lines))/lcd_img.getHeight() +lcd_off );
			else
	     act_pointer_row=(byte)((pointer_y-(canvas_height-lcd_img.getHeight()*lcd_lines.length))/lcd_img.getHeight() );


	if (pointer_y<lcd_img.getHeight())
	    keyPressed(KEY_STAR);
	else
	    { if (act_pointer_row<0) return;

	    if (pointer_y>canvas_height-lcd_img.getHeight()*lcd_lines.length)
		switch(state)
		{
		case STATEID_SETTINGSMENU:
		case STATEID_EDIT_PARAMS:		
		case STATEID_MOTORTEST:		
		    act_editor.pointer_press(pointer_x,act_pointer_row );
		    break;
		    /*

		    //		    settings_editor.pointer_press(pointer_x,(byte)((pointer_y-(canvas_height-lcd_img.getHeight()*lcd_lines.length))/lcd_img.getHeight())) ;
		    settings_editor.pointer_press(pointer_x,act_pointer_row );
		    break;


		    //		    params_editor.pointer_press(pointer_x,(byte)((pointer_y-(canvas_height-lcd_img.getHeight()*lcd_lines.length))/lcd_img.getHeight()) );

		    params_editor.pointer_press(pointer_x,act_pointer_row );
		    break;



		    motor_tester.pointer_press(pointer_x,act_pointer_row );

		    break;
		    */
		case STATEID_LCD:
		    if (pointer_x<(canvas_width/2))
			keyPressed(getKeyCode(LEFT));
		    else
			keyPressed(getKeyCode(RIGHT));
		    break;

		case STATEID_CONNECT_RECENT:
		case STATEID_SELECT_MIXER:
		case STATEID_PARAM_MENU:
		case STATEID_CONN_DETAILS:
		case STATEID_HANDLE_PARAMS:
		case STATEID_FILEOPEN:
		case STATEID_TRAFFIC:
		case STATEID_SELECT_COMPORT:	
		case STATEID_MAINMENU:
		case STATEID_SETTING_OPTIONS:
		case STATEID_SELECT_PARAMSET:
		case STATEID_DEVICESELECT:
		    
		    
		    //	    if (pointer_y>canvas_height-lcd_img.getHeight()*menu_items.length)
		    //	{			
		    /*		
			if (lcd_lines.length>max_lines)
			act_menu_select=(byte)((pointer_y-(canvas_height-lcd_img.getHeight()*max_lines))/lcd_img.getHeight() +lcd_off );
			else
			act_menu_select=(byte)((pointer_y-(canvas_height-lcd_img.getHeight()*lcd_lines.length))/lcd_img.getHeight() );
			
		    */

		    if (act_menu_select!=act_pointer_row)
			act_menu_select=act_pointer_row;
		    else
			keyPressed(getKeyCode(FIRE));
			    //}    
		    break;
		} 
    
	    }
    }


    int last_keycode=-1;
    int repeat_keycode=0;
    
    public void keyPressed(int keyCode)
    {
	System.out.println("keypress in state " + state);
	key_bitfield|=keyCode2mask(keyCode);

	if ((state==STATEID_MAINMENU)&&(keyCode==beta_unlock_code[beta_unlock_pos++]))
	    {
		if (beta_unlock_pos==beta_unlock_code.length)
		    {
			beta_unlock_pos=0;
		nextstate=state;
			act_msg="Hi Betatester" ;
			chg_state(STATEID_SUCCESS_MSG);
			settings.set_betatester();
			return;
		    }

	    }
	else
	    beta_unlock_pos=0;



	if (last_keycode==keyCode)
	    repeat_keycode++;
	else
	    {
		repeat_keycode=0;
		last_keycode=keyCode;
	    }
	
	debug.log("KeyCode:"+keyCode);
	// key-actions common in all states
	debug.process_key(keyCode);
	
	
	if ( key_bitfield==(keyCode2mask(KEY_NUM0)|keyCode2mask(KEY_STAR)))
	    {
		chg_state(STATEID_MAINMENU);
		return;
	    }
	
	
	if (state!=STATEID_MAP)
	    {
		if (((keyCode==KEY_STAR) || (keyCode==settings.key_back) )&&(state!=STATEID_KEYCONTROL))//&&(state!= STATEID_STRINGINPUT))
		    {
			chg_state(STATEID_MAINMENU);
			return;
		    }
	    }

	if (state==STATEID_MAP)
	    {
		if (keyCode==settings.key_back)
		    {
			chg_state(STATEID_MAINMENU);
			return;
		    }
	    }
	
	if (((keyCode==KEY_POUND)||(keyCode==settings.key_fullscreen))&&(state!=STATEID_KEYCONTROL))
	    {
		settings.toggle_fullscreen();

		return;
	    }

	// key actions per state
	switch(state)
	    {
//#if j2memap=="on"
	    case STATEID_MAP:
		root.m_map.keyPressed(keyCode);
		break;
//#endif
	    case STATEID_ERROR_MSG:
		if (nextstate==-1)
		    {
			chg_state(STATEID_MAINMENU);
			break;
		    }

	    case STATEID_GRAPH:
		switch(keyCode)
		    {
		    case KEY_NUM7:
			settings.graph_scale=!settings.graph_scale;
			break;

		    case KEY_NUM5:
			settings.graph_legend=!settings.graph_legend;
			break;

		    case KEY_NUM3:
			mk.freeze_debug_buff=!mk.freeze_debug_buff;
			break;
		    }

		break;
	    case STATEID_SUCCESS_MSG:
		chg_state(nextstate);
		nextstate=-1;
		break;

	    case STATEID_STRINGINPUT:
		if ((keyCode>=KEY_NUM2)&&(keyCode<=KEY_NUM9))
		    {
			act_input_str=act_input_str.substring(0,ipinput_pos) + 


			    (char)( 97 + (keyCode-KEY_NUM2)*3 + ((keyCode>KEY_NUM7)?1:0) +(repeat_keycode%(((keyCode==KEY_NUM7)||(keyCode==KEY_NUM9))?4:3)))

			    + act_input_str.substring(ipinput_pos+1,act_input_str.length());
		    }
		else if ((keyCode==KEY_NUM0))
		    {
			act_input_str=act_input_str.substring(0,ipinput_pos) + 
			    act_input_str.substring(ipinput_pos+1,act_input_str.length());
		    }
		else
		    {
		    switch (getGameAction (keyCode)) 
			{

			case LEFT:
			    if(ipinput_pos>0) ipinput_pos--;
			    break;
			    
			case RIGHT:
			    if(ipinput_pos<19)	ipinput_pos++;
			    break;

			case UP:
			    act_input_str=act_input_str.substring(0,ipinput_pos) + 
				(char)((byte) act_input_str.charAt(ipinput_pos)-1) + act_input_str.substring(ipinput_pos+1,act_input_str.length());

			    break;
			case DOWN:
			    act_input_str=act_input_str.substring(0,ipinput_pos) + 
				(char)((byte) act_input_str.charAt(ipinput_pos)+1) + act_input_str.substring(ipinput_pos+1,act_input_str.length());

			    break;


			case FIRE:
			    mk.params.set_name(act_input_str);
			    chg_state(STATEID_HANDLE_PARAMS);
			    break;
			}
	
		    }
		if (act_input_str.length()<=ipinput_pos) act_input_str+=" ";
		break;
	    case STATEID_IPINPUT:
		if ((keyCode>=KEY_NUM0)&&(keyCode<=KEY_NUM9))
		    {
			act_edit_ip[ipinput_pos/4]=DUBwiseHelper.mod_decimal(act_edit_ip[ipinput_pos/4],(ipinput_pos<15?2:3)-(ipinput_pos%4),0,(keyCode-KEY_NUM0),9);

			if(ipinput_pos<19)	ipinput_pos++;
			if ((ipinput_pos<18)&&(((ipinput_pos+1)%4)==0))ipinput_pos++;
		    }
		else
		    switch (getGameAction (keyCode)) 
			    {
			    case LEFT:
				if(ipinput_pos>0) ipinput_pos--;
				if (((ipinput_pos+1)%4)==0)ipinput_pos--;
				break;
			
			    case RIGHT:
				if(ipinput_pos<19)	ipinput_pos++;
				if(ipinput_pos<18)if (((ipinput_pos+1)%4)==0)ipinput_pos++;
				break;

			    case UP:
				act_edit_ip[ipinput_pos/4]=DUBwiseHelper.mod_decimal(act_edit_ip[ipinput_pos/4],(ipinput_pos<15?2:3)-(ipinput_pos%4),1,-1,9);

				break;

			    case DOWN:
				act_edit_ip[ipinput_pos/4]=DUBwiseHelper.mod_decimal(act_edit_ip[ipinput_pos/4],(ipinput_pos<15?2:3)-(ipinput_pos%4),-1,-1,9);


			    case FIRE:
				if (ipinput4proxy)
				    {
					settings.act_proxy_ip=act_edit_ip;
					mk.do_proxy("socket://"+DUBwiseHelper.ip_str(settings.act_proxy_ip,false));
					chg_state(STATEID_PROXY);
				    }
				else
				    {
					settings.act_conn_ip=act_edit_ip;
					connect_mk("socket://"+DUBwiseHelper.ip_str(settings.act_conn_ip,false),"TCP/IP Connection");
					chg_state(STATEID_CONN_DETAILS);
				    }

				break;

			    }
		break;

	    case STATEID_GPSVIEW:
		if (keyCode == this.KEY_NUM0)
		    mk.set_gps_target(mk.gps_position.Latitude,mk.gps_position.Longitude);
		if (keyCode == this.KEY_NUM1)
		    mk.gps_position.push_wp();
		if (keyCode == this.KEY_NUM2)
		    chg_state(STATEID_FILEOPEN);
		if (keyCode == this.KEY_NUM3)
		    mk.set_gps_target(mk.gps_position.LatWP[act_wp],mk.gps_position.LongWP[act_wp]);


		if (keyCode == this.KEY_NUM5)
		    heading_offset= mk.debug_data.analog[26];

		switch (getGameAction (keyCode)) 
			    {
			    case UP:
				if (act_wp!=0) act_wp--;
				break;
			
			    case DOWN:
				if (act_wp<mk.gps_position.last_wp) act_wp++;

				break;


			    }
	

		break;

	    case STATEID_ABOUT:
		
		switch (getGameAction (keyCode)) 
			    {
			    case UP:
				if (act_menu_select>=max_lines)
				    act_menu_select--;
				break;
			
			    case DOWN:
				if (act_menu_select<lcd_lines.length-1)
				    act_menu_select++;

				break;


			    }
	

		break;
		

	    case STATEID_RAWDEBUG:
		
		switch (getGameAction (keyCode)) 
			    {
			    case UP:
				if (rawdebug_cursor_y==0)
				    rawdebug_cursor_y=31;
				else
				    rawdebug_cursor_y--;
				break;
			
			    case DOWN:
				if (rawdebug_cursor_y==31)
				    rawdebug_cursor_y=0;
				else
				    rawdebug_cursor_y++;
				break;


			    }
		break;
	    case STATEID_KEYCONTROL:
		if (keyCode==KEY_POUND)
		    keycontrol_exit |= 1;
		else
		if (keyCode==KEY_STAR)
		    keycontrol_exit |= 2;
		else
		    mod_external_control_by_keycode(keyCode,(byte)1);


		if (keyCode==KEY_NUM5)
		    process_action(ACTIONID_START_ENGINES);

		if (keycontrol_exit==3) 
		    chg_state(STATEID_MAINMENU);

		    /*
		if ((keyCode >= this.KEY_NUM0) && (keyCode < this.KEY_NUM8))
		    keycontrol_bitfield[0]|=1<<(keyCode-this.KEY_NUM0);
		else
		if ((keyCode >= this.KEY_NUM8) && (keyCode <= this.KEY_NUM9))
		    keycontrol_bitfield[1]|=1<<(keyCode-this.KEY_NUM8);
		    
		else
		    switch (getGameAction (keyCode)) 
			    {
			    case UP:
				keycontrol_bitfield[1]|=4;
				break;
			
			    case DOWN:
				keycontrol_bitfield[1]|=8;
				break;

			
			    case LEFT:
				keycontrol_bitfield[1]|=16;
				break;

			    case RIGHT:
				keycontrol_bitfield[1]|=32;
				break;
				
			    case FIRE:
				keycontrol_bitfield[1]|=64;
				break;

			    }
		else
		    mk.send_keys(keycontrol_bitfield);
		    */
		break;


		/*
	    case STATEID_MOTORTEST:
		motor_tester.keypress(keyCode,getGameAction (keyCode));

		switch (getGameAction (keyCode)) 
			    {
			    case UP:
				act_motor_increase=-1;
				break;
			
			    case DOWN:
				act_motor_increase=1;
				break;

			    case FIRE:
				motor_test_sel_all=!motor_test_sel_all;
				break;

			    case LEFT:
				act_motor--;
				if (act_motor<0) {act_motor=0; chg_state(STATEID_MAINMENU); }
				break;

			    case RIGHT:
				act_motor++;
				act_motor%=4;
				break;
			    }


		break;
		*/


		/*
	    case STATEID_HANDLsE_PARAMS:
		
		menu_keypress(keyCode);
		break;
		*/
		/*
	    case STATEID_TRAFFIC:
		if ( getGameAction (keyCode)==FIRE ) 
		    chg_state(STATEID_CONN_DETAILS);
		else
		    menu_keypress(keyCode);
		break;
		*/

	    case STATEID_CONNECT_RECENT:
	    case STATEID_SELECT_MIXER:

	    case STATEID_SELECT_FIRMWARE:
	    case STATEID_SETTING_OPTIONS:
	    case STATEID_MAINMENU:
	    case STATEID_SELECT_PARAMSET:
	    case STATEID_SELECT_COMPORT:	

	    case STATEID_PARAM_MENU:
	    case STATEID_TRAFFIC:
	    case STATEID_CONN_DETAILS:
	    case STATEID_HANDLE_PARAMS:
	    case STATEID_CONFIRM_MOTORTEST:
//#if bluetooth=="on"
	    case STATEID_DEVICESELECT:
//#endif

//#if fileapi=="on"
	    case STATEID_FILEOPEN:
//#endif

		menu_keypress(keyCode);
		break;


	    case STATEID_EDIT_PARAMS:

		if (keyCode==settings.key_paramsave)
		    process_action( ACTIONID_WRITE_PARAMS);
	    case STATEID_MOTORTEST:

	    case STATEID_SETTINGSMENU:
		act_editor.keypress(keyCode,getGameAction (keyCode)) ;
		break;

		// handle menue
		//	    case STATEID_SELECT_SPEED_FORMAT:	
		//	    case STATEID_SELECT_GPS_FORMAT:

		/*
	    case STATEID_EDIT_PARAMS:
		params_editor.keypress(keyCode,getGameAction (keyCode)) ;
		break;
		*/
	    case STATEID_LCD:

		if ((keyCode >= this.KEY_NUM0) && (keyCode <= this.KEY_NUM9))		    
			mk.LCD.set_page(keyCode-this.KEY_NUM0);
		else
		switch (getGameAction (keyCode)) 
		    {
		    case LEFT:
		    case UP:
			mk.LCD.LCD_PREVPAGE();
			break;
			
		    case RIGHT:
		    case DOWN:
			mk.LCD.LCD_NEXTPAGE();
			break;


			
		    }
		break;
	    }
    
	

    }

  






}


