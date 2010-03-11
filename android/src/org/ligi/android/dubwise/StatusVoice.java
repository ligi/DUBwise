/**************************************************************************
 *                                          
 * Class to use TTS for presenting the user some status information
 *                                          
 * Author:  Marcus -LiGi- Bueschleb   
 *
 * Project URL:
 *  http://mikrokopter.de/ucwiki/en/DUBwise
 * 
 * License:
 *  http://creativecommons.org/licenses/by-nc-sa/2.0/de/ 
 *  (Creative Commons / Non Commercial / Share Alike)
 *  Additionally to the Creative Commons terms it is not allowed
 *  to use this project in _any_ violent manner! 
 *  This explicitly includes that lethal Weapon owning "People" and 
 *  Organisations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

package org.ligi.android.dubwise;

import java.util.HashMap;
import java.util.Locale;

import org.ligi.android.dubwise.con.MKProvider;
import org.ligi.ufo.MKCommunicator;

import android.app.Activity;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;

public class StatusVoice implements OnInitListener,Runnable, OnUtteranceCompletedListener {

	private static StatusVoice thisRef;
	
	private TextToSpeech mTts;
	private boolean init_done=false;
	private HashMap<String,String> voice_params;
	
	private boolean told_connection=false;
	
	
	public void init(Activity activity) {
		mTts = new TextToSpeech(activity, this);
		
		
	}
	
	@Override
	public void onInit(int arg0) {
		mTts.setLanguage(Locale.US);
	
		voice_params = new HashMap<String,String>();
		voice_params.put("VOICE", "MALE");
		
		voice_params.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
		        //String.valueOf(AudioManager.STREAM_ALARM));
				String.valueOf(AudioManager.STREAM_NOTIFICATION));
		mTts.setOnUtteranceCompletedListener(this);
		init_done=true;
		//new Thread(this).start();
		this.onUtteranceCompleted("start");
	}
	
	
	public static  StatusVoice getInstance() {
		if (thisRef==null)
			thisRef=new StatusVoice();
		
		return thisRef;
	}

	MKCommunicator mk=MKProvider.getMK();	
	
	@Override
	public void run() {
		
		while(true) {

			//Log.i("DUBwise","mk"+mk.ready() + "  init"+ init_done);
			int sleep=10;
			if (mk.ready() && init_done )
			{
				Log.i("DUBwise","will_speak" + mk.ready());

				// TODO do breaking news
				if (!mTts.isSpeaking())
					{
					
					mTts.stop();
					
					// 	isSpeaking() returns true too early - waiting a bit
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {	}

					int uterance=0;
					

								
				}

			
			}
			
	
			
			//for (int i=0;i<1000;i++)
			try {
				Thread.sleep(sleep);
				//Log.i("DUBwise" ,""+ mTts.isSpeaking());
			} catch (InterruptedException e) {	}
		}
	}

	int play_pos=0;
	
	String last_version_str="";
	
	@Override
	public void onUtteranceCompleted(String arg0) {
		
		voice_params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
        "start uterance pp" + play_pos);
		Log.i("DUBwise", "onuterancecomplete");
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}

		
		while (true)
		{
			if (!mk.ready()) {
				mTts.playSilence(100,  TextToSpeech.QUEUE_ADD, voice_params);
				return;
			}
			
			if (!last_version_str.equals(""+mk.version.major +"."+mk.version.minor + " " + (char)('A'+mk.version.patch)))
			{
				last_version_str=""+mk.version.major +"."+mk.version.minor + " " + (char)('A'+mk.version.patch);
				if (mk.is_mk())
					mTts.speak("Connected to Flight Control Version " + last_version_str,  TextToSpeech.QUEUE_FLUSH, voice_params);
				else if (mk.is_navi())
					mTts.speak("Connected to Navigation Control Version " + last_version_str,  TextToSpeech.QUEUE_FLUSH, voice_params);
				else if (mk.is_mk3mag())
					mTts.speak("Connected to Mikrokopter Compas Version " + last_version_str,  TextToSpeech.QUEUE_FLUSH, voice_params);
				else if (mk.is_fake())
					mTts.speak("Connected to a Simulated connection Version " + last_version_str,  TextToSpeech.QUEUE_FLUSH, voice_params);
				else
					mTts.speak("Connected to the unknown " + last_version_str,  TextToSpeech.QUEUE_FLUSH, voice_params);
				//mTts.speak("Connected to Mikrokopter version 0.78 A.",  TextToSpeech.QUEUE_FLUSH, voice_params);
				
				told_connection=true;
				return;
			}
				
		switch ((play_pos++)%8) {
			case 0:
				if(mk.is_navi()&&mk.hasNaviError())
					{
					mTts.speak("Navigation Control has the following Error " + mk.getNaviErrorString(),  TextToSpeech.QUEUE_ADD, voice_params);	
					return;
					}
				break;
			case 1:

				if (mk.UBatt()!=-1) {
					mTts.speak("Battery at " + mk.UBatt()/10.0 /*+"." + mk.UBatt()%10 */  + " Volts.",  TextToSpeech.QUEUE_ADD, voice_params);
					return;
					}
				break;
			
			case 2:
				if (mk.getCurrent()!=-1) {
					mTts.speak("Consuming " + mk.getCurrent()/10.0 + " Ampere",  TextToSpeech.QUEUE_ADD, voice_params);
					return;
				}
				break;

			case 3:
				if (mk.getCurrent()!=-1) {
					mTts.speak("thats " + (mk.getCurrent()*mk.UBatt())/100 + " Wats",  TextToSpeech.QUEUE_ADD, voice_params);
					return;
				}
				
				break;
				
			case 4:
			
				if (mk.getUsedCapacity()!=-1) {
					mTts.speak("Consumed " + mk.getUsedCapacity() + " milliamperehours",  TextToSpeech.QUEUE_ADD, voice_params);
					return;
				}
				break;
		
			case 5: 
				if (mk.getAlt()!=-1) {
					mTts.speak("Current height is " + mk.getAlt()/10 + " meters." ,  TextToSpeech.QUEUE_ADD, voice_params);
					return;
				}
				break;
				

			case 6: 
				if (mk.is_navi() || mk.is_fake())
					{
					mTts.speak(""+mk.SatsInUse()+ " Satelites are used for GPS.",  TextToSpeech.QUEUE_ADD, voice_params);	
					return;
					}
				
				break;
			
			case 7:
				mTts.playSilence(5000,  TextToSpeech.QUEUE_ADD, voice_params);
				return;
				
			
		}
		
		}
				
		
			
		
		
	/*	
		
		
		
		mTts.playSilence(500,  TextToSpeech.QUEUE_ADD, voice_params);
*/
	}
	
}
