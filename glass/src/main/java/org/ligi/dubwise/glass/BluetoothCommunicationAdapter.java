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

package org.ligi.dubwise.glass;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;

import org.ligi.java.io.CommunicationAdapterInterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Exception;import java.lang.IllegalAccessException;import java.lang.InstantiationException;import java.lang.Integer;import java.lang.NoSuchFieldException;import java.lang.NoSuchMethodException;import java.lang.Object;import java.lang.RuntimeException;import java.lang.String;import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
//import org.ligi.tracedroid.logging.Log;

/**
 * Connection Adapter for Bluetooth Connections
 * uses lib http://android-bluetooth.googlecode.com to work with
 * Android <2.0 and have the openSocket method without the need for SDP
 * But that uses non standard calls - so might not work everywhere ..
 *
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 */
public class BluetoothCommunicationAdapter
        implements CommunicationAdapterInterface {

    // magical uuid we have from stackoverflow ^^ TODO add url
    //private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String mac = "";
    private BluetoothDevice myDevice = null;
    private BluetoothSocket mySocket = null;

    /**
     * the mac addr - bytes seperated by :
     *
     * @param mac
     */
    public BluetoothCommunicationAdapter(String mac) {
        this.mac = mac;
    }

    private static BluetoothSocket createRfcommSocketToServiceRecord(BluetoothDevice device, int port, UUID uuid, boolean encrypt) throws IOException {
        try {
            BluetoothSocket socket = null;
            Log.i("btconnect connecting new start");
            Constructor<BluetoothSocket> constructor = BluetoothSocket.class.getDeclaredConstructor(
                    int.class, int.class, boolean.class, boolean.class, BluetoothDevice.class, int.class, ParcelUuid.class);
            Log.i("btconnect connecting new got const");
            if (constructor == null)
                throw new RuntimeException("can't find the constructor for socket");
            Log.i("btconnect connecting new set acc");
            constructor.setAccessible(true);
            Log.i("btconnect connecting new type");
            Field f_rfcomm_type = BluetoothSocket.class.getDeclaredField("TYPE_RFCOMM");
            Log.i("btconnect connecting new type acc");
            f_rfcomm_type.setAccessible(true);
            Log.i("btconnect connecting new type get");
            int rfcomm_type = (Integer) f_rfcomm_type.get(null);
            Log.i("btconnect connecting new instance");
            socket = constructor.newInstance(new Object[]{rfcomm_type, -1, false, true, device, port, uuid != null ? new ParcelUuid(uuid) : null});
            Log.i("btconnect connecting new return" + socket + " " + (socket != null));
            return socket;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            }
            throw new RuntimeException(e.getCause());
        }
    }

    public int getRSSI() {
        return 0; //TODO implement
    }

    public void connect() {
        try {
            //Looper.prepare();
            //Log.i("getting adapter - the native Android way ");
            Log.i("btconnect getting adapter - Test ");
            BluetoothAdapter myAdapter = BluetoothAdapter.getDefaultAdapter();
            Log.i("btconnect gettin remote device " + mac);

            if (myAdapter.isDiscovering())
                myAdapter.cancelDiscovery();

            myDevice = myAdapter.getRemoteDevice(mac);
            Log.i("btconnect getting socket");
            //mySocket = myDevice.createRfcommSocketToServiceRecord(myUUID);
            //mySocket = myDevice.createInsecureRfcommSocketToServiceRecord(myUUID);
            //mySocket = myDevice.c;
            mySocket = createRfcommSocketToServiceRecord(myDevice, 1, myUUID, false);

            Log.i("btconnect connecting socket");
            mySocket.connect();

            Log.i("btconnect done connection sequence");
        } catch (Exception e) {
            Log.w("problem btconnect" + e);
        }
    }

    public void disconnect() {
        Log.i("btconnect disconnect trigger");
        try {
            getInputStream().close();
            Log.i("btconnect disconnect trigger");
        } catch (Exception e) {
        }

        try {
            getOutputStream().close();
            Log.i("btconnect disconnect trigger");
        } catch (Exception e) {
        }

        try {
            mySocket.close();
            Log.i("btconnect disconnect trigger ");
        } catch (Exception e) {
        }
    }

    public InputStream getInputStream() {

        try {
            return mySocket.getInputStream();
        } catch (Exception e) {
            Log.i("getInputstream problem" + e);
            //connect();
            return null;
        }
    }

    public OutputStream getOutputStream() {
        try {
            return mySocket.getOutputStream();
        } catch (Exception e) {
            return null;
        }
    }

    public int available() {
        try {
            return getInputStream().available();
        } catch (Exception e) {
            return 0;
        }
    }

    public void flush() {
        try {
            getOutputStream().flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void write(byte[] buffer, int offset, int count) {
        try {
            getOutputStream().write(buffer, offset, count);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void write(byte[] buffer) {
        try {
            getOutputStream().write(buffer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void write(int oneByte) {
        try {
            getOutputStream().write(oneByte);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int read(byte[] b, int offset, int length) {
        try {
            if (getInputStream().available() > 0)
                return getInputStream().read(b, offset, length);
            else
                return 0;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public int read() {
        try {
            if (getInputStream().available() > 0)
                return getInputStream().read();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return -1;
    }

    public String getName() {
        return "no name";    // TODO implement name/url stuff
    }

    public String getURL() {
        return "no url";  // TODO implement name/url stuff
    }

    public void write(char data) {
        try {
            getOutputStream().write(data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static class Log {
        public static void i(String msg) {
            android.util.Log.i("Dubwise", msg);
        }

        public static void w(String msg) {
            android.util.Log.w("Dubwise", msg);
        }

        public static void e(String msg) {
            android.util.Log.e("Dubwise", msg);
        }

    }

}
