package com.mpatric.mp3agic;

public class Version {
	private static final String VERSION = "0.8.2";
	private static final String URL = "http://github.com/mpatric/mp3agic";
	
	public static String asString() {
		return getVersion() + " - " + Version.getUrl(); 
	}	

	public static String getVersion() {
		return VERSION;
	}
	
	public static String getUrl() {
		return URL;
	}
}
