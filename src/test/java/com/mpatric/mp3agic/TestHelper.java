package com.mpatric.mp3agic;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TestHelper {
	public static String bytesToHexString(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
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

	public static byte[] loadFile(String filename) throws IOException {
		RandomAccessFile file = new RandomAccessFile(filename, "r");
		byte[] buffer = new byte[(int) file.length()];
		file.read(buffer);
		file.close();
		return buffer;
	}

	public static void deleteFile(String filename) {
		File file = new File(filename);
		file.delete();
	}

	public static void replaceSpacesWithNulls(byte[] buffer) {
		for (int i = 0; i < buffer.length; i++) {
			if (buffer[i] == 0x20) {
				buffer[i] = 0x00;
			}
		}
	}

	public static void replaceNumbersWithBytes(byte[] bytes, int offset) {
		for (int i = offset; i < bytes.length; i++) {
			if (bytes[i] >= '0' && bytes[i] <= '9') {
				bytes[i] -= (byte) 48;
			}
		}
	}

	// self tests
	@Test
	public void shouldConvertBytesToHexAndBack() throws Exception {
		byte bytes[] = {(byte) 0x48, (byte) 0x45, (byte) 0x4C, (byte) 0x4C, (byte) 0x4F, (byte) 0x20, (byte) 0x74, (byte) 0x68, (byte) 0x65, (byte) 0x72, (byte) 0x65, (byte) 0x21};
		String hexString = TestHelper.bytesToHexString(bytes);
		assertEquals("48 45 4c 4c 4f 20 74 68 65 72 65 21", hexString);
		assertArrayEquals(bytes, TestHelper.hexStringToBytes(hexString));
	}
}
