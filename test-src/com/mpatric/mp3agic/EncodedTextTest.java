package com.mpatric.mp3agic;

import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.EncodedText;

import junit.framework.TestCase;

public class EncodedTextTest extends TestCase {
	
	private static final String TEST_STRING = "This is a string!";
	private static final byte[] BUFFER_WITH_A_SPECIAL_CHARACTER = {(byte) 0x49, (byte) -0x18, (byte) 0x6D};

	public void testShouldEncodedAndDecodeISO8859_1Text() throws Exception {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, TEST_STRING);
		assertEquals(EncodedText.CHARSET_ISO_8859_1, encodedText.getCharacterSet());
		assertEquals(TEST_STRING, encodedText.toString());
		EncodedText encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, encodedText.toBytes());
		assertEquals(encodedText, encodedText2);
	}
	
	public void testShouldEncodeThenDecodeUTF8Data() throws Exception {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, TEST_STRING);
		assertEquals(EncodedText.CHARSET_UTF_8, encodedText.getCharacterSet());
		assertEquals(TEST_STRING, encodedText.toString());
		EncodedText encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, encodedText.toBytes());
		assertEquals(encodedText, encodedText2);
	}
	
	public void testShouldEncodeThenDecodeUTF16Data() throws Exception {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_STRING);
		assertEquals(EncodedText.CHARSET_UTF_16, encodedText.getCharacterSet());
		assertEquals(TEST_STRING, encodedText.toString());
		EncodedText encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, encodedText.toBytes());
		assertEquals(encodedText, encodedText2);
	}
	
	public void testShouldEncodeThenDecodeUTF16BEData() throws Exception {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16BE, TEST_STRING);
		assertEquals(EncodedText.CHARSET_UTF_16BE, encodedText.getCharacterSet());
		assertEquals(TEST_STRING, encodedText.toString());
		EncodedText encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16BE, encodedText.toBytes());
		assertEquals(encodedText, encodedText2);
	}
	
	public void testShouldThrowExceptionWhenEncodingWithInvalidCharacterSet() throws Exception {
		try {
			new EncodedText((byte)4, TEST_STRING);
			fail("IllegalArgumentException expected but not thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid text encoding 4", e.getMessage());
		}
	}
	
	public void testShouldReturnNullWhenDecodingInvalidString() throws Exception {
		String s = "Not unicode";
		byte[] notUnicode = BufferTools.stringToByteBuffer(s, 0, s.length());
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, notUnicode);
		assertNull(encodedText.toString());
	}
	
	public void testShouldHandleBacktickCharacterInString() throws Exception {
		EncodedText encodedText = new EncodedText((byte)0, BUFFER_WITH_A_SPECIAL_CHARACTER);
		assertEquals("Ièm", encodedText.toString());
	}
}
