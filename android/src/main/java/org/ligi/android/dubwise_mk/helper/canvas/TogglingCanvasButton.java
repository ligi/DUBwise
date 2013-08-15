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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 */
public class TogglingCanvasButton extends CanvasButton {

    public boolean toggled = false;
    private Paint togglePaint;

    public TogglingCanvasButton(Context ctx, int resid, String txt) {
        super(ctx, resid, txt);

        togglePaint = new Paint();
        togglePaint.setStrokeWidth(4.0f);
        togglePaint.setColor(0x7722EE33);
        togglePaint.setAntiAlias(true);
        togglePaint.setShadowLayer(3.0f, 2.0f, 2.0f, 0x77223322);
    }

    public void draw(Canvas canvas) {
        if (toggled)
            canvas.drawCircle(this.getRect().centerX(), this.getRect().centerY(), this.getIconSize() / 2.0f, togglePaint);

        super.draw(canvas);
    }

    public boolean isTouched(MotionEvent e) {
        boolean res = super.isTouched(e);
        if (res && (e.getAction() == MotionEvent.ACTION_UP))
            toggled = !toggled;
        return res;
    }
}
