/***************************************************************
 *
 * File Access of DUBwise
 *                                                           
 * Author:        Marcus -LiGi- Bueschleb
 * Mailto:        LiGi @at@ LiGi DOTT de                    
 * 
 ***************************************************************/

//#if fileapi=="on"
import javax.microedition.io.*;
import javax.microedition.midlet.*;
import javax.microedition.io.file.*;

import java.io.*;
import java.util.*;


public class DUBwiseFileAccess
{
    public final static int MAX_FILELIST_LENGTH=100;
    public final static int MAX_PATH_DEPTH=10;

    byte act_path_depth=0;
    String[] act_path_arr;


    public boolean dirty=true;

    public String act_path()
    {
	String res="";
	for (int i=0;i<act_path_depth;i++)
	    res+=act_path_arr[i];
	return res;
    }
    
    public void refresh_if_dirty()
    {
	if (dirty) trigger();
	dirty=false;
    }

    String[] file_list;
    int file_list_length=0;
    

    DUBwiseCanvas canvas ;
    public DUBwiseFileAccess(DUBwiseCanvas _canvas )
    {
	canvas=_canvas;
    	file_list= new String[MAX_FILELIST_LENGTH];
	act_path_arr=new String[MAX_PATH_DEPTH];
    }

    public void fire()
    {

	if ((canvas.act_menu_select==0)&&(act_path_depth!=0))
	    {
		act_path_depth--;
		//act_path=act_path.substring(0,act_path.substring(0,act_path.length()-2).indexOf('/') );

		//act_path=last_path;
	    }
	else
	    {
		//last_path=act_path;
		if (act_path_depth==0)
		    act_path_arr[act_path_depth++]=file_list[canvas.act_menu_select]; 
		else
		    act_path_arr[act_path_depth++]=file_list[canvas.act_menu_select-1]; 
	    }
	canvas.act_menu_select=0;
	//chg_state(STATEID_FILEOPEN);
	dirty=true;
    }


    public void trigger()
    {
	if (act_path_depth==0)
	    {
		Enumeration drives = FileSystemRegistry.listRoots();
		int tmp_i=0;
		while(drives.hasMoreElements()) 
		    {  
			file_list[tmp_i]= (String) drives.nextElement();
			tmp_i++; 
			    
			if (MAX_FILELIST_LENGTH<tmp_i) 
			    break;
		    }			
			
		file_list_length=tmp_i;
			
		String[] menu_items=new String[tmp_i];
		//			lcd_lines=new String[tmp_i];
			
			
		for(tmp_i=0;tmp_i<file_list_length;tmp_i++)
		    menu_items[tmp_i]=file_list[tmp_i];

		canvas.setup_menu(menu_items,null);
			
	    }
	else
	    {

		try {
		    FileConnection fc = (FileConnection) Connector.open("file:///"+act_path()+"DUBwise");
		    //		    Enumeration filelist = fc.list("*", true);
		    fc.mkdir();
		    Enumeration filelist = fc.list();//"*", true);
		    int tmp_i=0;
		   		    while(filelist.hasMoreElements()) {
		     file_list[tmp_i] = (String) filelist.nextElement();
		    tmp_i++;
			/*				fc = (FileConnection)
							Connector.open("file:///CFCard/" + fileName);
							if(fc.isDirectory()) {
							System.out.println("\tDirectory Name: " + fileName);
							} else {
							System.out.println
							("\tFile Name: " + fileName + 
							"\tSize: "+fc.fileSize());
							}*/
            
		    }   
		
		    String[] menu_items=new String[tmp_i+1];
		    //			    lcd_lines=new String[tmp_i+1];
		    file_list_length=tmp_i+1;
			    
		    menu_items[0]="..";
		    for(tmp_i=1;tmp_i<file_list_length;tmp_i++)
			menu_items[tmp_i]=file_list[tmp_i-1];

		    canvas.setup_menu(menu_items,null);
		    fc.close();
		} catch (IOException ioe) {
		    System.out.println(ioe.getMessage());
		}
	    }


    }
}
//#endif
