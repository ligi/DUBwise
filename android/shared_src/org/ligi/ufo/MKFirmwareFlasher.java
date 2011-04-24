/*************************************************************************
 *                                          
 * Class to Flash firmwares for FC ; NC ; MK3MAG
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
import java.io.*;
import java.util.Random;

import org.ligi.java.io.CommunicationAdapterInterface;



public class MKFirmwareFlasher
    implements Runnable
{


    boolean sigfail;
    //    int last_avr_sig;

    public int avr_sig=0;

    public String[] flash_msgs;
    public int msg_pos=0;
    public int bootloader_init_attempt;

    public InputStream in;

    private byte[] bl_magic={(byte)'M',(byte)'K',(byte)'B',(byte)'L'};
    private byte bl_magic_pos=0;
    private boolean got_bl_magic=false;

    private int attempt=0;
    byte intension=0;
    public boolean success=false;
    public boolean job_done=false;
    public boolean started=false;

    private CommunicationAdapterInterface comm_adapter;
    	
    public MKFirmwareFlasher(CommunicationAdapterInterface comm_adapter,byte _intension)
    {
    	this.comm_adapter=comm_adapter;
    	intension=_intension;
	
    	flash_msgs=new String[100];
    	log("loading BL Handler");
    }


    public void start()
    {
	started=true;
    	connect();
	new Thread( this ).start(); // fire up main Thread 
    }

    public void sleep(int time)
    {
	try { Thread.sleep(time); }
	catch (Exception e)  {   }
    }

    private void log(String msg)
    {
	flash_msgs[msg_pos++]=msg;
    }

    private void log_(String msg)
    {
	flash_msgs[msg_pos]=msg;
    }


    
    public void connect()
    {
    }

    public void close_connections()
    {

    }

   



    public void run()
    {
	boolean init_sequence=true;	
	int send_buff_size=0;
	int gbl=0;
	int gbytes=0;
	log_("attempt:"+attempt);
	String got_str="";
	Random random = new Random(); // for fake conn
	while (!got_bl_magic)
	    {
		attempt++;
		
		try{
		    if (init_sequence)
			{
			    //			    if (attempt!=0)
			    //sleep((attempt%2==0)?80:800); //800
		
		    	if (attempt%10==9)
		    	{
					exit_bootloader(false);
//		    	Log.i("DUBwise","resetting bl");
		    	}
			    msg_pos=0;
			    flash_msgs[msg_pos++]=("mp:" + bl_magic_pos +"at:"+attempt+" gbl:"+gbl + " \n " + bl_magic_pos + " att" + attempt + "gby" + gbytes + "str" + got_str)  ;
			    got_str="";
			    flash_msgs[msg_pos]=null;
			    
			    if ((attempt%2)==0) // after work
				{
			    	
			    	comm_adapter.write('#');
				    comm_adapter.write('c');
				    comm_adapter.write('R');
			    
				    // CRC
				    comm_adapter.write((char)( (((byte)'#' +(byte)'c' + (byte)'R')%4096)/64 + '='));
				    comm_adapter.write((char)( (((byte)'#' +(byte)'c' + (byte)'R')%4096)%64 + '='));
				    
				    comm_adapter.write('\r');
			    
				    comm_adapter.flush();
				   sleep(random.nextInt()%50+300);
				}
			    
			    comm_adapter.write( 27);
			    comm_adapter.flush();
			    
			    //sleep(20); // orig
			    //sleep(2); 
			    sleep(random.nextInt()%100+200);
			    
			    comm_adapter.write( 0xAA);
			    comm_adapter.flush();
			   // sleep(180); // orig
			    //sleep(18); 
			    sleep(random.nextInt()%100+800);
			}
		    //		    else
		    //			sleep(20);
		    init_sequence=true;
		    //		    sleep(20);
		    //Log.i("DUBwise", "read ing from comm adapter");
		    //Log.i("DUBwise", "read " + comm_adapter.read());
		    while ( comm_adapter.available() > 0 )
			{
		    	int last_read=comm_adapter.read();
		    	got_str+=(char)last_read;
	
	//	    	Log.i("DUBwise", "got char" + last_read + "  " + (char)last_read);
		    	if ((byte)last_read==bl_magic[bl_magic_pos])
				{
		    		gbytes++;
		    		bl_magic_pos++;
				    if (bl_magic_pos==bl_magic.length)
					{
					    log("got bl magic");
					    got_bl_magic=true;
					    gbl++;
					    bl_magic_pos=0;

					    int foo,leech_cnt=0;
					    while (( comm_adapter.available() != 0)&&(leech_cnt<1000))
						{
						    leech_cnt++;
						    foo=comm_adapter.read();
						}

					}

				    
				}
			    else
				bl_magic_pos=0;
			}
		    

		    
		    if (got_bl_magic)
			{
			    //    sleep(50);
			    flash_msgs[msg_pos++]="reading avr_sig";
			    
			    comm_adapter.write( 't');
			    comm_adapter.flush();

			    sleep(120);
			    
			    //			    if ( comm_adapter.available() == 0 )
			    //throw new Exception("cant read avrsig");

			    /*
			    int sig1_timeout=0;
			    while ( comm_adapter.available() == 0)
				{ 
				    
				    if ((sig1_timeout++)>1000 ) 		    
					throw new Exception("cant read avrsig");
				    sleep(10);
				}

			    */
			    avr_sig=comm_adapter.read();

			    if (avr_sig==63)
				init_sequence=false;
			    /*    while (avr_sig==63)
			        avr_sig=comm_adapter.read();
			    */
			    flash_msgs[msg_pos++]="got avr sig " + avr_sig;
		
			    //			    last_avr_sig=avr_sig;
			    //			    for(int sig2_timeout=0;sig2_timeout<100;sig2_timeout++)
			    /*
			    int sig2_timeout=0;
			    while ( comm_adapter.available() == 0)
				{ 
				    
				    if ((sig2_timeout++)>1000 ) 		    
					throw new Exception("cant read avrsig-val2");
				    sleep(10);
				}*/

			    int avrsig_suff=comm_adapter.read();


			    if (avrsig_suff!=0)
				throw new Exception("val after avrsig is" +avrsig_suff +"should b 0");
			    
			    if ((avr_sig!=0x74)&&(avr_sig!=224)&&(avr_sig!=120))
				{
				    sigfail=true;
				    throw new Exception("avr sig" + avr_sig + " unknown");
				}
    

			}
		    
		    
		}
		catch (Exception e)  { 


		    log("" + e.getMessage());
		    sleep(1000);
		    exit_bootloader();
		    connect();
		    got_bl_magic=false;
		    init_sequence=false; // after work
		}
		
	    }

    
	
	log("have bl-magic & good avrsig" + avr_sig);

	    try{
		
		comm_adapter.write('T'); 
		//		comm_adapter.flush();
		comm_adapter.write(avr_sig);   // set devicetyp = 0x74 oder 0x76  
		comm_adapter.flush();
		
		if (comm_adapter.read()!=0x0d)
		    throw new Exception("cant get buffer size");
		
		comm_adapter.write('V'); 
		comm_adapter.flush();
		
		int bl_version_major=comm_adapter.read();
		int bl_version_minor=comm_adapter.read();
		
		flash_msgs[msg_pos++]="BL Version " + bl_version_major+"."+bl_version_minor;
		
		
		comm_adapter.write('b'); 
		comm_adapter.flush();
		
		if (comm_adapter.read()!='Y')
		    throw new Exception("cant get buffer size");
		
		
		send_buff_size=comm_adapter.read()*0x100;
		send_buff_size+=comm_adapter.read();

		
		flash_msgs[msg_pos++]="BUFF Size:" + send_buff_size;
		
	    }
	    catch (Exception e2)  { 
		exit_bootloader();
		return;
	    }
	
	    //	    	    if (send_buff_size>128)
	    //	    send_buff_size=128;
	    switch (intension)
		{
		case MKFirmwareHelper.BOOTLOADER_INTENSION_RESET_PARAMS:
		    try{

			flash_msgs[msg_pos]="reset params ..";
			comm_adapter.write( MKFirmwareHelper.cmd_reset_params);

			comm_adapter.flush();
			flash_msgs[msg_pos++]+=" done";
			success=true;
		    }
		    catch (Exception e2)  {    }	
		    
		    break;
		    
		case MKFirmwareHelper.BOOTLOADER_INTENSION_FLASH_FIRMWARE:
		    try{



		    byte[] flash_buff =new byte[send_buff_size]; ///!!
				
		    //String firmware_filename=(avr_sig==224)?"/navi.bin":((avr_sig==120)?"/mk3.bin":"/fc.bin");
		    //flash_msgs[msg_pos++]="Opening firmware " + firmware_filename + "..";
				
				
		    //InputStream in;
		    //try {
		    //in=this.getClass().getResourceAsStream(firmware_filename);	    
		    //	}
				
		    //	catch (Exception e) { 		    throw new Exception(" .. cant open firmware");			}
		    
		    int firmware_size=-1;
		    
		    try {
			firmware_size= ((int)in.read()<<24) |((int)in.read()<<16) | ((int)in.read()<<8) | ((int)in.read()&0xff) ;
		    }
		    catch (Exception e)
			{ 
			    throw new Exception(" .. cant read size");			
			}
		     

		    //		    in.mark(firmware_size+10);
		    int blocks2write=((firmware_size/send_buff_size))+(((firmware_size%send_buff_size)==0)?0:1); 
		    flash_msgs[msg_pos++]=".. open("+blocks2write+" blocks," + firmware_size + "bytes)";
	    
	    
		    //			if (true) throw new Exception("before erasing");
	    
		    //	if (true) throw new Exception("before erasing" );		
		    
		    flash_msgs[msg_pos++]="Erasing Flash ..";
		    comm_adapter.write('e'); 
		    comm_adapter.flush();
		    
		    if (comm_adapter.read()!=0x0d)
			throw new Exception("cant erase flash");
		    
		    flash_msgs[msg_pos]+="OK";
		    
		    
		    comm_adapter.write('A'); 
		    comm_adapter.write(0); 
		    comm_adapter.write(0); 
		    comm_adapter.flush();
		    
		    if (comm_adapter.read()!=0x0d)
			throw new Exception("cant set addr");
		    
		    flash_msgs[msg_pos++]="addr set";
		    
		    
		    //			int blocks2write=((firmware_size/send_buff_size));

		    
		    for ( int block=0; block<blocks2write; block ++)
			{
			    			
			    for (int s=0;s<send_buff_size;s++)
				flash_buff[s]=0;
			    int hex_bytes_read=in.read(flash_buff,0,send_buff_size);

			/*	
			    if (avr_sig==224)
			    {
		

				    for (int s=hex_bytes_read;s<send_buff_size;s++)
					flash_buff[s]=0;
				    hex_bytes_read=send_buff_size; // fix
				}

			*/


			    hex_bytes_read=send_buff_size; // fix

			    flash_msgs[msg_pos]="bl:" + (block+1) + "/" + blocks2write + " si:"+hex_bytes_read ;
			    		    
			    comm_adapter.write('B'); 
			    comm_adapter.write((hex_bytes_read>>8)& 0xFF); 
			    comm_adapter.write((hex_bytes_read)& 0xFF); 
			    comm_adapter.write('F'); 
			    comm_adapter.flush();
			    
			    
			    comm_adapter.write(flash_buff,0,hex_bytes_read);
			    comm_adapter.flush(); 				
			    
			    
			    if (avr_sig==224)
				{
				    int crc=0xFFFF;
				    for (int crc_pos=0;crc_pos<hex_bytes_read;crc_pos++)
					crc=MKFirmwareHelper.CRC16(flash_buff[crc_pos],crc);
				    comm_adapter.write((crc>>8)&0xff); 
				    comm_adapter.write(crc&0xff); 
				    comm_adapter.flush(); 
				}
			    //  flash_msgs[msg_pos]+="ok";
			    //				comm_adapter.flush();
			    
			    
			    
			    if (comm_adapter.read()!=0x0d)
				throw new Exception("abort write at block"+block);
			    
			    
			    
			    //			       sleep(1000);
			} // for block
		    

		    sleep(1000);
		    
		    
		    
		    success=true;
		    }
		    catch (Exception e2)  {  
		    msg_pos++;}	

		    break;
		}

		
	    sleep(50);
	    exit_bootloader();
	    job_done=true;

    }



    public void exit_bootloader(boolean close_conn)
    {
	try{
	    comm_adapter.write('E'); 
	    comm_adapter.flush();
	}
	catch (Exception e)  {   
	    flash_msgs[msg_pos++]="cant exit bootloader" ;
	}
	flash_msgs[msg_pos++]="Exit BL done" ;	    
	


	if (close_conn) 
		close_connections();
    }


    public void exit_bootloader()
    {
	try{
	    comm_adapter.write('E'); 
	    comm_adapter.flush();
	}
	catch (Exception e)  {   
	    flash_msgs[msg_pos++]="cant exit bootloader" ;
	}
	flash_msgs[msg_pos++]="Exit BL done" ;	    
	


	close_connections();
    }

    
    public String getCompleteLog() {
    	String res="";
    	for (int i=0;i<msg_pos;i++)
    		res+=flash_msgs[i];
    	return res;
    }

    public void setCommunicationAdapter(CommunicationAdapterInterface new_adapter) {
    	this.comm_adapter=new_adapter;
    }
    
}
