/********************************************************
 *                                             
 * class to handle Editing of MK Params via MIDP / J2ME
 *                                             
 * Author:        Marcus -LiGi- Bueschleb      
 * 
 ********************************************************/

import javax.microedition.lcdui.*;

public class MKParamsEditor
    implements org.ligi.ufo.MKParamDefinitions,  org.ligi.ufo.DUBwiseLangDefs
{

    public byte nextstate;

    private int act_tab=0;
    public int act_y=1;
    private int act_lcd_lines=10;

    DUBwiseCanvas canvas;

    public String[] lcd_lines;

    public String[] menu_items;

    org.ligi.ufo.ParamsClass edit_source;

    boolean select_mode;



    public MKParamsEditor(DUBwiseCanvas _canvas,org.ligi.ufo.ParamsClass _edit_source,byte _nextstate)
    {
	nextstate=_nextstate;
	edit_source=_edit_source;

	canvas=_canvas;
	lcd_lines=new String[40];


	System.out.println("initing params_editor");
	select_mode=(edit_source.tab_stringids!=null);
	//	refresh_lcd();
    }



    public void paint ( Graphics g)
    {
	if (select_mode)
	    {
		if ((canvas.menu_items[0]!=canvas.lcd_lines[0])||(canvas.menu_items[0]!=canvas.l(edit_source.tab_stringids[0])||((canvas.menu_items.length!=edit_source.tab_stringids.length+1)))) // usefull?
		    {
			act_y=1;
			menu_items=new String[edit_source.tab_stringids.length+1];
			for(int p=0;p<edit_source.tab_stringids.length;p++)
			    menu_items[p]=canvas.l(edit_source.tab_stringids[p]);
			menu_items[edit_source.tab_stringids.length]=canvas.l(STRINGID_BACK);			
			canvas.setup_menu(menu_items,null);
		    }
		canvas.paint_menu(g);
	    }

	else
	    {
		refresh_lcd();
		//		canvas.paint_lcd(g,false);
		canvas.paint_lcd(g);
	    }
    }

    
    public void refresh_lcd()
    {

	try {
	    act_lcd_lines=edit_source.field_types[act_tab].length*2+2;
	    
	    for ( int i=0;i<act_lcd_lines;i++)
		lcd_lines[i]="";
	    
	    
	    
	    //		lcd_lines[0]=(act_tab==0?"  ":"< ") + edit_source.tab_stringids[act_tab] + (act_tab==(edit_source.tab_stringids.length-1)?"  ":" >");
	    
	    for (int i=0;i<edit_source.field_types[act_tab].length;i++)
		{
		    if (edit_source.field_stringids!=null)
			lcd_lines[2*i]=canvas.l(edit_source.field_stringids[act_tab][i]);
		    else
			lcd_lines[2*i]=edit_source.field_strings[act_tab][i];
		    switch(edit_source.field_types[act_tab][i])
			{
			case  PARAMTYPE_BITSWITCH:
			    lcd_lines[1+2*i]=" " + (((edit_source.get_field_from_act(edit_source.field_positions[act_tab][i]/8)&(1<<edit_source.field_positions[act_tab][i]%8))==0)?canvas.l(STRINGID_OFF):canvas.l(STRINGID_ON) ) ;
			    break;

			case PARAMTYPE_BYTE:
			    lcd_lines[1+2*i]=" "+edit_source.get_field_from_act(edit_source.field_positions[act_tab][i]);

			    break;

			case PARAMTYPE_BITMASK:
			    lcd_lines[1+2*i]=" "+edit_source.get_field_from_act(edit_source.field_positions[act_tab][i]);
			    if ((edit_source.get_field_from_act(edit_source.field_positions[act_tab][i])>250)&&(edit_source.get_field_from_act(edit_source.field_positions[act_tab][i])<256))
				lcd_lines[1+2*i]+="[Poti"+(edit_source.get_field_from_act(edit_source.field_positions[act_tab][i])-250) +"]"; 
			    else		
				{
				    lcd_lines[1+2*i]+=" [";
				    for (int bit=0;bit<8;bit++)
					lcd_lines[1+2*i]+=((edit_source.get_field_from_act(edit_source.field_positions[act_tab][i]) & ( 1<<bit)) !=0)?"+":"-";
			    lcd_lines[1+2*i]+="]";
				}
				
			    break;

			case PARAMTYPE_MKBYTE:
			    lcd_lines[1+2*i]=" "+edit_source.get_field_from_act(edit_source.field_positions[act_tab][i]);
			    if ((edit_source.get_field_from_act(edit_source.field_positions[act_tab][i])>250)&&(edit_source.get_field_from_act(edit_source.field_positions[act_tab][i])<256))
				lcd_lines[1+2*i]+=" [Poti"+(edit_source.get_field_from_act(edit_source.field_positions[act_tab][i])-250) +"]";						     
			    break;


			case PARAMTYPE_KEY:
			    if (edit_source.get_field_from_act(edit_source.field_positions[act_tab][i])==-4242)
				lcd_lines[1+2*i]=canvas.l(STRINGID_NONE_ASSIGNED);
			    else
				lcd_lines[1+2*i]=" "+edit_source.get_field_from_act(edit_source.field_positions[act_tab][i]);
			    break;
			case PARAMTYPE_STICK:
			    lcd_lines[1+2*i]=" "+edit_source.get_field_from_act(edit_source.field_positions[act_tab][i]);
			    break;
			    
			default:
			    lcd_lines[1+2*i]=" "+canvas.l(edit_source.choice_stringids[edit_source.field_types[act_tab][i]-PARAMTYPE_CHOICE][edit_source.get_field_from_act(edit_source.field_positions[act_tab][i])]);				
			}
		    
		    }
	    
	    lcd_lines[act_lcd_lines-1]=canvas.l(STRINGID_BACK);		
	
	    canvas.lcd_lines=new String[act_lcd_lines];    
	    for ( int i=0;i<act_lcd_lines;i++)
		{
		    lcd_lines[i]=(act_y==i?"#":" ")+lcd_lines[i];
		    while(lcd_lines[i].length()<20)
			lcd_lines[i]+=" ";

		    canvas.lcd_lines[i]=lcd_lines[i];		    
		}

	}

	catch (Exception e){}
	
	//	for(int i=0;i<act_lcd_lines;i++)

	/*		
	    }
	else 
	    {
		canvas.lcd_lines=new String[1];
		canvas.lcd_lines[0]="reading params";
	    }
    */
    }


    public final static int KEYCODE_CLEAR=-8;
    
    public boolean editing_number=false;

    public void pointer_press(int x,byte row)
    {
	

	
	System.out.println("pointer row " +  row + " lcd_off" + canvas.lcd_off);
	if (select_mode)
	    {
		if (canvas.act_menu_select!=row)
		    canvas.act_menu_select=row;
		else
		    keypress (-4242,Canvas.FIRE);

	    }
	  else
	      {
		  if ((row%2)==0) 
		      act_y=row+1;
		  else
		      {
			  act_y=row;

			  System.out.println("y:" + act_y);
			  if (act_y==(canvas.lcd_lines.length-1))
			      keypress (-4242,Canvas.FIRE);
			  else
			      {
				  if (x<(canvas.canvas_width/2))
				      keypress (-4242,Canvas.LEFT);
				  else
				      keypress (-4242,Canvas.RIGHT);
			      }
		      }
	      }
    }


    public void keypress (int keyCode,int action)
    {
	if (select_mode)
	    {
		if ((action== Canvas.FIRE)||(canvas.settings.key_alternative_fire==keyCode))
		    {

			if (canvas.act_menu_select==(menu_items.length-1))
			    canvas.chg_state(nextstate);
			else

			    {
				act_tab=canvas.act_menu_select;
				select_mode=false;
				act_y=1;
			    }
		    }
		else
		    canvas.menu_keypress(keyCode);
	    }
	else
	    {


		if ((act_y!=(act_lcd_lines-1))&&(((keyCode >= Canvas.KEY_NUM0) && (keyCode <= Canvas.KEY_NUM9))|| ( keyCode==KEYCODE_CLEAR)))
			    {
				int act_pos=act_y/2;
				if((edit_source.field_types[act_tab][act_pos]==edit_source.PARAMTYPE_BYTE)||(edit_source.field_types[act_tab][act_pos]==edit_source.PARAMTYPE_MKBYTE) ||(edit_source.field_types[act_tab][act_pos]==edit_source.PARAMTYPE_BITMASK))
				    {
					if ((keyCode >= Canvas.KEY_NUM0) && (keyCode <= Canvas.KEY_NUM9))
					    {
						if((editing_number)&&( Math.abs(edit_source.get_field_from_act(edit_source.field_positions[act_tab][act_pos]))*10+(keyCode - Canvas.KEY_NUM0)<1000))
						    edit_source.set_field_from_act(edit_source.field_positions[act_tab][act_pos] , Math.abs(edit_source.get_field_from_act(edit_source.field_positions[act_tab][act_pos]))*10+(keyCode - Canvas.KEY_NUM0));
						else
						    edit_source.set_field_from_act(edit_source.field_positions[act_tab][act_pos] , (keyCode - Canvas.KEY_NUM0));
						editing_number=true;
						return;
					    }
					else
					    if ( keyCode==KEYCODE_CLEAR)
						edit_source.set_field_from_act(edit_source.field_positions[act_tab][act_pos],0);
				    }
				editing_number=false;
			
			    }

		switch (action) 
		    {
			
		    case Canvas.DOWN:
			if (act_y<(act_lcd_lines-2)) act_y+=2;
			else act_y=1;
			break;
			
		    case Canvas.UP:
			if (act_y!=1) act_y-=2;
			else act_y=act_lcd_lines-1;
			break;
			
		    default:



			if (act_y!=(act_lcd_lines-1))
			    {

				//

				int act_pos=act_y/2;

				if((edit_source.field_types[act_tab][act_pos]==edit_source.PARAMTYPE_KEY))
				    edit_source.set_field_from_act(edit_source.field_positions[act_tab][act_pos],keyCode);
			
				switch (action) 
				    {
				
				    case Canvas.RIGHT:
					switch(edit_source.field_types[act_tab][act_pos])
					    {
					    case PARAMTYPE_BITSWITCH:
					
						edit_source.field_from_act_xor((edit_source.field_positions[act_tab][act_pos]/8),1<<(edit_source.field_positions[act_tab][act_pos]%8));
						break;
					
					    case PARAMTYPE_BITMASK:
					    case PARAMTYPE_MKBYTE:
					    case PARAMTYPE_BYTE:

					
						edit_source.field_from_act_add_min_max(edit_source.field_positions[act_tab][act_pos],1,0,255);
						break;
					    case PARAMTYPE_STICK:
						edit_source.field_from_act_add_min_max(edit_source.field_positions[act_tab][act_pos],1,0,10);
						break;
					
					    default:
						edit_source.field_from_act_add_mod(edit_source.field_positions[act_tab][act_pos],1,edit_source.choice_stringids[edit_source.field_types[act_tab][act_pos]-PARAMTYPE_CHOICE].length);
						break;
					    }
					break;
				
				    case Canvas.LEFT:
					switch(edit_source.field_types[act_tab][act_pos])
					    {
					    case PARAMTYPE_BITSWITCH:
						edit_source.field_from_act_xor((edit_source.field_positions[act_tab][act_pos]/8),1<<(edit_source.field_positions[act_tab][act_pos]%8));
				    
						break;
					    case PARAMTYPE_BITMASK:
					    case PARAMTYPE_MKBYTE:
					    case PARAMTYPE_BYTE:
						edit_source.field_from_act_add_min_max(edit_source.field_positions[act_tab][act_pos],-1,0,255);
						break;
					    case PARAMTYPE_STICK:
						edit_source.field_from_act_add_min_max(edit_source.field_positions[act_tab][act_pos],-1,0,10);
						break;
					    default:
						edit_source.field_from_act_add_mod(edit_source.field_positions[act_tab][act_pos],1,edit_source.choice_stringids[edit_source.field_types[act_tab][act_pos]-PARAMTYPE_CHOICE].length);
						break;
					    }
				
				
					break;
				    }
			
			    }
			else
			    if ((action== Canvas.FIRE)||(canvas.settings.key_alternative_fire==keyCode))
				{
				    act_y=1;
				    //				    canvas.act_menu_select=0;
				    canvas.menu_items[0]="";

				    if (edit_source.tab_stringids==null)
					canvas.chg_state(nextstate);
				    else
					select_mode=true;
				}
		    }
		    
		    
		//		if (!select_mode)refresh_lcd();
		    
	    }
    } // keypress
}

