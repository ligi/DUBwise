package org.ligi.android.dubwise;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.*;

// not working atm - import org.bluez.*;
import org.ligi.android.dubwise.con.MKProvider;
import org.ligi.ufo.*;

public class TopView extends View

{
	private Paint mPaint = new Paint();
	private Paint mTextPaint = new Paint();

	public TopView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TopView(Activity context) {
		super(context);
	}

	Bitmap bt_off_img, bt_on_img, batt_img, rc_img, sats_img , alert_img, bt_on_highlight_img;

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		
		// load and scale the images
		bt_off_img = resize_to_screen(BitmapFactory.decodeResource(
				getResources(), R.drawable.bluetooth_off), 0.0f, 1f);

		bt_on_img = resize_to_screen(BitmapFactory.decodeResource(
				getResources(), R.drawable.bluetooth_on), 0.0f, 1f);
		
		bt_on_highlight_img =resize_to_screen(BitmapFactory.decodeResource(
				getResources(), R.drawable.bluetooth_on_highlight), 0.0f, 1f);

		batt_img = resize_to_screen(BitmapFactory.decodeResource(
				getResources(), R.drawable.batt), 0.0f, 1f);

		sats_img = resize_to_screen(BitmapFactory.decodeResource(
				getResources(), R.drawable.sats), 0.0f, 1f);

		rc_img = resize_to_screen(BitmapFactory.decodeResource(getResources(),
				R.drawable.rc), 0.0f, 1f);
		alert_img = resize_to_screen(BitmapFactory.decodeResource(getResources(),
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

	int act_symbol_pos = 0;

	private float getTextWidth(String text) {

		float[] widths = new float[text.length()];
		mTextPaint.getTextWidths(text, widths);
		float res = 0;
		for (int i = 0; i < widths.length; i++)
			res += widths[i];
		return res;
	}

	int spacer_items = 5;

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
		}
		else
			symbol_paint(canvas, bt_off_img);

		act_symbol_pos += spacer_items;

		// if (mk.UBatt()!=-1)
		// mPaint.getFontMetrics().
		
		if (mk.UBatt() != -1) {
			symbol_paint(canvas, batt_img);
			canvas.drawText("" + mk.UBatt() / 10.0, act_symbol_pos, this
					.getHeight() - 5, mTextPaint);
			act_symbol_pos += getTextWidth("" + mk.UBatt() / 10.0);
			act_symbol_pos += spacer_items;
		}

		if (mk.SenderOkay() >190) {
			symbol_paint(canvas, rc_img);
			act_symbol_pos += spacer_items;
		}

		if (mk.is_navi() || mk.is_fake()) {
			if (mk.SatsInUse() != -1) {
				symbol_paint(canvas, sats_img);
				canvas.drawText("" + mk.SatsInUse(), act_symbol_pos, this
						.getHeight() - 5, mTextPaint);
				act_symbol_pos += getTextWidth("" + mk.SatsInUse());
				act_symbol_pos += spacer_items;
			}
			if (mk.gps_position.ErrorCode != 0) {
				symbol_paint(canvas, alert_img);
				act_symbol_pos += spacer_items;
			}
		}
		
		// spend some cpu time ( Top doesnt need to be updated that often )
		//TODO make timing editable
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			
		}
		
		invalidate();
	}

	public Bitmap resize_to_screen(Bitmap orig, float x_scale_, float y_scale_) {
		// createa matrix for the manipulation
		Matrix matrix = new Matrix();
		float x_scale, y_scale;
		if (y_scale_ != 0f)
			y_scale = (getHeight() * y_scale_) / orig.getHeight();
		else
			// take x_scale
			y_scale = (getWidth() * x_scale_) / orig.getWidth();

		if (x_scale_ != 0f)
			x_scale = (getWidth() * x_scale_) / orig.getWidth();
		else
			x_scale = (getHeight() * y_scale_) / orig.getHeight();

		matrix.postScale(x_scale, y_scale);
		return Bitmap.createBitmap(orig, 0, 0, (int) (orig.getWidth()),
				(int) (orig.getHeight()), matrix, true);// BitmapContfig.ARGB_8888
		// );
	}

}
