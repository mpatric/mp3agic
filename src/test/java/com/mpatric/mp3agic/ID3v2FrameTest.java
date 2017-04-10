package com.mpatric.mp3agic;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ID3v2FrameTest {

	private static final String T_FRAME = "TPE1000 000ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";
	private static final String W_FRAME = "WXXX000!0000ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";
	private static final String C_FRAME = "COMM000$0000000ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";

	@Test
	public void shouldReadValid32TFrame() throws Exception {
		byte[] bytes = BufferTools.stringToByteBuffer("xxxxx" + T_FRAME, 0, 5 + T_FRAME.length());
		TestHelper.replaceNumbersWithBytes(bytes, 9);
		ID3v2Frame frame = new ID3v2Frame(bytes, 5);
		assertEquals(42, frame.getLength());
		assertEquals("TPE1", frame.getId());
		String s = "0ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";
		byte[] expectedBytes = BufferTools.stringToByteBuffer(s, 0, s.length());
		TestHelper.replaceNumbersWithBytes(expectedBytes, 0);
		assertArrayEquals(expectedBytes, frame.getData());
	}

	@Test
	public void shouldReadValid32WFrame() throws Exception {
		byte[] bytes = BufferTools.stringToByteBuffer(W_FRAME + "xxxxx", 0, W_FRAME.length());
		TestHelper.replaceNumbersWithBytes(bytes, 0);
		ID3v2Frame frame = new ID3v2Frame(bytes, 0);
		assertEquals(43, frame.getLength());
		assertEquals("WXXX", frame.getId());
		String s = "00ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";
		byte[] expectedBytes = BufferTools.stringToByteBuffer(s, 0, s.length());
		TestHelper.replaceNumbersWithBytes(expectedBytes, 0);
		assertArrayEquals(expectedBytes, frame.getData());
	}

	@Test
	public void shouldReadValid32CFrame() throws Exception {
		byte[] bytes = BufferTools.stringToByteBuffer(C_FRAME, 0, C_FRAME.length());
		TestHelper.replaceNumbersWithBytes(bytes, 0);
		ID3v2Frame frame = new ID3v2Frame(bytes, 0);
		assertEquals(46, frame.getLength());
		assertEquals("COMM", frame.getId());
		String s = "00000ABCDEFGHIJKLMNOPQRSTUVWXYZABCDE";
		byte[] expectedBytes = BufferTools.stringToByteBuffer(s, 0, s.length());
		TestHelper.replaceNumbersWithBytes(expectedBytes, 0);
		assertArrayEquals(expectedBytes, frame.getData());
	}

	@Test
	public void shouldPackAndUnpackHeaderToGiveEquivalentObject() throws Exception {
		byte[] bytes = new byte[26];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) ('A' + i);
		}
		ID3v2Frame frame = new ID3v2Frame("TEST", bytes);
		byte[] newBytes = frame.toBytes();
		ID3v2Frame frameCopy = new ID3v2Frame(newBytes, 0);
		assertEquals("TEST", frameCopy.getId());
		assertEquals(frame, frameCopy);
	}
}
