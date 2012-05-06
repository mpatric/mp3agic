package com.mpatric.mp3agic;

public class TestHelper {
	public static String bytesToHexString(byte[] bytes) {     
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			if (i > 0) hexString.append(' ');
			String hex = Integer.toHexString(0xff & bytes[i]);
			if (hex.length() == 1) hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}
	
	public static byte[] hexStringToBytes(String hex) {
		int len = hex.length();
		byte[] bytes = new byte[(len + 1) / 3];
	    for (int i = 0; i < len; i += 3) {
	        bytes[i / 3] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
	    }
		return bytes;
	}
}
