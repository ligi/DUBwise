/**************************************************
 *                                             
 * class to handle Editing of MK Params
 *                                             
 * Author:        Marcus -LiGi- Bueschleb      
 * 
 * see README for further Infos
 *
 *************************************************/

package org.ligi.android;

import android.view.*;

import org.ligi.ufo.*;

public class MKParamEditor
    implements MKParamDefinitions
{


    private int act_tab=0;
    private int act_y=0;
    private int act_lcd_lines=10;

    DUBwise root;

    public String[] lcd_lines;

    public String[] public_lcd_lines;
    
    public MKParamEditor(DUBwise _root)
    {
	root=_root;
	lcd_lines=new String[40];
	//	refresh_lcd();
    }
    
    /*
    public void paint ( Graphics g)
    {
	refresh_lcd();
	canvas.paint_lcd(g,false);
    }
    */
    
    public void refresh_lcd()
    {
	//	try
	    {

	if (root.mk!=null)
	    {
		act_lcd_lines=root.mk.params.field_names[act_tab].length*2+1;
		for ( int i=0;i<act_lcd_lines;i++)
		    lcd_lines[i]="";


		try
		    {

		lcd_lines[0]=(act_tab==0?"  ":"< ") + root.mk.params.tab_names[act_tab] + (act_tab==(root.mk.params.tab_names.length-1)?"  ":" >");

		for (int i=0;i<root.mk.params.field_names[act_tab].length;i++)
		    {
			lcd_lines[1+2*i]=root.mk.params.field_names[act_tab][i];
			if (root.mk.params.field_types[act_tab][i]== root.mk.params.PARAMTYPE_BITSWITCH)
			    lcd_lines[2+2*i]=(((root.mk.params.get_field_from_act(root.mk.params.field_positions[act_tab][i]/8)&(1<<root.mk.params.field_positions[act_tab][i]%8))==0)?"off":"on" ) ;
			if (root.mk.params.field_types[act_tab][i]== root.mk.params.PARAMTYPE_BYTE)
			    {
				lcd_lines[2+2*i]=""+root.mk.params.get_field_from_act(root.mk.params.field_positions[act_tab][i]);
				if ((root.mk.params.get_field_from_act(root.mk.params.field_positions[act_tab][i])>250)&&(root.mk.params.get_field_from_act(root.mk.params.field_positions[act_tab][i])<256))
				    lcd_lines[2+2*i]+="[Poti"+(root.mk.params.get_field_from_act(root.mk.params.field_positions[act_tab][i])-250) +"]";												    
			    }

			if (root.mk.params.field_types[act_tab][i]== root.mk.params.PARAMTYPE_STICK)
			    {
				lcd_lines[2+2*i]=""+root.mk.params.get_field_from_act(root.mk.params.field_positions[act_tab][i]);
			    }
		


		    }
		
		for (int i=0;i<act_lcd_lines;i++)
		    {
			lcd_lines[i]=(act_y==i?"#":" ")+lcd_lines[i];
		    }

		for ( int i=0;i<act_lcd_lines;i++)
		    while(lcd_lines[i].length()<20)
			{
			    lcd_lines[i]+=" ";
			}
		    }
		catch (Exception e){}
		public_lcd_lines=new String[act_lcd_lines];
		for(int i=0;i<act_lcd_lines;i++)
		    public_lcd_lines[i]=lcd_lines[i];
		
	    }
	else 
	    {
		public_lcd_lines=new String[1];
		public_lcd_lines[0]="reading params";
	    }

	    }
	    //	catch (Exception e){}
    }


    public final static int KEYCODE_CLEAR=-8;
    
    public boolean editing_number=false;

    public void keypress (int keyCode,int action)
    {
	if (act_y==0) switch (action) 
	    {
	    case KeyEvent.KEYCODE_DPAD_RIGHT:
		if (act_tab<root.mk.params.tab_names.length-1) act_tab++;
		break;

	    case KeyEvent.KEYCODE_DPAD_LEFT:
		if (act_tab!=0) act_tab--;
		break;
	    }
	else
	    {
		if(root.mk.params.field_types[act_tab][act_y/2-1]==root.mk.params.PARAMTYPE_BYTE)
		    {
			if ((keyCode >=KeyEvent.KEYCODE_0) && (keyCode <=KeyEvent.KEYCODE_9))
			    {
				if((editing_number)&&( Math.abs(root.mk.params.get_field_from_act(root.mk.params.field_positions[act_tab][act_y/2-1]))*10+(keyCode -KeyEvent.KEYCODE_0)<1000))
				root.mk.params.set_field_from_act(root.mk.params.field_positions[act_tab][act_y/2-1] , Math.abs(root.mk.params.get_field_from_act(root.mk.params.field_positions[act_tab][act_y/2-1]))*10+(keyCode -KeyEvent.KEYCODE_0));
				else
				root.mk.params.set_field_from_act(root.mk.params.field_positions[act_tab][act_y/2-1] , (keyCode -KeyEvent.KEYCODE_0));
				editing_number=true;
				return;
			    }
			else
			    if ( keyCode==KEYCODE_CLEAR)
				root.mk.params.set_field_from_act(root.mk.params.field_positions[act_tab][act_y/2-1],0);
		    }
		editing_number=false;
		    
		    switch (action) 
			{
			    
			case KeyEvent.KEYCODE_DPAD_RIGHT:
			    switch(root.mk.params.field_types[act_tab][act_y/2-1])
				{
				case PARAMTYPE_BITSWITCH:
				    
				    root.mk.params.field_from_act_xor((root.mk.params.field_positions[act_tab][act_y/2-1]/8),1<<(root.mk.params.field_positions[act_tab][act_y/2-1]%8));
				    break;
				case PARAMTYPE_BYTE:
				case PARAMTYPE_STICK:
				    
				    root.mk.params.field_from_act_add(root.mk.params.field_positions[act_tab][act_y/2-1],1);
				    break;
				}
			    break;
			    
			case KeyEvent.KEYCODE_DPAD_LEFT:
			    switch(root.mk.params.field_types[act_tab][act_y/2-1])
				{
				case PARAMTYPE_BITSWITCH:
				    root.mk.params.field_from_act_xor((root.mk.params.field_positions[act_tab][act_y/2-1]/8),1<<(root.mk.params.field_positions[act_tab][act_y/2-1]%8));
				    
				    break;
				case PARAMTYPE_BYTE:
				case PARAMTYPE_STICK:
				    root.mk.params.field_from_act_add(root.mk.params.field_positions[act_tab][act_y/2-1],-1);
				    break;
				}
			    
			    
			    break;
			}
		
	    }
	
	switch (action) 
	    {

	    case KeyEvent.KEYCODE_DPAD_DOWN:
		if (act_y<(act_lcd_lines-2)) act_y+=2;
		else act_y=0;
		break;

	    case KeyEvent.KEYCODE_DPAD_UP :
		if (act_y!=0) act_y-=2;
		else act_y=act_lcd_lines-1;
		break;
		
	    }

	refresh_lcd();


    }
}

