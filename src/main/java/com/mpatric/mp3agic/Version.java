package com.mpatric.mp3agic;

public class Version {
	private static final int MAJOR_VERSION = 0;
	private static final int MINOR_VERSION = 8;
	private static final String URL = "http://github.com/mpatric/mp3agic";
	
	public static String asString() {
		return getVersion() + " - " + Version.getUrl(); 
	}	
	public static int getMajorVersion() {
		return MAJOR_VERSION;
	}
	public static int getMinorVersion() {
		return MINOR_VERSION;
	}
	public static String getVersion() {
		return MAJOR_VERSION + "." + MINOR_VERSION;
	}
	public static String getUrl() {
		return URL;
	}
}
