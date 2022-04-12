package com.mpatric.mp3agic;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class BufferToolsTest {

	private static final byte BYTE_T = 0x54;
	private static final byte BYTE_A = 0x41;
	private static final byte BYTE_G = 0x47;
	private static final byte BYTE_DASH = 0x2D;
	private static final byte BYTE_FF = -0x01;
	private static final byte BYTE_FB = -0x05;
	private static final byte BYTE_90 = -0x70;
	private static final byte BYTE_44 = 0x44;
	private static final byte BYTE_E0 = -0x20;
	private static final byte BYTE_F0 = -0x10;
	private static final byte BYTE_81 = -0x7F;
	private static final byte BYTE_ESZETT = -0x21;

	// byte buffer to string

	@Test
	public void shouldExtractStringFromStartOfBuffer() throws UnsupportedEncodingException {
		byte[] buffer = {BYTE_T, BYTE_A, BYTE_G, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH};
		assertEquals("TAG", BufferTools.byteBufferToString(buffer, 0, 3));
	}

	@Test
	public void shouldExtractStringFromEndOfBuffer() throws UnsupportedEncodingException {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		assertEquals("TAG", BufferTools.byteBufferToString(buffer, 5, 3));
	}

	@Test
	public void shouldExtractStringFromMiddleOfBuffer() throws UnsupportedEncodingException {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		assertEquals("TAG", BufferTools.byteBufferToString(buffer, 5, 3));
	}

	@Test
	public void shouldExtractUnicodeStringFromMiddleOfBuffer() throws UnsupportedEncodingException {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, 0x03, (byte) 0xb3, 0x03, (byte) 0xb5, 0x03, (byte) 0xb9, 0x03, (byte) 0xac, BYTE_DASH, BYTE_DASH};
		assertEquals("\u03B3\u03B5\u03B9\u03AC", BufferTools.byteBufferToString(buffer, 2, 8, "UTF-16BE"));
	}

	@Test
	public void shouldThrowExceptionForOffsetBeforeStartOfArray() throws UnsupportedEncodingException {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		try {
			BufferTools.byteBufferToString(buffer, -1, 4);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) {
			// expected
		}
	}

	@Test
	public void shouldThrowExceptionForOffsetAfterEndOfArray() throws UnsupportedEncodingException {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		try {
			BufferTools.byteBufferToString(buffer, buffer.length, 1);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) {
			// expected
		}
	}

	@Test
	public void shouldThrowExceptionForLengthExtendingBeyondEndOfArray() throws UnsupportedEncodingException {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		try {
			BufferTools.byteBufferToString(buffer, buffer.length - 2, 3);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) {
			// expected
		}
	}

	// string to byte buffer

	@Test
	public void shouldConvertStringToBufferAndBack() throws UnsupportedEncodingException {
		String original = "1234567890QWERTYUIOP";
		byte[] buffer = BufferTools.stringToByteBuffer(original, 0, original.length());
		String converted = BufferTools.byteBufferToString(buffer, 0, buffer.length);
		assertEquals(original, converted);
	}

	@Test
	public void shouldConvertSubstringToBufferAndBack() throws UnsupportedEncodingException {
		String original = "1234567890QWERTYUIOP";
		byte[] buffer = BufferTools.stringToByteBuffer(original, 2, original.length() - 5);
		String converted = BufferTools.byteBufferToString(buffer, 0, buffer.length);
		assertEquals("34567890QWERTYU", converted);
	}

	@Test
	public void shouldConvertUnicodeStringToBufferAndBack() throws UnsupportedEncodingException {
		String original = "\u03B3\u03B5\u03B9\u03AC \u03C3\u03BF\u03C5";
		byte[] buffer = BufferTools.stringToByteBuffer(original, 0, original.length(), "UTF-16LE");
		String converted = BufferTools.byteBufferToString(buffer, 0, buffer.length, "UTF-16LE");
		assertEquals(original, converted);
	}

	@Test
	public void shouldConvertUnicodeSubstringToBufferAndBack() throws UnsupportedEncodingException {
		String original = "\u03B3\u03B5\u03B9\u03AC \u03C3\u03BF\u03C5";
		byte[] buffer = BufferTools.stringToByteBuffer(original, 2, original.length() - 5, "UTF-16LE");
		String converted = BufferTools.byteBufferToString(buffer, 0, buffer.length, "UTF-16LE");
		assertEquals("\u03B9\u03AC ", converted);
	}

	@Test
	public void shouldThrowAnExceptionWhenConvertingStringToBytesWithOffsetOutOfRange() throws UnsupportedEncodingException {
		String original = "1234567890QWERTYUIOP";
		try {
			BufferTools.stringToByteBuffer(original, -1, 1);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) { /* expected*/ }
		try {
			BufferTools.stringToByteBuffer(original, original.length(), 1);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) { /* expected*/ }
	}

	@Test
	public void shouldThrowAnExceptionWhenConvertingStringToBytesWithLengthOutOfRange() throws UnsupportedEncodingException {
		String original = "1234567890QWERTYUIOP";
		try {
			BufferTools.stringToByteBuffer(original, 0, -1);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) { /* expected*/ }
		try {
			BufferTools.stringToByteBuffer(original, 0, original.length() + 1);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) { /* expected*/ }
		try {
			BufferTools.stringToByteBuffer(original, 3, original.length() - 2);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) { /* expected*/ }
	}

	// string into existing byte buffer

	@Test
	public void shouldCopyStringToStartOfByteBuffer() throws UnsupportedEncodingException {
		byte[] buffer = new byte[10];
		Arrays.fill(buffer, (byte) 0);
		String s = "TAG-";
		BufferTools.stringIntoByteBuffer(s, 0, s.length(), buffer, 0);
		byte[] expectedBuffer = {BYTE_T, BYTE_A, BYTE_G, BYTE_DASH, 0, 0, 0, 0, 0, 0};
		assertArrayEquals(expectedBuffer, buffer);
	}

	@Test
	public void shouldCopyUnicodeStringToStartOfByteBuffer() throws UnsupportedEncodingException {
		byte[] buffer = new byte[10];
		Arrays.fill(buffer, (byte) 0);
		String s = "\u03B3\u03B5\u03B9\u03AC";
		BufferTools.stringIntoByteBuffer(s, 0, s.length(), buffer, 0, "UTF-16BE");
		byte[] expectedBuffer = {0x03, (byte) 0xb3, 0x03, (byte) 0xb5, 0x03, (byte) 0xb9, 0x03, (byte) 0xac, 0, 0};
		assertArrayEquals(expectedBuffer, buffer);
	}

	@Test
	public void shouldCopyStringToEndOfByteBuffer() throws UnsupportedEncodingException {
		byte[] buffer = new byte[10];
		Arrays.fill(buffer, (byte) 0);
		String s = "TAG-";
		BufferTools.stringIntoByteBuffer(s, 0, s.length(), buffer, 6);
		byte[] expectedBuffer = {0, 0, 0, 0, 0, 0, BYTE_T, BYTE_A, BYTE_G, BYTE_DASH};
		assertArrayEquals(expectedBuffer, buffer);
	}

	@Test
	public void shouldCopyUnicodeStringToEndOfByteBuffer() throws UnsupportedEncodingException {
		byte[] buffer = new byte[10];
		Arrays.fill(buffer, (byte) 0);
		String s = "\u03B3\u03B5\u03B9\u03AC";
		BufferTools.stringIntoByteBuffer(s, 0, s.length(), buffer, 2, "UTF-16BE");
		byte[] expectedBuffer = {0, 0, 0x03, (byte) 0xb3, 0x03, (byte) 0xb5, 0x03, (byte) 0xb9, 0x03, (byte) 0xac};
		assertArrayEquals(expectedBuffer, buffer);
	}

	@Test
	public void shouldCopySubstringToStartOfByteBuffer() throws UnsupportedEncodingException {
		byte[] buffer = new byte[10];
		Arrays.fill(buffer, (byte) 0);
		String s = "TAG-";
		BufferTools.stringIntoByteBuffer(s, 1, 2, buffer, 0);
		byte[] expectedBuffer = {BYTE_A, BYTE_G, 0, 0, 0, 0, 0, 0, 0, 0};
		assertArrayEquals(expectedBuffer, buffer);
	}

	@Test
	public void shouldCopyUnicodeSubstringToStartOfByteBuffer() throws UnsupportedEncodingException {
		byte[] buffer = new byte[10];
		Arrays.fill(buffer, (byte) 0);
		String s = "\u03B3\u03B5\u03B9\u03AC";
		BufferTools.stringIntoByteBuffer(s, 1, 2, buffer, 0, "UTF-16BE");
		byte[] expectedBuffer = {0x03, (byte) 0xb5, 0x03, (byte) 0xb9, 0, 0, 0, 0, 0, 0};
		assertArrayEquals(expectedBuffer, buffer);
	}

	@Test
	public void shouldCopySubstringToMiddleOfByteBuffer() throws UnsupportedEncodingException {
		byte[] buffer = new byte[10];
		Arrays.fill(buffer, (byte) 0);
		String s = "TAG-";
		BufferTools.stringIntoByteBuffer(s, 1, 2, buffer, 4);
		byte[] expectedBuffer = {0, 0, 0, 0, BYTE_A, BYTE_G, 0, 0, 0, 0};
		assertArrayEquals(expectedBuffer, buffer);
	}

	@Test
	public void shouldRaiseExceptionWhenCopyingStringIntoByteBufferWithOffsetOutOfRange() throws UnsupportedEncodingException {
		byte[] buffer = new byte[10];
		String s = "TAG-";
		try {
			BufferTools.stringIntoByteBuffer(s, -1, 1, buffer, 0);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) { /* expected*/ }
		try {
			BufferTools.stringIntoByteBuffer(s, s.length(), 1, buffer, 0);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) { /* expected*/ }
	}

	@Test
	public void shouldRaiseExceptionWhenCopyingStringIntoByteBufferWithLengthOutOfRange() throws UnsupportedEncodingException {
		byte[] buffer = new byte[10];
		String s = "TAG-";
		try {
			BufferTools.stringIntoByteBuffer(s, 0, -1, buffer, 0);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) { /* expected*/ }
		try {
			BufferTools.stringIntoByteBuffer(s, 0, s.length() + 1, buffer, 0);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) { /* expected*/ }
		try {
			BufferTools.stringIntoByteBuffer(s, 3, s.length() - 2, buffer, 0);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) { /* expected*/ }
	}

	@Test
	public void shouldRaiseExceptionWhenCopyingStringIntoByteBufferWithDestinationOffsetOutOfRange() throws UnsupportedEncodingException {
		byte[] buffer = new byte[10];
		String s = "TAG-";
		try {
			BufferTools.stringIntoByteBuffer(s, 0, 1, buffer, 10);
			fail("ArrayIndexOutOfBoundsException expected but not thrown");
		} catch (ArrayIndexOutOfBoundsException e) { /* expected*/ }
		try {
			BufferTools.stringIntoByteBuffer(s, 0, s.length(), buffer, buffer.length - s.length() + 1);
			fail("ArrayIndexOutOfBoundsException expected but not thrown");
		} catch (ArrayIndexOutOfBoundsException e) { /* expected*/ }
	}

	// trim strings

	@Test
	public void shouldRightTrimStringsCorrectly() throws UnsupportedEncodingException {
		assertEquals("", BufferTools.trimStringRight(""));
		assertEquals("", BufferTools.trimStringRight(" "));
		assertEquals("TEST", BufferTools.trimStringRight("TEST"));
		assertEquals("TEST", BufferTools.trimStringRight("TEST   "));
		assertEquals("   TEST", BufferTools.trimStringRight("   TEST"));
		assertEquals("   TEST", BufferTools.trimStringRight("   TEST   "));
		assertEquals("TEST", BufferTools.trimStringRight("TEST\t\r\n"));
		assertEquals("TEST", BufferTools.trimStringRight("TEST" + BufferTools.byteBufferToString(new byte[]{0, 0}, 0, 2)));
	}

	@Test
	public void shouldRightTrimUnicodeStringsCorrectly() throws UnsupportedEncodingException {
		assertEquals("\u03B3\u03B5\u03B9\u03AC", BufferTools.trimStringRight("\u03B3\u03B5\u03B9\u03AC"));
		assertEquals("\u03B3\u03B5\u03B9\u03AC", BufferTools.trimStringRight("\u03B3\u03B5\u03B9\u03AC   "));
		assertEquals("   \u03B3\u03B5\u03B9\u03AC", BufferTools.trimStringRight("   \u03B3\u03B5\u03B9\u03AC"));
		assertEquals("   \u03B3\u03B5\u03B9\u03AC", BufferTools.trimStringRight("   \u03B3\u03B5\u03B9\u03AC   "));
		assertEquals("\u03B3\u03B5\u03B9\u03AC", BufferTools.trimStringRight("\u03B3\u03B5\u03B9\u03AC\t\r\n"));
		assertEquals("\u03B3\u03B5\u03B9\u03AC", BufferTools.trimStringRight("\u03B3\u03B5\u03B9\u03AC" + BufferTools.byteBufferToString(new byte[]{0, 0}, 0, 2)));
	}

	@Test
	public void shouldRightPadStringsCorrectly() {
		assertEquals("1234", BufferTools.padStringRight("1234", 3, ' '));
		assertEquals("123", BufferTools.padStringRight("123", 3, ' '));
		assertEquals("12 ", BufferTools.padStringRight("12", 3, ' '));
		assertEquals("1  ", BufferTools.padStringRight("1", 3, ' '));
		assertEquals("   ", BufferTools.padStringRight("", 3, ' '));
	}

	@Test
	public void shouldRightPadUnicodeStringsCorrectly() {
		assertEquals("\u03B3\u03B5\u03B9\u03AC", BufferTools.padStringRight("\u03B3\u03B5\u03B9\u03AC", 3, ' '));
		assertEquals("\u03B3\u03B5\u03B9", BufferTools.padStringRight("\u03B3\u03B5\u03B9", 3, ' '));
		assertEquals("\u03B3\u03B5 ", BufferTools.padStringRight("\u03B3\u03B5", 3, ' '));
		assertEquals("\u03B3  ", BufferTools.padStringRight("\u03B3", 3, ' '));
	}

	@Test
	public void shouldPadRightWithNullCharacters() {
		assertEquals("123", BufferTools.padStringRight("123", 3, '\00'));
		assertEquals("12\00", BufferTools.padStringRight("12", 3, '\00'));
		assertEquals("1\00\00", BufferTools.padStringRight("1", 3, '\00'));
		assertEquals("\00\00\00", BufferTools.padStringRight("", 3, '\00'));
	}

	@Test
	public void shouldExtractBitsCorrectly() {
		byte b = -0x36; // 11001010
		assertFalse(BufferTools.checkBit(b, 0));
		assertTrue(BufferTools.checkBit(b, 1));
		assertFalse(BufferTools.checkBit(b, 2));
		assertTrue(BufferTools.checkBit(b, 3));
		assertFalse(BufferTools.checkBit(b, 4));
		assertFalse(BufferTools.checkBit(b, 5));
		assertTrue(BufferTools.checkBit(b, 6));
		assertTrue(BufferTools.checkBit(b, 7));
	}

	@Test
	public void shouldSetBitsInBytesCorrectly() {
		byte b = -0x36; // 11001010
		assertEquals(-0x36, BufferTools.setBit(b, 7, true)); // 11010010
		assertEquals(-0x35, BufferTools.setBit(b, 0, true)); // 11001011
		assertEquals(-0x26, BufferTools.setBit(b, 4, true)); // 11011010
		assertEquals(-0x36, BufferTools.setBit(b, 0, false)); // 11010010
		assertEquals(0x4A, BufferTools.setBit(b, 7, false)); // 01001010
		assertEquals(-0x3E, BufferTools.setBit(b, 3, false)); // 11000010
	}

	@Test
	public void shouldUnpackIntegerCorrectly() {
		assertEquals(0xFFFB9044, BufferTools.unpackInteger(BYTE_FF, BYTE_FB, BYTE_90, BYTE_44));
		assertEquals(0x00000081, BufferTools.unpackInteger((byte) 0, (byte) 0, (byte) 0, BYTE_81));
		assertEquals(0x00000101, BufferTools.unpackInteger((byte) 0, (byte) 0, (byte) 1, (byte) 1));
	}

	@Test
	public void shouldUnpackSynchsafeIntegersCorrectly() {
		assertEquals(1217, BufferTools.unpackSynchsafeInteger((byte) 0, (byte) 0, (byte) 0x09, (byte) 0x41));
		assertEquals(1227, BufferTools.unpackSynchsafeInteger((byte) 0, (byte) 0, (byte) 0x09, (byte) 0x4B));
		assertEquals(1002, BufferTools.unpackSynchsafeInteger((byte) 0, (byte) 0, (byte) 0x07, (byte) 0x6A));
		assertEquals(0x0101, BufferTools.unpackSynchsafeInteger((byte) 0, (byte) 0, (byte) 2, (byte) 1));
		assertEquals(0x01010101, BufferTools.unpackSynchsafeInteger((byte) 8, (byte) 4, (byte) 2, (byte) 1));
	}

	@Test
	public void shouldPackIntegerCorrectly() {
		assertArrayEquals(new byte[]{BYTE_FF, BYTE_FB, BYTE_90, BYTE_44}, BufferTools.packInteger(0xFFFB9044));
	}

	@Test
	public void shouldPackSynchsafeIntegersCorrectly() {
		assertArrayEquals(new byte[]{(byte) 0, (byte) 0, (byte) 0x09, (byte) 0x41}, BufferTools.packSynchsafeInteger(1217));
		assertArrayEquals(new byte[]{(byte) 0, (byte) 0, (byte) 0x09, (byte) 0x4B}, BufferTools.packSynchsafeInteger(1227));
		assertArrayEquals(new byte[]{(byte) 0, (byte) 0, (byte) 0x07, (byte) 0x6A}, BufferTools.packSynchsafeInteger(1002));
		assertArrayEquals(new byte[]{(byte) 0, (byte) 0, (byte) 2, (byte) 1}, BufferTools.packSynchsafeInteger(0x0101));
		assertArrayEquals(new byte[]{(byte) 8, (byte) 4, (byte) 2, (byte) 1}, BufferTools.packSynchsafeInteger(0x01010101));
	}

	@Test
	public void shouldPackAndUnpackIntegerBackToOriginalValue() {
		int original = 12345;
		byte[] bytes = BufferTools.packInteger(original);
		int unpacked = BufferTools.unpackInteger(bytes[0], bytes[1], bytes[2], bytes[3]);
		assertEquals(original, unpacked);
	}

	@Test
	public void shouldPackAndUnpackSynchsafeIntegerBackToOriginalValue() {
		int original = 12345;
		byte[] bytes = BufferTools.packSynchsafeInteger(original);
		int unpacked = BufferTools.unpackSynchsafeInteger(bytes[0], bytes[1], bytes[2], bytes[3]);
		assertEquals(original, unpacked);
	}

	@Test
	public void shouldCopyBuffersWithValidOffsetsAndLengths() {
		byte[] buffer = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, BufferTools.copyBuffer(buffer, 0, buffer.length));
		assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, BufferTools.copyBuffer(buffer, 1, buffer.length - 1));
		assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8}, BufferTools.copyBuffer(buffer, 0, buffer.length - 1));
		assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, BufferTools.copyBuffer(buffer, 1, buffer.length - 2));
		assertArrayEquals(new byte[]{4}, BufferTools.copyBuffer(buffer, 4, 1));
	}

	@Test
	public void throwsExceptionWhenCopyingBufferWithInvalidOffsetAndOrLength() {
		byte[] buffer = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

		try {
			BufferTools.copyBuffer(buffer, -1, buffer.length);
			fail("ArrayIndexOutOfBoundsException expected but not thrown");
		} catch (ArrayIndexOutOfBoundsException e) { /* expected*/ }

		try {
			BufferTools.copyBuffer(buffer, buffer.length, 1);
			fail("ArrayIndexOutOfBoundsException expected but not thrown");
		} catch (ArrayIndexOutOfBoundsException e) { /* expected*/ }

		try {
			BufferTools.copyBuffer(buffer, 1, buffer.length);
			fail("ArrayIndexOutOfBoundsException expected but not thrown");
		} catch (ArrayIndexOutOfBoundsException e) { /* expected*/ }
	}

	@Test
	public void shouldDetermineUnsynchronisationSizesCorrectly() {
		assertEquals(0, BufferTools.sizeUnsynchronisationWouldAdd(new byte[]{}));
		assertEquals(0, BufferTools.sizeUnsynchronisationWouldAdd(new byte[]{BYTE_FF, 1, BYTE_FB}));
		assertEquals(1, BufferTools.sizeUnsynchronisationWouldAdd(new byte[]{BYTE_FF, BYTE_FB}));
		assertEquals(1, BufferTools.sizeUnsynchronisationWouldAdd(new byte[]{0, BYTE_FF, BYTE_FB, 0}));
		assertEquals(1, BufferTools.sizeUnsynchronisationWouldAdd(new byte[]{0, BYTE_FF}));
		assertEquals(2, BufferTools.sizeUnsynchronisationWouldAdd(new byte[]{BYTE_FF, BYTE_FB, 0, BYTE_FF, BYTE_FB}));
		assertEquals(3, BufferTools.sizeUnsynchronisationWouldAdd(new byte[]{BYTE_FF, BYTE_FF, BYTE_FF}));
	}

	@Test
	public void shouldDetermineSynchronisationSizesCorrectly() {
		assertEquals(0, BufferTools.sizeSynchronisationWouldSubtract(new byte[]{}));
		assertEquals(0, BufferTools.sizeSynchronisationWouldSubtract(new byte[]{BYTE_FF, 1, BYTE_FB}));
		assertEquals(1, BufferTools.sizeSynchronisationWouldSubtract(new byte[]{BYTE_FF, 0, BYTE_FB}));
		assertEquals(1, BufferTools.sizeSynchronisationWouldSubtract(new byte[]{0, BYTE_FF, 0, BYTE_FB, 0}));
		assertEquals(1, BufferTools.sizeSynchronisationWouldSubtract(new byte[]{0, BYTE_FF, 0}));
		assertEquals(2, BufferTools.sizeSynchronisationWouldSubtract(new byte[]{BYTE_FF, 0, BYTE_FB, 0, BYTE_FF, 0, BYTE_FB}));
		assertEquals(3, BufferTools.sizeSynchronisationWouldSubtract(new byte[]{BYTE_FF, 0, BYTE_FF, 0, BYTE_FF, 0}));
	}

	@Test
	public void shouldUnsynchroniseThenSynchroniseFFExBytesCorrectly() {
		byte[] buffer = {BYTE_FF, BYTE_FB, 2, 3, 4, BYTE_FF, BYTE_E0, 7, 8, 9, 10, 11, 12, 13, BYTE_FF, BYTE_F0};
		byte[] expectedBuffer = {BYTE_FF, 0, BYTE_FB, 2, 3, 4, BYTE_FF, 0, BYTE_E0, 7, 8, 9, 10, 11, 12, 13, BYTE_FF, 0, BYTE_F0};
		byte[] unsynchronised = BufferTools.unsynchroniseBuffer(buffer);
		byte[] synchronised = BufferTools.synchroniseBuffer(unsynchronised);
		assertArrayEquals(expectedBuffer, unsynchronised);
		assertArrayEquals(buffer, synchronised);
	}

	@Test
	public void shouldUnsynchroniseThenSynchroniseFF00BytesCorrectly() {
		byte[] buffer = {BYTE_FF, 0, 2, 3, 4, BYTE_FF, 0, 7, 8, 9, 10, 11, 12, 13, BYTE_FF, 0};
		byte[] expectedBuffer = {BYTE_FF, 0, 0, 2, 3, 4, BYTE_FF, 0, 0, 7, 8, 9, 10, 11, 12, 13, BYTE_FF, 0, 0};
		byte[] unsynchronised = BufferTools.unsynchroniseBuffer(buffer);
		byte[] synchronised = BufferTools.synchroniseBuffer(unsynchronised);
		assertArrayEquals(expectedBuffer, unsynchronised);
		assertArrayEquals(buffer, synchronised);
	}

	@Test
	public void shouldUnsynchroniseThenSynchroniseBufferFullOfFFsCorrectly() {
		byte[] buffer = {BYTE_FF, BYTE_FF, BYTE_FF, BYTE_FF};
		byte[] expectedBuffer = {BYTE_FF, 0, BYTE_FF, 0, BYTE_FF, 0, BYTE_FF, 0};
		byte[] unsynchronised = BufferTools.unsynchroniseBuffer(buffer);
		byte[] synchronised = BufferTools.synchroniseBuffer(unsynchronised);
		assertArrayEquals(expectedBuffer, unsynchronised);
		assertArrayEquals(buffer, synchronised);
	}

	@Test
	public void shouldUnsynchroniseThenSynchroniseBufferMinimalBufferCorrectly() {
		byte[] buffer = {BYTE_FF};
		byte[] expectedBuffer = {BYTE_FF, 0};
		byte[] unsynchronised = BufferTools.unsynchroniseBuffer(buffer);
		byte[] synchronised = BufferTools.synchroniseBuffer(unsynchronised);
		assertArrayEquals(expectedBuffer, unsynchronised);
		assertArrayEquals(buffer, synchronised);
	}

	@Test
	public void shouldReturnOriginalBufferIfNoUnynchronisationOrSynchronisationIsRequired() {
		byte[] buffer = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
		byte[] unsynchronised = BufferTools.unsynchroniseBuffer(buffer);
		byte[] synchronised = BufferTools.synchroniseBuffer(buffer);
		assertEquals(buffer, unsynchronised);
		assertEquals(buffer, synchronised);
	}

	@Test
	public void shouldReplaceTokensWithSpecifiedStrings() {
		String source = "%1-%2 something %1-%3";
		assertEquals("ONE-%2 something ONE-%3", BufferTools.substitute(source, "%1", "ONE"));
		assertEquals("%1-TWO something %1-%3", BufferTools.substitute(source, "%2", "TWO"));
		assertEquals("%1-%2 something %1-THREE", BufferTools.substitute(source, "%3", "THREE"));
	}

	@Test
	public void shouldReturnOriginalStringIfTokenToSubstituteDoesNotExistInString() {
		String source = "%1-%2 something %1-%3";
		assertEquals("%1-%2 something %1-%3", BufferTools.substitute(source, "%X", "XXXXX"));
	}

	@Test
	public void shouldReturnOriginalStringForSubstitutionWithEmptyString() {
		String source = "%1-%2 something %1-%3";
		assertEquals("%1-%2 something %1-%3", BufferTools.substitute(source, "", "WHATEVER"));
	}

	@Test
	public void shouldSubstituteEmptyStringWhenDestinationStringIsNull() {
		String source = "%1-%2 something %1-%3";
		assertEquals("-%2 something -%3", BufferTools.substitute(source, "%1", null));
	}

	@Test
	public void shouldConvertNonAsciiCharactersToQuestionMarksInString() {
		assertEquals("?12?34?567???89?", BufferTools.asciiOnly("ü12¬34ü567¬¬¬89ü"));
	}

	@Test
	public void convertsBufferContainingHighAscii() throws UnsupportedEncodingException {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G};
		assertEquals("T" + (char) (223) + "G", BufferTools.byteBufferToString(buffer, 0, 3));
	}

	// finding terminators

	@Test
	public void findsSingleTerminator() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, BYTE_G, BYTE_A};
		assertEquals(4, BufferTools.indexOfTerminator(buffer, 0, 1));
	}

	@Test
	public void findsFirstSingleTerminator() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, BYTE_G, BYTE_A, 0, BYTE_G, BYTE_A};
		assertEquals(4, BufferTools.indexOfTerminator(buffer, 0, 1));
	}

	@Test
	public void findsFirstSingleTerminatorAfterFromIndex() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, BYTE_G, BYTE_A, 0, BYTE_G, BYTE_A};
		assertEquals(7, BufferTools.indexOfTerminator(buffer, 5, 1));
	}

	@Test
	public void findsSingleTerminatorWhenFirstElement() {
		byte[] buffer = {0, BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T};
		assertEquals(0, BufferTools.indexOfTerminator(buffer, 0, 1));
	}

	@Test
	public void findsSingleTerminatorWhenLastElement() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0};
		assertEquals(4, BufferTools.indexOfTerminator(buffer, 0, 1));
	}

	@Test
	public void ReturnsMinusOneWhenNoSingleTerminator() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T};
		assertEquals(-1, BufferTools.indexOfTerminator(buffer, 0, 1));
	}

	@Test
	public void findsDoubleTerminator() {
		byte[] buffer = {BYTE_T, 0, BYTE_G, BYTE_T, 0, 0, BYTE_G, BYTE_A};
		assertEquals(4, BufferTools.indexOfTerminator(buffer, 0, 2));
	}

	@Test
	public void findsNotFindDoubleTerminatorIfNotOnEvenByte() {
		byte[] buffer = {BYTE_T, 0, BYTE_G, BYTE_T, BYTE_T, 0, 0, BYTE_G, BYTE_A};
		assertEquals(-1, BufferTools.indexOfTerminator(buffer, 0, 2));
	}

	@Test
	public void findsFirstDoubleTerminator() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, 0, BYTE_G, BYTE_A, 0, 0, BYTE_G, BYTE_A};
		assertEquals(4, BufferTools.indexOfTerminator(buffer, 0, 2));
	}

	@Test
	public void findsFirstDoubleTerminatorOnAnEvenByte() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, 0, 0, BYTE_T, BYTE_G, BYTE_A, 0, 0, BYTE_G, BYTE_A};
		assertEquals(8, BufferTools.indexOfTerminator(buffer, 0, 2));
	}

	@Test
	public void findsFirstDoubleTerminatorAfterFromIndex() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, 0, BYTE_G, BYTE_A, 0, 0, BYTE_G, BYTE_A};
		assertEquals(8, BufferTools.indexOfTerminator(buffer, 6, 2));
	}

	@Test
	public void findsDoubleTerminatorWhenFirstElement() {
		byte[] buffer = {0, 0, BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T};
		assertEquals(0, BufferTools.indexOfTerminator(buffer, 0, 2));
	}

	@Test
	public void findsDoubleTerminatorWhenLastElement() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, 0};
		assertEquals(4, BufferTools.indexOfTerminator(buffer, 0, 2));
	}

	@Test
	public void returnsMinusOneWhenNoDoubleTerminator() {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T};
		assertEquals(-1, BufferTools.indexOfTerminator(buffer, 0, 2));
	}
}
