package main.common;

public class Debug {


	public static boolean debugMode = true;

	public static void log(String msg) {
		if (debugMode) {
			System.out.println("[Log] : " + msg);
		}
	}

	public static void err(String msg) {
		if (debugMode) {
			System.err.println("[Error] : " + msg);
		}
	}

	public static void log(Object object) {
		if (object == null)
			log("null");
		else
			log(object.toString());
	}

	public static void err(Object object) {
		if (object == null)
			err("null");
		else
			err(object.toString());		
	}
	
}
