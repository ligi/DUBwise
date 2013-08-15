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

package org.ligi.android.dubwise_mk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.androidhelper.AndroidHelper;
import org.ligi.ufo.MKCommunicator;
import org.ligi.ufo.VesselData;

public class TopView extends View {

    private Paint mPaint = new Paint();
    private Paint mTextPaint = new Paint();
    private Bitmap bt_off_img, bt_on_img, batt_img, rc_img, sats_img, alert_img, bt_on_highlight_img;
    private int act_symbol_pos = 0;
    private int spacer_items = 5;

    public TopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopView(Activity context) {
        super(context);
    }

    /**
     * load an icon and scale to the height of this View
     *
     * @param resId - ResId of the Bitmap to load
     * @return
     */
    private Bitmap loadSymbol(int resId) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resId);
        return AndroidHelper.at(bmp).scaleRelative2View(this, 0.0f, 1f);
    }

    /**
     * paint a symbol and move the act_symbol_pos
     *
     * @param c
     * @param img
     */
    private void paintSymbol(Canvas c, Bitmap img) {
        c.drawBitmap(img, act_symbol_pos, 0, mPaint);
        act_symbol_pos += img.getWidth();
    }

    /**
     * paint a text and move the act_symbol_pos
     *
     * @param c
     * @param text
     */
    private void paintText(Canvas c, String text) {
        c.drawText(text, act_symbol_pos, this.getHeight() - 5, mTextPaint);
        act_symbol_pos += AndroidHelper.at(mTextPaint).getTextWidth(text);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        // load and scale the images
        bt_off_img = loadSymbol(R.drawable.bluetooth_off);
        bt_on_img = loadSymbol(R.drawable.bluetooth_on);
        bt_on_highlight_img = loadSymbol(R.drawable.bluetooth_on_highlight);
        batt_img = loadSymbol(R.drawable.batt);
        sats_img = loadSymbol(R.drawable.sats);
        rc_img = loadSymbol(R.drawable.rc);
        alert_img = loadSymbol(R.drawable.alert);

        // set up the Paint's
        mTextPaint.setColor(Color.BLUE);
        mTextPaint.setAntiAlias(false); // text looks better without alias
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setShadowLayer(2, 2, 2, Color.BLACK);

        mTextPaint.setTextSize(this.getHeight());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        MKCommunicator mk = MKProvider.getMK();

        act_symbol_pos = 0;

        // connection

        if (mk.connected) {
            if (((mk.stats.bytes_in >> 4) & 1) == 1)
                paintSymbol(canvas, bt_on_img);
            else
                paintSymbol(canvas, bt_on_highlight_img);

            act_symbol_pos += spacer_items;

            // if (mk.UBatt()!=-1)
            // mPaint.getFontMetrics().

            if (VesselData.battery.getVoltage() != -1) {
                paintSymbol(canvas, batt_img);
                paintText(canvas, "" + VesselData.battery.getVoltage() / 10.0);
                act_symbol_pos += spacer_items;
            }

            if (mk.SenderOkay() > 190) {
                paintSymbol(canvas, rc_img);
                act_symbol_pos += spacer_items;
            }

            if (mk.is_navi() || mk.is_fake()) {
                if (mk.SatsInUse() != -1) {
                    paintSymbol(canvas, sats_img);
                    paintText(canvas, "" + mk.SatsInUse());
                    act_symbol_pos += spacer_items;
                }
                if (mk.hasNaviError()) {
                    paintSymbol(canvas, alert_img);
                    act_symbol_pos += spacer_items;
                }
            }

        } else
            paintSymbol(canvas, bt_off_img);

        // spend some cpu time ( Top doesnt need to be updated that often )
        //TODO make timing editable
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {

        }

        invalidate();
    }

}
