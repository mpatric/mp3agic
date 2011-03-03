package com.mpatric.mp3agic;

import java.util.Arrays;

import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.ID3v2Frame;

import junit.framework.TestCase;

public class ID3v2FrameTest extends TestCase {
	
	private static final String T_FRAME = "TPE1000 000ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";
	private static final String W_FRAME = "WXXX000!0000ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";
	private static final String C_FRAME = "COMM000$0000000ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";
	
	public void testShouldReadValid32TFrame() throws Exception {
		byte[] bytes = BufferTools.stringToByteBuffer("xxxxx" + T_FRAME, 0, 5 + T_FRAME.length());
		replaceNumbersWithBytes(bytes, 9);
		ID3v2Frame frame = new ID3v2Frame(bytes, 5);
		assertEquals(42, frame.getLength());
		assertEquals("TPE1", frame.getId());
		String s = "0ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";
		byte[] expectedBytes = BufferTools.stringToByteBuffer(s, 0, s.length());
		replaceNumbersWithBytes(expectedBytes, 0);
		assertTrue(Arrays.equals(expectedBytes, frame.getData()));
	}
	
	public void testShouldReadValid32WFrame() throws Exception {
		byte[] bytes = BufferTools.stringToByteBuffer(W_FRAME + "xxxxx", 0, W_FRAME.length());
		replaceNumbersWithBytes(bytes, 0);
		ID3v2Frame frame = new ID3v2Frame(bytes, 0);
		assertEquals(43, frame.getLength());
		assertEquals("WXXX", frame.getId());
		String s = "00ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";
		byte[] expectedBytes = BufferTools.stringToByteBuffer(s, 0, s.length());
		replaceNumbersWithBytes(expectedBytes, 0);
		assertTrue(Arrays.equals(expectedBytes, frame.getData())); 
	}
	
	public void testShouldReadValid32CFrame() throws Exception {
		byte[] bytes = BufferTools.stringToByteBuffer(C_FRAME, 0, C_FRAME.length());
		replaceNumbersWithBytes(bytes, 0);
		ID3v2Frame frame = new ID3v2Frame(bytes, 0);
		assertEquals(46, frame.getLength());
		assertEquals("COMM", frame.getId());
		String s = "00000ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";
		byte[] expectedBytes = BufferTools.stringToByteBuffer(s, 0, s.length());
		replaceNumbersWithBytes(expectedBytes, 0);
		assertTrue(Arrays.equals(expectedBytes, frame.getData())); 
	}
	
	public void testShouldPackAndUnpackHeaderToGiveEquivalentObject() throws Exception {
		byte[] bytes = new byte [26];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) ('A' + i);
		}
		ID3v2Frame frame = new ID3v2Frame("TEST", bytes);
		byte[] newBytes = frame.toBytes();
		ID3v2Frame frameCopy = new ID3v2Frame(newBytes, 0);
		assertEquals("TEST", frameCopy.getId());
		assertEquals(frame, frameCopy);
	}
	
	private void replaceNumbersWithBytes(byte[] bytes, int offset) {
		for (int i = offset; i < bytes.length; i++) {
			if (bytes[i] >= '0' && bytes[i] <= '9') {
				bytes[i] -= (byte)48;
			}
		}
	}
}
