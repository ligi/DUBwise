/**************************************************************************
 *                                          
 * View to Draw a top bar with information like Voltage / Connection state
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

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.*;

import org.ligi.android.common.bitmap.BitmapScaler;
import org.ligi.android.dubwise.conn.MKProvider;
import org.ligi.ufo.*;

public class TopView extends View {
	
	private Paint mPaint = new Paint();
	private Paint mTextPaint = new Paint();
	private Bitmap bt_off_img, bt_on_img, batt_img, rc_img, sats_img , alert_img, bt_on_highlight_img;
	private int act_symbol_pos = 0;
	private int spacer_items = 5;

	public TopView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TopView(Activity context) {
		super(context);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		
		// load and scale the images
		bt_off_img = BitmapScaler.relative2View(this,BitmapFactory.decodeResource(
				getResources(), R.drawable.bluetooth_off), 0.0f, 1f);

		bt_on_img = BitmapScaler.relative2View(this,BitmapFactory.decodeResource(
				getResources(), R.drawable.bluetooth_on), 0.0f, 1f);
		
		bt_on_highlight_img =BitmapScaler.relative2View(this,BitmapFactory.decodeResource(
				getResources(), R.drawable.bluetooth_on_highlight), 0.0f, 1f);

		batt_img = BitmapScaler.relative2View(this,BitmapFactory.decodeResource(
				getResources(), R.drawable.batt), 0.0f, 1f);

		sats_img = BitmapScaler.relative2View(this,BitmapFactory.decodeResource(
				getResources(), R.drawable.sats), 0.0f, 1f);

		rc_img = BitmapScaler.relative2View(this,BitmapFactory.decodeResource(getResources(),
				R.drawable.rc), 0.0f, 1f);
		alert_img = BitmapScaler.relative2View(this,BitmapFactory.decodeResource(getResources(),
				R.drawable.alert), 0.0f, 1f);

		// set up the Paint's
		mTextPaint.setColor(Color.BLUE);
		mTextPaint.setAntiAlias(false); // text looks better without alias
		mTextPaint.setFakeBoldText(true);
		mTextPaint.setShadowLayer(2, 2, 2, Color.BLACK);

		mTextPaint.setTextSize(this.getHeight());

	}

	public void symbol_paint(Canvas c, Bitmap img) {
		c.drawBitmap(img, act_symbol_pos, 0, mPaint);
		act_symbol_pos += img.getWidth();
	}

	public void textPaint(Canvas c, String text) {
		c.drawText(text, act_symbol_pos,this.getHeight() - 5, mTextPaint);
		act_symbol_pos += getTextWidth(text);
	}

	private float getTextWidth(String text) {
		float[] widths = new float[text.length()];
		mTextPaint.getTextWidths(text, widths);
		float res = 0;
		for (int i = 0; i < widths.length; i++)
			res += widths[i];
		return res;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		MKCommunicator mk = MKProvider.getMK();

		act_symbol_pos = 0;

		// connection

		
		if (mk.connected){
			if (((mk.stats.bytes_in>>4)&1)==1)
				symbol_paint(canvas, bt_on_img);
			else
				symbol_paint(canvas, bt_on_highlight_img);

			act_symbol_pos += spacer_items;

			// if (mk.UBatt()!=-1)
			// mPaint.getFontMetrics().
			
			if (VesselData.battery.getVoltage() != -1) {
				symbol_paint(canvas, batt_img);
				textPaint(canvas,""+ VesselData.battery.getVoltage() / 10.0);
				act_symbol_pos += spacer_items;
			}

			if (mk.SenderOkay() >190) {
				symbol_paint(canvas, rc_img);
				act_symbol_pos += spacer_items;
			}

			if (mk.is_navi() || mk.is_fake()) {
				if (mk.SatsInUse() != -1) {
					symbol_paint(canvas, sats_img);
					textPaint( canvas, ""+ mk.SatsInUse());
					act_symbol_pos += spacer_items;
				}
				if (mk.hasNaviError()) {
					symbol_paint(canvas, alert_img);
					act_symbol_pos += spacer_items;
				}
			}

		}
		else
			symbol_paint(canvas, bt_off_img);

		// spend some cpu time ( Top doesnt need to be updated that often )
		//TODO make timing editable
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			
		}
		
		invalidate();
	}

}
