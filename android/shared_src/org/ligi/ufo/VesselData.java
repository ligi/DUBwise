package org.ligi.ufo;

/**
 * should collect infos which are common between 
 * @author ligi
 *
 */
public class VesselData {

	public static class battery {
		private static int voltage=-1;
		
		/**
		 * @return the Voltage in 0.1V or -1 if unknown 
		 */
		public static int getVoltage() {
			return voltage;
		}
	
		/**
		 * 
		 * @param new_voltage
		 */
		public static void setUBatt(int new_voltage) {
			voltage=new_voltage;
		}
	}
}
