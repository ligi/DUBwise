/**************************************************************************
 *
 * Activity to let the user easily access UAV related information
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

package org.ligi.android.dubwise_mk;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import org.ligi.android.dubwise_mk.helper.IconicAdapter;
import org.ligi.android.dubwise_mk.helper.IconicMenuItem;

import java.util.ArrayList;

public class InformationDeskActivity
        extends BaseListActivity {

    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<IconicMenuItem> menu_items_vector = new ArrayList<IconicMenuItem>();

        menu_items_vector.add(new IconicMenuItem("DUBwise wiki Page", android.R.drawable.ic_menu_info_details, new Intent("android.intent.action.VIEW",
                Uri.parse("http://mikrokopter.de/ucwiki/en/DUBwise"))));

        menu_items_vector.add(new IconicMenuItem("The Authors Blog", android.R.drawable.ic_menu_info_details, new Intent("android.intent.action.VIEW",
                Uri.parse("http://ligi.de"))));
        
        /* dam not working
        menu_items_vector.add(new IconicMenuItem("Mikrokopter Chat" , android.R.drawable.ic_menu_info_details, new Intent( "android.intent.action.VIEW", 
        		Uri.parse( "irc://irc.freenode.net/mikrokopter"))));
        */

        menu_items_vector.add(new IconicMenuItem("BL-Circuit 1.1", android.R.drawable.ic_menu_info_details, new Intent("android.intent.action.VIEW",
                Uri.parse("http://mikrokopter.de/ucwiki/BL-Ctrl_V1_1?action=AttachFile&do=get&target=BL_CtrlV1_1_sch.gif"))));
        menu_items_vector.add(new IconicMenuItem("BL-Circuit 1.2", android.R.drawable.ic_menu_info_details, new Intent("android.intent.action.VIEW",
                Uri.parse("http://gallery.mikrokopter.de/main.php?g2_view=core.DownloadItem&g2_itemId=39357"))));
        menu_items_vector.add(new IconicMenuItem("BL-Jumper", android.R.drawable.ic_menu_info_details, new Intent("android.intent.action.VIEW",
                Uri.parse("http://www.mikrokopter.de/ucwiki/BL-Ctrl_V1.2?action=AttachFile&do=get&target=Adr_Tabelle.gif"))));
        menu_items_vector.add(new IconicMenuItem("Calibrate MK3Mag", android.R.drawable.ic_menu_info_details, new Intent("android.intent.action.VIEW",
                Uri.parse("http://gallery.mikrokopter.de/main.php?g2_view=core.DownloadItem&g2_itemId=34488"))));
        menu_items_vector.add(new IconicMenuItem("FC TestPoints", android.R.drawable.ic_menu_info_details, new Intent("android.intent.action.VIEW",
                Uri.parse("http://gallery.mikrokopter.de/main.php?g2_view=core.DownloadItem&g2_itemId=18131"))));

        this.setListAdapter(new IconicAdapter(this, (menu_items_vector.toArray())));

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        IconicMenuItem item = ((IconicMenuItem) (this.getListAdapter().getItem(position)));

        if (item.intent != null) {
            startActivity(item.intent);
        }

    }

}
