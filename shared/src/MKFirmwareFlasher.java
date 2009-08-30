/*******************************************
 *                                          
 * Handling of MK LCD                       
 *                                          
 * Author:        Marcus -LiGi- Bueschleb   
 * see README for further Infos
 *
 *
 *******************************************/

package org.ligi.ufo;
import java.io.*;

//#ifdef j2me
//# import javax.microedition.io.*;
//#endif

public class MKFirmwareFlasher
    implements Runnable
{

    public String[] flash_msgs;
    public int msg_pos=0;
    public int bootloader_init_attempt;


    private InputStream	reader;    
    private OutputStream writer;    

    private StreamConnection connection;

    private byte[] bl_magic={(byte)'M',(byte)'K',(byte)'B',(byte)'L'};
    private byte bl_magic_pos=0;
    private boolean got_bl_magic=false;

    private int attempt=0;


    byte intension=0;


    public boolean success=false;
    public boolean job_done=false;
    public boolean started=false;

    public MKFirmwareFlasher(String _url,byte _intension)
    {
	intension=_intension;
	url=_url;
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


    String url;
    boolean sigfail;
    //    int last_avr_sig;

    public int avr_sig=0;

    public void connect()
    {

	try {
	    connection = (StreamConnection) Connector.open(url);
	    
	    reader=connection.openInputStream();
	    writer=connection.openOutputStream();
	}
	catch (Exception e) {}
    }

    public void close_connections()
    {

	try {
	    int foo,leech_cnt=0;
	    while (( reader.available() != 0)&&(leech_cnt<1000))
		{
		    leech_cnt++;
		    foo=reader.read();
		}
	}
	catch (Exception e) {}

	//	if ((!force)&&root.canvas.do_vibra) root.vibrate(500);
	try{ reader.close(); }
	catch (Exception inner_ex) { }

	try{ writer.close(); }
	catch (Exception inner_ex) { }
	
//#ifdef j2me
//#	try{ connection.close(); }
//#	catch (Exception inner_ex) { }
//#endif

    }

   


    public InputStream in;
    public void run()
    {
	boolean init_sequence=true;	
	int send_buff_size=0;
	int gbl=0;
	log_("attempt:"+attempt);
	while (!got_bl_magic)
	    {
		attempt++;
		try{
		    if (init_sequence)
			{
			    //			    if (attempt!=0)
			    //	sleep((attempt%2==0)?80:800); //800
			    
			    msg_pos=0;
			    flash_msgs[msg_pos++]=("at:"+attempt+" gbl:"+gbl);
			    flash_msgs[msg_pos]=null;
			    
			    if ((attempt%2)==0) // after work
				{
				    writer.write('#');
				    writer.write(0);
				    writer.write('R');
			    
				    // CRC
				    writer.write((char)( (((byte)'#' +0 + (byte)'R')%4096)/64 + '='));
				    writer.write((char)( (((byte)'#' +0 + (byte)'R')%4096)%64 + '='));
				    
				    writer.write('\r');
			    
				    writer.flush();
				    //sleep(20);
				}
			    
			    writer.write( 27);
			    writer.flush();
			    
			    sleep(20);
			    
			    writer.write( 0xAA);
			    writer.flush();
			    sleep(180);
			}
		    //		    else
		    //			sleep(20);
		    init_sequence=true;
		    //		    sleep(20);
		    while ( reader.available() > 0 )
			{
			    if ((byte)reader.read()==bl_magic[bl_magic_pos])
				{
				    bl_magic_pos++;
				    if (bl_magic_pos==bl_magic.length)
					{
					    log("got bl magic");
					    got_bl_magic=true;
					    gbl++;
					    bl_magic_pos=0;

					    int foo,leech_cnt=0;
					    while (( reader.available() != 0)&&(leech_cnt<1000))
						{
						    leech_cnt++;
						    foo=reader.read();
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
			    
			    writer.write( 't');
			    writer.flush();

			    sleep(120);
			    
			    //			    if ( reader.available() == 0 )
			    //throw new Exception("cant read avrsig");

			    /*
			    int sig1_timeout=0;
			    while ( reader.available() == 0)
				{ 
				    
				    if ((sig1_timeout++)>1000 ) 		    
					throw new Exception("cant read avrsig");
				    sleep(10);
				}

			    */
			    avr_sig=reader.read();

			    if (avr_sig==63)
				init_sequence=false;
			    /*    while (avr_sig==63)
			        avr_sig=reader.read();
			    */
			    flash_msgs[msg_pos++]="got avr sig " + avr_sig;
		
			    //			    last_avr_sig=avr_sig;
			    //			    for(int sig2_timeout=0;sig2_timeout<100;sig2_timeout++)
			    /*
			    int sig2_timeout=0;
			    while ( reader.available() == 0)
				{ 
				    
				    if ((sig2_timeout++)>1000 ) 		    
					throw new Exception("cant read avrsig-val2");
				    sleep(10);
				}*/

			    int avrsig_suff=reader.read();


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
		
		writer.write('T'); 
		//		writer.flush();
		writer.write(avr_sig);   // set devicetyp = 0x74 oder 0x76  
		writer.flush();
		
		if (reader.read()!=0x0d)
		    throw new Exception("cant get buffer size");
		
		writer.write('V'); 
		writer.flush();
		
		int bl_version_major=reader.read();
		int bl_version_minor=reader.read();
		
		flash_msgs[msg_pos++]="BL Version " + bl_version_major+"."+bl_version_minor;
		
		
		writer.write('b'); 
		writer.flush();
		
		if (reader.read()!='Y')
		    throw new Exception("cant get buffer size");
		
		
		send_buff_size=reader.read()*0x100;
		send_buff_size+=reader.read();

		
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
			writer.write( MKFirmwareHelper.cmd_reset_params);

			writer.flush();
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
					crc=MKFirmwareHelper.CRC16(flash_buff[crc_pos],crc);
				    writer.write((crc>>8)&0xff); 
				    writer.write(crc&0xff); 
				    writer.flush(); 
				}
			    //  flash_msgs[msg_pos]+="ok";
			    //				writer.flush();
			    
			    
			    
			    if (reader.read()!=0x0d)
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



    public void exit_bootloader()
    {
	try{
	    writer.write('E'); 
	    writer.flush();
	}
	catch (Exception e)  {   
	    flash_msgs[msg_pos++]="cant exit bootloader" ;
	}
	flash_msgs[msg_pos++]="Exit BL done" ;	    
	


	close_connections();
    }


}
