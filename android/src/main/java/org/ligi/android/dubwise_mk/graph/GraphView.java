/**************************************************************************
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

package org.ligi.android.dubwise_mk.graph;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.ufo.DUBwiseDefinitions;

// not working atm - import org.bluez.*;

/**
 * View part of the Graph
 *
 * @author Marcus -LiGi- Bueschleb
 */
public class GraphView extends View implements DUBwiseDefinitions, OnSharedPreferenceChangeListener

{
    private Paint mPaint = new Paint();

    public final static int[] graph_colors = {0xFF0aff15, 0xFFCC1315,
            0xFFf8ef02, 0xFF1638ff};
    public int[] graph_sources = {0, 1, 2, 3};
    public String[] graph_names = {"nick int", "roll int", "nick acc",
            "roll acc"};

    public final static int GRAPH_COUNT = 4;
    private int canvas_height = 100;
    private int canvas_width = 100;
    private int line_middle_y = 100;

    private boolean do_grid;
    private boolean do_legend;
    private SharedPreferences settings;

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GraphView(Activity context) {
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
        canvas_width = w;
        line_middle_y = h / 2;
        canvas_height = h;

        MKProvider.getMK().setup_debug_buff(graph_sources, w, 1);
        line_points = new float[canvas_width * 4];
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Paint paint = mPaint;
        paint.setAntiAlias(false);

        paint.setARGB(255, 255, 0, 0);

		/*
         canvas.drawText("cw " +canvas_width + ":" +
		 MKProvider.getMK().debug_data.analog[0] + " -- "+
		 MKProvider.getMK().debug_buff_lastset + " legend " + do_legend,
		 0, 0, paint);
		 */
        int line_scaler = MKProvider.getMK().debug_buff_max / (canvas_height / 2) + 1;

        if (do_grid) {

            int scale = 10;
            //                      if ((10/line_scaler)<5)scale =1;                                                       
            if (((10 / line_scaler) * 2) < (canvas_height / 2)) scale = 10;
            if (((50 / line_scaler) * 2) < (canvas_height / 2)) scale = 50;
            if (((100 / line_scaler) * 2) < (canvas_height / 2)) scale = 100;
            if (((250 / line_scaler) * 2) < (canvas_height / 2)) scale = 250;
            if (((500 / line_scaler) * 2) < (canvas_height / 2)) scale = 500;
            if (((1000 / line_scaler) * 2) < (canvas_height / 2)) scale = 1000;
            if (((10000 / line_scaler) * 2) < (canvas_height / 2)) scale = 10000;

            // g.drawString("scale:"+scale + "line scaler" + line_scaler,0,y_off,Graphics.TOP | Graphics.LEFT);            


            int jump = 0;
            paint.setStrokeWidth(0);
            paint.setAntiAlias(false);

            paint.setColor(Color.WHITE);
            canvas.drawLine(0, line_middle_y, canvas_width, line_middle_y, paint);

            while ((jump / line_scaler) < canvas_height) {
                paint.setTextAlign(Align.LEFT);
                canvas.drawText("" + jump, 0, line_middle_y - jump / line_scaler, paint);
                if (jump != 0)
                    canvas.drawText("-" + jump, 0, line_middle_y + jump / line_scaler, paint);
                canvas.drawLine(0, line_middle_y - jump / line_scaler, canvas_width, line_middle_y - jump / line_scaler, paint);
                canvas.drawLine(0, line_middle_y + jump / line_scaler, canvas_width, line_middle_y + jump / line_scaler, paint);
                jump += scale;
            }
        } // do grid


        for (int gr = 0; gr < GRAPH_COUNT; gr++) {

            paint.setStrokeWidth(0);

            // !!TODO checkme
            paint.setColor(graph_colors[gr]);

            paint.setStrokeWidth(0);
            // check_local_max(mk.debug_data.analog[graph_sources[gr]]);

            //int line_scaler = MKProvider.getMK().debug_buff_max / canvas_height
            //	+ 1;


            for (int x = 0; x < canvas_width - 1; x++) {
				/*
				 * int p= (((-graph_offset+x-canvas_width-5))); if (p<1)
				 * p+=graph_data[0].length; p%=(graph_data[0].length-1);
				 * 
				 * 
				 * draw_graph_part(g,x,graph_data[gr][p]/line_scaler,graph_data[gr
				 * ][p+1]/line_scaler);
				 */

                int p = (((MKProvider.getMK().debug_buff_off + x - canvas_width)));
                if (p < 0)
                    p += MKProvider.getMK().debug_buff_len;
                p %= (MKProvider.getMK().debug_buff_len - 2);

                if (p < MKProvider.getMK().debug_buff_lastset)

                // draw_graph_part(g,x,mk.debug_buff[p][gr]/line_scaler,mk.debug_buff[p+1][gr]/line_scaler);
                {
                    line_points[x * 4] = x;
                    line_points[x * 4 + 1] = line_middle_y - MKProvider.getMK().debug_buff[p][gr] / line_scaler;
                    line_points[x * 4 + 2] = x + 1;
                    line_points[x * 4 + 3] = line_middle_y
                            - MKProvider.getMK().debug_buff[p + 1][gr]
                            / line_scaler;
                }
			    /*
					canvas.drawLine(x, line_middle_y
							- MKProvider.getMK().debug_buff[p][gr]
							/ line_scaler, x + 1, line_middle_y
							- MKProvider.getMK().debug_buff[p + 1][gr]
							/ line_scaler, paint);
*/
                // canvas.drawLine(x,10,x+1,10,paint);
                // System.out.println("--p"+p + "gr " + gr + " l " +
                // mk.debug_buff.length +" ls:" + mk.debug_buff_lastset );


            }
            canvas.drawLines(line_points, paint);
        }

        if (do_legend) {
            //          paint.setPathEffect( new DashPathEffect(new float[] { 15, 5, 8, 5 }, 1.0f) );
            paint.setColor(0xaaFFFFFF);
            canvas.drawRoundRect(new RectF(canvas_width - 20, canvas_height - (paint.getTextSize() * GRAPH_COUNT), canvas_width, canvas_height), 5f, 5f, paint);

            for (int d = 0; d < GRAPH_COUNT; d++) {
                paint.setColor(graph_colors[d]);
                paint.setTextAlign(Align.RIGHT);

                canvas.drawRect(canvas_width - 20, canvas_height - paint.getTextSize() * (d + 1) + (paint.getTextSize()) / 2 - 2, canvas_width, canvas_height - paint.getTextSize() * (d + 1) + (paint.getTextSize()) / 2 + 2, paint);
                paint.setColor(Color.WHITE);
                canvas.drawText(graph_names[d], canvas_width - 23, canvas_height - paint.getTextSize() * (d), paint);
                //                            y_off+=spacer_small;

            }

            //     g.setStrokeStyle(Graphics.SOLID);
        }

        invalidate();
    }


    public void onSharedPreferenceChanged(SharedPreferences _settings, String arg1) {
        refresh_settings(_settings);
        System.out.println(" pref change !!!");
    }

    public void refresh_settings(SharedPreferences _settings) {
        do_grid = _settings.getBoolean("do_grid", true);
        do_legend = _settings.getBoolean("do_legend", true);

        MKProvider.getMK().user_intent = USER_INTENT_GRAPH;
    }

}
