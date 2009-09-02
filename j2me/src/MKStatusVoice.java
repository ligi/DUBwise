/******************************************
 * 
 * Voice output for MK
 *                      
 * Author:        Marcus -LiGi- Bueschleb
 * 
 *******************************************/

import javax.microedition.media.*;
import javax.microedition.media.control.*;
import java.util.*;
import java.io.*;


public class MKStatusVoice
    implements Runnable,PlayerListener
{

    DUBwiseCanvas canvas=null;
    Player player;

    Player up_player;
    Player down_player;
    
    public final int PLAYERSTATE_IDLE=0;
    public final int PLAYERSTATE_PLAYING=1;
    public final int PLAYERSTATE_FIN=2;


    public int volume=100;
    public int delay=5;

    int act_player_state=PLAYERSTATE_IDLE;
    VolumeControl vc;

    public MKStatusVoice(DUBwiseCanvas _canvas)
    {
	canvas=_canvas;

	new Thread( this ).start(); // fire up main Thread 
    }

    public void playerUpdate(Player player,  String event, Object data)
    {
      if(event == PlayerListener.END_OF_MEDIA) {
	  try {
	      defplayer();
	  } 
	  catch(MediaException me) {	  }
	  act_player_state=PLAYERSTATE_FIN;
	  player=null;

      }
    }
    
    void defplayer() throws MediaException {
      if (player != null) {
         if(player.getState() == Player.STARTED) {
            player.stop();
         }
         if(player.getState() == Player.PREFETCHED) {
            player.deallocate();
         }
         if(player.getState() == Player.REALIZED ||   player.getState() == Player.UNREALIZED) {
           player.close();
         }
      }
      player = null;
    }


    public Player init_player(String name)
    {
	Player _player=null;
	try {
		
	    try {
		_player = Manager.createPlayer(getClass().getResourceAsStream(name+".mp3"), "audio/mp3");
	    }
	    catch (Exception e)  { 
	    	_player = Manager.createPlayer(getClass().getResourceAsStream(name+".wav"), "audio/x-wav");
	    }
	    _player.addPlayerListener(this);
	    _player.realize();


	    vc = (VolumeControl) _player.getControl("VolumeControl");
	    if(vc != null) {
		vc.setLevel(volume);
	    }
	    
	    _player.prefetch();
	    _player.setLoopCount(1);
	    _player.start();	
	}
	catch (Exception e)  { 
	    
	}	
	return _player;

    }

    public void play_up()
    {

	try {

	    if (up_player==null) 
		up_player=init_player("up");


	    up_player.start();	
	    act_player_state=PLAYERSTATE_PLAYING;
	}
	catch (Exception e)  { 
	    
	}	
    }


    public void play_down()
    {
	try {
	    if (down_player==null) 
		down_player=init_player("down");
	    down_player.start();	
	    act_player_state=PLAYERSTATE_PLAYING;
	}
	catch (Exception e)  { 
	    
	}	
    }


    public boolean play(String what)
    {

	// start play
	try {
	    act_player_state=PLAYERSTATE_PLAYING;
	    player=init_player(what);
	    player.start();	
	}
	catch (Exception e)  { 
	    act_player_state=PLAYERSTATE_FIN;
	}	

	// wait for end
	while (act_player_state!=PLAYERSTATE_FIN)
	    {
		try { Thread.sleep(5); }
		catch (Exception e)  {   }
	    }


	return true;
    }

    // play number
    public void play(int what)
    {

	if (what==0)
	    {
		play("0");
		return; 
	    }

	if (((what/1000)%10)!=0)
	    {
		play((what/1000)%10);
		play("thousand");
		what%=1000;
	    }
	
		
	if (((what/100)%10)!=0)
	    {
		play((what/100)%10);
		play("hundred");
		what%=100;
		
	    }

	



	if (what<20)
	    {
		
		if (what<13)
		    {
			if(what!=0)
			    play(""+what);
		    }
		else
		     {
			 switch (what%10)
			     {
			     case 3:
				 play("thir");
				 break;
			     case 5:
				 play("fif");
				 break;
			     default:
				 play (""+what%10);
			     } 
			 play("teen");
		     }
	    }
	else
	    {
		
		switch (what/10)
		    {

		    case 2:
			play("twen");
			break;
		    case 3:
			play("thir");
			break;
		    case 5:
			play("fif");
			break;
		    default:
			play (""+what/10);
		    } 
		play("ty");
		if ((what%10)!=0)
		    play (what%10);
	    }
   

 }



	
    int info_from_debug_set=-1;
    int volt_timeout=0;

    public int BASE_SLEEP=10;

    public int last_alt=-1;


    public int loop_cnt;
    public int volts_play_cnt;

    boolean conn_told=false;
    boolean disconn_told=true;
    boolean sender_warning_told=false;


    public void run()
    {


        while(true)
            {
		loop_cnt++;
		if ((canvas.mk!=null)&&(canvas.settings.do_sound))
		    {

			if ((canvas.mk.connected)&&(!canvas.mk.force_disconnect))
			    {
			if (!conn_told)
			    {
				if (canvas.mk.version.known&&canvas.settings.do_intro_voice)
				    {
					if (!canvas.settings.minimal_voice) {
					play("connected");
					play("to"); }

					if (canvas.mk.is_mk())
					   play("mikrokopter");

					play("version");
					
					play(canvas.mk.version.major);
					play("point");
					play(canvas.mk.version.minor);
					play("established");
					conn_told=true;
					disconn_told=false;
				    }
			    }
			else
			    {

		
				if ((canvas.mk.SenderOkay()==0))
				    {
					if (!sender_warning_told)
					    {
						if (!canvas.settings.minimal_voice) play("warning");
						play("rc-signal");
						play("lost");
						sender_warning_told=true;
					    }
				    }
				else
				    sender_warning_told=false;


				if ((canvas.mk.AngleNick()>400)||(canvas.mk.AngleRoll()>400)||(canvas.mk.AngleNick()<-400)||(canvas.mk.AngleRoll()<-400))
				    {
					play("tilt");
					play("warning");
				    }

				volt_timeout--;
				
				if (info_from_debug_set!=canvas.mk.stats.debug_data_count)
				    { // only when newdata
					
					
					if (volt_timeout<0)
					    {
						if ((canvas.mk.UBatt()!=-1)&&(canvas.settings.do_volts_voice))
						    {
							if (!canvas.settings.minimal_voice) 
							    {
								play("battery");
								play("at");
							    }
							volts_play_cnt++;
							
							volt_timeout=(delay*1000)/BASE_SLEEP;
							int ubatt=canvas.mk.UBatt();
							info_from_debug_set=canvas.mk.stats.debug_data_count;
							play((ubatt/10));
							
							if(((ubatt%10)!=0)&& (!canvas.settings.voice_nopoint))
							    {
								play("point");
								play((ubatt%10));
								
							    }
							play("volts");
							
						    }

						if ((canvas.mk.Alt()!=-1)&&(canvas.settings.do_alt_voice))
						    {
							if (!canvas.settings.minimal_voice) {
							    play("altitude");
							    play("at");
							}

							
							play( canvas.mk.Alt()/10);
							if (!canvas.settings.voice_nopoint) {
							play("point");
							play( canvas.mk.Alt()%10); }
							
						
							play("meters");
						    }
						
						if ((canvas.mk.stats.flying_time()!=0))
						    {
							play("flight");
							play("time");
							switch (canvas.mk.stats.flying_time()/60)
							    {
							    case 0:
								break;		
							    case 1:
								play("1");
								play("minute");
								break;
							    default:
								play(canvas.mk.stats.flying_time()/60);
								play("minutes");
							    }

							switch (canvas.mk.stats.flying_time()%60)
							    {
							    case 0:
								//							    case 1:
								//								play("1");
								break;
							    default:
								play(canvas.mk.stats.flying_time()%60);
								play("seconds");
							    }

							
							
						    }

						switch (canvas.mk.SatsInUse())
						    {
						    case -1:
							// do nothing
							break;
						    case 1:
							play(1);
							play("satellite");


							break;
						    default:
							play(canvas.mk.SatsInUse());
							play("satellites");
							
						     }
					    }
					
					
					
					if (canvas.settings.do_altimeter_sound)
					    {
						if (last_alt==-1) last_alt=canvas.mk.Alt();
						
						
						if (last_alt>canvas.mk.Alt()+canvas.settings.altsteps)
						    {
							play("down");
							
							last_alt-=canvas.settings.altsteps;
						    }
						if (last_alt<canvas.mk.Alt()-canvas.settings.altsteps)
						    
						    {
							play("up");
							
							last_alt+=canvas.settings.altsteps;
						    }
					    }
					else
					    last_alt=-1;
				    }
			    }
			    }
			else
			    {
				if (!disconn_told)
				    play("disconnected");
				conn_told=false;
				disconn_told=true;
				
			    }
		    }

		try { 
		    Thread.sleep(BASE_SLEEP); 
		    
		    //if (delay<1)
		    //	Thread.sleep(1000); 
		    //else
		    //	Thread.sleep(delay*1000); 
		    
		}
		catch (Exception e)  {   }
		
	    }
    }
    

    
}

