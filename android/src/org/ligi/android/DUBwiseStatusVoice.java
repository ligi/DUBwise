/**************************************
 * 
 * Voice output 
 *                      
 * Author:        Marcus -LiGi- Bueschleb
 * 
 * see README for further Infos
 *
 *
 **************************************/

package org.ligi.android;

import android.media.*;
import android.media.MediaPlayer.*;

import org.ligi.ufo.*;

public class DUBwiseStatusVoice
    implements Runnable 
	       //,OnCompletionListener
{

    MKCommunicator mk=null;
    DUBwise root=null;
    MediaPlayer player;
    
    public final int PLAYERSTATE_IDLE=0;
    public final int PLAYERSTATE_PLAYING=1;
    public final int PLAYERSTATE_FIN=2;

    int act_player_state=PLAYERSTATE_IDLE;

    int last_sound=-1;

    public DUBwiseStatusVoice(DUBwise _root)
    {

	root=_root;

	new Thread( this ).start(); // fire up main Thread 
    }

    public void start_playing(int resid)
    {
	last_sound=resid;
	
	try {
	    player=MediaPlayer.create(root,  R.raw.voice_sample_01-1+resid);	

	    player.start();

	    
	}
	catch (Exception e)  { 
	    
	}

    }

    public void wait_for_end()
    {
	while (player.isPlaying())
	    {
		try { Thread.sleep(50); }
		catch (Exception e)  {   }
	    }
	try { Thread.sleep(50); }
		catch (Exception e)  {   }
	player.stop();
	player.release();

	player=null;

	System.gc();
		try { Thread.sleep(150); }
		catch (Exception e)  {   }
   }

    public void run()
    {
        while(true)
            {

		
		if (root.mk.connected&&(root.do_sound)&&(root.mk.debug_data.UBatt()!=-1)&&(!root.mk.force_disconnect))
		    {
			int ubatt=root.mk.debug_data.UBatt();

			start_playing( (ubatt/10));
			wait_for_end();

			if((ubatt%10)!=0)
			    {
				start_playing( 14);
				wait_for_end();
				start_playing((ubatt%10));
				wait_for_end();
			    }
			start_playing(15);
			wait_for_end();
	
		    }

		try { Thread.sleep(5000); }
		catch (Exception e)  {   }
		
	    }
    }
    

    
}

