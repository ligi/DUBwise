package org.ligi.ufo.logging;

public class SysOutLogger implements LoggingInterface {

	public void e(String msg) {
		System.out.println("E:"+msg);
	}

	public void i(String msg) {
		System.out.println("I:"+msg);
	}

	public void w(String msg) {
		System.out.println("W:"+msg);
	}

}
