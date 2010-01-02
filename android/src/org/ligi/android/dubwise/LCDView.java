package org.ligi.android.dubwise;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.*;
import android.graphics.Paint.Align;

// not working atm - import org.bluez.*;
import org.ligi.ufo.*;

public class LCDView extends View implements DUBwiseDefinitions, OnSharedPreferenceChangeListener

{
	private Paint mPaint = new Paint();
	SharedPreferences settings;
	public LCDView(Context context, AttributeSet attrs) {
		super(context, attrs);
		MKProvider.getMK().user_intent = USER_INTENT_LCD;			
	}

	public LCDView(Activity context) {
		super(context);
		settings = context.getSharedPreferences("DUBWISE", 0);
		// needed to get Key Events
		setFocusable(true);
	
		settings.registerOnSharedPreferenceChangeListener(this);
		refresh_settings(settings);
	}

    float[] line_points;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		char_height=w/15;
		char_width=w/20;
	}

	int char_height=0;
	int char_width=0;
	@Override
	protected void onDraw(Canvas canvas) {

		
		Paint textPaint=new Paint();
		
		textPaint.setColor(0xFF42DD42);
		textPaint.setAntiAlias(false);
		textPaint.setColor(0xFF000000);
		textPaint.setTextSize(char_height-5);
		textPaint.setTextAlign(Align.CENTER);
		//textPaint.setShadowLayer(2, 2, 2, 0xFFFFFFFF);
		textPaint.setFakeBoldText(true);
		for (int line=0;line<4;line++)
			{
			for (int c=0;c<20;c++)
			{
				mPaint.setColor(0xFF42DD43);
			
				canvas.drawRect(new Rect(c*(this.getWidth()/20)+1 , char_height*line+1, (c+1)*(this.getWidth()/20)-2  , char_height*(line+1)-2), mPaint);
				//canvas.drawRect(new Rect(c*(this.getWidth()/20) , char_height*line, (c+1)*(this.getWidth()/20)  , char_height*(line+1)), mPaint);
				//canvas.drawText(""+MKProvider.getMK().LCD.get_act_page()[line], 10, 10+line*mPaint.getTextSize(), mPaint);
				//canvas.drawRect(new Rect(chr*(this.getWidth()/20) , char_height*line, (chr+1)*(this.getWidth()/20)  , char_height*(line+1)), mPaint);
				canvas.drawText("" + MKProvider.getMK().LCD.get_act_page()[line].charAt(c), c*(this.getWidth()/20)+this.getWidth()/40 , char_height*(line)-mPaint.getFontMetrics().bottom-2, textPaint);
				
			}}
		invalidate();
	}


	
	public void onSharedPreferenceChanged(SharedPreferences _settings, String arg1) {
		refresh_settings(_settings);
		System.out.println(" pref change !!!");
	}

	public void refresh_settings(SharedPreferences _settings) {
		MKProvider.getMK().user_intent = USER_INTENT_GRAPH;			
	}

}
