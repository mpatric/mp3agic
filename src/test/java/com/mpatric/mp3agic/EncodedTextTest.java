package com.mpatric.mp3agic;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CharacterCodingException;

import org.junit.Test;

import static org.junit.Assert.*;

public class EncodedTextTest {

	private static final String TEST_STRING = "This is a string!";
	// "This is a String!"
	private static final String TEST_STRING_HEX_ISO8859_1 = "54 68 69 73 20 69 73 20 61 20 73 74 72 69 6e 67 21";
	// γειά σου
	private static final String UNICODE_TEST_STRING = "\u03B3\u03B5\u03B9\u03AC \u03C3\u03BF\u03C5";
	// "γειά σου" (This can't be encoded in ISO-8859-1)
	private static final String UNICODE_TEST_STRING_HEX_UTF8 = "ce b3 ce b5 ce b9 ce ac 20 cf 83 ce bf cf 85";
	// "γειά σου" (This can't be encoded in ISO-8859-1)
	private static final String UNICODE_TEST_STRING_HEX_UTF16LE = "b3 03 b5 03 b9 03 ac 03 20 00 c3 03 bf 03 c5 03";
	// "γειά σου" (This can't be encoded in ISO-8859-1)
	private static final String UNICODE_TEST_STRING_HEX_UTF16BE = "03 b3 03 b5 03 b9 03 ac 00 20 03 c3 03 bf 03 c5";

	private static final byte[] BUFFER_WITH_A_BACK_TICK = {(byte) 0x49, (byte) 0x60, (byte) 0x6D};

	@Test
	public void shouldConstructFromStringOrBytes() {
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

	@Test
	public void shouldUseAppropriateEncodingWhenConstructingFromStringOnly() {
		EncodedText encodedText;
		String s;
		encodedText = new EncodedText(TEST_STRING);
		s = encodedText.toString();
		assertNotNull(s);
		encodedText = new EncodedText(UNICODE_TEST_STRING);
		s = encodedText.toString();
		assertNotNull(s);
	}

	@Test
	public void shouldEncodeAndDecodeISO8859_1Text() {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, TEST_STRING);
		assertEquals(EncodedText.CHARSET_ISO_8859_1, encodedText.getCharacterSet());
		assertEquals(TEST_STRING, encodedText.toString());
		EncodedText encodedText2;
		byte[] bytes;
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

	@Test
	public void shouldEncodeAndDecodeUTF8Text() {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, UNICODE_TEST_STRING);
		assertEquals(EncodedText.CHARSET_UTF_8, encodedText.getCharacterSet());
		assertEquals(UNICODE_TEST_STRING, encodedText.toString());
		EncodedText encodedText2;
		byte[] bytes;
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

	@Test
	public void shouldEncodeAndDecodeUTF16Text() {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, UNICODE_TEST_STRING);
		assertEquals(EncodedText.CHARSET_UTF_16, encodedText.getCharacterSet());
		assertEquals(UNICODE_TEST_STRING, encodedText.toString());
		byte[] bytes;
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

	@Test
	public void shouldEncodeAndDecodeUTF16BEText() {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16BE, UNICODE_TEST_STRING);
		assertEquals(EncodedText.CHARSET_UTF_16BE, encodedText.getCharacterSet());
		assertEquals(UNICODE_TEST_STRING, encodedText.toString());
		byte[] bytes;
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

	@Test
	public void UTF16ShouldDecodeBEWhenSpecifiedInBOM() {
		// id3 v2.2 and 2.3: encoding set to UTF_16 (type 1), but BOM set to big endian, so interpret as UTF_16BE
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16BE, UNICODE_TEST_STRING);
		byte[] bytes = encodedText.toBytes(true, true);
		EncodedText encodedText2 = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, bytes);
		assertEquals(encodedText, encodedText2);
	}

	@Test
	public void shouldThrowExceptionWhenEncodingWithInvalidCharacterSet() {
		try {
			new EncodedText((byte) 4, TEST_STRING);
			fail("IllegalArgumentException expected but not thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid text encoding 4", e.getMessage());
		}
	}

	@Test
	public void shouldInferISO8859_1EncodingFromBytesWithNoBOM() {
		EncodedText encodedText = new EncodedText(TestHelper.hexStringToBytes(TEST_STRING_HEX_ISO8859_1));
		assertEquals(EncodedText.TEXT_ENCODING_ISO_8859_1, encodedText.getTextEncoding());
	}

	@Test
	public void shouldDetectUTF8EncodingFromBytesWithBOM() {
		EncodedText encodedText = new EncodedText(TestHelper.hexStringToBytes("ef bb bf " + UNICODE_TEST_STRING_HEX_UTF8));
		assertEquals(EncodedText.TEXT_ENCODING_UTF_8, encodedText.getTextEncoding());
	}

	@Test
	public void shouldDetectUTF16EncodingFromBytesWithBOM() {
		EncodedText encodedText = new EncodedText(TestHelper.hexStringToBytes("ff fe " + UNICODE_TEST_STRING_HEX_UTF16LE));
		assertEquals(EncodedText.TEXT_ENCODING_UTF_16, encodedText.getTextEncoding());
	}

	@Test
	public void shouldDetectUTF16BEEncodingFromBytesWithBOM() {
		EncodedText encodedText = new EncodedText(TestHelper.hexStringToBytes("fe ff " + UNICODE_TEST_STRING_HEX_UTF16BE));
		assertEquals(EncodedText.TEXT_ENCODING_UTF_16BE, encodedText.getTextEncoding());
	}

	@Test
	public void shouldTranscodeFromOneEncodingToAnother() throws CharacterCodingException {
		EncodedText encodedText;
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, TestHelper.hexStringToBytes("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f"));
		encodedText.setTextEncoding(EncodedText.TEXT_ENCODING_ISO_8859_1, true);
		assertEquals("43 61 66 e9 20 50 61 72 61 64 69 73 6f", TestHelper.bytesToHexString(encodedText.toBytes()));
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, TestHelper.hexStringToBytes("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f"));
		encodedText.setTextEncoding(EncodedText.TEXT_ENCODING_UTF_8, true);
		assertEquals("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f", TestHelper.bytesToHexString(encodedText.toBytes()));
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, TestHelper.hexStringToBytes("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f"));
		encodedText.setTextEncoding(EncodedText.TEXT_ENCODING_UTF_16, true);
		assertEquals("43 00 61 00 66 00 e9 00 20 00 50 00 61 00 72 00 61 00 64 00 69 00 73 00 6f 00", TestHelper.bytesToHexString(encodedText.toBytes()));
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, TestHelper.hexStringToBytes("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f"));
		encodedText.setTextEncoding(EncodedText.TEXT_ENCODING_UTF_16BE, true);
		assertEquals("00 43 00 61 00 66 00 e9 00 20 00 50 00 61 00 72 00 61 00 64 00 69 00 73 00 6f", TestHelper.bytesToHexString(encodedText.toBytes()));
	}

	@Test(expected = CharacterCodingException.class)
	public void shouldThrowAnExceptionWhenAttemptingToTranscodeToACharacterSetWithUnmappableCharacters() throws CharacterCodingException {
		// given
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, UNICODE_TEST_STRING);

		// expect exception
		encodedText.setTextEncoding(EncodedText.TEXT_ENCODING_ISO_8859_1, true);
	}

	@Test
	public void shouldThrowExceptionWhenTranscodingWithInvalidCharacterSet() throws CharacterCodingException {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, TestHelper.hexStringToBytes("43 61 66 c3 a9 20 50 61 72 61 64 69 73 6f"));
		try {
			encodedText.setTextEncoding((byte) 4, true);
			fail("IllegalArgumentException expected but not thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid text encoding 4", e.getMessage());
		}
	}

	@Test
	public void shouldReturnNullWhenDecodingInvalidString() throws UnsupportedEncodingException {
		String s = "Not unicode";
		byte[] notUnicode = BufferTools.stringToByteBuffer(s, 0, s.length());
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, notUnicode);
		assertNull(encodedText.toString());
	}

	@Test
	public void shouldHandleBacktickCharacterInString() {
		EncodedText encodedText = new EncodedText((byte) 0, BUFFER_WITH_A_BACK_TICK);
		assertEquals("I" + (char) (96) + "m", encodedText.toString());
	}

	@Test
	public void shouldStillReturnBytesWhenStringIsEmpty() {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, "");
		assertArrayEquals(new byte[]{}, encodedText.toBytes(false, false));
		assertArrayEquals(new byte[]{}, encodedText.toBytes(true, false));
		assertArrayEquals(new byte[]{0}, encodedText.toBytes(false, true));
		assertArrayEquals(new byte[]{0}, encodedText.toBytes(true, true));
	}

	@Test
	public void shouldStillReturnBytesWhenUnicodeStringIsEmpty() {
		EncodedText encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, "");
		assertArrayEquals(new byte[]{}, encodedText.toBytes(false, false));
		assertArrayEquals(new byte[]{(byte) 0xff, (byte) 0xfe}, encodedText.toBytes(true, false));
		assertArrayEquals(new byte[]{0, 0}, encodedText.toBytes(false, true));
		assertArrayEquals(new byte[]{(byte) 0xff, (byte) 0xfe, 0, 0}, encodedText.toBytes(true, true));
	}

	@Test
	public void shouldStillReturnBytesWhenDataIsEmpty() {
		EncodedText encodedText;
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, new byte[]{});
		assertArrayEquals(new byte[]{}, encodedText.toBytes(false, false));
		assertArrayEquals(new byte[]{}, encodedText.toBytes(true, false));
		assertArrayEquals(new byte[]{0}, encodedText.toBytes(false, true));
		assertArrayEquals(new byte[]{0}, encodedText.toBytes(true, true));
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, new byte[]{0});
		assertArrayEquals(new byte[]{}, encodedText.toBytes(false, false));
		assertArrayEquals(new byte[]{}, encodedText.toBytes(true, false));
		assertArrayEquals(new byte[]{0}, encodedText.toBytes(false, true));
		assertArrayEquals(new byte[]{0}, encodedText.toBytes(true, true));
	}

	@Test
	public void shouldStillReturnBytesWhenUnicodeDataIsEmpty() {
		EncodedText encodedText;
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, new byte[]{});
		assertArrayEquals(new byte[]{}, encodedText.toBytes(false, false));
		assertArrayEquals(new byte[]{(byte) 0xff, (byte) 0xfe}, encodedText.toBytes(true, false));
		assertArrayEquals(new byte[]{0, 0}, encodedText.toBytes(false, true));
		assertArrayEquals(new byte[]{(byte) 0xff, (byte) 0xfe, 0, 0}, encodedText.toBytes(true, true));
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, new byte[]{0, 0});
		assertArrayEquals(new byte[]{}, encodedText.toBytes(false, false));
		assertArrayEquals(new byte[]{(byte) 0xff, (byte) 0xfe}, encodedText.toBytes(true, false));
		assertArrayEquals(new byte[]{0, 0}, encodedText.toBytes(false, true));
		assertArrayEquals(new byte[]{(byte) 0xff, (byte) 0xfe, 0, 0}, encodedText.toBytes(true, true));
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, new byte[]{(byte) 0xff, (byte) 0xfe});
		assertArrayEquals(new byte[]{}, encodedText.toBytes(false, false));
		assertArrayEquals(new byte[]{(byte) 0xff, (byte) 0xfe}, encodedText.toBytes(true, false));
		assertArrayEquals(new byte[]{0, 0}, encodedText.toBytes(false, true));
		assertArrayEquals(new byte[]{(byte) 0xff, (byte) 0xfe, 0, 0}, encodedText.toBytes(true, true));
		encodedText = new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, new byte[]{(byte) 0xff, (byte) 0xfe, 0, 0});
		assertArrayEquals(new byte[]{}, encodedText.toBytes(false, false));
		assertArrayEquals(new byte[]{(byte) 0xff, (byte) 0xfe}, encodedText.toBytes(true, false));
		assertArrayEquals(new byte[]{0, 0}, encodedText.toBytes(false, true));
		assertArrayEquals(new byte[]{(byte) 0xff, (byte) 0xfe, 0, 0}, encodedText.toBytes(true, true));
	}
}
