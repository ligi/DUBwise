/*                                                                                                                          
 * This software is free software; you can redistribute it and/or modify                                                     
 * it under the terms of the GNU General Public License as published by                                                     
 * the Free Software Foundation; either version 3 of the License, or                                                        
 * (at your option) any later version.                                                                                      
 *                                                                                                                          
 * This program is distributed in the hope that it will be useful, but                                                      
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY                                               
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License                                                  
 * for more details.                                                                                                        
 *                                                                                                                          
 * You should have received a copy of the GNU General Public License along                                                  
 * with this program; if not, write to the Free Software Foundation, Inc.,                                                  
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA                                                                    
 */
package org.ligi.android.common.intents;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

/**
 * some static functions to help with intents/actions
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 * License GPLv3
 */

public class IntentHelper {

	/**
	 * checks if an action is avail and start it if it is or option to goto market to install
	 * 
	 * @param ctx - the Context
	 * @param action - the Action
	 * @return if started
	 */
	public static boolean action(Context ctx,String action) {
		if (!isIntentAvailable(ctx.getPackageManager(),new Intent(action))) {
			open_market_for_missing_action(ctx,action);
			return false;
		}
		
		try {
			ctx.startActivity(new Intent(action)); }
		catch (Exception e) { return false; }
		
		return true;
	}
	
	public static void goToMarketPackage(final Context ctx,String package_name) {
		ctx.startActivity(new Intent().setAction(Intent.ACTION_VIEW)
				.setData(Uri.parse("market://details?id="+package_name)));
	}
	
	public static void goToMarketSearch(final Context ctx,String query) {
		ctx.startActivity(new Intent().setAction(Intent.ACTION_VIEW)
		        .setData(Uri.parse("market://search?q=" + query)));
	}
	
	
	
	public static void open_market_for_missing_action(final Context ctx,final String action) {
		new AlertDialog.Builder(ctx).setTitle("App not found")
		 .setMessage("I am missing an APP to do that - please install!")
		 .setPositiveButton("OK", new OnClickListener() {

			public void onClick(DialogInterface arg0, int arg1) {
				goToMarketPackage(ctx,action);
			}
			 
		 })
		 .show();
	}
	
	/**
	 * @see action + 
	 * 
	 * @param ctx
	 * @param action
	 * 
	 *  @return if we started the action or have to install stuff first
	 */
	public static boolean action4result(Activity ctx,String action,int requestCode) {
		if (!isIntentAvailable(ctx.getPackageManager(),new Intent(action))) {
			open_market_for_missing_action(ctx,action);
			return false;
		}
		
		try {
			ctx.startActivityForResult(new Intent(action), requestCode); }
		catch (Exception e) {
			return false;
		}
		
		return true;
	}

	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 * 
	 * @param pm
	 * @param i
	 * @return
	 */
	public static boolean isIntentAvailable(PackageManager pm,Intent i) {
		return isIntentAvailable( pm,i,PackageManager.MATCH_DEFAULT_ONLY);
	}
	
	public static boolean isIntentAvailable(PackageManager pm,Intent i,int flags) {
		return pm.queryIntentActivities(i,flags).size() >0;
	}
	
	/**
	 * Indicates whether the specified action can be used as an service. This
	 * method queries the package manager for installed packages that can
	 * respond to an service with the specified action. If no suitable package is
	 * found, this method returns false.
	 * 
	 * @param pm
	 * @param i
	 * @param flags ( default PackageManager.MATCH_DEFAULT_ONLY )
	 * 
	 * @return
	 */
	public static boolean isServiceAvailable(Intent i,PackageManager pm) {
		return pm.queryIntentServices(i,PackageManager.MATCH_DEFAULT_ONLY).size() >0;
	}
	
	public static boolean isServiceAvailable(Intent i,PackageManager pm,int flags) {
		return pm.queryIntentServices(i,flags).size() >0;
	}
	
	@SuppressWarnings("rawtypes") // have to use raw type here
    public static void startActivityClass(Context ctx,Class activity_class) {
		ctx.startActivity(new Intent(ctx,activity_class));
	}
	
    public static class IntentStartOnClick implements View.OnClickListener{
    	private Intent i;
    	private Activity ctx;
    	
    	public IntentStartOnClick(Intent i,Activity ctx) {
    		this.i=i;
    		this.ctx=ctx;
    	}

		public void onClick(View v) {
			 ctx.startActivity(i);
		}
		
		public void bind2view(Button btn) {
			if (btn!=null)
				btn.setOnClickListener(this);
		}
		public void bind2view(int resId) {
			bind2view((Button)ctx.findViewById(resId));
		}
		public void bind2view(int resId,View parent) {
			bind2view((Button)parent.findViewById(resId));
		}

    }
}
