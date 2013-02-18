package com.mpatric.mp3agic;

import java.nio.charset.CharacterCodingException;
import java.util.Arrays;

import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.EncodedText;

import junit.framework.TestCase;

public class EncodedTextTest extends TestCase {
	
	private static final String TEST_STRING = "This is a string!";
	private static final String TEST_STRING_HEX_ISO8859_1 = "54 68 69 73 20 69 73 20 61 20 73 74 72 69 6e 67 21";
	
	private static final String UNICODE_TEST_STRING = "\u03B3\u03B5\u03B9\u03AC \u03C3\u03BF\u03C5";
	private static final String UNICODE_TEST_STRING_HEX_UTF8 = "ce b3 ce b5 ce b9 ce ac 20 cf 83 ce bf cf 85";
	private static final String UNICODE_TEST_STRING_HEX_UTF16LE = "b3 03 b5 03 b9 03 ac 03 20 00 c3 03 bf 03 c5 03";
	private static final String UNICODE_TEST_STRING_HEX_UTF16BE = "03 b3 03 b5 03 b9 03 ac 00 20 03 c3 03 bf 03 c5";
	
	private static final byte[] BUFFER_WITH_A_BACKTICK = {(byte) 0x49, (byte) 0x60, (byte) 0x6D};
	
	public void testShouldConstructFromStringOrBytes() throws Exception {
		EncodedText encodedText, encodedText2;
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, TEST_STRING);
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, TestHelper.hexStringToBytes(TEST_STRING_HEX_ISO8859_1));
		assertEquals(encodedText, encodedText2);
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, UNICODE_TEST_STRING);
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, TestHelper.hexStringToBytes(UNICODE_TEST_STRING_HEX_UTF8));
		assertEquals(encodedText, encodedText2);
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, UNICODE_TEST_STRING);
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TestHelper.hexStringToBytes(UNICODE_TEST_STRING_HEX_UTF16LE));
		assertEquals(encodedText, encodedText2);
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16BE, UNICODE_TEST_STRING);
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16BE, TestHelper.hexStringToBytes(UNICODE_TEST_STRING_HEX_UTF16BE));
		assertEquals(encodedText, encodedText2);
	}
	
	public void testShouldUseAppropriateEncodingWhenConstructingFromStringOnly() throws Exception {
		EncodedText encodedText;
		String s;
		encodedText = new EncodedText(TEST_STRING);
		s = encodedText.toString();
		assertNotNull(s);
		encodedText = new EncodedText(UNICODE_TEST_STRING);
		s = encodedText.toString();
		assertNotNull(s);
	}
	
	public void testShouldEncodeAndDecodeISO8859_1Text() throws Exception {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, TEST_STRING);
		assertEquals(EncodedText.CHARSET_ISO_8859_1, encodedText.getCharacterSet());
		assertEquals(TEST_STRING, encodedText.toString());
		EncodedText encodedText2;
		byte bytes[];
		// no bom & no terminator
		bytes = encodedText.toBytes();
		assertEquals(TEST_STRING_HEX_ISO8859_1, TestHelper.bytesToHexString(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, bytes);
		assertEquals(encodedText, encodedText2);
		// bom & no terminator
		bytes = encodedText.toBytes(true);
		assertEquals(TEST_STRING_HEX_ISO8859_1, TestHelper.bytesToHexString(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, bytes);
		assertEquals(encodedText, encodedText2);
		// no bom & terminator
		bytes = encodedText.toBytes(false, true);
		assertEquals(TEST_STRING_HEX_ISO8859_1 + " 00", TestHelper.bytesToHexString(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, bytes);
		assertEquals(encodedText, encodedText2);
		// bom & terminator
		bytes = encodedText.toBytes(true, true);
		assertEquals(TEST_STRING_HEX_ISO8859_1 + " 00", TestHelper.bytesToHexString(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, bytes);
		assertEquals(encodedText, encodedText2);
	}
	
	public void testShouldEncodeAndDecodeUTF8Text() throws Exception {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, UNICODE_TEST_STRING);
		assertEquals(EncodedText.CHARSET_UTF_8, encodedText.getCharacterSet());
		assertEquals(UNICODE_TEST_STRING, encodedText.toString());
		EncodedText encodedText2;
		byte bytes[];
		// no bom & no terminator
		bytes = encodedText.toBytes();
		String c = TestHelper.bytesToHexString(bytes);
		assertEquals(UNICODE_TEST_STRING_HEX_UTF8, c);
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, bytes);
		assertEquals(encodedText, encodedText2);
		// bom & no terminator
		bytes = encodedText.toBytes(true);
		assertEquals(UNICODE_TEST_STRING_HEX_UTF8, TestHelper.bytesToHexString(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, bytes);
		assertEquals(encodedText, encodedText2);
		// no bom & terminator
		bytes = encodedText.toBytes(false, true);
		assertEquals(UNICODE_TEST_STRING_HEX_UTF8 + " 00", TestHelper.bytesToHexString(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, bytes);
		assertEquals(encodedText, encodedText2);
		// bom & terminator
		bytes = encodedText.toBytes(true, true);
		assertEquals(UNICODE_TEST_STRING_HEX_UTF8 + " 00", TestHelper.bytesToHexString(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, bytes);
		assertEquals(encodedText, encodedText2);
	}
	
	public void testShouldEncodeAndDecodeUTF16Text() throws Exception {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, UNICODE_TEST_STRING);
		assertEquals(EncodedText.CHARSET_UTF_16, encodedText.getCharacterSet());
		assertEquals(UNICODE_TEST_STRING, encodedText.toString());
		byte bytes[];
		EncodedText encodedText2;
		// no bom & no terminator
		bytes = encodedText.toBytes();
		assertEquals(UNICODE_TEST_STRING_HEX_UTF16LE, TestHelper.bytesToHexString(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, bytes);
		assertEquals(encodedText, encodedText2);
		// bom & no terminator
		bytes = encodedText.toBytes(true);
		assertEquals("ff fe " + UNICODE_TEST_STRING_HEX_UTF16LE, TestHelper.bytesToHexString(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, bytes);
		assertEquals(encodedText, encodedText2);
		// no bom & terminator
		bytes = encodedText.toBytes(false, true);
		assertEquals(UNICODE_TEST_STRING_HEX_UTF16LE + " 00 00", TestHelper.bytesToHexString(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, bytes);
		assertEquals(encodedText, encodedText2);
		// bom & terminator
		bytes = encodedText.toBytes(true, true);
		assertEquals("ff fe " + UNICODE_TEST_STRING_HEX_UTF16LE + " 00 00", TestHelper.bytesToHexString(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, bytes);
		assertEquals(encodedText, encodedText2);
	}
	
	public void testShouldEncodeAndDecodeUTF16BEText() throws Exception {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16BE, UNICODE_TEST_STRING);
		assertEquals(EncodedText.CHARSET_UTF_16BE, encodedText.getCharacterSet());
		assertEquals(UNICODE_TEST_STRING, encodedText.toString());
		byte bytes[];
		EncodedText encodedText2;
		// no bom & no terminator
		bytes = encodedText.toBytes();
		assertEquals(UNICODE_TEST_STRING_HEX_UTF16BE, TestHelper.bytesToHexString(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16BE, bytes);
		assertEquals(encodedText, encodedText2);
		// bom & no terminator
		bytes = encodedText.toBytes(true);
		assertEquals("fe ff " + UNICODE_TEST_STRING_HEX_UTF16BE, TestHelper.bytesToHexString(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16BE, bytes);
		assertEquals(encodedText, encodedText2);
		// no bom & terminator
		bytes = encodedText.toBytes(false, true);
		assertEquals(UNICODE_TEST_STRING_HEX_UTF16BE + " 00 00", TestHelper.bytesToHexString(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16BE, bytes);
		assertEquals(encodedText, encodedText2);
		// bom & terminator
		bytes = encodedText.toBytes(true, true);
		assertEquals("fe ff " + UNICODE_TEST_STRING_HEX_UTF16BE + " 00 00", TestHelper.bytesToHexString(bytes));
		encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16BE, bytes);
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
	
	public void testShouldInferISO8859_1EncodingFromBytesWithNoBOM() throws Exception {
		EncodedText encodedText = new EncodedText(TestHelper.hexStringToBytes(TEST_STRING_HEX_ISO8859_1));
		assertEquals(EncodedText.TEXT_ENCODING_ISO_8859_1, encodedText.getTextEncoding());
	}
	
	public void testShouldDetectUTF8EncodingFromBytesWithBOM() throws Exception {
		EncodedText encodedText = new EncodedText(TestHelper.hexStringToBytes("ef bb bf " + UNICODE_TEST_STRING_HEX_UTF8));
		assertEquals(EncodedText.TEXT_ENCODING_UTF_8, encodedText.getTextEncoding());
	}
	
	public void testShouldDetectUTF16EncodingFromBytesWithBOM() throws Exception {
		EncodedText encodedText = new EncodedText(TestHelper.hexStringToBytes("ff fe " + UNICODE_TEST_STRING_HEX_UTF16LE));
		assertEquals(EncodedText.TEXT_ENCODING_UTF_16, encodedText.getTextEncoding());
	}
	
	public void testShouldDetectUTF16BEEncodingFromBytesWithBOM() throws Exception {
		EncodedText encodedText = new EncodedText(TestHelper.hexStringToBytes("fe ff " + UNICODE_TEST_STRING_HEX_UTF16BE));
		assertEquals(EncodedText.TEXT_ENCODING_UTF_16BE, encodedText.getTextEncoding());
	}
	
	public void testShouldTranscodeFromOneEncodingToAnother() throws Exception {
		EncodedText encodedText;
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, TestHelper.hexStringToBytes("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f"));
		encodedText.setTextEncoding(EncodedText.TEXT_ENCODING_ISO_8859_1, true);
		assertEquals("43 61 66 e9 20 50 61 72 61 64 69 73 6f", TestHelper.bytesToHexString(encodedText.toBytes()));
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8 ,TestHelper.hexStringToBytes("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f"));
		encodedText.setTextEncoding(EncodedText.TEXT_ENCODING_UTF_8, true);
		assertEquals("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f", TestHelper.bytesToHexString(encodedText.toBytes()));
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8 ,TestHelper.hexStringToBytes("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f"));
		encodedText.setTextEncoding(EncodedText.TEXT_ENCODING_UTF_16, true);
		assertEquals("43 00 61 00 66 00 e9 00 20 00 50 00 61 00 72 00 61 00 64 00 69 00 73 00 6f 00", TestHelper.bytesToHexString(encodedText.toBytes()));
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8 ,TestHelper.hexStringToBytes("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f"));
		encodedText.setTextEncoding(EncodedText.TEXT_ENCODING_UTF_16BE, true);
		assertEquals("00 43 00 61 00 66 00 e9 00 20 00 50 00 61 00 72 00 61 00 64 00 69 00 73 00 6f", TestHelper.bytesToHexString(encodedText.toBytes()));
	}
	
	public void testShouldThrowAnExceptionWhenAttemptingToTranscodeToACharacterSetWithUnmappableCharacters() throws Exception {
		EncodedText encodedText;
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, UNICODE_TEST_STRING);
		try {
			encodedText.setTextEncoding(EncodedText.TEXT_ENCODING_ISO_8859_1, true);
			fail("CharacterCodingException expected but not thrown");
		} catch (CharacterCodingException e) {
		}
	}
	
	public void testShouldThrowExceptionWhenTranscodingWithInvalidCharacterSet() throws Exception {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8 ,TestHelper.hexStringToBytes("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f"));
		try {
			encodedText.setTextEncoding((byte)4, true);
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
	
	public void testShouldStillReturnBytesWhenStringIsEmpty() throws Exception {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, "");
		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(false, false)));
		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(true, false)));
		assertTrue(Arrays.equals(new byte[] {0}, encodedText.toBytes(false, true)));
		assertTrue(Arrays.equals(new byte[] {0}, encodedText.toBytes(true, true)));
	}
	
	public void testShouldStillReturnBytesWhenUnicodeStringIsEmpty() throws Exception {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, "");
		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(false, false)));
		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe}, encodedText.toBytes(true, false)));
		assertTrue(Arrays.equals(new byte[] {0, 0}, encodedText.toBytes(false, true)));
		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe, 0, 0}, encodedText.toBytes(true, true)));
	}
	
	public void testShouldStillReturnBytesWhenDataIsEmpty() throws Exception {
		EncodedText encodedText;
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, new byte[] {});
		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(false, false)));
		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(true, false)));
		assertTrue(Arrays.equals(new byte[] {0}, encodedText.toBytes(false, true)));
		assertTrue(Arrays.equals(new byte[] {0}, encodedText.toBytes(true, true)));
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, new byte[] {0});
		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(false, false)));
		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(true, false)));
		assertTrue(Arrays.equals(new byte[] {0}, encodedText.toBytes(false, true)));
		assertTrue(Arrays.equals(new byte[] {0}, encodedText.toBytes(true, true)));
	}
	
	public void testShouldStillReturnBytesWhenUnicodeDataIsEmpty() throws Exception {
		EncodedText encodedText;
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, new byte[] {});
		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(false, false)));
		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe}, encodedText.toBytes(true, false)));
		assertTrue(Arrays.equals(new byte[] {0, 0}, encodedText.toBytes(false, true)));
		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe, 0, 0}, encodedText.toBytes(true, true)));
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, new byte[] {0, 0});
		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(false, false)));
		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe}, encodedText.toBytes(true, false)));
		assertTrue(Arrays.equals(new byte[] {0, 0}, encodedText.toBytes(false, true)));
		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe, 0, 0}, encodedText.toBytes(true, true)));
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, new byte[] {(byte)0xff, (byte)0xfe});
		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(false, false)));
		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe}, encodedText.toBytes(true, false)));
		assertTrue(Arrays.equals(new byte[] {0, 0}, encodedText.toBytes(false, true)));
		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe, 0, 0}, encodedText.toBytes(true, true)));
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, new byte[] {(byte)0xff, (byte)0xfe, 0, 0});
		assertTrue(Arrays.equals(new byte[] {}, encodedText.toBytes(false, false)));
		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe}, encodedText.toBytes(true, false)));
		assertTrue(Arrays.equals(new byte[] {0, 0}, encodedText.toBytes(false, true)));
		assertTrue(Arrays.equals(new byte[] {(byte)0xff, (byte)0xfe, 0, 0}, encodedText.toBytes(true, true)));
	}
}
