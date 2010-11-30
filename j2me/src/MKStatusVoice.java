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
import org.ligi.ufo.VesselData;

public class MKStatusVoice
    implements Runnable,PlayerListener
{

    DUBwiseCanvas canvas=null;
    Player player;

    public final int PLAYERSTATE_IDLE    =0;
    public final int PLAYERSTATE_PLAYING =1;
    public final int PLAYERSTATE_FIN     =2;


    public int act_player_state=PLAYERSTATE_IDLE;
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

      player = null;
      }
    }

    byte sound_method=0;


    public Player init_player(String name)
    {
	Player _player=null;
	try {
	    switch(sound_method)
		{
		case 0:
		    _player = Manager.createPlayer(getClass().getResourceAsStream(name+".mp3"), "AUDIO/MP3");
		    break;
		case 1:
		    _player = Manager.createPlayer(getClass().getResourceAsStream(name+".mp3"), "AUDIO/MPEG3");
		    break;
		case 2:
		    _player = Manager.createPlayer(getClass().getResourceAsStream(name+".mp3"), "audio/mpeg3");
		    break;
		case 3:
		    _player = Manager.createPlayer(getClass().getResourceAsStream(name+".mp3"), "audio/mp3");
			break;
		case 4:
		    _player = Manager.createPlayer(getClass().getResourceAsStream(name+".wav"), "AUDIO/X-WAV");
		}
	    
	    _player.addPlayerListener(this);
	    _player.realize();


	    vc = (VolumeControl) _player.getControl("VolumeControl");
	    if(vc != null) {
		vc.setLevel(canvas.settings.sound_volume);
	    }
	    
	    _player.prefetch();
	    _player.setLoopCount(1);
	    _player.start();	
	}
	catch (Exception e)  { 
	    try {    Thread.sleep(100); }
	    catch (Exception foo_e)  { 		}
	    sound_method=(byte)((sound_method+1)%5	);
	    _player=init_player(name);
	}	
	return _player;

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
    int voice_timeout=0;

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
		if ((canvas.mk!=null)&&(canvas.settings.do_sound)&&((canvas.mk.connected)&&(!canvas.mk.force_disconnect)))
		     
		    {
			if ((!conn_told)&&canvas.mk.version.known&&canvas.settings.do_intro_voice)
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


			if ((VesselData.attitude.getNick()>400)||(VesselData.attitude.getRoll()>400)||(VesselData.attitude.getNick()<-400)||(VesselData.attitude.getRoll()<-400))
			    {
				play("tilt");
				play("warning");
			    }

			voice_timeout++;
			
			if (info_from_debug_set!=canvas.mk.stats.debug_data_count)
			    { // only when newdata
				if (voice_timeout>(canvas.settings.voice_delay*1000)/BASE_SLEEP)
				    {
					voice_timeout=0;
					if ((VesselData.battery.getVoltage()!=-1)&&(canvas.settings.do_volts_voice))
					    {
						if (!canvas.settings.minimal_voice) 
						    {
							play("battery");
							play("at");
						    }
						volts_play_cnt++;
						
						int ubatt=VesselData.battery.getVoltage();
						info_from_debug_set=canvas.mk.stats.debug_data_count;
						play((ubatt/10));
						
						if(((ubatt%10)!=0)&& (!canvas.settings.voice_nopoint))
						    {
							play("point");
							play((ubatt%10));
							
						    }
						play("volts");
						
					    }

					if ((canvas.mk.getAlt()!=-1)&&(canvas.settings.do_alt_voice))
					    {
						if (!canvas.settings.minimal_voice) {
						    play("altitude");
						    play("at");
						}
						
						
						play( canvas.mk.getAlt()/10);
						if (!canvas.settings.voice_nopoint) {
						    play("point");
						    play( canvas.mk.getAlt()%10); }
						
						
						play("meters");
					    }
					
					if ((canvas.mk.stats.flying_time()!=0)&&(canvas.settings.do_flighttime_voice))
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
					
					if(canvas.settings.do_satelites_voice)	
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
					

					if(canvas.settings.do_current_voice&&(canvas.mk.getCurrent()!=-1))	
	

						{
						    play(canvas.mk.getCurrent()/10);
						    play("point");
						    play(canvas.mk.getCurrent()%10);
						    play("ampere");
						    
						}
					
				    }
				
				
					
				if (canvas.settings.do_altimeter_sound)
				    {
					if (last_alt==-1) last_alt=canvas.mk.getAlt();
					
					
					if (last_alt>canvas.mk.getAlt()+canvas.settings.altsteps)
					    {
						play("down");
						last_alt-=canvas.settings.altsteps;
					    }
					if (last_alt<canvas.mk.getAlt()-canvas.settings.altsteps)
					    {
						play("up");
						last_alt+=canvas.settings.altsteps;
					    }
				    }
				else
				    last_alt=-1;
			    }
		    }
	    
		if((!disconn_told)&&((!canvas.mk.connected)))//&&(!canvas.mk.force_disconnect)))
		    {
			play("disconnected");
			conn_told=false;
			disconn_told=true;
		    }
	    
	
		try { 
		    Thread.sleep(BASE_SLEEP); 
		}
		catch (Exception e)  {   }
		
	    }
    }
    
}

