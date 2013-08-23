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
 *  Organizations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

package org.ligi.android.dubwise_mk.blackbox;

import org.ligi.android.dubwise_mk.app.App;
import org.ligi.android.dubwise_mk.helper.DUBwiseBackgroundTask;
import org.ligi.tracedroid.collecting.TraceDroidMetaInfo;
import org.ligi.tracedroid.logging.Log;
import org.ligi.ufo.MKCommunicator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * class to persist telemetry Data for later analysis
 *
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 */
public class BlackBox implements DUBwiseBackgroundTask {

    private static BlackBox singleton = null;
    private String act_fname = "none";
    private int act_records = 0;
    private boolean running = false;

    public static BlackBox getInstance() {
        if (singleton == null)
            singleton = new BlackBox();
        return singleton;
    }

    /**
     * @return the filename where the recent flight is recorded
     */
    public String getActFileName() {
        return act_fname;
    }

    /**
     * @return how many records are recorded from the recent flight
     */
    public int getActRecords() {
        return act_records;
    }

    @Override
    public void run() {
        MKCommunicator mk = App.getMK();

        while (running) {

            if (BlackBoxPrefs.isBlackBoxEnabled() && mk.isConnected() && mk.isFlying()) {

                DateFormat path_dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                DateFormat fname_dateFormat = new SimpleDateFormat("HH_mm_ss");
                Date date = new Date();

                // create the path
                new File(BlackBoxPrefs.getPath() + "/" + path_dateFormat.format(date)).mkdirs();

                act_fname = BlackBoxPrefs.getPath() + "/" + path_dateFormat.format(date) + "/" + fname_dateFormat.format(date) + ".csv";
                File outFile = new File(act_fname);
                act_records = 0;
                try {
                    FileWriter writer = new FileWriter(outFile);


                    String header = "";

                    for (int i = 0; i < mk.debug_data.names.length; i++)
                        header += ((i == 0) ? "" : ";") + mk.debug_data.names[i];
                    header += "\n";
                    writer.write(header);
                    while (mk.isConnected() && mk.isFlying()) {
                        String act_line = "";

                        for (int i = 0; i < mk.debug_data.names.length; i++)
                            act_line += ((i == 0) ? "" : ";") + mk.debug_data.analog[i];
                        act_line += "\n";
                        writer.write(act_line);
                        act_records++;


                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                        }

                    }

                    writer.close();

                    FileWriter meta_writer = new FileWriter(new File(act_fname + ".metadata"));
                    meta_writer.write("Client: " + mk.getExtendedConnectionName());
                    meta_writer.write("\nVersion: " + mk.version.version_str);
                    meta_writer.write("\nRecords: " + act_records);
                    meta_writer.write("\nDUBwise Version: " + TraceDroidMetaInfo.getAppVersion());
                    meta_writer.close();

                } catch (IOException e) {
                    Log.w("problem writing the BlackBox File" + e);
                }
            }

            try {
                // wait a long time when BlackBox is disabled - a short time when enabled
                Thread.sleep(BlackBoxPrefs.isBlackBoxEnabled() ? 100 : 1000);
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public String getDescription() {
        return "persist telemetry data";
    }

    @Override
    public String getName() {
        return "BlackBox";
    }

    @Override
    public void start() {
        running = true;
        new Thread(singleton).start();
    }

    @Override
    public void stop() {
        running = false;
    }
}
