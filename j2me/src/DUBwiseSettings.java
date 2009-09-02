/***************************************************************
 *
 * Settings related Part of DUBwise
 *  ( e.g. saving to / reading from RMS )
 *                                                          
 * Author:        Marcus -LiGi- Bueschleb
 * Mailto:        LiGi @at@ LiGi DOTT de                    
 *
***************************************************************/

import javax.microedition.rms.*;
import java.io.*;
import java.util.Random;
import java.util.Vector;

public class DUBwiseSettings 
    extends org.ligi.ufo.ParamsClass
    implements org.ligi.ufo.DUBwiseDefinitions,DUBwiseUIDefinitions,org.ligi.ufo.DUBwiseLangDefs
{
    // name/handle for the recordStore to memorize some stuff
    private final static String RECORD_STORE_NAME="DUBSETT_V5";

    /* all settings hold here */
    public byte act_skin=SKINID_DARK;

    public int key_back;
    public int key_fullscreen;
    public int key_clear;
    public int key_paramsave;
    public int key_alternative_fire;

    public int voice_delay;
    public int voice_volume;
    public byte graph_interval;
    public int remote_cam_stick;

    public int webreporter_interval;

    public String connection_name="";
    public String connection_url="";

    public boolean do_vibra;
    public boolean do_sound;


    public boolean do_altimeter_sound;

    public boolean do_volts_voice;

    // new
    public boolean do_alt_voice;
    public boolean do_intro_voice;
    public boolean do_flighttime_voice;
    public boolean do_satelites_voice;

    public boolean minimal_voice;
    public boolean voice_nopoint;

    public boolean big_fonts;

    public byte altsteps;

    public boolean horizon_invert_nick;
    public boolean horizon_invert_roll;
    public boolean horizon_display_altitude;
    public boolean horizon_display_flytime;

    public boolean fullscreen;
    public boolean do_scrollbg;
    public boolean expert_mode;
    public boolean dev_mode;
    public boolean betatester_mode;

    public boolean reload_settings;
    public boolean instant_error_show;

    public boolean graph_legend;
    public boolean graph_scale;

    public boolean webreporter;
    public long dubwise_id;



//#if devicecontrol=="on"
    public boolean keep_lighton=false;
//#endif

    int[] act_proxy_ip=default_ip; // { ip , ip , ip , ip , port }
    int[] act_conn_ip=default_ip; // { ip , ip , ip , ip , port }

    /* end of all settings hold here */


    public final static int SETTINGS_POS_SKIN           =0;
    public final static int SETTINGS_POS_BITFIELD1      =1;
    public final static int SETTINGS_POS_GPS_FORMAT     =2;
    public final static int SETTINGS_POS_SPEED_FORMAT   =3;
    public final static int SETTINGS_POS_EXTERN_NICK    =4;
    public final static int SETTINGS_POS_EXTERN_ROLL    =5;
    public final static int SETTINGS_POS_EXTERN_GIER    =6;
    public final static int SETTINGS_POS_EXTERN_GAS     =7;
    public final static int SETTINGS_POS_EXTERN_HIGHT   =8;
    public final static int SETTINGS_POS_KEY_BACK       =9;
    public final static int SETTINGS_POS_KEY_FULL       =10;
    public final static int SETTINGS_POS_KEY_CLEAR      =11;
    public final static int SETTINGS_POS_VOICEVOLUME    =12;
    public final static int SETTINGS_POS_VOICEDELAY     =13;
    public final static int SETTINGS_POS_BITFIELD2      =14;
    public final static int SETTINGS_POS_LANG           =15;
    public final static int SETTINGS_POS_GRAPHINTERVAL  =16;
    public final static int SETTINGS_POS_BITFIELD3      =17;
    public final static int SETTINGS_POS_ALTSTEPS       =18;
    public final static int SETTINGS_POS_CAMSTICK       =19;

    public final static int SETTINGS_POS_PRIMARYABO     =20;
    public final static int SETTINGS_POS_SECONDARYABO   =21;
    public final static int SETTINGS_POS_DEFAULTABO     =22;

    public final static int SETTINGS_POS_KEY_PARAMSAVE  =23;
    public final static int SETTINGS_POS_WEBREPORTER_INTERVAL=24;
    public final static int SETTINGS_POS_BITFIELD4      =25;
    public final static int SETTINGS_POS_KEY_ALTERNATIVE_FIRE      =26;

    public final static int SETTINGS_FIELD_LENGTH       =27;

    int[] settings_field;


    Random random;
    public DUBwiseSettings(DUBwiseCanvas _canvas)
    {
	canvas= _canvas;
	settings_field=new int[SETTINGS_FIELD_LENGTH];
	recent_conns=new Vector();
	default_extern_control=new byte[11];
	// set defaults
	settings_field=plain_defaults;
	random = new Random();
	dubwise_id=random.nextLong();
    }


    public int get_field_from_act(int pos) { return settings_field[pos];}
    public void set_field_from_act(int pos,int val){

	settings_field[pos]=val;
	field2setting(pos,val);
    }

    public void toggle_fullscreen()
    {
	//	fullscreen=!fullscreen;
	 set_field_from_act(1,settings_field[1]^1);
	 //	if (fullscreen)canvas.setFullScreenMode(true);
    }

    public void set_betatester()
    {
	settings_field[SETTINGS_POS_BITFIELD3]|=2;
    }

    public void field2setting(int pos,int val)
    {
	try {
	boolean new_fullscreen=fullscreen;
	boolean new_do_scrollbg=do_scrollbg;

	switch (pos)
	    {
	    case SETTINGS_POS_WEBREPORTER_INTERVAL:
		webreporter_interval=val;
		break;

	    case SETTINGS_POS_ALTSTEPS:
		altsteps=(byte)val;
		break;
	    case SETTINGS_POS_GRAPHINTERVAL:
		if (val<1)
		    graph_interval=1;
		else
		    graph_interval=(byte)val;
		break;


	    case SETTINGS_POS_BITFIELD1:
		new_fullscreen=((val&1)!=0);
		new_do_scrollbg=((val&2)!=0);

		do_sound=((val&4)!=0);
		do_vibra=((val&8)!=0);
		expert_mode=((val&16)!=0);
//#if devicecontrol=="on"
		keep_lighton=((val&32)!=0);
//#endif
		graph_legend=((val&64)!=0);
		graph_scale=((val&128)!=0);

		break;

	    case SETTINGS_POS_BITFIELD2:
		do_volts_voice=((val&1)!=0);	
		reload_settings=((val&2)!=0);     
		do_altimeter_sound=((val&4)!=0);
		horizon_invert_roll=((val&8)!=0);
		horizon_invert_nick=((val&16)!=0);
		horizon_display_flytime=((val&32)!=0);
		horizon_display_altitude=((val&64)!=0);
		dev_mode=((val&128)!=0);
		break;

	    case SETTINGS_POS_BITFIELD3:
		instant_error_show=((val&1)!=0);
		betatester_mode=((val&2)!=0);
		big_fonts=((val&4)!=0);
		webreporter=((val&8)!=0);
		
	
		do_alt_voice=((val&16)!=0);
		do_intro_voice=((val&32)!=0);

		voice_nopoint=((val&64)==0);
		minimal_voice=((val&128)!=0);

		canvas.regenerate_fonts=true;
		break;

	    case SETTINGS_POS_BITFIELD4:
		do_flighttime_voice=((val&1)!=0);
		do_satelites_voice=((val&2)!=0);
		break;


	    case SETTINGS_POS_GPS_FORMAT:
		canvas.mk.gps_position.act_gps_format=(byte)val;
		break;
	    case SETTINGS_POS_SPEED_FORMAT:
		canvas.mk.gps_position.act_speed_format=(byte)val;
		break;
	    case SETTINGS_POS_EXTERN_NICK:
		default_extern_control[EXTERN_CONTROL_NICK]=(byte)val;	    
		break;
	    case SETTINGS_POS_EXTERN_ROLL:
		default_extern_control[EXTERN_CONTROL_ROLL]=(byte)val;	    
		break;
	    case SETTINGS_POS_EXTERN_GIER:
		default_extern_control[EXTERN_CONTROL_GIER]=(byte)val;	    
		break;
	    case SETTINGS_POS_EXTERN_GAS:
		default_extern_control[EXTERN_CONTROL_GAS]=(byte)val;	    
		break;
	    case SETTINGS_POS_EXTERN_HIGHT:
		default_extern_control[EXTERN_CONTROL_HIGHT]=(byte)val;	    
		break;
		
	    case SETTINGS_POS_KEY_ALTERNATIVE_FIRE:
		key_alternative_fire=val;
		break;

	    case SETTINGS_POS_KEY_PARAMSAVE:
		key_paramsave=val;
		break;
	    case SETTINGS_POS_KEY_BACK:
		key_back=val;	    
		break;
	    case SETTINGS_POS_KEY_FULL:
		key_fullscreen=val;
		break;
	    case SETTINGS_POS_KEY_CLEAR:
		key_clear=val;
		break;
	    case SETTINGS_POS_CAMSTICK:
		remote_cam_stick=val;
		break;

	    case SETTINGS_POS_LANG:
		canvas.act_lang=(byte)val;
		canvas.load_strings();
		break;

	    case SETTINGS_POS_SKIN:
		act_skin=(byte)val;
		canvas.load_skin_images();
		break;




	    case SETTINGS_POS_VOICEVOLUME:
	    
		voice_volume=val;
//#if voice_mode!="no_voice"
		canvas.status_voice.volume=voice_volume; 
		
//#endif
		break;
		
	    case SETTINGS_POS_VOICEDELAY:
		voice_delay=val;
		break;


	    case SETTINGS_POS_PRIMARYABO:
		canvas.mk.primary_abo=val;
		break;
	    case SETTINGS_POS_SECONDARYABO:
		canvas.mk.secondary_abo=val;
		break;
	    case  SETTINGS_POS_DEFAULTABO:
		canvas.mk.default_abo=val;
		break;

	    }

	if (new_fullscreen!=fullscreen)
	    {
		fullscreen=new_fullscreen;
		canvas.setFullScreenMode(fullscreen);
	    }


	if (new_do_scrollbg!=do_scrollbg)
	    {
		do_scrollbg=new_do_scrollbg;
		canvas.load_skin_images();
	    }

	/* snippet to extract default fields
	  
	String ts="";
	for ( int i=0;i<SETTINGS_FIELD_LENGTH;i++)
	    ts+=","+settings_field[i];

	System.out.println(ts);
	*/
	}
	catch (Exception e) { }

    }

    public int[] plain_defaults={0,0,0,0,42,42,42,1,1,-4242,-4242,-4242,0,10,0,0,7,0,7,0,10,30,1000,-4242,10,0,-4242};
    public int[] fancy_defaults={0,255,0,0,42,42,42,1,1,-4242,-4242,-4242,150,10,5,0,7,1,10,0,1,3,100,-4242,10,3,-4242};


    public void process_all_settings()
    {
	for ( int i=0;i<SETTINGS_FIELD_LENGTH;i++)
	    field2setting(i,settings_field[i]);
    }

    public void load_plain_defaults()
    {
	settings_field=plain_defaults;
	process_all_settings();
    }

    public void load_fancy_defaults()
    {
	settings_field=fancy_defaults;
	process_all_settings();
    }

    public byte[] default_extern_control;//=default_extern_keycontrol ;


    public int[] _tab_stringids={STRINGID_USERINTERFACE,STRINGID_UNITS,STRINGID_KEYCONTROL,STRINGID_SPECIALKEYS,STRINGID_GRAPH,STRINGID_SOUND,STRINGID_VOICE,STRINGID_COCKPIT,STRINGID_TIMING,STRINGID_WEBREPORTER,STRINGID_OTHER};


    public int[][] _field_stringids ={
	{
	    STRINGID_LANGUAGE,
	    STRINGID_SKIN,
	    STRINGID_FULLSCREEN,
	    STRINGID_SCROLLBG
//#if devicecontrol=="on"
	    ,STRINGID_PERMALIGHT
//#endif
	    ,STRINGID_BIGFONTS
	},
	{STRINGID_SPEEDFORMAT,STRINGID_GPSFORMAT},
	{STRINGID_NICK ,STRINGID_ROLL,STRINGID_GIER,STRINGID_GASINCREASE,STRINGID_HEIGHTINCREASE },
	{STRINGID_BACKTOMAINMENU,STRINGID_ALTERNATIVE_FIRE,STRINGID_FULLSCREEN,STRINGID_CLEAR,STRINGID_PARAMSAVE },
	{STRINGID_LEGEND,STRINGID_SCALEGRID,STRINGID_GRAPHINTERVAL},
	{STRINGID_SOUND,STRINGID_ALTIMETER,STRINGID_ALTSTEPS,STRINGID_VOLUME},// sound
	{STRINGID_MINIMAL,STRINGID_VOICE_NOPOINT,STRINGID_INTRO,STRINGID_DELAYINS,STRINGID_VOLTS,STRINGID_ALTITUDE,STRINGID_SATELITES,STRINGID_FLIGHTTIME}, // voice

	{STRINGID_SHOWALTITUDE,STRINGID_SHOWFLIGHTTIME,STRINGID_INVERTROLL,STRINGID_INVERTNICK},
	{STRINGID_PRIMARYABO,STRINGID_SECONDARYABO,STRINGID_DEFAULTABO}, ////

	{STRINGID_WEBREPORTER,STRINGID_INTERVAL},
	{STRINGID_INSTANTERRORSHOW,STRINGID_VIBRA,STRINGID_REMOTECAMSTICK,STRINGID_ALWAYSRELOADPARAMS,STRINGID_EXPERTMODE,STRINGID_DEVMODE}};

    public int[][] _choice_stringids={ {STRINGID_LANG0,STRINGID_LANG1,STRINGID_LANG2}, /* FIXME - make dynamic */{ STRINGID_DARK,STRINGID_LIGHT } , {STRINGID_KMH,STRINGID_MPH,STRINGID_CMS},{STRINGID_DECIMAL,STRINGID_MINSEC} } ;



    public int[][] _field_positions=    { 
	{SETTINGS_POS_LANG,SETTINGS_POS_SKIN,8,9
//#if devicecontrol=="on"
       ,13
//#endif
       , SETTINGS_POS_BITFIELD3*8+2
} , {2,3} , {4,5,6,7,8} , {SETTINGS_POS_KEY_BACK,SETTINGS_POS_KEY_ALTERNATIVE_FIRE,SETTINGS_POS_KEY_FULL,SETTINGS_POS_KEY_CLEAR,SETTINGS_POS_KEY_PARAMSAVE},{SETTINGS_POS_BITFIELD1*8 +6,SETTINGS_POS_BITFIELD1*8 +7, SETTINGS_POS_GRAPHINTERVAL} ,
	{SETTINGS_POS_BITFIELD1*8+2,SETTINGS_POS_BITFIELD2*8+2,SETTINGS_POS_ALTSTEPS,SETTINGS_POS_VOICEVOLUME }, 

	{SETTINGS_POS_BITFIELD3*8+7,SETTINGS_POS_BITFIELD3*8+6,SETTINGS_POS_BITFIELD3*8+5,SETTINGS_POS_VOICEDELAY,SETTINGS_POS_BITFIELD2*8+0, SETTINGS_POS_BITFIELD3*8+4 , SETTINGS_POS_BITFIELD4*8+0, SETTINGS_POS_BITFIELD4*8+1  }, //voice


	{SETTINGS_POS_BITFIELD2*8+6,SETTINGS_POS_BITFIELD2*8+5,SETTINGS_POS_BITFIELD2*8+3,SETTINGS_POS_BITFIELD2*8+4},

	{SETTINGS_POS_PRIMARYABO,SETTINGS_POS_SECONDARYABO,SETTINGS_POS_DEFAULTABO},
	{SETTINGS_POS_BITFIELD3*8+3,SETTINGS_POS_WEBREPORTER_INTERVAL},
	{SETTINGS_POS_BITFIELD3*8+0,SETTINGS_POS_BITFIELD1*8+3,SETTINGS_POS_CAMSTICK,SETTINGS_POS_BITFIELD2*8+1,SETTINGS_POS_BITFIELD1*8+4,SETTINGS_POS_BITFIELD2*8+7}
    };
    public int[][] _field_types={ {PARAMTYPE_CHOICE+0,PARAMTYPE_CHOICE+1,PARAMTYPE_BITSWITCH,PARAMTYPE_BITSWITCH
//#if devicecontrol=="on"
		 ,PARAMTYPE_BITSWITCH
//#endif
	 ,PARAMTYPE_BITSWITCH
} , 
				  {PARAMTYPE_CHOICE+2,PARAMTYPE_CHOICE+3} , 
				  {PARAMTYPE_BYTE,PARAMTYPE_BYTE,PARAMTYPE_BYTE,PARAMTYPE_BYTE,PARAMTYPE_BYTE}, 
				  {PARAMTYPE_KEY,PARAMTYPE_KEY,PARAMTYPE_KEY,PARAMTYPE_KEY,PARAMTYPE_KEY } , // keys
				  {PARAMTYPE_BITSWITCH,PARAMTYPE_BITSWITCH,PARAMTYPE_BYTE}, 
				  {PARAMTYPE_BITSWITCH,PARAMTYPE_BITSWITCH,PARAMTYPE_BYTE,PARAMTYPE_BYTE} , //sound
				  {PARAMTYPE_BITSWITCH,PARAMTYPE_BITSWITCH,PARAMTYPE_BITSWITCH,PARAMTYPE_BYTE,PARAMTYPE_BITSWITCH,PARAMTYPE_BITSWITCH,PARAMTYPE_BITSWITCH,PARAMTYPE_BITSWITCH} , // voice
				  {PARAMTYPE_BITSWITCH,PARAMTYPE_BITSWITCH,PARAMTYPE_BITSWITCH,PARAMTYPE_BITSWITCH},
				  {PARAMTYPE_BYTE,PARAMTYPE_BYTE,PARAMTYPE_BYTE},
				  {PARAMTYPE_BITSWITCH,PARAMTYPE_BYTE},
				  {PARAMTYPE_BITSWITCH,PARAMTYPE_BITSWITCH,PARAMTYPE_STICK,PARAMTYPE_BITSWITCH,PARAMTYPE_BITSWITCH,PARAMTYPE_BITSWITCH}
    };
    DUBwiseCanvas canvas;

	
    public void load()
    {
	tab_stringids=_tab_stringids;

	field_positions=_field_positions;
	field_types=_field_types;

	choice_stringids=_choice_stringids;

	field_stringids=_field_stringids;
	
 	try
	    {
		RecordStore recStore= RecordStore.openRecordStore(RECORD_STORE_NAME, false);

		if (recStore.getNumRecords()==1)
		    {

			DataInputStream din = new DataInputStream( new ByteArrayInputStream(recStore.getRecord(1)) );

			byte str_count=din.readByte();
			
			for (int str_i=0;str_i<str_count;str_i++)
			    switch(str_i)
				{
				case 0:
				    connection_url=din.readUTF();
				    break;
				case 1:
				    connection_name=din.readUTF();
				    break;
				}

			byte field_count=din.readByte();
			for (int field_i=0;field_i<field_count;field_i++)
			    settings_field[field_i]=din.readInt();

			for ( int i=0;i<5;i++)
			    {
				act_proxy_ip[i]=din.readInt();
				act_conn_ip[i]=din.readInt();
			    }


			try
			    {
				byte recent_count=din.readByte();
				for (int str_i=0;str_i<recent_count;str_i++)
				    recent_conns.addElement(din.readUTF());

				dubwise_id=din.readLong( );
				System.out.println("dubwise id read");
			    }
			catch (Exception ex)
			    {  	 			    }		
			      System.out.println("read exception e" );
		            }
		recStore.closeRecordStore();
	    }
	catch (Exception e)
	    {  	 

	    }


	process_all_settings();


    }


    Vector recent_conns;
    
    public int recent_connection_count()
    {
	return recent_conns.size()/2;
    }

    public void add_recent(String url,String name)
    {
	if (!recent_conns.contains(url))
	    {
		recent_conns.addElement(url);
		recent_conns.addElement(name);
	    }
    }

    public String recent_conn_name(int id)
    {
	return (String)recent_conns.elementAt(id*2+1);
    }

    public String recent_conn_url(int id)
    {
	return (String)recent_conns.elementAt(id*2);
    }


    public String[] all_recent_connection_strings()
    {
	String[] res = new String[recent_conns.size()/2];
	for ( int tmp_i=0;tmp_i<recent_conns.size();tmp_i++)
	    res[tmp_i/2]=(String)recent_conns.elementAt(tmp_i);
	return res;
    }




    public void save()
    {
	try
	    {
		RecordStore.deleteRecordStore(RECORD_STORE_NAME);
	    }
	catch (Exception e)
	    { }

	try {
		RecordStore recStore = RecordStore.openRecordStore(RECORD_STORE_NAME, true );

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream      dout = new   DataOutputStream( bout );

		dout.writeByte(2);
		dout.writeUTF(connection_url);
		dout.writeUTF(connection_name);

		dout.writeByte(SETTINGS_FIELD_LENGTH);
		for ( int i=0;i<SETTINGS_FIELD_LENGTH;i++)
		    dout.writeInt(settings_field[i]);

		for ( int i=0;i<5;i++)
		    {
			dout.writeInt(act_proxy_ip[i]);
			dout.writeInt(act_conn_ip[i]);
		    }
		



		dout.writeByte( recent_conns.size());
		for ( int i=0;i<recent_conns.size();i++)
		    dout.writeUTF((String)recent_conns.elementAt(i));

		dout.writeLong( dubwise_id);

		recStore.addRecord(bout.toByteArray(),0,bout.size());
		recStore.closeRecordStore();


	    }
	catch (Exception e)
	    {  	    }

    }

}
