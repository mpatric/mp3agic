package com.mpatric.mp3agic;

import java.util.Arrays;

import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.EncodedText;

import junit.framework.TestCase;

public class EncodedTextTest extends TestCase {
	
	private static final String TEST_STRING = "This is a string!";
	private static final String UNICODE_TEST_STRING = "\u03B3\u03B5\u03B9\u03AC \u03C3\u03BF\u03C5";
	private static final byte[] BUFFER_WITH_A_BACKTICK = {(byte) 0x49, (byte) 0x60, (byte) 0x6D};
	
	public void testShouldConvertBytesToHexAndBack() throws Exception {
		byte bytes[] = {(byte)0x48, (byte)0x45, (byte)0x4C, (byte)0x4C, (byte)0x4F, (byte)0x20, (byte)0x74, (byte)0x68, (byte)0x65, (byte)0x72, (byte)0x65, (byte)0x21};
		String hexString = asHex(bytes);
		assertEquals("48 45 4c 4c 4f 20 74 68 65 72 65 21", hexString);
		assertTrue(Arrays.equals(bytes, fromHex(hexString)));
	}
	
	public void testShouldEncodedAndDecodeISO8859_1Text() throws Exception {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, TEST_STRING);
		assertEquals(EncodedText.CHARSET_ISO_8859_1, encodedText.getCharacterSet());
		assertEquals(TEST_STRING, encodedText.toString());
		byte bytes[];
		EncodedText encodedText2;
		// no bom & no terminator
		bytes = encodedText.toBytes();
		assertEquals("54 68 69 73 20 69 73 20 61 20 73 74 72 69 6e 67 21", asHex(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, bytes);
		assertEquals(encodedText, encodedText2);
		// bom & no terminator
		bytes = encodedText.toBytes(true);
		assertEquals("54 68 69 73 20 69 73 20 61 20 73 74 72 69 6e 67 21", asHex(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, bytes);
		assertEquals(encodedText, encodedText2);
		// no bom & terminator
		bytes = encodedText.toBytes(false, true);
		assertEquals("54 68 69 73 20 69 73 20 61 20 73 74 72 69 6e 67 21 00", asHex(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, bytes);
		assertEquals(encodedText, encodedText2);
		// bom & terminator
		bytes = encodedText.toBytes(true, true);
		assertEquals("54 68 69 73 20 69 73 20 61 20 73 74 72 69 6e 67 21 00", asHex(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, bytes);
		assertEquals(encodedText, encodedText2);
	}
	
	public void testShouldEncodedAndDecodeUTF8Text() throws Exception {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, UNICODE_TEST_STRING);
		assertEquals(EncodedText.CHARSET_UTF_8, encodedText.getCharacterSet());
		assertEquals(UNICODE_TEST_STRING, encodedText.toString());
		byte bytes[];
		EncodedText encodedText2;
		// no bom & no terminator
		bytes = encodedText.toBytes();
		assertEquals("ce b3 ce b5 ce b9 ce ac 20 cf 83 ce bf cf 85", asHex(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, bytes);
		assertEquals(encodedText, encodedText2);
		// bom & no terminator
		bytes = encodedText.toBytes(true);
		assertEquals("ce b3 ce b5 ce b9 ce ac 20 cf 83 ce bf cf 85", asHex(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, bytes);
		assertEquals(encodedText, encodedText2);
		// no bom & terminator
		bytes = encodedText.toBytes(false, true);
		assertEquals("ce b3 ce b5 ce b9 ce ac 20 cf 83 ce bf cf 85 00", asHex(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, bytes);
		assertEquals(encodedText, encodedText2);
		// bom & terminator
		bytes = encodedText.toBytes(true, true);
		assertEquals("ce b3 ce b5 ce b9 ce ac 20 cf 83 ce bf cf 85 00", asHex(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, bytes);
		assertEquals(encodedText, encodedText2);
	}
	
	public void testShouldEncodedAndDecodeUTF16Text() throws Exception {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, UNICODE_TEST_STRING);
		assertEquals(EncodedText.CHARSET_UTF_16, encodedText.getCharacterSet());
		assertEquals(UNICODE_TEST_STRING, encodedText.toString());
		byte bytes[];
		EncodedText encodedText2;
		// no bom & no terminator
		bytes = encodedText.toBytes();
		assertEquals("b3 03 b5 03 b9 03 ac 03 20 00 c3 03 bf 03 c5 03", asHex(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, bytes);
		assertEquals(encodedText, encodedText2);
		// bom & no terminator
		bytes = encodedText.toBytes(true);
		assertEquals("ff fe b3 03 b5 03 b9 03 ac 03 20 00 c3 03 bf 03 c5 03", asHex(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, bytes);
		assertEquals(encodedText, encodedText2);
		// no bom & terminator
		bytes = encodedText.toBytes(false, true);
		assertEquals("b3 03 b5 03 b9 03 ac 03 20 00 c3 03 bf 03 c5 03 00 00", asHex(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, bytes);
		assertEquals(encodedText, encodedText2);
		// bom & terminator
		bytes = encodedText.toBytes(true, true);
		assertEquals("ff fe b3 03 b5 03 b9 03 ac 03 20 00 c3 03 bf 03 c5 03 00 00", asHex(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, bytes);
		assertEquals(encodedText, encodedText2);
	}
	
	public void testShouldEncodedAndDecodeUTF16BEText() throws Exception {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16BE, UNICODE_TEST_STRING);
		assertEquals(EncodedText.CHARSET_UTF_16BE, encodedText.getCharacterSet());
		assertEquals(UNICODE_TEST_STRING, encodedText.toString());
		byte bytes[];
		EncodedText encodedText2;
		// no bom & no terminator
		bytes = encodedText.toBytes();
		assertEquals("03 b3 03 b5 03 b9 03 ac 00 20 03 c3 03 bf 03 c5", asHex(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16BE, bytes);
		assertEquals(encodedText, encodedText2);
		// bom & no terminator
		bytes = encodedText.toBytes(true);
		assertEquals("fe ff 03 b3 03 b5 03 b9 03 ac 00 20 03 c3 03 bf 03 c5", asHex(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16BE, bytes);
		assertEquals(encodedText, encodedText2);
		// no bom & terminator
		bytes = encodedText.toBytes(false, true);
		assertEquals("03 b3 03 b5 03 b9 03 ac 00 20 03 c3 03 bf 03 c5 00 00", asHex(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16BE, bytes);
		assertEquals(encodedText, encodedText2);
		// bom & terminator
		bytes = encodedText.toBytes(true, true);
		assertEquals("fe ff 03 b3 03 b5 03 b9 03 ac 00 20 03 c3 03 bf 03 c5 00 00", asHex(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16BE, bytes);
		assertEquals(encodedText, encodedText2);
	}
	
	public void testShouldInferISO8859_1EncodingFromLackOfBOM() throws Exception {
		EncodedText encodedText = new EncodedText(fromHex("48 45 4c 4c 4f 20 74 68 65 72 65 21 00"));
		assertEquals(EncodedText.TEXT_ENCODING_ISO_8859_1, encodedText.getTextEncoding());
	}
	
	public void testShouldInferUTF8EncodingFromBOM() throws Exception {
		EncodedText encodedText = new EncodedText(fromHex("ef bb bf ce b3 ce b5 ce b9 ce ac 20 cf 83 ce bf cf 85 00"));
		assertEquals(EncodedText.TEXT_ENCODING_UTF_8, encodedText.getTextEncoding());
	}
	
	public void testShouldInferUTF16EncodingFromBOM() throws Exception {
		EncodedText encodedText = new EncodedText(fromHex("ff fe b3 03 b5 03 b9 03 ac 03 20 00 c3 03 bf 03 c5 03 00 00"));
		assertEquals(EncodedText.TEXT_ENCODING_UTF_16, encodedText.getTextEncoding());
	}
	
	public void testShouldInferUTF16BEEncodingFromBOM() throws Exception {
		EncodedText encodedText = new EncodedText(fromHex("fe ff 03 b3 03 b5 03 b9 03 ac 00 20 03 c3 03 bf 03 c5 00 00"));
		assertEquals(EncodedText.TEXT_ENCODING_UTF_16BE, encodedText.getTextEncoding());
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
		EncodedText encodedText = new EncodedText((byte)0, BUFFER_WITH_A_BACKTICK);
		assertEquals("I" + (char)(96) + "m", encodedText.toString());
	}
	
	private static String asHex(byte[] bytes) {     
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			if (i > 0) hexString.append(' ');
			String hex = Integer.toHexString(0xff & bytes[i]);
			if (hex.length() == 1) hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}
	
	private static byte[] fromHex(String hex) {
		int len = hex.length();
		byte[] bytes = new byte[(len + 1) / 3];
	    for (int i = 0; i < len; i += 3) {
	        bytes[i / 3] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
	    }
		return bytes;
	}
}
