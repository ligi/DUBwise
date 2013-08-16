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

package org.ligi.android.dubwise_mk;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.android.dubwise_mk.helper.DUBwiseStringHelper;
import org.ligi.ufo.MKCommunicator;
import org.ligi.ufo.MKParamsGeneratedDefinitionsToStrings;
import org.ligi.ufo.MKStickData;

/**
 * Activity to show the RC-Data
 *
 * @author ligi
 */
public class RCDataActivity extends BaseActivity implements Runnable, MKParamsGeneratedDefinitionsToStrings {

    private boolean dead = false;
    private ProgressBar[] progress_bars;
    private TextView[] text_overlays;

    private int channels = MKStickData.MAX_STICKS;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);
        LinearLayout lin = new LinearLayout(this);

        lin.setOrientation(LinearLayout.VERTICAL);

        progress_bars = new ProgressBar[channels];
        text_overlays = new TextView[channels];

        for (int i = 0; i < channels; i++) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

            FrameLayout frame = new FrameLayout(this);
            frame.setLayoutParams(lp);

            progress_bars[i] = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            ;
            progress_bars[i].setMax(256);
            progress_bars[i].setVerticalFadingEdgeEnabled(false);
            progress_bars[i].setPadding(7, 7, 7, 1);

            frame.addView(progress_bars[i]);

            text_overlays[i] = new TextView(this);
            String txt = "Channel " + (i + 1) + this.getResources().getString(DUBwiseStringHelper.table[PARAMID2STRINGID[MKProvider.getMK().params.stick_stringids[i]]]) + " ";
            text_overlays[i].setTag(txt);
            text_overlays[i].setTextColor(0xFF000000);
            text_overlays[i].setShadowLayer(2, 1, 1, 0xffffffff);
            text_overlays[i].setPadding(17, 7, 7, 1);
            frame.addView(text_overlays[i]);
            lin.addView(frame);
        }

        scroll.addView(lin);

        setContentView(scroll);

        dead = false;
        new Thread(this).start();

    }

    final Handler mHandler = new Handler();

    // Create runnable for posting
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            for (byte i = 0; i < channels; i++) {
                int stick_val = MKProvider.getMK().stick_data.getStickValue(i);
                progress_bars[i].setProgress(stick_val + 127);
                text_overlays[i].setText(text_overlays[i].getTag() + "(" + stick_val + ")");
            }
        }
    };

    public void run() {
        MKProvider.getMK().user_intent = MKCommunicator.USER_INTENT_RCDATA;
        while (!dead) {
            mHandler.post(mUpdateResults);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }

        }
    }


    @Override
    protected void onDestroy() {
        dead = true;
        super.onDestroy();
    }


}
