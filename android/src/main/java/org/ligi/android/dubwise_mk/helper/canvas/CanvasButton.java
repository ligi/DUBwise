/****************************************************
 *
 * Author:        Marcus -LiGi- Bueschleb     
 *
 * see README for further Info
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
 *****************************************************/

package org.ligi.android.dubwise_mk.helper.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 */
public class CanvasButton {

    private RectF rect;
    private Bitmap bitmap;
    private Paint highlightPaint, normalPaint;

    private float icon_size = 64.0f;
    private boolean highlight = false;
    private CanvasButtonAction action = null;
    private String label;

    public CanvasButton(Context ctx, int resid, String label) {
        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),
                resid), (int) icon_size, (int) icon_size, true);
        rect = new RectF(0.0f, 0.0f, icon_size, icon_size);

        highlightPaint = new Paint();
        highlightPaint.setStrokeWidth(4.0f);
        highlightPaint.setColor(0x77EEEE33);
        highlightPaint.setAntiAlias(true);
        highlightPaint.setShadowLayer(3.0f, 2.0f, 2.0f, 0x77223322);

        normalPaint = new Paint();
        normalPaint.setTextAlign(Align.RIGHT);
        normalPaint.setFakeBoldText(true);

        normalPaint.setTextSize((4 * icon_size) / 5.0f);
        normalPaint.setShadowLayer(3.0f, 2.0f, 2.0f, 0xBBFFFFFF);

        normalPaint.setAntiAlias(true);

        //label=ctx.getString(txt_resid);
        this.label = label;
    }

    public void layout(float left, float top) {
        rect = new RectF(left, top, left + icon_size, top + icon_size);
    }

    public void draw(Canvas canvas) {
        if (highlight) {
            //canvas.drawCircle(rect.left+icon_size/2, rect.top+icon_size/2, icon_size, highlightPaint);

            Rect _txt_bounds = new Rect();
            normalPaint.getTextBounds(label, 0, label.length(), _txt_bounds);
            RectF txt_bounds = new RectF(_txt_bounds);

            txt_bounds.offsetTo(rect.left - icon_size / 2 - txt_bounds.width() - icon_size, rect.top + normalPaint.getTextSize());

            txt_bounds.union(rect);

            //canvas.drawCircle(rect.left+icon_size/2, rect.top+icon_size/2, icon_size, highlightPaint);
            canvas.drawRoundRect(new RectF(txt_bounds), 7.0f, 7.0f, highlightPaint);
            canvas.drawText(label, rect.left - icon_size / 2, rect.top + normalPaint.getTextSize(), normalPaint);
        }
        canvas.drawBitmap(bitmap, rect.left, rect.top, normalPaint);
    }

    public void setAction(CanvasButtonAction action) {
        this.action = action;
    }

    public boolean isTouched(MotionEvent e) {
        if (rect.contains(e.getX(), e.getY())) {
            highlight = true;

            if (e.getAction() == MotionEvent.ACTION_UP) {
                if (action != null)
                    action.action();

                highlight = false;
            }

            return true;
        }
        highlight = false;
        return false;
    }

    public float getIconSize() {
        return icon_size;
    }

    public RectF getRect() {
        return rect;
    }

}
