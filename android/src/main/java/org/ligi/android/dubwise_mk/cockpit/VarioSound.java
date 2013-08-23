package org.ligi.android.dubwise_mk.cockpit;

import android.media.AudioManager;
import android.media.SoundPool;

import org.ligi.android.dubwise_mk.app.App;
import org.ligi.ufo.MKCommunicator;

import java.util.HashMap;


public class VarioSound implements Runnable {

    private SoundPool mSoundPool;
    private HashMap<Integer, Integer> mSoundPoolMap;
    private AudioManager mAudioManager;
    private App mContext;
    public final static int SOUND_UP = 1;
    public final static int SOUND_DOWN = 2;

    public VarioSound(App theContext) {
        return;
        /*
		mContext = theContext;
        mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        mSoundPoolMap = new HashMap<Integer, Integer> ();
        mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);

        addSound(SOUND_UP,R.raw.up);
        addSound(SOUND_DOWN,R.raw.down);
        
        new Thread(this).start();
        
        last_audible_alt=getMK().getAlt();
        */
    }

    private MKCommunicator getMK() {
        return mContext.getMK();
    }

    private int last_audible_alt;
    private final static int ALT_SOUND_PLAY_DISTANCE = 5;

    @Override
    public void run() {
        while (true) {

            if (mContext.getMK().isConnected()) {
                if (last_audible_alt - getMK().getAlt() > ALT_SOUND_PLAY_DISTANCE) {
                    playSound(SOUND_UP);
                    last_audible_alt = getMK().getAlt();
                } else if (last_audible_alt - getMK().getAlt() < -ALT_SOUND_PLAY_DISTANCE) {
                    playSound(SOUND_DOWN);
                    last_audible_alt = getMK().getAlt();
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }


    public void addSound(int index, int SoundID) {
        mSoundPoolMap.put(index, mSoundPool.load(mContext, SoundID, 1));
    }

    public void playSound(int index) {
        float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
    }

  	
  	
      /*
       * 
    // originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
 //   private final float duration = 0.2f; // seconds
	private final int sampleRate = 8000;
    
    public VarioSound() {
    	
    	genTone();
    	  
    	
    	new Thread(this).start();
    }
    
    void playSound(){
    	//audioTrack.play();
        //audioTrack.release();
        //audioTrack.
    }
    
    public void play_tone(double  freqOfTone,float duration) {
    	
    	int numSamples = (int)(duration * sampleRate);
    	double sample[] = new double[numSamples];
    	final byte generatedSnd[] = new byte[2 * numSamples];
    	   
    	 // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }    
    	
    	AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
        sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
        AudioFormat.ENCODING_PCM_16BIT, numSamples,
        AudioTrack.MODE_STATIC);

		audioTrack.write(generatedSnd, 0, generatedSnd.length);
		
		//playSound();
		audioTrack.play();
	
		try {
			
			Thread.sleep((int)(duration*500f));
		} catch (InterruptedException e) {
		}
		audioTrack.release();

    }
    
	@Override
	public void run() {
		while (true) {				
				
			play_tone(440d,0.3f);
		}
	}

	void genTone(){
       
    }
*/

}
