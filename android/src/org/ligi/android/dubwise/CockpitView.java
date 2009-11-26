package org.ligi.android.dubwise;

import android.app.Activity;
import android.view.View;
import android.graphics.*;

// not working atm - import org.bluez.*;
import org.ligi.ufo.*;

public class CockpitView extends View implements DUBwiseDefinitions

{
	private Paint mPaint = new Paint();

	public CockpitView(Activity context) {
		super(context);

		// needed to get Key Events
		setFocusable(true);


	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	}

	@Override
	protected void onDraw(Canvas canvas) {

		Paint paint = mPaint;
		paint.setAntiAlias(true);

		paint.setARGB(255, 255, 0, 0);

        canvas.rotate((MKProvider.getMK().AngleRoll()*(-90))/3000,getWidth()/2,getHeight()/2);
        paint.setARGB(255,177,129,0);
        // roll rect                                                                                                                       
        canvas.drawRect(-getWidth(),getHeight()/2,2*getWidth(),3*getHeight()/2,paint);

        int bar_height=20;
        // nick rect                                                                                                                       
        paint.setARGB(200,0,200,0);
        canvas.drawRoundRect(new RectF(getWidth()/3,getHeight()/2 -bar_height/2 + MKProvider.getMK().AngleNick()*getHeight()/(3*3000) ,2*getWidth()/3, getHeight()/2+ MKProvider.getMK().AngleNick()*getHeight()/(3*3000)+bar_height),5,5,paint);


        canvas.restore();

//        paint.setARGB(state_intro_frame,0,0,255);
        //              canvas.drawRoundRect(new RectF(getWidth()/2-getWidth()/8,getHeight()/2-getWidth()/8,getWidth()/2+getWidth()/8,getH\
//eight()/2+getWidth()/8),5,5,paint);                                                                                                                

        //canvas.drawRoundRect(new RectF(flight_x,flight_y,flight_x+getWidth()/8,flight_y+getWidth()/8),5,5,paint);
        //paint.setARGB(255,0,0,0);

        
		invalidate();
	}
}
