package org.ligi.android.dubwise.uavtalk;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.ligi.android.dubwise.helper.DUBwiseBackgroundTask;
import org.openpilot.uavtalk.UAVObject;
import org.openpilot.uavtalk.UAVObjects;
import org.openpilot.uavtalk.UAVTalkDefinitions;
import org.openpilot.uavtalk.UAVTalkHelper;
import org.openpilot.uavtalk.ValueParser;

public class DUBwiseFlightTelemetry implements DUBwiseBackgroundTask{

	private static DUBwiseFlightTelemetry ht;
	private boolean running;
	
	@Override
	public void start() {
		getTaskInstance();
		ht.running=true;
		new Thread(this).start();
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public String getName() {
		return "DUBwise host";
	}

	@Override
	public void stop() {
		if (ht!=null)
			running=false;
	}
	
	public String last_conn="";
	public int bytes_in=0;
	public int bytes_out=0;
	
	@Override
	public void run() {
		UAVObjects.init();
		
		try {
			DatagramSocket rcv_datagram_socket = new DatagramSocket(9000);
			//DatagramSocket snd_datagram_socket = new DatagramSocket(9001);
			//DatagramSocket send_datagram_socket = new DatagramSocket(9000);
			
			long time=System.currentTimeMillis();
			
			while (running) {
				try {
					byte[] arr =new byte[255];
					//DatagramPacket dp2=new DatagramPacket(UAVTalkHelper.generateUAVTalkPackage(UAVTalkDefinitions.TYPE_ACK, UAVObjects.getFlightTelemetryStats().getID()), UAVTalkHelper.MIN_PACKAGE_SIZE);
					//DatagramPacket dp2=new DatagramPacket(UAVTalkHelper.generateUAVTalkPackage(UAVTalkDefinitions.TYPE_OBJ_REQ, UAVObjects.getAttitudeRaw().getID()), UAVTalkHelper.MIN_PACKAGE_SIZE);
					//DatagramPacket dp2=new DatagramPacket(UAVTalkHelper.generateUAVTalkPackage(UAVTalkDefinitions.TYPE_OBJ_REQ, UAVObjects.getAttitudeActual().getID()), UAVTalkHelper.MIN_PACKAGE_SIZE);
					//DatagramPacket dp2=new DatagramPacket(UAVTalkHelper.generateUAVTalkPackage(UAVTalkDefinitions.TYPE_OBJ_REQ, UAVObjects.getSystemAlarms().getObjID()), UAVTalkHelper.MIN_PACKAGE_SIZE);
					DatagramPacket dp2=UAVTalkPackage2DatagramPacket(UAVTalkDefinitions.TYPE_ACK, UAVObjects.getGCSTelemetryStats());
				
					DatagramPacket dp3=UAVTalkPackage2DatagramPacket(UAVTalkDefinitions.TYPE_OBJ_ACK, UAVObjects.getFlightTelemetryStats());
				
					DatagramPacket dp4=UAVTalkPackage2DatagramPacket(UAVTalkDefinitions.TYPE_OBJ_ACK, UAVObjects.getFlightBatteryState());
					DatagramPacket dp=new DatagramPacket(arr,255);
					//ds.send(dp);
					System.out.println("rec get");
					rcv_datagram_socket.setSoTimeout(100);
					rcv_datagram_socket.receive(dp);
					bytes_in+=dp.getLength();
					System.out.println("rec set" +  dp.getAddress() + " " + dp.getLength());
					//snd_datagram_socket.connect(dp.getSocketAddress());
					System.out.println("set OK");
					dp3.setAddress(dp.getAddress());
					dp4.setAddress(dp.getAddress());
					rcv_datagram_socket.send(dp3);
					bytes_out+=dp3.getLength();
					
					last_conn=dp.getSocketAddress().toString();
					
			//		ds.setSoTimeout(100);
					
				//	rcv_datagram_socket.setSoTimeout(100);
				//	rcv_datagram_socket.receive(dp); 
					
					if (System.currentTimeMillis()>(time+1000))	{ 
							rcv_datagram_socket.send(dp4);
							time=System.currentTimeMillis();			
					}
					
					UAVObjects.getFlightTelemetryStats().setStatus(UAVObjects.getFlightTelemetryStats().STATUS_HANDSHAKEACK);
					
					try {
						System.out.println("  " + UAVObjects.getObjectByID(ValueParser.parse_int_from_arr_4(4, arr)).getObjName());
					}
					catch (Exception e) { 
						System.out.println(" obj not found" + e);
					}	
					
					for (int i=0;i<dp.getLength();i++)
						System.out.print(" " + String.format("0x%h=%d",arr[i],arr[i]) );
					
					System.out.println();
					
					rcv_datagram_socket.send(dp2);
					bytes_out+=dp2.getLength();
				}
				catch (Exception e) { 
							 System.out.println("---" + e.toString());
				}
			}
		} catch (Exception e) { 
			System.out.println("---" + e.toString());
		}
	
	}	
	

	public static String gs(byte[] in) {
		String res="";
		for (int i=0;i<in.length;i++)
			res+=" " + in[i] +" ";
		return res;
	}
	
	public DatagramPacket UAVTalkPackage2DatagramPacket(byte type,UAVObject obj) {
		byte[] pkg=UAVTalkHelper.generateUAVTalkPackage(type,obj);
		return new DatagramPacket(pkg,pkg.length);
	}

	public static DUBwiseFlightTelemetry getInstance() {
		if (ht==null)
			ht=new DUBwiseFlightTelemetry();
		return ht;
	}
	
	public static DUBwiseBackgroundTask getTaskInstance() {
		return getInstance();
	}
}
