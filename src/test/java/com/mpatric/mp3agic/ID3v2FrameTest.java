package com.mpatric.mp3agic;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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

	@Test
	public void shouldCorrectlyUnpackHeader() throws Exception {
		byte[] bytes = BufferTools.stringToByteBuffer(W_FRAME + "?????", 0, W_FRAME.length());
		TestHelper.replaceNumbersWithBytes(bytes, 0);
		final ID3v2Frame frame = new ID3v2Frame(bytes, 0);
		assertFalse(frame.hasDataLengthIndicator());
		assertFalse(frame.hasCompression());
		assertFalse(frame.hasEncryption());
		assertFalse(frame.hasGroup());
		assertFalse(frame.hasPreserveFile());
		assertFalse(frame.hasPreserveTag());
		assertFalse(frame.isReadOnly());
		assertFalse(frame.hasUnsynchronisation());
	}

	@Test
	public void shouldStoreAndRetrieveData() throws Exception {
		final byte[] oldBytes = BufferTools.stringToByteBuffer(C_FRAME, 0, C_FRAME.length());
		TestHelper.replaceNumbersWithBytes(oldBytes, 0);
		final ID3v2Frame frame = new ID3v2Frame(oldBytes, 0);
		final byte[] newBytes = BufferTools.stringToByteBuffer(W_FRAME + "?????", 0, W_FRAME.length());
		TestHelper.replaceNumbersWithBytes(newBytes, 0);
		frame.setData(newBytes);
		final byte[] expectedBytes = BufferTools.stringToByteBuffer(W_FRAME, 0, W_FRAME.length());
		TestHelper.replaceNumbersWithBytes(expectedBytes, 0);
		assertArrayEquals(expectedBytes, frame.getData());
	}

	@Test
	public void shouldCorrectlyImplementHashCodeAndEquals() throws Exception {
		EqualsVerifier.forClass(ID3v2Frame.class)
				.usingGetClass()
				.suppress(Warning.NONFINAL_FIELDS)
				.verify();
	}

}
