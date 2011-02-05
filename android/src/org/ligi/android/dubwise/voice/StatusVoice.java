/**************************************************************************
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
 *  Organizations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

package org.ligi.android.dubwise.voice;

import java.util.HashMap;
import java.util.Locale;

import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.android.dubwise.helper.DUBwiseBackgroundTask;
import org.ligi.tracedroid.logging.Log;
import org.ligi.ufo.MKCommunicator;
import org.ligi.ufo.VesselData;

import android.app.Activity;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
/**
 * 
 * Class to use TTS for presenting the user some status information
 *      
 * @author ligi
 *
 */
public class StatusVoice implements OnInitListener, DUBwiseBackgroundTask,
		OnUtteranceCompletedListener {

	private static StatusVoice thisRef;

	private TextToSpeech mTts;
	private boolean init_done = false;
	private HashMap<String, String> voice_params;

	private boolean init_started=false;

	private MKCommunicator mk = MKProvider.getMK();
	private int last_nc_flags=-1;
	private int last_fc_flags=-1;

	private boolean told_range_limit=false;
	private boolean told_mc_mode=false;
	
	private boolean told_tr_mode=false;

	private boolean told_ch_mode=false;
	private boolean told_free_mode=false;
	private boolean told_ph_mode=false;
	
	//private boolean told_rclost=false;
	
	private final static int sleep = 10;
	private int pause_timeout=0;

	private String last_spoken;
	private int play_pos = 0;

	private String last_version_str = "";

	private boolean running;
	private Activity activity;
	
	public void init(Activity activity) {
		this.activity=activity;
	}
	
	public boolean isInitStarted() {
		return init_started;
	}

	@Override
	public void onInit(int arg0) {
		
		mTts.setLanguage(Locale.US);

		voice_params = new HashMap<String, String>();
		voice_params.put("VOICE", "MALE");

		voice_params.put(TextToSpeech.Engine.KEY_PARAM_STREAM,	String.valueOf(VoicePrefs.getStreamEnum()));
		mTts.setOnUtteranceCompletedListener(this);
		init_done = true;
		new Thread(this).start();
		this.onUtteranceCompleted("start");
	}

	public static StatusVoice getInstance() {
		if (thisRef == null)
			thisRef = new StatusVoice();
		return thisRef;
	}

	public void stop() {
		running=false;
	}
	
	@Override
	public void run() {
		while (running) {

			// Log.i("DUBwise","mk"+mk.ready() + "  init"+ init_done);
			
			if (VoicePrefs.isVoiceEnabled()&&mk.ready() && init_done) {
				
			if (!mTts.isSpeaking()) { // no breaking news
				String what2speak="";
				//Log.i("DUBwise","not speaking");
				// breaking news section
				if ((!last_version_str.equals("" + mk.version.major + "."
						+ mk.version.minor + " "
						+ (char) ('A' + mk.version.patch)))
						&& (VoicePrefs.isConnectionInfoEnabled())) {
		
							last_version_str = "" + mk.version.major + "."
									+ mk.version.minor + " "
									+ (char) ('A' + mk.version.patch);
							if (mk.is_mk())
								what2speak+="Connected to Flight Control Version "
										+ last_version_str;
							else if (mk.is_navi())
								what2speak+="Connected to Navigation Control Version "
										+ last_version_str;
							else if (mk.is_mk3mag())
								what2speak+="Connected to Mikrokopter Compas Version "
										+ last_version_str;
							else if (mk.is_fake())
								what2speak+=
										"Connected to a Simulated connection Version "
												+ last_version_str;
							else
								what2speak+="Connected to the unknown "
										+ last_version_str;
					}
				
				else if (mk.is_navi()&&mk.gps_position.NCFlags!=last_nc_flags)	{
					last_nc_flags=mk.gps_position.NCFlags;

					if (mk.gps_position.isFreeModeEnabled()) {
						if (!told_free_mode)
							what2speak+=" Free Mode Enabled. ";
						told_free_mode=true;
					}
					else
						told_free_mode=false;
					
					if (mk.gps_position.isPositionHoldEnabled()) {
						if (!told_ph_mode)
							what2speak+=" Position Hold Enabled. ";
						told_ph_mode=true;
					}
					else
						told_ph_mode=false;
					
					if (mk.gps_position.isComingHomeEnabled()) {
						if (!told_ch_mode)
							what2speak+=" Coming Home Enabled ";
						told_ch_mode=true;
					}
					else
						told_ch_mode=false;

					if (mk.gps_position.isTargetReached())	{
						if (!told_tr_mode)
							what2speak+=" Target Reached. ";
						told_tr_mode=true;
					}
					else
						told_tr_mode=false;

					
					if (mk.gps_position.isManualControlEnabled()) {
						if (!told_mc_mode)
							what2speak+=" Manual Control Enabled. ";
						told_mc_mode=true;
					}
					else
						told_mc_mode=false;

					if (mk.gps_position.isRangeLimitReached()) 	{
						if (!told_range_limit)
							what2speak+=" Range Limit Reached. ";
						told_range_limit=true;
					}
					else
						told_range_limit=false;


				}
				else if (what2speak.equals("")&&mk.is_navi()&&mk.gps_position.FCFlags!=last_fc_flags)
				{
					last_fc_flags=mk.gps_position.FCFlags;

					if (mk.gps_position.areEnginesOn())
						what2speak+=" Engines are on ";
					else
						what2speak+=" Engines are off ";
					
					if (mk.gps_position.isFlying())
						what2speak+=" UFO is flying ";
					

					if (mk.gps_position.isCalibrating())
						what2speak+=" UFO is Calibrating ";
					
				
				}

				if ((VoicePrefs.isVoiceRCLostEnabled())&&what2speak.equals("")) {
					if (mk.SenderOkay()<190) {
						what2speak+=" RC Signal lost";
						//told_rclost=true;
						}
/*					else
						told_rclost=false;
*/
					}
				
				if (((pause_timeout--)<0)&&what2speak.equals("")) {
					
					switch ((play_pos++) % 14) {
						case 0:
							if (mk.is_navi() && mk.hasNaviError()
									&& VoicePrefs.isVoiceNaviErrorEnabled()) {
								what2speak+=
										" Navigation Control has the following Error "
												+ mk.getNaviErrorString();
					
							}
							break;
						case 1:
	
							if ((VesselData.battery.getVoltage() != -1)
									&& VoicePrefs.isVoiceVoltsEnabled()) {
								what2speak+=" Battery at " + VesselData.battery.getVoltage() / 10.0 /*
																			 * +"."
																			 * +
																			 * mk.UBatt
																			 * ()%10
																			 */
										+ " Volts.";
					
							}
							break;
	
						case 2:
							if ((VesselData.battery.getCurrent() != -1)
									&& VoicePrefs.isVoiceCurrentEnabled()) {
								what2speak+=" Consuming " + VesselData.battery.getCurrent() / 10.0
										+ " Ampere";
								
							}
							break;
	
						case 3:
							if ((VesselData.battery.getCurrent() != -1)
									&& VoicePrefs.isVoiceCurrentEnabled()) {
								what2speak+=" thats "
										+ (VesselData.battery.getCurrent() * VesselData.battery.getVoltage()) / 100
										+ " Wats";
							}
	
							break;
	
						case 4:
	
							if ((VesselData.battery.getUsedCapacity() != -1)
									&& VoicePrefs.isVoiceUsedCapacityEnabled()) {
								what2speak+=" Consumed " + VesselData.battery.getUsedCapacity()
										+ " milliamperehours";
	
							}
							break;
	
						case 5:
	
							if ((mk.getAlt() != -1)
									&& (VoicePrefs.isVoiceAltEnabled())) {
								what2speak+=" Current height is " + mk.getAlt() / 10
										+ " meters.";
	
							}
							
							
							break;
							
						case 6:	
							if ((mk.gps_position.Distance2Home >0)
									&& (VoicePrefs.isDistance2HomeEnabled())) {
								what2speak+=" Distance to Home " + mk.gps_position.Distance2Home/10
										+ " meters.";
	
							}
							break;
	
						case 7:	
							if ((mk.gps_position.Distance2Target >0)
									&& (VoicePrefs.isDistance2TargetEnabled())) {
								what2speak+=" Distance to Target " + mk.gps_position.Distance2Target/10
										+ " meters.";
	
							}
							break;
							
						case 8:
							if ((mk.stats.flying_time()>0)
									&& (VoicePrefs.isFlightTimeEnabled())) {
										what2speak+=" Flight time";
										if ((mk.stats.flying_time()/60)!=0)
											what2speak+=" " + mk.stats.flying_time()/60 + " minutes ";
										
										
										if ((mk.stats.flying_time()%60)!=0)
											what2speak+=" " + mk.stats.flying_time()%60 + " seconds. ";
										
										
							}
							break;
					
							
						case 9:
							if ((mk.is_navi() || mk.is_fake())
									&& (VoicePrefs.isVoiceSatelitesEnabled())) {
								what2speak+= " " + mk.SatsInUse()
										+ " Satelites are used for GPS.";
	
							}
	
							break;
	
						case 10:
							if ((mk.is_navi() || mk.is_fake())
									&& (VoicePrefs.isSpeedEnabled())) {
								what2speak+= " Current speed " + formatSpeedValue(mk.gps_position.GroundSpeed)
										+ " kilometer per hour.";
	
							}
	
							break;
	
						case 11:
							if ((mk.is_navi() || mk.is_fake())
									&& (VoicePrefs.isMaxSpeedEnabled())) {
								what2speak+= " Max speed " + formatSpeedValue(mk.stats.max_speed)
										+ " kilometer per hour. ";
	
							}
							break;
	
						case 12:
	
							if ((mk.stats.max_alt != -1)
									&& (VoicePrefs.isVoiceMaxAltEnabled())) {
								what2speak+=" Max height was " + mk.stats.max_alt / 10
										+ " meters.";
	
							}
							
							
							break;
	
						case 13:
							pause_timeout=VoicePrefs.getPauseTimeInMS()/sleep;
							break;
					} // switch
				}

				if (!what2speak.equals("")) {
					last_spoken=what2speak;
					mTts.speak(what2speak, TextToSpeech.QUEUE_ADD, voice_params);
				 }
				}
			}
			try {
				Thread.sleep(sleep);
				// Log.i("DUBwise" ,""+ mTts.isSpeaking());
			} catch (InterruptedException e) {
			}
		}
	}


	public String getLastSpoken() {
		return last_spoken;
	}
	public int getPauseTimeout() {
		return pause_timeout;
	}
	
	public void setPauseTimeout(int new_timeout) {
		pause_timeout=new_timeout;
	}
	
	public String formatSpeedValue(int speed) {
		return "" + ((int)(speed*0.36)/10) + "." +((int)(speed*0.36)%10);
	}
	
	@Override
	public void onUtteranceCompleted(String arg0) {
		Log.i( "onuterancecomplete");
	}

	@Override
	public String getDescription() {
		return "Speaking values of UAV";
	}

	@Override
	public String getName() {
		return "StatusVoice";
	}

	@Override
	public void start() {

		if (isInitStarted()) // don't do it again
			return;
		init_started=true;
		VoicePrefs.init(activity.getApplication());
		mTts = new TextToSpeech(activity.getApplication(), this);
		running=true;	
	}
}
