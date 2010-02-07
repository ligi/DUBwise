package org.ligi.android.dubwise.con;

import org.ligi.android.dubwise.R;
import org.ligi.android.dubwise.R.id;
import org.ligi.android.dubwise.R.layout;
import org.ligi.android.dubwise.helper.ActivityCalls;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.CompoundButton.OnCheckedChangeListener;

import android.app.Activity;

import android.content.SharedPreferences;

public class ConnectViaTCPActivity extends Activity implements OnClickListener, OnCheckedChangeListener {

	// public MapView map;
	/** Called when the activity is first created. */
    
    EditText port_text;
    EditText host_text;
    EditText user_text;
    EditText pwd_text;
    
    CheckBox qmk_check;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityCalls.beforeContent(this);
		SharedPreferences settings = ActivityCalls.getSharedPreferences(this);

		setContentView(R.layout.connect_tcp);

		port_text=((EditText) findViewById(R.id.PortEditText));
		port_text.setText(""+settings.getInt("tcp-port", 64400) );
		
		user_text=((EditText) findViewById(R.id.UserNameEditText));
		user_text.setText(settings.getString("qmk-user", "anonymous") );
		
		pwd_text=((EditText) findViewById(R.id.PasswordEditText));
		pwd_text.setText(settings.getString("qmk-pwd", "") );
				
		host_text=((EditText) findViewById(R.id.HostEditText));
		host_text.setText(settings.getString("tcp-host", "127.0.0.1") );
		
		qmk_check=((CheckBox) findViewById(R.id.QMKCheckBox));
		qmk_check.setChecked(settings.getBoolean("tcp-qmk", true));
		
		qmk_check.setOnCheckedChangeListener(this);
		((Button) findViewById(R.id.ConnectButton)).setOnClickListener(this );
		
		updateQMKVisibility(qmk_check.isChecked());
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ActivityCalls.afterContent(this);
	}

	public void onClick( View arg0 ) {
        
		int port=Integer.parseInt(""+port_text.getText());
		String host=""+host_text.getText();
		boolean qmk=qmk_check.isChecked();
		
		MKProvider.getMK().do_log=false;
        MKProvider.getMK().connect_to(host_text.getText() + ":" + port , host_text.getText() + ":" + port_text.getText() );
    
        SharedPreferences settings = ActivityCalls.getSharedPreferences(this);
        
        SharedPreferences.Editor editor=settings.edit();
        
        editor.putInt("tcp-port",port);
        editor.putString("tcp-host",host);
        editor.putBoolean("tcp-qmk", qmk);
        
        editor.commit();
        
        Log.i("DUBwise","connecting to " + host +":" + port + " via tcp qmk:" + qmk );
        
        TCPConnectionAdapter tcp_com=new TCPConnectionAdapter(host,port,qmk);
        
        MKProvider.getMK().setCommunicationAdapter(tcp_com);
        
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCalls.onDestroy(this);
	}

	public void updateQMKVisibility(boolean visible)
	{
		((TableRow)findViewById(R.id.UserNameTableRow)).setVisibility(visible?View.VISIBLE:View.GONE);
		((TableRow)findViewById(R.id.PasswordTableRow)).setVisibility(visible?View.VISIBLE:View.GONE);

	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView==qmk_check)
			updateQMKVisibility(isChecked);
	
	}

}
