/***************************************
 *                                      
 * searches 4 Bluetooth Devices         
 *                                      
 * Author:        Marcus -LiGi- Bueschleb
 * 
 * see README for further Infos
 *
 ****************************************/

//package org.ligi.dubwise;
//#if bluetooth=="on"
import javax.bluetooth.*;

public class BTSearcher
    implements DiscoveryListener
	       
{
    private LocalDevice m_LclDevice = null;	
    private DiscoveryAgent m_DscrAgent=null;

    public boolean searching=true;
    public boolean error=false;
    public String  err_log="none";


    public void log(String err_str)
    {
	err_log+=err_str;
	System.out.println(err_str);
    }

    public final int MAX_DEVICES=10;

    public RemoteDevice[] remote_devices;
    public int remote_device_count=0;   
    public String[] remote_device_name;
    public String[] remote_device_mac;


    public BTSearcher()
    {

	remote_devices=new RemoteDevice[MAX_DEVICES];
	remote_device_name=new String[MAX_DEVICES];
	remote_device_mac=new String[MAX_DEVICES];

	remote_device_count=0;   	
	
	searching=true;
	try
	    {
		//First get the local device and obtain the discovery agent. 
		m_LclDevice = LocalDevice.getLocalDevice();
		m_DscrAgent=  m_LclDevice.getDiscoveryAgent();
      
		m_DscrAgent.startInquiry(DiscoveryAgent.GIAC,this);
	    }
        catch (BluetoothStateException ex) 
	    {
		error=true;
		log("Problem in searching the blue tooth devices\n" + ex);
		
	    }
    }


    public void inquiryCompleted(int transID) {

	try { 
	    log("search complete with " + remote_device_count + " devices");
	    for(int i=0;i<remote_device_count;i++)
		{
		    log("#" + i + " -> addr: " + remote_devices[i].getBluetoothAddress());
		    remote_device_mac[i]=remote_devices[i].getBluetoothAddress();

		    remote_device_name[i]=remote_devices[i].getBluetoothAddress();
		    try {
			log("#" + i + "name:" + remote_devices[i].getFriendlyName(true));
			remote_device_name[i]=remote_devices[i].getFriendlyName(true);
		    }
		    catch (Exception e) 
			{
			    log("Problem getting name of BT-Device( -> taking mac as name): " + e);
			}	


		}
	}
	catch (Exception e) 
	    {
		log("Problem in searching the blue tooth devices" + e);
	    }	
	searching=false;
    }


    public void search_again()
    {
    }

    //Called when device is found during inquiry 
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod)
    {
	log("found device ");	
	try 
	    {
		if (remote_device_count!=(MAX_DEVICES-1))
		    {
			remote_devices[remote_device_count]=btDevice;
			remote_device_count++;
		    }
	    }
	catch (Exception e) 
	    {
		log("Device Discovered Error: " + e);	
	    }

    }


    public void serviceSearchCompleted(int transID, int respCode)
    {   }

    public void servicesDiscovered(int transID, ServiceRecord[] records)
    {    }



}

//#endif
