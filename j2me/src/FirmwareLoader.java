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

import java.util.Vector;
import java.io.*;
             
public class FirmwareLoader
    implements Runnable // for http download task
	       ,org.ligi.ufo.DUBwiseLangDefs
	       , DUBwiseUIDefinitions
{ 

    public final static String base_url= "http://mikrocontroller.cco-ev.de/mikrosvn/Projects/DUBwise/trunk/misc/firmwares/";
    boolean got_list=false;
    String list_str="";

    String[] names;
    String[] filenames;
    
    DUBwiseCanvas canvas;


    int selected=0;
    public void menu_fire(byte pos)
    {

	if ( pos<(names.length-1))
	    {
		selected=pos;
		canvas.chg_state(STATEID_FLASHING);
	    }
	else
	    canvas.chg_state(STATEID_MAINMENU);
	    
    }


    public InputStream get_input_str()
    {
	if ( selected<fws_in_jar)
	    return this.getClass().getResourceAsStream("/fw_"+avrsig+"_"+selected+".bin");	    

	return null;
    }


    int fws_in_jar=0;
    int avrsig;

    public FirmwareLoader(int _avrsig,DUBwiseCanvas _canvas)
    {
	avrsig=_avrsig;
	canvas=_canvas;

	// firmware files in jar
	Vector jar_names_vector = new Vector();

	String tmp_str="";
	try {
	    InputStream in=this.getClass().getResourceAsStream("/fw_"+avrsig+".lst");	    
	    char ch=0;
	    while (in.available()>0)
		{
		    if ( (ch=(char)in.read())!='\n')
			tmp_str+=ch;
		    else
			{
			    jar_names_vector.addElement( tmp_str );
			    tmp_str="";
			}

		}
	}
	    catch (Exception e) {}
	    
	fws_in_jar= jar_names_vector.size();

	names=new String[ fws_in_jar+2];

	if ( jar_names_vector.size()>0)
	for(int loop=0; loop<fws_in_jar; loop++)
	    names[loop] = (String)jar_names_vector.elementAt(loop);
		
	names[jar_names_vector.size()]="Download";
	names[jar_names_vector.size()+1]=canvas.l(STRINGID_BACK);

	//	new Thread( this ).start(); // fire up main Thread 
    }

    public void run()
    {
	if (!got_list)
	    {
		list_str=DUBwiseHelper.get_http_string(base_url+"list");
		

		String[] split=DUBwiseHelper.split_str(list_str,"\n");
		names=new String[ split.length];
		filenames=new String[ split.length];
		

		for ( int i=0;i<split.length;i++)
		    {
			String[] sp2=DUBwiseHelper.split_str(split[i],":");
			if (sp2.length==2)
			   {
			       names[i]=sp2[0];
			       filenames[i]=sp2[1];
			   }
			else
			    names[i]="fail";

			
		    }
		
		System.out.println(list_str);
		
		got_list=true;
	    }
	else
	    {

	    }
    }

    
}
