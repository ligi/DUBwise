/**************************************************************************
 *                                          
 * View to draw the MK LCD
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

package org.ligi.android.dubwise_mk.lcd;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.*;
import android.graphics.Paint.Align;

import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.ufo.*;

public class LCDView extends View implements DUBwiseDefinitions

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
	
	}

    float[] line_points;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		char_height=w/10;
		char_width=w/20;
	}

	int char_height=0;
	int char_width=0;
	@Override
	protected void onDraw(Canvas canvas) {
		Paint textPaint=new Paint();
		
		textPaint.setColor(0xFF00ff01);		
		textPaint.setAntiAlias(false);
		
		textPaint.setTextSize(char_height*0.65f);
		textPaint.setTextAlign(Align.CENTER);
		//textPaint.setShadowLayer(2, 2, 2, 0xFFFFFFFF);
		textPaint.setFakeBoldText(true);
		for (int line=0;line<4;line++)	{
			for (int c=0;c<20;c++)	{
				mPaint.setColor(0xFF004000);
			
				canvas.drawRect(new RectF((this.getWidth()*c/20.0f)+1.0f , char_height*line+1, (this.getWidth()*(c+1)/20.0f)-2.0f  , char_height*(line+1)-2), mPaint);
				//canvas.drawRect(new Rect(c*(this.getWidth()/20) , char_height*line, (c+1)*(this.getWidth()/20)  , char_height*(line+1)), mPaint);
				//canvas.drawText(""+MKProvider.getMK().LCD.get_act_page()[line], 10, 10+line*mPaint.getTextSize(), mPaint);
				//canvas.drawRect(new Rect(chr*(this.getWidth()/20) , char_height*line, (chr+1)*(this.getWidth()/20)  , char_height*(line+1)), mPaint);
				canvas.drawText("" + MKProvider.getMK().LCD.get_act_page()[line].charAt(c), (float)c*(this.getWidth()/20.0f)+this.getWidth()/40.0f , char_height*(line)+3.0f*char_height/4.0f, textPaint);
			}}
		invalidate();
	}
	
}
