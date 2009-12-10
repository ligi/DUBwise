package org.ligi.android.dubwise;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import android.app.Activity;
import android.content.IntentFilter;


public class MotorTestaActivity extends Activity implements OnSeekBarChangeListener {

	public final static int ENGINE_WARNING_THRESHOLD = 23;
	SeekBar[] seek_bars;
	SeekBar seek_all;

	Toast toast;
	CheckBox allow_full_speed;

	int engines = 4;

	// public MapView map;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityCalls.beforeContent(this);
		
		LinearLayout linear = new LinearLayout(this);

		linear.setOrientation(LinearLayout.VERTICAL);

		toast = Toast
				.makeText(
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
			seek_bars[i].setPadding(8, 1,	 0, 1);
			TextView tmp_text = new TextView(this);

			tmp_text.setText("Engine " + i);
			linear.addView(tmp_text);
			linear.addView(seek_bars[i]);
		}

		TextView tmp_text = new TextView(this);
		tmp_text.setText("All Engines");
		
		linear.addView(tmp_text);

		seek_all = new SeekBar(this);
		seek_all.setOnSeekBarChangeListener(this);
		seek_all.setPadding(8, 1,	 0, 1);
		linear.addView(seek_all);

		setContentView(linear);

	}


	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
		
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub

		if ((!fromUser))
			return;

		if ((progress > ENGINE_WARNING_THRESHOLD)
				&&
				(!allow_full_speed.isChecked()))
		{
			seekBar.setProgress(ENGINE_WARNING_THRESHOLD);
			toast.show();
		}

		if (seekBar == seek_all) {
			for (int i = 0; i < engines; i++)
				seek_bars[i].setProgress(seekBar.getProgress());
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

}
