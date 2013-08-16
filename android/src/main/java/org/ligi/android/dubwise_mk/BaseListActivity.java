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

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.ligi.android.dubwise_mk.balance.BalanceActivity;
import org.ligi.android.dubwise_mk.cockpit.CockpitActivity;
import org.ligi.android.dubwise_mk.conn.ConnectionListActivity;
import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.android.dubwise_mk.flightsettings.FlightSettingsActivity;
import org.ligi.android.dubwise_mk.graph.GraphActivity;
import org.ligi.android.dubwise_mk.helper.IconicAdapter;
import org.ligi.android.dubwise_mk.helper.IconicMenuItem;
import org.ligi.android.dubwise_mk.lcd.LCDActivity;
import org.ligi.android.dubwise_mk.piloting.PilotingListActivity;
import org.ligi.ufo.MKCommunicator;

import java.util.ArrayList;
import java.util.List;

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