/**************************************************************************
 *                                          
 * Static helper functions for DUBwise
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

package org.ligi.android.dubwise.helper;

import android.graphics.Paint;

public class DUBwiseHelper {

    public static float getTextWidth(String text,Paint paint) {

        float[] widths = new float[text.length()];
        paint.getTextWidths(text, widths);
        float res = 0;
        for (int i = 0; i < widths.length; i++)
            res += widths[i];
        return res;
    }
}
