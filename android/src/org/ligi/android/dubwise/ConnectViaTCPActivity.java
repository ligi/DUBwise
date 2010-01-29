package org.ligi.android.dubwise;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Button;

import android.app.Activity;

import android.content.SharedPreferences;

public class ConnectViaTCPActivity extends Activity implements OnClickListener {

	// public MapView map;
	/** Called when the activity is first created. */
    
    EditText port_text;
    EditText host_text;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityCalls.beforeContent(this);
		SharedPreferences settings = getSharedPreferences("DUBWISE", 0);

		setContentView(R.layout.connect_tcp);
		
		
		port_text=((EditText) findViewById(R.id.PortEditText));
		port_text.setText("54321" );
		
		host_text=((EditText) findViewById(R.id.HostEditText));
		host_text.setText("192.168.1.42" );
		
		((Button) findViewById(R.id.ConnectButton)).setOnClickListener(this );
		
		/*((CheckBox) findViewById(R.id.LegendCheckBox)).setChecked(settings.getBoolean("do_legend",true));
		((CheckBox) findViewById(R.id.GridCheckBox)).setChecked(settings.getBoolean("do_grid",true));
		System.out.println("do grid" + settings.getBoolean("do_grid",true));
		 */
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
	}

	public void onClick( View arg0 ) {
        //MKProvider.getMK().do_log=true;
        MKProvider.getMK().do_log=false;
        MKProvider.getMK().connect_to(host_text.getText() + ":" + port_text.getText() , host_text.getText() + ":" + port_text.getText() );
    }

}
