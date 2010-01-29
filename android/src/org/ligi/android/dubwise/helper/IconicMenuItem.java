package org.ligi.android.dubwise.helper;

import android.content.Intent;

public class IconicMenuItem {
        
        public int drawable;
        public String label;
        public Intent intent=null;
        public int action=-1;
        
        public IconicMenuItem( String label , int drawable,Intent intent) {
            this.drawable=drawable;
            this.label=label;
            this.intent=intent;
        }
        
        public IconicMenuItem( String label , int drawable,int action) {
            this.drawable=drawable;
            this.label=label;
            this.action=action;
        }
        
        
    }