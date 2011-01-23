package org.ligi.android.dubwise.uavtalk;


import org.ligi.tracedroid.logging.Log;
import org.openpilot.uavtalk.UAVObjectFieldDescription;
import org.openpilot.uavtalk.UAVObjects;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;


public class UAVObjectFieldEdit {

	public static EditText edit_number(Context ctx,UAVObjectFieldDescription descr,final byte pos,int flags) {
		EditText txt=new EditText(ctx);
		txt.setInputType(InputType.TYPE_CLASS_NUMBER | flags);
		txt.setText(UAVObjectFieldHelper.getFieldValueStr(descr));
		
		class myTxtWatcher implements TextWatcher {

			UAVObjectFieldDescription descr;
			
			public myTxtWatcher(UAVObjectFieldDescription descr) {
				this.descr=descr;
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				try {
				switch (descr.getType()) {
					
					case UAVObjectFieldDescription.FIELDTYPE_FLOAT32:
						UAVObjects.getObjectByID(descr.getObjId()).setField(descr.getFieldId(),pos, Float.parseFloat(s.toString()));
						break;

					case UAVObjectFieldDescription.FIELDTYPE_UINT8:
					case UAVObjectFieldDescription.FIELDTYPE_UINT16:
					case UAVObjectFieldDescription.FIELDTYPE_UINT32:
					case UAVObjectFieldDescription.FIELDTYPE_INT8:
					case UAVObjectFieldDescription.FIELDTYPE_INT16:
					case UAVObjectFieldDescription.FIELDTYPE_INT32:
						UAVObjects.getObjectByID(descr.getObjId()).setField(descr.getFieldId(),pos, Integer.parseInt(s.toString()));
						break;
				}
				} catch (java.lang.NumberFormatException e) { } // be robust non valid user input 
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
		}
		
		txt.addTextChangedListener(new myTxtWatcher(descr) );
		
		return txt;
		
	}
	
	public static void editField(Context  ctx,int objid,int fieldid,final byte arr_pos,ArrayAdapter arr_adapt) {

		UAVObjectFieldDescription descr=UAVObjects.getObjectByID(objid).getFieldDescriptions()[fieldid];
		AlertDialog.Builder alert=(new AlertDialog.Builder(ctx)).setTitle("Edit " + descr.getName());
		
		
		switch (descr.getType()) {
			case UAVObjectFieldDescription.FIELDTYPE_ENUM:
				Spinner s=new Spinner(ctx);
				
								
				ArrayAdapter<String> enum_adapter=new ArrayAdapter<String>(ctx,R.layout.simple_spinner_item,descr.getEnumOptions());
				enum_adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
				s.setAdapter(enum_adapter);
				s.setSelection((Byte) UAVObjects.getObjectByID(descr.getObjId()).getField(descr.getFieldId(),arr_pos)); 
				
				alert.setView(s);
				//s.setOnItemSelectedListener(new foo extends OnItemSelectedListener)
				class myItemSelectedListener implements OnItemSelectedListener {
					
					private UAVObjectFieldDescription descr;
					private ArrayAdapter arr_adapt2notify;
					
					public myItemSelectedListener(UAVObjectFieldDescription descr,ArrayAdapter arr_adapt2nofify) {
						this.descr=descr;
						this.arr_adapt2notify= arr_adapt2nofify;
					}
					
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						Log.d("Setting to " + arg2);
						UAVObjects.getObjectByID(descr.getObjId()).setField(descr.getFieldId(),arr_pos,(byte)arg2);
						arr_adapt2notify.notifyDataSetChanged();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
					
				}
				s.setOnItemSelectedListener(new myItemSelectedListener(descr,arr_adapt));
				
				break;
			
			case UAVObjectFieldDescription.FIELDTYPE_FLOAT32:
				alert.setView(edit_number(ctx,descr,arr_pos,InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED));
				break;
				
			case UAVObjectFieldDescription.FIELDTYPE_INT8:
			case UAVObjectFieldDescription.FIELDTYPE_INT16:
			case UAVObjectFieldDescription.FIELDTYPE_INT32:
				alert.setView(edit_number(ctx,descr,arr_pos,InputType.TYPE_NUMBER_FLAG_SIGNED));
				break;

			case UAVObjectFieldDescription.FIELDTYPE_UINT8:
			case UAVObjectFieldDescription.FIELDTYPE_UINT16:
			case UAVObjectFieldDescription.FIELDTYPE_UINT32:
				alert.setView(edit_number(ctx,descr,arr_pos,0));
				break;
		}
		
		alert.setPositiveButton("OK", null).show();
		
	}
}
