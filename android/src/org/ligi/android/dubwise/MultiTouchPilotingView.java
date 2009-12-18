package org.ligi.android.dubwise;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.graphics.*;

public class MultiTouchPilotingView extends View implements OnTouchListener

{
	private Paint mPaint = new Paint();
	public MultiTouchPilotingView(Activity context) {
		super(context);
		this.setOnTouchListener(this );
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	}

	@Override
	protected void onDraw(Canvas canvas) {
	    
	    

	    mPaint.setColor(0xCCCCCCCC);
	    canvas.drawCircle(x1,y1,30,mPaint );
	    canvas.drawCircle(x2,y2,30,mPaint );
	    
		invalidate();
	}
	float x1=0;
	float x2=0;
	float y1=0;
	float y2=0;
	
    public boolean onTouch( View arg0, MotionEvent arg1 ) {
      
        x1=arg1.getX(0 );
        x2=arg1.getX(1 );
        y1=arg1.getY(0 );
        y2=arg1.getY(1);
        Log.i("DUBwise" , "got event"  +x1);
        return true;
    }

}
