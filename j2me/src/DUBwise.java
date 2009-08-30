/***********************************************************************
 *                                                           
 * DUBwise == Digital UFO Broadcasting with intelligent service equipment
 * main MIDLet Source file
 *                                                           
 * Author:        Marcus -LiGi- Bueschleb
 * Mailto:        LiGi @at@ LiGi DOTT de                    
 *
 ************************************************************************/

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

//#if j2memap=="on"
import com.eightmotions.map.MapDisplay;
import com.eightmotions.util.UtilMidp;
//#endif
public class DUBwise
    extends MIDlet

{
    public Display display;
    public  DUBwiseCanvas canvas;
    public boolean loaded=false;

    public void log(String str)
    {
	//	canvas.debug.log(str);
    }
    public void vibrate(int duration)
    {
	display.vibrate(duration);
    }

//#if j2memap=="on"
    MapDisplay m_map;
    Canvas map_canvas;
//#endif      

    protected void startApp()
        throws MIDletStateChangeException
    {
	if (loaded)
	    {
		System.out.println("app already loaded");
		display.setCurrent(canvas);
		return; // when allready done -> do not do it again
	    }

	//	m_map.init();
	// Display.getDisplay(this).setCurrent(m_map.getCanvas());

//#if j2memap=="on"
	UtilMidp.checkMIDP(this);  //Initialise the utility library...
	m_map=new MapDisplay();
	m_map.setModeProxy(true);
	map_canvas=m_map.getCanvas();
//#endif      

	display  = Display.getDisplay(this);
	canvas=new  DUBwiseCanvas(this);

	// fire up canvas
	display.setCurrent(canvas);
	loaded=true;
	//	System.out.println("DUBwise start done");
    }

    
    public void quit() {
	destroyApp(true);
	notifyDestroyed();
    }
    protected void pauseApp()     {
	quit();
	System.out.println("app paused");
    }   

    protected void destroyApp(boolean arg0)  
    {
	
    }

}
