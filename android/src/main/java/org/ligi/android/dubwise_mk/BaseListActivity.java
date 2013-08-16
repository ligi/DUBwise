/**************************************************************************
 *
 * Main Menu ( & startup ) Activity for DUBwise 
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
 *  Organizations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

package org.ligi.android.dubwise_mk;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class BaseListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView=new ListView(this);
        setContentView(listView);

        listView.setOnItemClickListener(this);
    }

    public void setListAdapter(ListAdapter adapter) {
        listView.setAdapter(adapter);
    }

    public ListAdapter getListAdapter() {
        return listView.getAdapter();
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onListItemClick(listView,view,position,id);
    }
}