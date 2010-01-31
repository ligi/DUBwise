package org.ligi.android.dubwise;

import org.ligi.android.dubwise.con.MKProvider;
import org.ligi.android.dubwise.helper.ActivityCalls;
import org.ligi.ufo.MKCommunicator;
import org.ligi.ufo.MKStickData;

import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

import android.widget.TextView;

import android.app.Activity;

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

		ScrollView scroll=new ScrollView(this);
		
		TableLayout table=new TableLayout(this);
		
		scroll.addView(table);
		
		//LayoutParams lp=new TableLayout.LayoutParams();
		//lp.width=LayoutParams.FILL_PARENT;
		//table.setLayoutParams(lp);
		
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
			tmp_text.setText("Channel " + (i+1)  );
			row.addView(tmp_text);
		
			
			row.addView(progress_bars[i]);
		}

		setContentView(scroll);

		new Thread(this).start();

	}


	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
	}

	
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
