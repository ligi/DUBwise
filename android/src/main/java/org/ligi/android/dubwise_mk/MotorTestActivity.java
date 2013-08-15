/**************************************************************************
 *
 * Activity to test the Motors
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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.android.dubwise_mk.helper.ActivityCalls;
import org.ligi.tracedroid.logging.Log;

public class MotorTestActivity extends Activity implements OnSeekBarChangeListener, Runnable {

    public final static int ENGINE_WARNING_THRESHOLD = 23;
    private SeekBar[] seek_bars;
    private SeekBar seek_all;

    private Toast toast;
    private CheckBox allow_full_speed;

    private int engines = 4;
    private boolean stopped = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        engines = MKProvider.getMK().mixer_manager.getLastUsedEngine() + 1;

        ActivityCalls.beforeContent(this);

        ScrollView scroll = new ScrollView(this);

        LinearLayout linear = new LinearLayout(this);
        scroll.addView(linear);

        linear.setOrientation(LinearLayout.VERTICAL);

        toast = Toast.makeText(
                this,
                "Value too Dangerous - Clipping! Activate 'Allow Full Speed' to Override",
                Toast.LENGTH_LONG);

        seek_bars = new SeekBar[engines];

        allow_full_speed = new CheckBox(this);
        allow_full_speed.setText("allow full speed");
        linear.addView(allow_full_speed);

        for (int i = 0; i < engines; i++) {
            seek_bars[i] = new SeekBar(this);
            seek_bars[i].setOnSeekBarChangeListener(this);
            seek_bars[i].setPadding(8, 1, 0, 1);
            TextView tmp_text = new TextView(this);

            tmp_text.setText("Motor " + (i + 1));
            linear.addView(tmp_text);
            linear.addView(seek_bars[i]);
        }

        TextView tmp_text = new TextView(this);
        tmp_text.setText("All Engines");
        tmp_text.setPadding(0, 10, 0, 0);
        linear.addView(tmp_text);

        seek_all = new SeekBar(this);
        seek_all.setOnSeekBarChangeListener(this);
        seek_all.setPadding(8, 1, 0, 1);
        linear.addView(seek_all);

        setContentView(scroll);

    }

    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        // TODO Auto-generated method stub

        if ((!fromUser))
            return;

        if ((progress > ENGINE_WARNING_THRESHOLD)
                &&
                (!allow_full_speed.isChecked())) {
            seekBar.setProgress(ENGINE_WARNING_THRESHOLD);
            toast.show();
        }

        if (seekBar == seek_all) {
            for (int i = 0; i < engines; i++)
                seek_bars[i].setProgress(seekBar.getProgress());
        }

    }


    public void onStartTrackingTouch(SeekBar arg0) {
    }


    public void onStopTrackingTouch(SeekBar seekBar) {
    }


    @Override
    public void onPause() {
        super.onPause();
        stopped = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityCalls.afterContent(this);
        new Thread(this).start();
    }


    @Override
    public void run() {
        while (!stopped) {
            try {
                Thread.sleep(100);

                int[] mt_param = new int[12];

                for (int i = 0; i < 12; i++)
                    mt_param[i] = 0;

                for (int i = 0; i < engines; i++)
                    mt_param[i] = seek_bars[i].getProgress();


                MKProvider.getMK().motor_test(mt_param);

                Log.d("motortest updated");
            } catch (InterruptedException e) {
                // sleep no important
            }
        }
    }


    @Override
    protected void onDestroy() {
        ActivityCalls.onDestroy(this);
        super.onDestroy();
    }
}
