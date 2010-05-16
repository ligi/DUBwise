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

package org.ligi.ufo;

import java.util.Vector;

import org.ligi.tracedroid.logging.Log;

/**
 *                                          
 * Main Class to Communicate with the UFO
 *       
 * @author ligi
 *
 */
public class MKCommunicator
    implements Runnable,DUBwiseDefinitions
{
	
	public final static byte lib_version_major=0;
    public final static byte lib_version_minor=17;

    private CommunicationAdapterInterface comm_adapter;

	public byte slave_addr=-1;

	//int last_avr_sig=-1;
	
    public int primary_abo=10;
    public int secondary_abo=30;
    public int default_abo=1000;

    public int angle_nick=-4242;
    public int angle_roll=-4242;

    public boolean freeze_debug_buff=false;

    public boolean disconnect_notify=false;

    //public byte last_navi_error=0;

    public boolean mixer_change_notify=false;
    public boolean mixer_change_success=false;

    //public boolean change_notify=false;
    public boolean thread_running=true;

    Vector notify_listeners=new Vector();
    
    public void addNotificationListener(DUBwiseNotificationListenerInterface i) {
    	notify_listeners.addElement(i);
    }
    
    private void notifyAll(byte notification) {
    	for (int i=0;i<notify_listeners.size();i++)
    		try {
    		((DUBwiseNotificationListenerInterface)(notify_listeners.elementAt(i))).processNotification(notification);
    		}
    		catch(Exception e){}
    }
    
    public final static String lib_version_str()
    {
    	return "V"+lib_version_major+"."+lib_version_minor;
    }
    
	public int AngleNick() {
		switch (slave_addr) {

		case FC_SLAVE_ADDR:
			return angle_nick;

		case FAKE_SLAVE_ADDR:
		case NAVI_SLAVE_ADDR:
			return debug_data.analog[1];

		default:
			return -1;
		}

	}

	public int AngleRoll() {
		switch (slave_addr) {

		case FC_SLAVE_ADDR:
			return angle_roll;

		case FAKE_SLAVE_ADDR:
		case NAVI_SLAVE_ADDR:
			return debug_data.analog[1];

		default:
			return -1;
		}

	}
    
	
	/**
	 * 
	 * @return -1 if value is not available otherwise the altitude in dm
	 */
    public int getAlt() // in dm
    	{
    	// 	calculation background thanks to gregor: http://forum.mikrokopter.de/topic-post112323.html#post112323
    	int alt=0;
    	if (is_mk()||is_riddim()||is_fake())
    		alt=debug_data.analog[5]/2;
    	else		
    		if (is_navi())
    			alt=gps_position.Altimeter/2;
    	
    	if ( alt<0) alt=0;  // mk
    	if ( alt>20000) alt=0; // navi

    	if (is_mk()||is_navi()||is_riddim()||is_fake())
    		return alt;
    	else
    		return -1;
		
    }
    
    /**
     * 
     * @return -1 if the value is not avialable otherwise the charge in mAh
     * 
     */
    public int getUsedCapacity() {
    	
    	// had no info about that in FC version <0.78
    	if (is_mk()&&(version.compare(0, 78)==MKVersion.VERSION_PREVIOUS))
    		return -1;
    	
    	// had no info about that in NC version <0.18
    	if (is_navi()&&(version.compare(0, 18)==MKVersion.VERSION_PREVIOUS))
    		return -1;

    	if (is_mk())
    		return debug_data.analog[23];
    	
    	if (is_navi())
    		return gps_position.UsedCapacity;
    	

    	if (is_fake())
    		return 42;
    	
    	return -1;
    }

    /**
     * 
     * @return -1 if the value is not avialable otherwise the current in 0.1A
     * 
     */
    public int getCurrent() {
    	
    	// had no info about that in FC version <0.78
    	if (is_mk()&&(version.compare(0, 78)==MKVersion.VERSION_PREVIOUS))
    		return -1;
    	
    	// had no info about that in NC version <0.18
    	if (is_navi()&&(version.compare(0, 18)==MKVersion.VERSION_PREVIOUS))
    		return -1;

    	if (is_mk())
    		return debug_data.analog[22];
    	
    	if (is_navi())
    		return gps_position.Current;
    	
    	if (is_fake())
    		return 5;
    	return -1;
    }

    
    public String Alt_formated()
    	{
    	return "" + getAlt()/10 + "m";
    	}
    

    /***************** Section: public Attributes **********************************************/
    public boolean connected=false; // flag for the connection state

    public String mk_url=""; // buffer the url which is given in the constuctor for reconnectin purposes

    public final static int DATA_BUFF_LEN = 20; // in lines

    public String[] data_buff;

    //    boolean do_log=false;
    public boolean do_log=true;

    int data_buff_pos=0;

    //    public final static int DATA_IN_BUFF_SIZE=512;
    public final static int DATA_IN_BUFF_SIZE=2048;
    //    public final static int DATA_IN_BUFF_SIZE=4096;

    public byte user_intent=0;

    
    //    byte bootloader_stage= BOOTLOADER_STAGE_NONE;

    public MKLCD LCD;
    public MKVersion version;
    public MKDebugData debug_data;

    public int[] extern_control;

    public MKGPSPosition gps_position;

    public MixerManager mixer_manager;
    public MKStickData stick_data;
    public MKParamsParser params;
    public MKWatchDog watchdog;
    public MKProxy proxy=null;
    public MKStatistics stats ;
    //    public DUBwiseDebug debug;

    //    public UFOProber	ufo_prober;
    public long connection_start_time=-1;


    public String error_str = null;
    

    public final static byte FC_SLAVE_ADDR              = 1;
    public final static byte NAVI_SLAVE_ADDR            = 2;
    public final static byte MK3MAG_SLAVE_ADDR          = 3;
    public final static byte FOLLOWME_SLAVE_ADDR        = 10;

    public final static byte RIDDIM_SLAVE_ADDR          = 12;
    public final static byte RE_SLAVE_ADDR              = 23;

    public final static byte FAKE_SLAVE_ADDR              = 42;





    public boolean is_navi()
    {
	return (slave_addr==NAVI_SLAVE_ADDR);
    }

    public boolean is_fake()
    {
	return (slave_addr==FAKE_SLAVE_ADDR);
    }


    public boolean is_mk()
    {
	return (slave_addr==FC_SLAVE_ADDR);
    }

    public boolean is_mk3mag()
    {
	return (slave_addr==MK3MAG_SLAVE_ADDR);
    }

    public boolean is_rangeextender()
    {
	return (slave_addr==RE_SLAVE_ADDR);
    }

    public boolean is_followme()
    {
	return (slave_addr==FOLLOWME_SLAVE_ADDR);
    }

    public boolean is_riddim()
    {
	return (slave_addr==RIDDIM_SLAVE_ADDR);
    }

    public boolean is_incompatible()
    {
	switch (slave_addr)
	    {
	    case FC_SLAVE_ADDR:
	    case NAVI_SLAVE_ADDR:
	    case MK3MAG_SLAVE_ADDR:
	    case FOLLOWME_SLAVE_ADDR:
	    case RE_SLAVE_ADDR:
	    case RIDDIM_SLAVE_ADDR:
	    case FAKE_SLAVE_ADDR:
		return false;
	    default:
		return true;
	    }
    }

    public String extended_name()
    {
	switch (slave_addr)
	    {
	    case -1:
		return "No Device";

	    case FC_SLAVE_ADDR:
		return "MK-Connection";

	    case NAVI_SLAVE_ADDR:
		return "Navi-Connection";

	    case MK3MAG_SLAVE_ADDR:
		return "MK3MAG-Connection";

	    case FOLLOWME_SLAVE_ADDR:
		return "FollowMe Connection";

	    case RE_SLAVE_ADDR:
		return "RangeExtender Connection";

	    case RIDDIM_SLAVE_ADDR:
		return "Riddim Connection";

	    case FAKE_SLAVE_ADDR:
		return "Fake Connection";

	    default:
		return "Incompatible Device";
	    }
    }

    /****************** Section: private Attributes **********************************************/
//#ifdef j2me
//#    private javax.microedition.io.StreamConnection connection;
//#endif


    //private java.io.InputStream	reader;    
    //private java.io.OutputStream writer;    


    public String name;
    //    DUBwise root;


    private boolean sending=false;
    private boolean recieving=false;


    /******************  Section: public Methods ************************************************/
    public MKCommunicator()   
    {

	data_buff=new String[DATA_BUFF_LEN];
	for (int i=0;i<DATA_BUFF_LEN;i++)
	    data_buff[i]="";
	//	debug=debug_;
	//	root=root_;
	version=new MKVersion();
	debug_data=new MKDebugData();
	stick_data=new MKStickData();
	mixer_manager=new MixerManager();
	
	params=new MKParamsParser();
	extern_control=new int[EXTERN_CONTROL_LENGTH];
	extern_control[EXTERN_CONTROL_CONFIG]=1;
	extern_control[EXTERN_CONTROL_FRAME]=1;
	
	LCD= new MKLCD(this);
	watchdog=new MKWatchDog(this);
	gps_position=new MKGPSPosition();
	stats = new MKStatistics();
	proxy =new MKProxy(this);
	//	ufo_prober=new UFOProber();
	new Thread( this ).start(); // fire up main Thread 
    }



    public void write_raw(byte[] _data)
    {
	wait4send();
	sending=true;
	try {
		comm_adapter.write(_data,0,_data.length);
		comm_adapter.flush(); 

	stats.bytes_out+=_data.length;
	}
	catch ( Exception e){}
	sending=false;
    }

    public void do_proxy(String proxy_url)
    {
	proxy.connect(proxy_url);
    }
    
    //    int port;

    //  URL string: "btspp://XXXXXXXXXXXX:1" - the X-Part is the MAC-Adress of the Bluetooth-Device connected to the Fligth-Control
    public void connect_to(String _url,String _name)
    {
	name=_name;
	mk_url=_url; // remember URL for connecting / reconnecting later
	force_disconnect=false;
	connected=false;

	if ( _url=="fake" )
	    {
		connection_start_time=System.currentTimeMillis();
		gps_position.ErrorCode=1; // its an error that its a fake - just intent to show the symbol ;-)
		slave_addr=FAKE_SLAVE_ADDR;
		version.set_fake_data();
		connected=true;
	    }
    }

    
    /**
     * returns if connected and got a version from the Device
     * @return
     */
    public boolean ready()
    {
    	return (connected&&(version.major!=-1));
    }


    public String get_buff(int age)
    {

	age%=DATA_BUFF_LEN;
	
 	if (age<=data_buff_pos)
	    return ""+data_buff[data_buff_pos-age];
	else
	    return ""+data_buff[DATA_BUFF_LEN+data_buff_pos-age];
	

    }

    
    /******************  Section: private Methods ************************************************/
    private void connect()
    {
    	if(comm_adapter==null) 
    		{
    		log("trying to connect without communication adapter");
    		return; // makes no sense without a communication adapter
    		}
    	
    	comm_adapter.connect();
    	log("trying to connect to" + mk_url);
	
    	try{
    	   String magic="conn:foo bar\r\n";
    	   comm_adapter.write(magic.getBytes());
    	   comm_adapter.flush();
    	   connection_start_time=System.currentTimeMillis();
    	   connected=true; // if we get here everything seems to be OK
    	   stats.reset();
    	   log("connecting OK");
    	}
    	catch (Exception ex) 
    	{
    	   // TODO difference fatal errors from those which will lead to reconnection
    	   log("Problem connecting" + "\n" + ex);
    	}	
    }
    
    private int[] Decode64(byte[] in_arr, int offset,int len) 
    {
	int ptrIn=offset;	
	int a,b,c,d,x,y,z;
	int ptr=0;
	
	int[] out_arr=new int[len];

	while(len!=0)
	    {
		a=0;
		b=0;
		c=0;
		d=0;
		try {
		a = in_arr[ptrIn++] - '=';
		b = in_arr[ptrIn++] - '=';
		c = in_arr[ptrIn++] - '=';
		d = in_arr[ptrIn++] - '=';
		}
		catch (Exception e) {}
		//if(ptrIn > max - 2) break;     // nicht mehr Daten verarbeiten, als empfangen wurden

		x = (a << 2) | (b >> 4);
		y = ((b & 0x0f) << 4) | (c >> 2);
		z = ((c & 0x03) << 6) | d;

		if((len--)!=0) out_arr[ptr++] = x; else break;
		if((len--)!=0) out_arr[ptr++] = y; else break;
		if((len--)!=0) out_arr[ptr++] = z; else break;
	    }
	
	return out_arr;

    }



    public void wait4send()
    {
	while(sending) //||recieving)
	    sleep(51);
    }


    public void sleep(int time)
    {
	try { Thread.sleep(time); }
	catch (Exception e)  {   }
    }

    // FC - Function Mappers

    // send a version Request to the FC - the reply to this request will be processed in process_data when it arrives
    public void get_version()
    {
	stats.version_data_request_count++;
	send_command(0,'v');
    }

    // TODO FIxme
    public void set_gps_target(int longitude,int latitude)
    {
	int[] target=new int[8];
	target[0]= (0xFF)&(longitude<<24);
	target[1]= (0xFF)&(longitude<<16);
	target[2]= (0xFF)&(longitude<<8);
	target[3]= (0xFF)&(longitude);
	//	send_command(0,'s',target);
    }

    public void add_gps_wp(int status,int index,int longitude,int latitude,int hold_time)
    {
	int[] waypoint_struct=new int[30];
	waypoint_struct[0]= (0xFF)&(longitude<<24);
	waypoint_struct[1]= (0xFF)&(longitude<<16);
	waypoint_struct[2]= (0xFF)&(longitude<<8);
	waypoint_struct[3]= (0xFF)&(longitude);
	
	waypoint_struct[4]= (0xFF)&(latitude<<24);
	waypoint_struct[5]= (0xFF)&(latitude<<16);
	waypoint_struct[6]= (0xFF)&(latitude<<8);
	waypoint_struct[7]= (0xFF)&(latitude);
	
	// alt
	waypoint_struct[8]= (0xFF)&(0);
	waypoint_struct[9]= (0xFF)&(0);
	waypoint_struct[10]= (0xFF)&(0);
	waypoint_struct[11]= (0xFF)&(0);
	
	// status
	waypoint_struct[12]= (0xFF)&(status);
	
	// heading
	waypoint_struct[13]= (0xFF)&(0);
	waypoint_struct[14]= (0xFF)&(0);
	
	// tolerance
	waypoint_struct[15]= (0xFF)&(0);
	
	// holdtime
	waypoint_struct[16]= (0xFF)&(hold_time);
	
	// event flag
	waypoint_struct[17]= (0xFF)&(0);
	
	//index
	waypoint_struct[18]= (0xFF)&(index);
	
	
	// 11 reserved
	
	send_command(NAVI_SLAVE_ADDR,'w',waypoint_struct);
    }

    /** 
     * send a MotorTest request 
     * @param array of intswith the speed for each Motor 
    **/
    public void motor_test(int[] params)
    	{
    	stats.motortest_request_count++;
		send_command(FC_SLAVE_ADDR,'t',params);
    	}

    public void set_mixer_table(int[] params)
    	{	
    	send_command(FC_SLAVE_ADDR,'m',params);
    	}


    public void send_follow_me(int time,long lon,long lat)
    	{
    	long alt=0;

    	int[] params=new int[29];

    	params[0]=(int)((lon)&0xFF) ;
    	params[1]=(int)((lon>>8)&0xFF) ;
    	params[2]=(int)((lon>>16)&0xFF) ;
    	params[3]=(int)((lon>>24)&0xFF) 	;

    	params[4]=(int)((lat)&0xFF) ;
    	params[5]=(int)((lat>>8)&0xFF) ;
    	params[6]=(int)((lat>>16)&0xFF) ;
    	params[7]=(int)((lat>>24)&0xFF) ;

    	params[8]=(int)((alt)&0xFF );
    	params[9]=(int)((alt>>8)&0xFF );
    	params[10]=(int)((alt>>16)&0xFF); 
    	params[11]=(int)((alt>>24)&0xFF );

    	params[12]=2;  // newdata

    	params[13]=0;  // heading
    	params[14]=0;  // heading
	
    	params[15]=1;  // tolerance

    	params[16]=time;  // time
    	params[17]=0;  // event

    	send_command(NAVI_SLAVE_ADDR,'s',params);
    }


    /**
     * sends the array extern_control as extern_control data
     */
    public void send_extern_control()    {
    	stats.external_control_request_count++;
    	send_command(FC_SLAVE_ADDR,'b',extern_control);
    }

    /*    public void send_keys(int[] params)
	  {
	  send_command(FC_SLAVE_ADDR,'k',params);
	  }*/
    
    // get params
    public void get_params(int id)    {
	wait4send();
	send_command(FC_SLAVE_ADDR,'q',id+1);
	stats.params_data_request_count++;
    }
    
    
    public void set_active_paramset(int id) {
    	wait4send();
    	send_command(FC_SLAVE_ADDR,'f',id);
    }

    /**
     * request a Debug Name ( names for analog Values )
     * 
     * @param id the Id of the Value
     */
    public void requestDebugName(int id) {
    	stats.debug_name_request_count++;
    	wait4send();
		send_command(0,'a',id);
    }

 
    public void trigger_LCD_by_page(int page) {
    	wait4send();
    	send_command(0,'l',page);
    	stats.lcd_data_request_count++;
    }

    public void trigger_debug()
    {
	if (sending||recieving) return; // its not that important - can be dropped
	send_command(0,'c');
    }


    public void switch_todo()
    {
	sleep(150);
	version.reset();
	//	LCD= new MKLCD(this);
	debug_data=new MKDebugData();

    }

    public void switch_to_fc()
    {
    	wait4send();
    	send_command(NAVI_SLAVE_ADDR,'u',0);
    	switch_todo();
    	switch_todo();

    }

    

    public void switch_to_mk3mag()
    {
    	wait4send();
    	send_command(NAVI_SLAVE_ADDR   ,'u',1);
    	switch_todo();
    }

    public final static byte[] navi_switch_magic={27,27,0x55,(byte)0xAA,0,(byte)'\r'};
    public void switch_to_navi()
    {
	wait4send();
	sending=true;
	try
	    {
		comm_adapter.write(navi_switch_magic);
		stats.bytes_out+=6;
		comm_adapter.flush();
	    }
	catch (Exception e)  {   }
	sending=false;
	
	switch_todo();

    }


    public void start_engines()
    {
	int[] start_cmd = { 1,0,0,0,0 };
	wait4send();
	send_command(FC_SLAVE_ADDR,'e',start_cmd);
    }
	int msg_pos=0;


    //    public boolean bootloader_intension_flash=false;

    //    public boolean bootloader_finish_ok=false;


    //    public void jump_bootloader()
    // {
	/*
	bootloader_finish_ok=false;
	msg_pos=0;
	bootloader_stage= BOOTLOADER_STAGE_NONE;
	//   flash_msgs=new String[100];
	//   flash_msgs[msg_pos++]="Initiializing Bootloader";
	wait4send();
	sending=true;
	
	try
	    {
		int attempt=0;

		while(bootloader_stage!= BOOTLOADER_STAGE_GOT_MKBL)
		    {
			flash_msgs[msg_pos]="attempt "+attempt;
			attempt++;
			send_command_nocheck((byte)FC_SLAVE_ADDR,'R',new int[0]);
			
			try{
			writer.write( 27);
			writer.flush();

			sleep(20);
			
			writer.write( 0xAA);
			writer.flush();
			}
			catch (Exception e)  { }
			sleep((attempt%2==0)?80:800); //800
		    }
		msg_pos++;
	    }

	catch (Exception e)  {   
		flash_msgs[msg_pos++]="Exception:" +e.getMessage() ;
		flash_msgs[msg_pos++]=e.toString() ;
	}

	new Thread( this ).start(); // fire up main Thread 
	*/
    //    }


    public void get_error_str()
    {
	send_command(NAVI_SLAVE_ADDR,'e');
    }

    public void trigger_rcdata()
    {
	send_command(FC_SLAVE_ADDR,'p');
    }


    public void trigger_mixer_read()
    {
    	send_command(FC_SLAVE_ADDR,'n');
    }

    public void write_params(int to) 
    {
	params.update_backup(to);
	write_params_(to) ;
    }

    public void write_params_(int to) 
    {
	wait4send();
	params.active_paramset=to;
	send_command(FC_SLAVE_ADDR,'s',params.field_bak[to]);
    
    }

    public void set_debug_interval(int interval)
    {
	send_command(2,'d',interval);
    }


    public void set_gpsosd_interval(int interval)
    {
	send_command(NAVI_SLAVE_ADDR,'o',interval);
    }

    public void send_command(int modul,char cmd)
    {
	send_command(modul,cmd,new int[0]);
    }


    
    /**
     * send a command with one int param
     * 
     * @param modul
     * @param cmd
     * @param param
     */
    public void send_command(int modul,char cmd,int param) {
    	int[] params=new int[1];
    	params[0]=param;
    	send_command(modul,cmd,params);
    }

    public void send_command_nocheck(byte modul,char cmd,int[] params)
    {
//	char[] send_buff=new char[5 + (params.length/3 + (params.length%3==0?0:1) )*4]; // 5=1*start_char+1*addr+1*cmd+2*crc

	byte[] send_buff=new byte[3 + (params.length/3 + (params.length%3==0?0:1) )*4]; // 5=1*start_char+1*addr+1*cmd+2*crc
	send_buff[0]='#';
	send_buff[1]=(byte)(modul+'a');
	send_buff[2]=(byte)cmd;
	
	for(int param_pos=0;param_pos<(params.length/3 + (params.length%3==0?0:1)) ;param_pos++)
	    {
		int a = (param_pos*3<params.length)?params[param_pos*3]:0;
		int b = ((param_pos*3+1)<params.length)?params[param_pos*3+1]:0;
		int c = ((param_pos*3+2)<params.length)?params[param_pos*3+2]:0;

		send_buff[3+param_pos*4] =  (byte)((a >> 2)+'=' );
		send_buff[3+param_pos*4+1] = (byte)('=' + (((a & 0x03) << 4) | ((b & 0xf0) >> 4)));
		send_buff[3+param_pos*4+2] = (byte)('=' + (((b & 0x0f) << 2) | ((c & 0xc0) >> 6)));
		send_buff[3+param_pos*4+3] = (byte)('=' + ( c & 0x3f));

		//send_buff[3+foo]='=';
	    }

	/*	for(int foo=0;foo<(params.length/3 + (params.length%3==0?0:1) )*4;foo++)
		{
		int a = (foo<params.length) params[foo];
		int a = params[foo];
		
		//send_buff[3+foo]='=';
		}
	*/
	try 
	    {
		int tmp_crc=0;
		for ( int tmp_i=0; tmp_i<send_buff.length;tmp_i++)
		    tmp_crc+=(int)send_buff[tmp_i];
			
		comm_adapter.write(send_buff,0,send_buff.length);
		tmp_crc%=4096;

		comm_adapter.write( (char)(tmp_crc/64 + '='));
		comm_adapter.write( (char)(tmp_crc%64 + '='));
		comm_adapter.write('\r');
		stats.bytes_out+=send_buff.length+3;
		comm_adapter.flush();
	    }
	catch (Exception e)
	    { // problem sending data to FC
	    }

    }
    // send command to FC ( add crc and pack into pseudo Base64
    public void send_command(int modul,char cmd,int[] params)
    {
	//	if (modul==0) return;
	sending=true;
	send_command_nocheck((byte)modul,cmd,params);
	sending=false;
    }





    public int UBatt()
    {

	switch (slave_addr)
	    {
	    case FC_SLAVE_ADDR:
	    case RIDDIM_SLAVE_ADDR:
		return debug_data.analog[9];

	    case NAVI_SLAVE_ADDR:
		return gps_position.UBatt;

	    case FOLLOWME_SLAVE_ADDR:
		return debug_data.analog[8];

	    case FAKE_SLAVE_ADDR:
		return 127;

		
	    default:
		return -1; // No Info
	    }

    }

    public int SatsInUse()
    {

	switch (slave_addr)
	    {
	    case NAVI_SLAVE_ADDR:
		return gps_position.SatsInUse;

	    case FOLLOWME_SLAVE_ADDR:
		return debug_data.analog[12];

	    case FAKE_SLAVE_ADDR:
		return 7;

	    default:
		return -1; // No Info
	    }

    }

    public int SenderOkay()
    {
	switch (slave_addr)
	    {
	    case FC_SLAVE_ADDR:
		return debug_data.analog[10];

	    case NAVI_SLAVE_ADDR:
		return gps_position.SenderOkay;

	    case FAKE_SLAVE_ADDR:
		return 200;

	    default:
		return -1; // No Info
	    }
    }


    public    int[][] debug_buff=null;
    public    int     debug_buff_off=0;
    public    int     debug_buff_len=0;
    public    int    debug_buff_interval=0;
    public    int    debug_buff_lastset=0;
    public    int    debug_buff_max=1;

    public    int[] debug_buff_targets=null;

    public void setup_debug_buff(int[] targets,int len,int interval)
    {
	debug_buff=new int[len][targets.length];

	debug_buff_off=0;
	debug_buff_len=len;

	debug_buff_interval=interval;
	if (debug_buff_interval<2)debug_buff_interval=2;
	debug_buff_targets=targets;
	debug_buff_max=1;
	debug_buff_lastset=0;
    }

    public int chg_debug_max(int val)
    {
	if (val>debug_buff_max)
	    debug_buff_max=val;
	if (-val>debug_buff_max)
	    debug_buff_max=-val;
	return val;
    }

    public void destroy_debug_buff()
    {
	debug_buff_targets=null;
    }

    public void update_debug_buff()
    {
	if (freeze_debug_buff) return;
	if (debug_buff_targets!=null)
	    {
		for (int sp=0;sp<debug_buff_targets.length;sp++)
		    debug_buff[debug_buff_off][sp]=chg_debug_max(debug_data.analog[debug_buff_targets[sp]]);
		if (debug_buff_off>debug_buff_lastset)
		    debug_buff_lastset=debug_buff_off;
		
		debug_buff_off=(debug_buff_off+1)%debug_buff_len;
	    }
    }

    public void process_data(byte[] data,int len)
    {

	// check crc
	int tmp_crc=0;
	for ( int i=0 ; i<len-2 ; i++)
	    tmp_crc+=(int)data[i];

	tmp_crc%=4096;
	
	if (!((data[len-2]==(char)(tmp_crc/64 + '='))
	    &&
	      (data[len-1]==(char)(tmp_crc%64 + '='))))
	    {
		stats.crc_fail++;
		return;
	    }
	// end of c

	//	slave_addr=data[1];
	log("command " +(char)data[2]   + "len " + len);		


	int[] decoded_data=Decode64(data,3,len-5);
	log("decoded");		
	switch((char)data[2])
	    {

	    case 'A': // debug Data Names
	    	stats.debug_names_count++;
	    	Log.i("got debug label" + decoded_data[0]);
	    	debug_data.set_names_by_mk_data(decoded_data);
		break;

	    case 'B': // external_control confirm frames
	    	stats.external_control_confirm_frame_count++;
		break;

	    case 'L': // LCD Data
	    	stats.lcd_data_count++;
	    	LCD.handle_lcd_data(decoded_data);

		break;
	    
	    
	    case 'N': // debug Data
	    	mixer_manager.setByMKData(decoded_data);
	    	break;
	    	
	    case 'D': // debug Data
	    	log("got debug data");
	    	stats.debug_data_count++;
	    	debug_data.set_by_mk_data(decoded_data,version);

	    	if (is_mk())
		    	{
	    		stats.process_mkflags(debug_data.motor_val(0)); // TODO remove dirty hack
	    		stats.process_alt(getAlt());
		    	}
	    	update_debug_buff();
	    	log("processed debug data");
	    	
		break;
		
	    case 'V': // Version Info
	    	stats.version_data_count++;
	    	

	    	version.set_by_mk_data(decoded_data);
	    	
	    	if (slave_addr!=data[1]-'a') //new slave address
		    {
	    		slave_addr=(byte)(data[1]-'a');
	    		//change_notify=true;
	    		notifyAll(DUBwiseNotificationListenerInterface.NOTIFY_CONNECTION_CHANGED);
		    }

		/*
		switch(slave_addr)
		    {
		    case FC_SLAVE_ADDR:
			ufo_prober.set_to_mk();
			break;

		    case NAVI_SLAVE_ADDR:
			ufo_prober.set_to_navi();
			break;

		    case MK3MAG_SLAVE_ADDR:
			ufo_prober.set_to_mk3mag();
			break;

		    case RE_SLAVE_ADDR:
			ufo_prober.set_to_rangeextender();
			break;
		    default:
			ufo_prober.set_to_incompatible();
			break;
		    }
		*/

		
		break;

	    case 'w':
	    	log("processing angles");		
		angle_nick=MKHelper.parse_signed_int_2(decoded_data[0],decoded_data[1]);
	        angle_roll=MKHelper.parse_signed_int_2(decoded_data[2],decoded_data[3]);
	        log("done processing angles");		
		stats.angle_data_count++;
		break;


	    case 'Q':
		if (is_mk())
		    {
			stats.params_data_count++;
			params.set_by_mk_data(decoded_data);
		    }
		break;

	    case 'M':
		mixer_change_notify=true;
		mixer_change_success=(decoded_data[0]==1);
		break;
	    case 'P':
	    	stats.stick_data_count++;
	    	stick_data.set_by_mk_data(decoded_data);
	    	break;


        case 'E':  // Error Str from Navi
        	error_str="";
        	for(int foo=0;foo<20;foo++)
        		if (decoded_data[foo]!=0) 
        			error_str+=(char)decoded_data[foo];
		break;

		
	    case 'O': // OSD Values Str from Navi
	    	stats.navi_data_count++;
	    	log("got navi data(" + len +"):");
		
	    
	    	gps_position.set_by_mk_data(decoded_data,version);

	    	stats.process_mkflags(gps_position.FCFlags);
	    	stats.process_compas(gps_position.CompasHeading);
	    	stats.process_speed(gps_position.GroundSpeed);
	    	stats.process_alt(getAlt());

		break;


	    default:
	    	stats.other_data_count++;
		break;

	    }
	
	log("command processing done");		
    }

    

    public boolean force_disconnect=true;

    public void close_connections(boolean force)
    {

    	//	if ((!force)&&root.canvas.do_vibra) root.vibrate(500);
    	force_disconnect|=force;

    	if (comm_adapter!=null) 
    		comm_adapter.disconnect();
	
    	slave_addr=-1;
    	//	ufo_prober.set_to_none();
    	stats.reset(); 
    	connected=false;
    	version.reset();
    	//	if (bootloader_stage==BOOTLOADER_STAGE_NONE)
    	//disconnect_notify=true;
    	
    	notifyAll(DUBwiseNotificationListenerInterface.NOTIFY_DISCONNECT);
    }


    
    // Thread to recieve data from Connection
    public void run()
    {
	//	boolean sigfail=false;
	/*	if (bootloader_stage==BOOTLOADER_STAGE_GOT_MKBL)
	    {
	    try {

			//			if (send_buff_size>128)
			//    send_buff_size=128;

			//			if (!bootloader_intension
			if (bootloader_intension_flash)
			    {		
				
				byte[] flash_buff =new byte[send_buff_size]; ///!!
				
				String firmware_filename=(avr_sig==224)?"/navi.bin":((avr_sig==120)?"/mk3.bin":"/fc.bin");
				flash_msgs[msg_pos++]="Opening firmware " + firmware_filename + "..";
				
				
				InputStream in;
				try {
				    in=this.getClass().getResourceAsStream(firmware_filename);	    
				}
				
				catch (Exception e) { 		    throw new Exception(" .. cant open firmware");			}
				int firmware_size=-1;
				try {
								    
				firmware_size= ((int)in.read()<<24) |((int)in.read()<<16) | ((int)in.read()<<8) | ((int)in.read()&0xff) ;
				}
				catch (Exception e) { 		    throw new Exception(" .. cant read size");			}
				
				int blocks2write=((firmware_size/send_buff_size)); 
				flash_msgs[msg_pos++]=".. open("+blocks2write+" blocks," + firmware_size + "bytes)";
				
				
				//			if (true) throw new Exception("before erasing");
				
				//	if (true) throw new Exception("before erasing" );		
				
				flash_msgs[msg_pos++]="Erasing Flash ..";
				writer.write('e'); 
				writer.flush();
				
				if (reader.read()!=0x0d)
				    throw new Exception("cant erase flash");
				
				flash_msgs[msg_pos]+="OK";
				
				
				writer.write('A'); 
				writer.write(0); 
				writer.write(0); 
				writer.flush();
				
				if (reader.read()!=0x0d)
				    throw new Exception("cant set addr");
				
				flash_msgs[msg_pos++]="addr set";
				
				
				//			int blocks2write=((firmware_size/send_buff_size));
				if ((firmware_size%send_buff_size)>0)
				    blocks2write++;
				
				for ( int block=0; block<blocks2write; block ++)
				    {
					int hex_bytes_read=in.read(flash_buff,0,send_buff_size);
					
					flash_msgs[msg_pos]="bl:" + block + "/" + blocks2write + " si:"+hex_bytes_read ;
					
					
					writer.write('B'); 
					writer.write((hex_bytes_read>>8)& 0xFF); 
					writer.write((hex_bytes_read)& 0xFF); 
					writer.write('F'); 
					writer.flush();
					
					
					writer.write(flash_buff,0,hex_bytes_read);
					writer.flush(); 				
					
					
					if (avr_sig==224)
					    {
						int crc=0xFFFF;
						for (int crc_pos=0;crc_pos<hex_bytes_read;crc_pos++)
						    crc=CRC16(flash_buff[crc_pos],crc);
						writer.write(crc>>8); 
						writer.write(crc&0xff); 
						writer.flush(); 
					    }
					//  flash_msgs[msg_pos]+="ok";
					//				writer.flush();
					
					
					
					if (reader.read()!=0x0d)
					    throw new Exception("abort write at block"+block);
					
					
					
					//			       sleep(1000);
				    }


	*/
				//		flash_msgs[msg_pos]="bl:" + block + "/" + blocks2write + " si:"+hex_bytes_read ;
				/*
				  
			int inp=0;
			int block=0;
			while (inp!=-1)
			    {
				int flash_buff_pos=0;
				int crc=0xFFFF;
				
				while ((flash_buff_pos<send_buff_size)&&(inp!=-1))
				    {
					inp=in.read();
					if (inp!=-1)
					    {
						crc=CRC16(inp,crc);
						flash_buff[flash_buff_pos++]=(byte)inp;
					    }
				    }
				//				flash_msgs[msg_pos]="block" + block + "size:"+flash_buff_pos;
				
				block++;        
			
				boolean block_fin=false;


				while(!block_fin)
				    {
					
					writer.write('B'); 
					writer.write((flash_buff_pos>>8)& 0xFF); 
					writer.write((flash_buff_pos)& 0xFF); 
					writer.write('F'); 
					writer.flush();

					//					int ret_v=-1;
					
					writer.write(flash_buff,0,flash_buff_pos);
					flash_msgs[msg_pos]="bl:" + block + "si:"+flash_buff_pos ;
					
					writer.flush(); 				
					//				    flash_msgs[msg_pos]+="wtc";
					
					
					// append crc if navi
					if (avr_sig==224)
					    {
						writer.write(crc>>8); 
						writer.write(crc&0xff); 
						writer.flush(); 
					    }
					//  flash_msgs[msg_pos]+="ok";
					//				writer.flush();
					// 			if (reader.read()!=0x0d)
					//				    throw new Exception("abort write at block"+block);
					
					
					//ret_v=reader.read();
					//				    flash_msgs[msg_pos]="ret"+ret_v + "crc"+crc;
					
					if (reader.read()==0x0d)
					    block_fin=true;
					
				    }

			    }
			*/



	/**
			flash_msgs[++msg_pos]="written last block ";
			msg_pos++;
			flash_buff=null;

			ufo_prober.set_to_none();
			stats.reset();
			version=new MKVersion();
			System.gc();
		    }
		else // bootloader intension clear settings
		    {

			flash_msgs[msg_pos]="reset params ..";
			writer.write('B'); 
			writer.write(0); 
			writer.write(4);
			writer.write('E'); 
			writer.flush();
			
			writer.write(0xFF); 
			writer.write(0xFF); 
			writer.write(0xFF); 
			writer.write(0xFF); 
			writer.flush();
			flash_msgs[msg_pos++]+=" done";
		    }

	    flash_msgs[msg_pos++]="Exiting Bootloader" ;
	    params=new MKParamsParser();
	    try{
		writer.write('E'); 
		writer.flush();
	    }
	    catch (Exception e)  {   
		flash_msgs[msg_pos++]="cant exit bootloader" ;
	    }
	    flash_msgs[msg_pos++]="Exit BL done" ;	    

	    bootloader_finish_ok=true;
	    }
	    
	    catch (Exception e)  {   
		flash_msgs[msg_pos++]="Fail:" +e.getMessage() ;


	    flash_msgs[msg_pos++]="Exiting Bootloader" ;
	    params=new MKParamsParser();
	    try{
		writer.write('E'); 
		writer.flush();
	    }
	    catch (Exception e2)  {   
		flash_msgs[msg_pos++]="cant exit bootloader" ;
	    }
	    flash_msgs[msg_pos++]="Exit BL done" ;
	    if (sigfail&&(bl_retrys<3))
		{
		    bl_retrys++;
		    init_bootloader=true;
		}
	    close_connections(false);
	    }


	    sending=false;
	    }

	**/

	byte[] data_set=new byte[1024];
	int data_set_pos=0;

	byte[] data_in_buff=new byte[DATA_IN_BUFF_SIZE];
	
	int input;
	int pos=0;

	log("Thread started");
	while(thread_running)
	    {

		
		if (!connected) 
		    {
			sleep(10);
		
			if (!force_disconnect) connect();
			log ("not connected - forced:" + force_disconnect);
		    }
		else  if (slave_addr==FAKE_SLAVE_ADDR)
		    {
			debug_data.set_fake_data();
			update_debug_buff();
			stats.debug_data_count++;
			sleep(50);
		    }
	else
			try{
			
			/*		
				while(sending)
				{try { Thread.sleep(50); }
				catch (Exception e)  {   }
				}
			*/
			recieving=true;
			int read_count ;

			if (comm_adapter.available()<DATA_IN_BUFF_SIZE)
			    read_count     =comm_adapter.read(data_in_buff,0,comm_adapter.available());
			else
			    read_count     =comm_adapter.read(data_in_buff,0,DATA_IN_BUFF_SIZE);

			//			log("Connected - reading data " + read_count);		
			//	pos=0;
			input=0;
			//data_buff[data_buff_pos]="";
			// recieve data-set

			//			int read_count =reader.read(data_in_buff,0,reader.available());
			stats.bytes_in+=read_count;
			if (read_count>0)
			    {
				log("read" + read_count + " ds_pos" + data_set_pos);		
			
				for ( pos=0;pos<read_count;pos++)
				    {
				    //data_in_buff[pos]+=127;
				    log("" +data_in_buff[pos] + "->" + (char)data_in_buff[pos]);
					if ((data_in_buff[pos]==13)||(data_in_buff[pos]==10))
					    {
						data_buff[data_buff_pos]=new String(data_set, 0, data_set_pos);
						data_buff_pos++;
						data_buff_pos%=DATA_BUFF_LEN;


						
						try{
							
							if (data_set_pos>3) process_data(data_set,data_set_pos); 
							
						
						}
						catch (Exception e) 
						    { 			
							log(".. problem processing"); 
							log(e.toString()); 
							}




						proxy.write(data_set,0,data_set_pos);
						//							proxy.writer.write('\r');
						//proxy.writer.write('\n');
						//proxy.writer.flush();
						/*
						if (proxy!=null)
						    {
							


						    }
						*/
						data_set_pos=0;

					    }
					else
					    // {
						data_set[data_set_pos++]=data_in_buff[pos];
						
						/*

						if ( (data_set_pos>4) && (data_set[data_set_pos-4]==(byte)'M') && (data_set[data_set_pos-3]==(byte)'K')  && (data_set[data_set_pos-2]==(byte)'B') && (data_set[data_set_pos-1]==(byte)'L'))

						    {

							bootloader_stage= BOOTLOADER_STAGE_GOT_MKBL;
							return;
						    }
						
						    }*/

				    }
					
			
			    }
			else
			    {
				recieving=false;
				sleep(21); 
			    }
			/*
			while ((input != 13)) //&&(input!=-1))
			    {
				{
				    //log("pre read");		
				    log(""+reader.available());
				    input = reader.read() ; 
				    log("Byte rcv" + input +"pos"+ pos);		
				    
				    proxy.write(input);
				    
				    data_buff[data_buff_pos]+=(char)input;
				    
				    if ((data_buff[data_buff_pos].length()>3)&&(data_buff[data_buff_pos].substring(data_buff[data_buff_pos].length()-4,data_buff[data_buff_pos].length()).equals("MKBL")))
					{
					    bootloader_stage= BOOTLOADER_STAGE_GOT_MKBL;
					    return;
					}
				    if (input==-1) throw new Exception("disconnect");
				    else 
					{
					    stats.bytes_in++;
					    data_set[pos]=input;
					    pos++; 
					}
				}
				
			    }
			
			

			data_buff_pos++;
			data_buff_pos%=DATA_BUFF_LEN;
			recieving=false;
			log("Data recieved (" + pos + "Bytes)");		
			log("processing ..");		
			*/

			/*
			  if (proxy!=null)
			  {
			  proxy.writer.write('\r');
			  proxy.writer.write('\n');
			  proxy.writer.flush();
			  }
			*/
			/*if (pos>5) 
			    {
				try{process_data(data_set,pos); }
				catch (Exception e) 
				    { 			
					log(".. problem processing"); 
					log(e.toString()); 
				    }
				
				log(".. processing done");		
			    }
			*/
		    }
		    catch (Exception ex) 
			{
			    log("Problem reading from MK -> closing conn");
			    log(ex.toString());
			    // close the connection 
			    close_connections(false);
			}	
		
		// sleep a bit to  get someting more done
		//		sleep(5); //50
		
	    } // while
	//	log("Leaving Communicator thread");

    } // run()

    public int getPotiValue(int poti_id)
    {
    	int val=stick_data.stick[params.poti_pos[poti_id]]+127;
    	
    	// clip the values
    	if (val<0)
    		return 0;
    	if (val>250)
    		return 250;
    	return val;
    }

    /**
     * Set the Communication Adapter - this is needed to differ between 
     * android and J2ME without preprocessing
     * 
     * @param _comm_adapter - the communication adapter to use
     */
    public void setCommunicationAdapter(CommunicationAdapterInterface _comm_adapter) {
		comm_adapter=_comm_adapter;
	}
    
    public CommunicationAdapterInterface getCommunicationAdapter() {
    	return comm_adapter;
    }
	
    
    public String getNaviErrorString() {
    	return error_str;
    }
    
    public boolean hasNaviError() {
    	return (gps_position.ErrorCode!=0);
    }
    
    public boolean isConnected() {
    	return ((comm_adapter!=null)&&connected);
    }

    public void log(String str)
    {
//#ifdef android
    	//if (do_log)	Log.d("MK-Comm",str);
//#endif
//	canvas.debug.log(str);
	//	System.out.println(str);
    }

    /**
     * @return the time in seconds we are connected 
     */
    public int getConnectionTime()
    {
	if (connected)
	    return (int)((System.currentTimeMillis()-connection_start_time)/1000);
	else
	    return 0;
    }
    
    /**
     * @return if the UFO is flying
     */
    public boolean isFlying() {
    	// TODO read from flags
    	return stats.flying_time()>0;
    }
}