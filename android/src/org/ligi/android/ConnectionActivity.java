package org.ligi.android;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.text.method.NumberKeyListener;

import  	android.app.AlertDialog.*;
import  	android.app.AlertDialog;

import android.content.SharedPreferences;
/**
 * Demonstrates wrapping a layout in a ScrollView.
 *
 */
public class ConnectionActivity extends Activity {


    EditText port_edit;
    EditText host_edit;

    Activity here;

    DUBwise root;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection);
	here=this;
	
	SharedPreferences settings = getSharedPreferences("DUBwise", 0);

	port_edit=(EditText)findViewById( R.id.edit_port);
	host_edit=(EditText)findViewById( R.id.edit_host);

	port_edit.setKeyListener(new NumberKeyListener(){
		@Override
		    protected char[] getAcceptedChars() {

		    return new char[]{'1','2','3','4','5','6','7','8','9','0'};
		}
	    });

	host_edit.setText(settings.getString("conn_host","10.0.2.2"));
	port_edit.setText(settings.getString("conn_port","54321"));

	Button save_btn=(Button)findViewById( R.id.save_btn);

	save_btn.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 // Perform action on click


		 java.net.Socket connection;
		 java.io.InputStream	reader;    
		 java.io.OutputStream writer;    
		 try {
		 connection = new java.net.Socket(host_edit.getText().toString(),Integer.parseInt(port_edit.getText().toString())); 


		 SharedPreferences settings = getSharedPreferences("DUBwise", 0);
		 SharedPreferences.Editor editor = settings.edit();
		 editor.putString("conn_host", host_edit.getText().toString());
		 editor.putString("conn_port", port_edit.getText().toString());
		 
		 // Don't forget to commit your edits!!!
		 editor.commit();



		 finish();
		 }
		 catch (Exception e) {

		     new AlertDialog.Builder(here).setTitle("Connection Problem").setMessage("" + e.toString()).setPositiveButton("OK",null).create().show();

		 }
             }
         });


	Button cancel_btn=(Button)findViewById( R.id.cancel_btn);

	cancel_btn.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
		 finish();
             }
         });

	

    }
}
