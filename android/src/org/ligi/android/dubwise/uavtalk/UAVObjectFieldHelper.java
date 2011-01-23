package org.ligi.android.dubwise.uavtalk;

import org.openpilot.uavtalk.UAVObjectFieldDescription;
import org.openpilot.uavtalk.UAVObjects;

public class UAVObjectFieldHelper {

	public static String getFieldValueStr(UAVObjectFieldDescription field_desc) {
	    return getFieldValueStr(field_desc,(byte)0);
	}

	public static String getFieldValueStr(UAVObjectFieldDescription field_desc,byte elm) {
	    Object act_val_obj=UAVObjects.getObjectByID(field_desc.getObjId()).getField(field_desc.getFieldId(),elm);
	    switch (field_desc.getType()) {
        	case UAVObjectFieldDescription.FIELDTYPE_INT8:
        		return ""+((Byte)act_val_obj);
        		
        	case UAVObjectFieldDescription.FIELDTYPE_INT16:
        	case UAVObjectFieldDescription.FIELDTYPE_INT32:
        	case UAVObjectFieldDescription.FIELDTYPE_UINT16:
        	case UAVObjectFieldDescription.FIELDTYPE_UINT8:
        		return ""+(Integer)act_val_obj;	
        		
        	case UAVObjectFieldDescription.FIELDTYPE_UINT32:
        		return ""+(Long)act_val_obj;	
        		
        	case UAVObjectFieldDescription.FIELDTYPE_ENUM:
        		return field_desc.getEnumOptions()[(Byte)act_val_obj];
        		
        	case UAVObjectFieldDescription.FIELDTYPE_FLOAT32:
        		return "" + (Float)act_val_obj;
        		
        }
	    return "error";
	}
}
