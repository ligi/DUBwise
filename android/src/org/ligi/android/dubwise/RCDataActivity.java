package org.ligi.android.dubwise;

import org.ligi.ufo.MKCommunicator;
import org.ligi.ufo.MKStickData;

import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;

import android.widget.TextView;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;


public class RCDataActivity extends Activity implements Runnable
{

	
	ProgressBar[] progress_bars;
	
	int channels = MKStickData.MAX_STICKS;

	// public MapView map;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCalls.beforeContent(this);

		TableLayout table=new TableLayout(this);

		
		LayoutParams lp=new TableLayout.LayoutParams();
		lp.width=LayoutParams.FILL_PARENT;
		table.setLayoutParams(lp);
		
		table.setColumnStretchable(1, true);
		progress_bars = new ProgressBar[channels];
		
		for (int i = 0; i < channels; i++) {
			TableRow row=new TableRow(this);
			table.addView(row);
			
			row.setPadding(0, 5, 0, 5);
			
			
			progress_bars[i] = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);;
			progress_bars[i].setMax(256);
			progress_bars[i].setVerticalFadingEdgeEnabled(false);
			progress_bars[i].setPadding(10, 0, 5, 0);
			
			
			TextView tmp_text = new TextView(this);
			tmp_text.setText("Channel " + i  );
			row.addView(tmp_text);
		
			
			row.addView(progress_bars[i]);
		}

		setContentView(table);

		new Thread(this).start();

	}


	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
	}

	
	@Override
	public void run() {
		MKProvider.getMK().user_intent=MKCommunicator.USER_INTENT_RCDATA;
		while (!this.isFinishing()) {
			for (int i=0;i<channels;i++)
				progress_bars[i].setProgress(MKProvider.getMK().stick_data.stick[i]+127);
				
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {	}
			
		}
	}
}
