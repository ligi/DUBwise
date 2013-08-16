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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import org.ligi.android.dubwise_mk.balance.BalanceActivity;
import org.ligi.android.dubwise_mk.blackbox.BlackBox;
import org.ligi.android.dubwise_mk.blackbox.BlackBoxPrefs;
import org.ligi.android.dubwise_mk.cockpit.CockpitActivity;
import org.ligi.android.dubwise_mk.conn.ConnectionListActivity;
import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.android.dubwise_mk.flightsettings.FlightSettingsActivity;
import org.ligi.android.dubwise_mk.graph.GraphActivity;
import org.ligi.android.dubwise_mk.helper.DUBwiseBackgroundHandler;
import org.ligi.android.dubwise_mk.helper.IconicAdapter;
import org.ligi.android.dubwise_mk.helper.IconicMenuItem;
import org.ligi.android.dubwise_mk.lcd.LCDActivity;
import org.ligi.android.dubwise_mk.piloting.PilotingListActivity;
import org.ligi.android.dubwise_mk.voice.StatusVoice;
import org.ligi.android.dubwise_mk.voice.VoicePrefs;
import org.ligi.ufo.MKCommunicator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends SherlockActivity {

    private ViewGroup contentView;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private static PowerManager.WakeLock mWakeLock;
    private static boolean did_init;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DUBwisePrefs.init(this);

        if (DUBwisePrefs.keepLightNow()) {
            if (mWakeLock == null) {
                final PowerManager pm = (PowerManager) (getSystemService(Context.POWER_SERVICE));
                mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "DUBwise");
            }
            mWakeLock.acquire();
        }

        // do only once
        if (!did_init) {
            //BluetoothMaster.init(activity);
            VoicePrefs.init(this);
            StatusVoice.getInstance().init(this);
            BlackBoxPrefs.init(this);


            // start the default connection
            StartupConnectionService.start(this);

            if (BlackBoxPrefs.isBlackBoxEnabled()) {
                DUBwiseBackgroundHandler.getInstance().addAndStartTask(BlackBox.getInstance());
            }

            did_init = true;
        }

        if (VoicePrefs.isVoiceEnabled() && !DUBwiseBackgroundHandler.getInstance().getBackgroundTasks().contains(StatusVoice.getInstance())) {
            DUBwiseBackgroundHandler.getInstance().addAndStartTask(StatusVoice.getInstance());
        }

        setContentView(R.layout.base_layout);
        contentView = (ViewGroup) findViewById(R.id.content_frame);

        drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IconicMenuItem item = ((IconicMenuItem) (drawerList.getAdapter().getItem(position)));

                if (item.intent != null) {
                    startActivity(item.intent);
                }

            }
        });
        refresh_list();


        //mTitle = mDrawerTitle = getTitle();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        // a little hack because I strongly disagree with the style guide here
        // ;-)
        // not having the Actionbar overfow menu also with devices with hardware
        // key really helps discoverability
        // http://stackoverflow.com/questions/9286822/how-to-force-use-of-overflow-menu-on-devices-with-menu-button
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore - but at least we tried ;-)
        }


    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setContentView(View view) {
        contentView.addView(view);

        if (DUBwisePrefs.isFullscreenEnabled()) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

    }


    public void refresh_list() {
        MKCommunicator mk = MKProvider.getMK();
        List<IconicMenuItem> menuItemsList = new ArrayList<IconicMenuItem>();

        menuItemsList.add(new IconicMenuItem("Connection",
                android.R.drawable.ic_menu_share, new Intent(this,
                ConnectionListActivity.class)));

        menuItemsList.add(new IconicMenuItem("Settings",
                android.R.drawable.ic_menu_preferences, new Intent(this,
                SettingsListActivity.class)));


        if (DUBwisePrefs.isExpertModeEnabled()) {
            menuItemsList.add(new IconicMenuItem("OpenGL",
                    android.R.drawable.ic_menu_preferences, new Intent(this,
                    OpenGLActivity.class)));

            menuItemsList.add(new IconicMenuItem("Flash Firmware",
                    android.R.drawable.ic_menu_preferences, new Intent(this,
                    FlashFirmwareActivity.class)));

            menuItemsList.add(new IconicMenuItem("Control Panel",
                    android.R.drawable.ic_menu_preferences, new Intent(this,
                    ControlPanelActivity.class)));

            menuItemsList.add(new IconicMenuItem("Voice",
                    android.R.drawable.ic_menu_view, new Intent(this,
                    VoiceControlActivity.class)));
        }

        if (mk.connected) {

            menuItemsList.add(new IconicMenuItem("Device Details",
                    android.R.drawable.ic_menu_view, new Intent(this,
                    DeviceDetails.class)));


            menuItemsList.add(new IconicMenuItem("LCD",
                    android.R.drawable.ic_menu_view, new Intent(this,
                    LCDActivity.class)));

            if (mk.is_mk() || mk.is_navi() || mk.is_fake())
                menuItemsList.add(new IconicMenuItem("Pilot",
                        android.R.drawable.ic_menu_preferences, new Intent(
                        this, PilotingListActivity.class)));

            if (mk.is_navi() || mk.is_fake())
                menuItemsList.add(new IconicMenuItem("Follow me",
                        android.R.drawable.ic_menu_crop, new Intent(
                        this, FollowMeActivity.class)));

            if (mk.is_mk() || mk.is_fake())
                menuItemsList.add(new IconicMenuItem("Motor Test",
                        android.R.drawable.ic_menu_rotate, new Intent(this,
                        MotorTestActivity.class)));

            if (mk.is_mk() || mk.is_fake())
                menuItemsList.add(new IconicMenuItem("RCData",
                        android.R.drawable.ic_menu_view, new Intent(this,
                        RCDataActivity.class)));

            if (mk.is_mk() || mk.is_fake() || mk.is_navi())
                menuItemsList.add(new IconicMenuItem("Balance",
                        android.R.drawable.ic_menu_crop, new Intent(this,
                        BalanceActivity.class)));

            if (mk.is_mk() || mk.is_navi() || mk.is_fake())
                menuItemsList.add(new IconicMenuItem("Cockpit",
                        android.R.drawable.ic_menu_view, new Intent(this,
                        CockpitActivity.class)));

            if (mk.is_mk() || mk.is_navi() || mk.is_fake() || mk.is_mk3mag())
                menuItemsList.add(new IconicMenuItem("Analog Values",
                        android.R.drawable.ic_menu_view, new Intent(this,
                        AnalogValuesActivity.class)));


            if (mk.is_mk() || mk.is_fake())
                menuItemsList.add(new IconicMenuItem("Flight Settings",
                        android.R.drawable.ic_menu_edit, new Intent(this,
                        FlightSettingsActivity.class)));

            if (mk.is_mk() || mk.is_fake())
                menuItemsList.add(new IconicMenuItem("Edit Mixer",
                        android.R.drawable.ic_menu_edit, new Intent(this,
                        MixerEditActivity.class)));

            if (mk.is_mk() || mk.is_fake())
                menuItemsList.add(new IconicMenuItem("Graph",
                        android.R.drawable.ic_menu_view, new Intent(this,
                        GraphActivity.class)));

        }
        menuItemsList.add(new IconicMenuItem("Information Desk",
                android.R.drawable.ic_menu_info_details, new Intent(this,
                InformationDeskActivity.class)));

        drawerList.setAdapter(new IconicAdapter(this, (menuItemsList.toArray())));

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.top);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh_list();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            android.view.MenuItem homeMenuItem = new HomeMenuItemForNavigationDrawer();
            drawerToggle.onOptionsItemSelected(homeMenuItem);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static void shutdownDUBwise() {
        MKProvider.getMK().close_connections(true);
        MKProvider.getMK().stop();
        DUBwiseBackgroundHandler.getInstance().stopAll();
        MKProvider.disposeMK();
        did_init = false;
    }

    @Override
    protected void onDestroy() {
        if ((mWakeLock != null) && (mWakeLock.isHeld())) {
            mWakeLock.release();
        }
        super.onDestroy();
    }

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences("DUBwise", 0);
    }

}
