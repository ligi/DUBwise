package org.ligi.android.dubwise;

import android.content.Intent;

public class IconicMenuItem {
        
        int drawable;
        String label;
        Intent intent=null;
        int action=-1;
        
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