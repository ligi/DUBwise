package org.ligi.android.dubwise;

import java.util.Vector;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import android.util.Log;

public class InformationDeskActivity
        extends ListActivity {

     /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            ActivityCalls.beforeContent(this);
        
        Vector<IconicMenuItem> menu_items_vector=new Vector<IconicMenuItem>();
        

        menu_items_vector.add(new IconicMenuItem("Jumper" , android.R.drawable.ic_menu_info_details, new Intent( "android.intent.action.VIEW", Uri.parse( "http://www.mikrokopter.de/ucwiki/BL-Ctrl_V1.2?action=AttachFile&do=get&target=Adr_Tabelle.gif" ))));
        menu_items_vector.add(new IconicMenuItem("BL-Circuit 1.1" , android.R.drawable.ic_menu_info_details, new Intent( "android.intent.action.VIEW", Uri.parse( "http://mikrokopter.de/ucwiki/BL-Ctrl_V1_1?action=AttachFile&do=get&target=BL_CtrlV1_1_sch.gif"))));
        menu_items_vector.add(new IconicMenuItem("BL-Circuit 1.2" , android.R.drawable.ic_menu_info_details, new Intent( "android.intent.action.VIEW", Uri.parse( "http://gallery.mikrokopter.de/main.php/v/tech/BL_CTRL_1_2.gif.html?g2_imageViewsIndex=1"))));
        
        this.setListAdapter(new IconicAdapter(this,(menu_items_vector.toArray())));
     
        ActivityCalls.afterContent(this);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        ActivityCalls.afterContent(this);

        Log.d("DUBWISE", "resume");
        
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void log(String msg) {
        Log.d("DUWISE", msg);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    
        IconicMenuItem item  = ((IconicMenuItem)(this.getListAdapter().getItem(position) )) ;
        
        if (item.intent!=null)
            startActivity(item.intent);
        
    }
}
