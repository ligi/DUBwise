/**************************************************************************
 *                                          
 * Author:  Marcus -LiGi- Bueschleb   
 *
 * Project URL:
 *  http://mikrokopter.de/ucwiki/en/DUBwise
 * 
 * License:
 *  http://creativecommons.org/licenses/by-nc-sa/2.0/de/ 
 *  (Creative Commons / Non Commercial / Share Alike)
 *  Additionally to the Creative Commons terms it is not allowed
 *  to use this project in _any_ violent manner! 
 *  This explicitly includes that lethal Weapon owning "People" and 
 *  Organisations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

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