/***************************************************************
 *
 * Code for on Device Debugging of DUBwise
 *                                                           
 * Author:        Marcus -LiGi- Bueschleb
 * Mailto:        LiGi @at@ LiGi DOTT de                    
 * 
 ***************************************************************/

import javax.microedition.lcdui.*;


public class DUBwiseDebug
    extends Canvas
{


    public  boolean showing=false;
    public  boolean paused=false;

    public String debug_msg="";

    private byte[] debug_screen_sequence={KEY_POUND,KEY_NUM4,KEY_NUM2};
    private byte   debug_screen_sequence_pos=0;
    

    public final static int DEBUG_HISTORY_LENGTH=100;

    
    public String[] debug_msgs;

    public int debug_pos=0;
    public int debug_paused_pos=0;


    int y_off=0;


    public DUBwiseCanvas canvas;

    public DUBwiseDebug(DUBwiseCanvas canvas_)
    {
	canvas=canvas_;

	debug_msgs=new String[DEBUG_HISTORY_LENGTH];
	for (int tmp_i=0;tmp_i<DEBUG_HISTORY_LENGTH;tmp_i++)
	    debug_msgs[tmp_i]="";
    }

    public void log(String str)
    {
	if (debug_pos==DEBUG_HISTORY_LENGTH)
	    debug_pos=0;

	debug_msgs[debug_pos]=str;
	debug_pos++;
	//	    debug_msgs[debug_pos]=str;
    }


    public void err(String str)
    {
	if (debug_pos==DEBUG_HISTORY_LENGTH)
	    debug_pos=0;

	debug_msgs[debug_pos]=str;
	debug_pos++;
	showing=false;
	paused=true;
	//	    debug_msgs[debug_pos]=str;
    }

    public void paint (Graphics g)
    {

       Font debug_font= Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);  

       g.setFont(debug_font);

	g.setColor(0x0000FF);
	g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
	g.setColor(0xFFFFFF);

	y_off=0;
	if (!paused) debug_paused_pos=debug_pos;
	for (int tmp_pos=debug_paused_pos;((tmp_pos>0)&&(y_off<canvas.getHeight()));tmp_pos--)
	    {
		debug_msg=debug_msgs[tmp_pos];
		String tmp_str="";

		for(int tmp_i=0;tmp_i<debug_msg.length();tmp_i++)
		    {
			if ((debug_msg.charAt(tmp_i)=='\r')||(debug_msg.charAt(tmp_i)=='\n')) 
			    {
			    g.drawString(tmp_str,5,y_off,Graphics.TOP | Graphics.LEFT);
			    y_off+=debug_font.getHeight();
			    tmp_str="";
			    }
			else
			    tmp_str+=debug_msg.charAt(tmp_i);
		    }
		g.drawString(tmp_pos+" "+tmp_str,0,y_off,Graphics.TOP | Graphics.LEFT);
		y_off+=debug_font.getHeight();
	    }

    }
    
    public void process_key(int keyCode)
    {


	if (!showing)
	    {
		if (keyCode==debug_screen_sequence[debug_screen_sequence_pos])
		    {
			debug_screen_sequence_pos++;
			if(debug_screen_sequence_pos==debug_screen_sequence.length)
			    {
				showing=true;
				debug_screen_sequence_pos=0;
			    }
		    }
		else
		    debug_screen_sequence_pos=0;
	    }
	else
	    {
		if (keyCode==KEY_STAR)
		    showing=false;

		if (keyCode==KEY_NUM0)
		    paused=!paused;


		switch (getGameAction (keyCode)) 
			    {
			    case UP:
				debug_paused_pos++;
				break;
			
			    case DOWN:
				debug_paused_pos--;
				break;

			    }

		    
	    }


    }
    


}
