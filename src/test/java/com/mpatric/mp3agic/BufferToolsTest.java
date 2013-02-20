package com.mpatric.mp3agic;

import java.util.Arrays;

import com.mpatric.mp3agic.BufferTools;

import junit.framework.TestCase;

public class BufferToolsTest extends TestCase {

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
	
	public void testShouldExtractStringFromStartOfBuffer() throws Exception {
		byte[] buffer = {BYTE_T, BYTE_A, BYTE_G, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH};
		assertEquals("TAG", BufferTools.byteBufferToString(buffer, 0, 3));
	}
	
	public void testShouldExtractStringFromEndOfBuffer() throws Exception {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		assertEquals("TAG", BufferTools.byteBufferToString(buffer, 5, 3));
	}
	
	public void testShouldExtractStringFromMiddleOfBuffer() throws Exception {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		assertEquals("TAG", BufferTools.byteBufferToString(buffer, 5, 3));
	}
	
	public void testShouldExtractUnicodeStringFromMiddleOfBuffer() throws Exception {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, 0x03, (byte)0xb3, 0x03, (byte)0xb5, 0x03, (byte)0xb9, 0x03, (byte)0xac, BYTE_DASH, BYTE_DASH};
		assertEquals("\u03B3\u03B5\u03B9\u03AC", BufferTools.byteBufferToString(buffer, 2, 8, "UTF-16BE"));
	}
	
	public void testShouldThrowExceptionForOffsetBeforeStartOfArray() throws Exception {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		try {
			BufferTools.byteBufferToString(buffer, -1, 4);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) {
			// expected
		}
	}
	
	public void testShouldThrowExceptionForOffsetAfterEndOfArray() throws Exception {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		try {
			BufferTools.byteBufferToString(buffer, buffer.length, 1);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) {
			// expected
		}
	}
	
	public void testShouldThrowExceptionForLengthExtendingBeyondEndOfArray() throws Exception {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		try {
			BufferTools.byteBufferToString(buffer, buffer.length - 2, 3);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) {
			// expected
		}
	}
	
	// string to byte buffer
	
	public void testShouldConvertStringToBufferAndBack() throws Exception {
		String original = "1234567890QWERTYUIOP";
		byte buffer[] = BufferTools.stringToByteBuffer(original, 0, original.length());
		String converted = BufferTools.byteBufferToString(buffer, 0, buffer.length);
		assertEquals(original, converted);
	}
	
	public void testShouldConvertSubstringToBufferAndBack() throws Exception {
		String original = "1234567890QWERTYUIOP";
		byte buffer[] = BufferTools.stringToByteBuffer(original, 2, original.length() - 5);
		String converted = BufferTools.byteBufferToString(buffer, 0, buffer.length);
		assertEquals("34567890QWERTYU", converted);
	}
	
	public void testShouldConvertUnicodeStringToBufferAndBack() throws Exception {
		String original = "\u03B3\u03B5\u03B9\u03AC \u03C3\u03BF\u03C5";
		byte buffer[] = BufferTools.stringToByteBuffer(original, 0, original.length(), "UTF-16LE");
		String converted = BufferTools.byteBufferToString(buffer, 0, buffer.length, "UTF-16LE");
		assertEquals(original, converted);
	}
	
	public void testShouldConvertUnicodeSubstringToBufferAndBack() throws Exception {
		String original = "\u03B3\u03B5\u03B9\u03AC \u03C3\u03BF\u03C5";
		byte buffer[] = BufferTools.stringToByteBuffer(original, 2, original.length() - 5, "UTF-16LE");
		String converted = BufferTools.byteBufferToString(buffer, 0, buffer.length, "UTF-16LE");
		assertEquals("\u03B9\u03AC ", converted);
	}
	
	public void testShouldThrowAnExceptionWhenConvertingStringToBytesWithOffsetOutOfRange() throws Exception {
		String original = "1234567890QWERTYUIOP";
		try {
			BufferTools.stringToByteBuffer(original, -1, 1);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) {
		}
		try {
			BufferTools.stringToByteBuffer(original, original.length(), 1);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) {
		}
	}
	
	public void testShouldThrowAnExceptionWhenConvertingStringToBytesWithLengthOutOfRange() throws Exception {
		String original = "1234567890QWERTYUIOP";
		try {
			BufferTools.stringToByteBuffer(original, 0, -1);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) {
		}
		try {
			BufferTools.stringToByteBuffer(original, 0, original.length() + 1);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) {
		}
		try {
			BufferTools.stringToByteBuffer(original, 3, original.length() - 2);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) {
		}
	}
	
	// string into existing byte buffer
	
	public void testShouldCopyStringToStartOfByteBuffer() throws Exception {
		byte buffer[] = new byte[10];
		Arrays.fill(buffer, (byte)0);
		String s = "TAG-";
		BufferTools.stringIntoByteBuffer(s, 0, s.length(), buffer, 0);
		byte[] expectedBuffer = {BYTE_T, BYTE_A, BYTE_G, BYTE_DASH, 0, 0, 0, 0, 0, 0};
		assertTrue(Arrays.equals(expectedBuffer, buffer));
	}
	
	public void testShouldCopyUnicodeStringToStartOfByteBuffer() throws Exception {
		byte buffer[] = new byte[10];
		Arrays.fill(buffer, (byte)0);
		String s = "\u03B3\u03B5\u03B9\u03AC";
		BufferTools.stringIntoByteBuffer(s, 0, s.length(), buffer, 0, "UTF-16BE");
		byte[] expectedBuffer = {0x03, (byte)0xb3, 0x03, (byte)0xb5, 0x03, (byte)0xb9, 0x03, (byte)0xac, 0, 0};
		assertTrue(Arrays.equals(expectedBuffer, buffer));
	}
	
	public void testShouldCopyStringToEndOfByteBuffer() throws Exception {
		byte buffer[] = new byte[10];
		Arrays.fill(buffer, (byte)0);
		String s = "TAG-";
		BufferTools.stringIntoByteBuffer(s, 0, s.length(), buffer, 6);
		byte[] expectedBuffer = {0, 0, 0, 0, 0, 0, BYTE_T, BYTE_A, BYTE_G, BYTE_DASH};
		assertTrue(Arrays.equals(expectedBuffer, buffer));
	}
	
	public void testShouldCopyUnicodeStringToEndOfByteBuffer() throws Exception {
		byte buffer[] = new byte[10];
		Arrays.fill(buffer, (byte)0);
		String s = "\u03B3\u03B5\u03B9\u03AC";
		BufferTools.stringIntoByteBuffer(s, 0, s.length(), buffer, 2, "UTF-16BE");
		byte[] expectedBuffer = {0, 0, 0x03, (byte)0xb3, 0x03, (byte)0xb5, 0x03, (byte)0xb9, 0x03, (byte)0xac};
		assertTrue(Arrays.equals(expectedBuffer, buffer));
	}
	
	public void testShouldCopySubstringToStartOfByteBuffer() throws Exception {
		byte buffer[] = new byte[10];
		Arrays.fill(buffer, (byte)0);
		String s = "TAG-";
		BufferTools.stringIntoByteBuffer(s, 1, 2, buffer, 0);
		byte[] expectedBuffer = {BYTE_A, BYTE_G, 0, 0, 0, 0, 0, 0, 0, 0};
		assertTrue(Arrays.equals(expectedBuffer, buffer));
	}
	
	public void testShouldCopyUnicodeSubstringToStartOfByteBuffer() throws Exception {
		byte buffer[] = new byte[10];
		Arrays.fill(buffer, (byte)0);
		String s = "\u03B3\u03B5\u03B9\u03AC";
		BufferTools.stringIntoByteBuffer(s, 1, 2, buffer, 0, "UTF-16BE");
		byte[] expectedBuffer = {0x03, (byte)0xb5, 0x03, (byte)0xb9, 0, 0, 0, 0, 0, 0};
		assertTrue(Arrays.equals(expectedBuffer, buffer));
	}
	
	public void testShouldCopySubstringToMiddleOfByteBuffer() throws Exception {
		byte buffer[] = new byte[10];
		Arrays.fill(buffer, (byte)0);
		String s = "TAG-";
		BufferTools.stringIntoByteBuffer(s, 1, 2, buffer, 4);
		byte[] expectedBuffer = {0, 0, 0, 0, BYTE_A, BYTE_G, 0, 0, 0, 0};
		assertTrue(Arrays.equals(expectedBuffer, buffer));
	}
	
	public void testShouldRaiseExceptionWhenCopyingStringIntoByteBufferWithOffsetOutOfRange() throws Exception {
		byte buffer[] = new byte[10];
		String s = "TAG-";
		try {
			BufferTools.stringIntoByteBuffer(s, -1, 1, buffer, 0);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) {
		}
		try {
			BufferTools.stringIntoByteBuffer(s, s.length(), 1, buffer, 0);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) {
		}
	}
	
	public void testShouldRaiseExceptionWhenCopyingStringIntoByteBufferWithLengthOutOfRange() throws Exception {
		byte buffer[] = new byte[10];
		String s = "TAG-";
		try {
			BufferTools.stringIntoByteBuffer(s, 0, -1, buffer, 0);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) {
		}
		try {
			BufferTools.stringIntoByteBuffer(s, 0, s.length() + 1, buffer, 0);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) {
		}
		try {
			BufferTools.stringIntoByteBuffer(s, 3, s.length() - 2, buffer, 0);
			fail("StringIndexOutOfBoundsException expected but not thrown");
		} catch (StringIndexOutOfBoundsException e) {
		}
	}
	
	public void testShouldRaiseExceptionWhenCopyingStringIntoByteBufferWithDestinationOffsetOutOfRange() throws Exception {
		byte buffer[] = new byte[10];
		String s = "TAG-";
		try {
			BufferTools.stringIntoByteBuffer(s, 0, 1, buffer, 10);
			fail("ArrayIndexOutOfBoundsException expected but not thrown");
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		try {
			BufferTools.stringIntoByteBuffer(s, 0, s.length(), buffer, buffer.length - s.length() + 1);
			fail("ArrayIndexOutOfBoundsException expected but not thrown");
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}
	
	// trim strings
	
	public void testShouldRightTrimStringsCorrectly() throws Exception {
		assertEquals("", BufferTools.trimStringRight(""));
		assertEquals("", BufferTools.trimStringRight(" "));
		assertEquals("TEST", BufferTools.trimStringRight("TEST"));
		assertEquals("TEST", BufferTools.trimStringRight("TEST   "));
		assertEquals("   TEST", BufferTools.trimStringRight("   TEST"));
		assertEquals("   TEST", BufferTools.trimStringRight("   TEST   "));
		assertEquals("TEST", BufferTools.trimStringRight("TEST\t\r\n"));
		assertEquals("TEST", BufferTools.trimStringRight("TEST" + BufferTools.byteBufferToString(new byte[] {0, 0}, 0, 2)));
	}
	
	public void testShouldRightTrimUnicodeStringsCorrectly() throws Exception {
		assertEquals("\u03B3\u03B5\u03B9\u03AC", BufferTools.trimStringRight("\u03B3\u03B5\u03B9\u03AC"));
		assertEquals("\u03B3\u03B5\u03B9\u03AC", BufferTools.trimStringRight("\u03B3\u03B5\u03B9\u03AC   "));
		assertEquals("   \u03B3\u03B5\u03B9\u03AC", BufferTools.trimStringRight("   \u03B3\u03B5\u03B9\u03AC"));
		assertEquals("   \u03B3\u03B5\u03B9\u03AC", BufferTools.trimStringRight("   \u03B3\u03B5\u03B9\u03AC   "));
		assertEquals("\u03B3\u03B5\u03B9\u03AC", BufferTools.trimStringRight("\u03B3\u03B5\u03B9\u03AC\t\r\n"));
		assertEquals("\u03B3\u03B5\u03B9\u03AC", BufferTools.trimStringRight("\u03B3\u03B5\u03B9\u03AC" + BufferTools.byteBufferToString(new byte[] {0, 0}, 0, 2)));
	}
	
	public void testShouldRightPadStringsCorrectly() throws Exception {
		assertEquals("1234", BufferTools.padStringRight("1234", 3, ' '));
		assertEquals("123", BufferTools.padStringRight("123", 3, ' '));
		assertEquals("12 ", BufferTools.padStringRight("12", 3, ' '));
		assertEquals("1  ", BufferTools.padStringRight("1", 3, ' '));
		assertEquals("   ", BufferTools.padStringRight("", 3, ' '));
	}
	
	public void testShouldRightPadUnicodeStringsCorrectly() throws Exception {
		assertEquals("\u03B3\u03B5\u03B9\u03AC", BufferTools.padStringRight("\u03B3\u03B5\u03B9\u03AC", 3, ' '));
		assertEquals("\u03B3\u03B5\u03B9", BufferTools.padStringRight("\u03B3\u03B5\u03B9", 3, ' '));
		assertEquals("\u03B3\u03B5 ", BufferTools.padStringRight("\u03B3\u03B5", 3, ' '));
		assertEquals("\u03B3  ", BufferTools.padStringRight("\u03B3", 3, ' '));
	}
	
	public void testShouldPadRightWithNullCharacters() throws Exception {
		assertEquals("123", BufferTools.padStringRight("123", 3, '\00'));
		assertEquals("12\00", BufferTools.padStringRight("12", 3, '\00'));
		assertEquals("1\00\00", BufferTools.padStringRight("1", 3, '\00'));
		assertEquals("\00\00\00", BufferTools.padStringRight("", 3, '\00'));
	}
	
	public void testShouldExtractBitsCorrectly() {
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
	
	public void testShouldSetBitsInBytesCorrectly() {
		byte b = -0x36; // 11001010
		assertEquals(-0x36, BufferTools.setBit(b, 7, true)); // 11010010
		assertEquals(-0x35, BufferTools.setBit(b, 0, true)); // 11001011
		assertEquals(-0x26, BufferTools.setBit(b, 4, true)); // 11011010
		assertEquals(-0x36, BufferTools.setBit(b, 0, false)); // 11010010
		assertEquals(0x4A, BufferTools.setBit(b, 7, false)); // 01001010
		assertEquals(-0x3E, BufferTools.setBit(b, 3, false)); // 11000010
	}
	
	public void testShouldUnpackIntegerCorrectly() {
		assertEquals(0xFFFB9044, BufferTools.unpackInteger(BYTE_FF, BYTE_FB, BYTE_90, BYTE_44));
		assertEquals(0x00000081, BufferTools.unpackInteger((byte)0, (byte)0, (byte)0, BYTE_81));
		assertEquals(0x00000101, BufferTools.unpackInteger((byte)0, (byte)0, (byte)1, (byte)1));
	}
	
	public void testShouldUnpackSynchsafeIntegersCorrectly() throws Exception {
		assertEquals(1217, BufferTools.unpackSynchsafeInteger((byte)0, (byte)0, (byte)0x09, (byte)0x41));
		assertEquals(1227, BufferTools.unpackSynchsafeInteger((byte)0, (byte)0, (byte)0x09, (byte)0x4B));
		assertEquals(1002, BufferTools.unpackSynchsafeInteger((byte)0, (byte)0, (byte)0x07, (byte)0x6A));
		assertEquals(0x0101, BufferTools.unpackSynchsafeInteger((byte)0, (byte)0, (byte)2, (byte)1));
		assertEquals(0x01010101, BufferTools.unpackSynchsafeInteger((byte)8, (byte)4, (byte)2, (byte)1));
	}
	
	public void testShouldPackIntegerCorrectly() throws Exception {
		assertTrue(Arrays.equals(new byte[] {BYTE_FF, BYTE_FB, BYTE_90, BYTE_44}, BufferTools.packInteger(0xFFFB9044)));
	}
	
	public void testShouldPackSynchsafeIntegersCorrectly() throws Exception {
		assertTrue(Arrays.equals(new byte[] {(byte)0, (byte)0, (byte)0x09, (byte)0x41}, BufferTools.packSynchsafeInteger(1217)));
		assertTrue(Arrays.equals(new byte[] {(byte)0, (byte)0, (byte)0x09, (byte)0x4B}, BufferTools.packSynchsafeInteger(1227)));
		assertTrue(Arrays.equals(new byte[] {(byte)0, (byte)0, (byte)0x07, (byte)0x6A}, BufferTools.packSynchsafeInteger(1002)));
		assertTrue(Arrays.equals(new byte[] {(byte)0, (byte)0, (byte)2, (byte)1}, BufferTools.packSynchsafeInteger(0x0101)));
		assertTrue(Arrays.equals(new byte[] {(byte)8, (byte)4, (byte)2, (byte)1}, BufferTools.packSynchsafeInteger(0x01010101)));
	}
	
	public void testShouldPackAndInpackIntegerBackToOriginalValue() throws Exception {
		int original = 12345;
		byte[] bytes = BufferTools.packInteger(original);
		int unpacked = BufferTools.unpackInteger(bytes[0], bytes[1], bytes[2], bytes[3]);
		assertEquals(original, unpacked);
	}
	
	public void testShouldPackAndInpackSynchsafeIntegerBackToOriginalValue() throws Exception {
		int original = 12345;
		byte[] bytes = BufferTools.packSynchsafeInteger(original);
		int unpacked = BufferTools.unpackSynchsafeInteger(bytes[0], bytes[1], bytes[2], bytes[3]);
		assertEquals(original, unpacked);
	}
	
	public void testShouldCopyBuffersWithValidOffsetsAndLengths() throws Exception {
		byte[] buffer = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		assertTrue(Arrays.equals(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, BufferTools.copyBuffer(buffer, 0, buffer.length)));
		assertTrue(Arrays.equals(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9}, BufferTools.copyBuffer(buffer, 1, buffer.length - 1)));
		assertTrue(Arrays.equals(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8}, BufferTools.copyBuffer(buffer, 0, buffer.length - 1)));
		assertTrue(Arrays.equals(new byte[] {1, 2, 3, 4, 5, 6, 7, 8}, BufferTools.copyBuffer(buffer, 1, buffer.length - 2)));
		assertTrue(Arrays.equals(new byte[] {4}, BufferTools.copyBuffer(buffer, 4, 1)));
	}
	
	public void testThrowExceptionWhenCopyingBufferWithInvalidOffsetAndOrLength() throws Exception {
		byte[] buffer = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		tryCopyBufferWithInvalidOffsetAndOrLength(buffer, -1, buffer.length);
		tryCopyBufferWithInvalidOffsetAndOrLength(buffer, buffer.length, 1);
		tryCopyBufferWithInvalidOffsetAndOrLength(buffer, 1, buffer.length);
	}
	
	public void testShouldDetermineUnsynchronisationSizesCorrectly() throws Exception {
		assertEquals(0, BufferTools.sizeUnsynchronisationWouldAdd(new byte[] {}));
		assertEquals(0, BufferTools.sizeUnsynchronisationWouldAdd(new byte[] {BYTE_FF, 1, BYTE_FB}));
		assertEquals(1, BufferTools.sizeUnsynchronisationWouldAdd(new byte[] {BYTE_FF, BYTE_FB}));
		assertEquals(1, BufferTools.sizeUnsynchronisationWouldAdd(new byte[] {0, BYTE_FF, BYTE_FB, 0}));
		assertEquals(1, BufferTools.sizeUnsynchronisationWouldAdd(new byte[] {0, BYTE_FF}));
		assertEquals(2, BufferTools.sizeUnsynchronisationWouldAdd(new byte[] {BYTE_FF, BYTE_FB, 0, BYTE_FF, BYTE_FB}));
		assertEquals(3, BufferTools.sizeUnsynchronisationWouldAdd(new byte[] {BYTE_FF, BYTE_FF, BYTE_FF}));
	}
	
	public void testShouldDetermineSynchronisationSizesCorrectly() throws Exception {
		assertEquals(0, BufferTools.sizeSynchronisationWouldSubtract(new byte[] {}));
		assertEquals(0, BufferTools.sizeSynchronisationWouldSubtract(new byte[] {BYTE_FF, 1, BYTE_FB}));
		assertEquals(1, BufferTools.sizeSynchronisationWouldSubtract(new byte[] {BYTE_FF, 0, BYTE_FB}));
		assertEquals(1, BufferTools.sizeSynchronisationWouldSubtract(new byte[] {0, BYTE_FF, 0, BYTE_FB, 0}));
		assertEquals(1, BufferTools.sizeSynchronisationWouldSubtract(new byte[] {0, BYTE_FF, 0}));
		assertEquals(2, BufferTools.sizeSynchronisationWouldSubtract(new byte[] {BYTE_FF, 0, BYTE_FB, 0, BYTE_FF, 0, BYTE_FB}));
		assertEquals(3, BufferTools.sizeSynchronisationWouldSubtract(new byte[] {BYTE_FF, 0, BYTE_FF, 0, BYTE_FF, 0}));
	}
	
	public void testShouldUnsynchroniseThenSynchroniseFFExBytesCorrectly() throws Exception {
		byte[] buffer = {BYTE_FF, BYTE_FB, 2, 3, 4, BYTE_FF, BYTE_E0, 7, 8, 9, 10, 11, 12, 13, BYTE_FF, BYTE_F0};
		byte[] expectedBuffer = {BYTE_FF, 0, BYTE_FB, 2, 3, 4, BYTE_FF, 0, BYTE_E0, 7, 8, 9, 10, 11, 12, 13, BYTE_FF, 0, BYTE_F0};
		byte[] unsynchronised = BufferTools.unsynchroniseBuffer(buffer);
		byte[] synchronised = BufferTools.synchroniseBuffer(unsynchronised);
		assertTrue(Arrays.equals(expectedBuffer, unsynchronised));
		assertTrue(Arrays.equals(buffer, synchronised));
	}
	
	public void testShouldUnsynchroniseThenSynchroniseFF00BytesCorrectly() throws Exception {
		byte[] buffer = {BYTE_FF, 0, 2, 3, 4, BYTE_FF, 0, 7, 8, 9, 10, 11, 12, 13, BYTE_FF, 0};
		byte[] expectedBuffer = {BYTE_FF, 0, 0, 2, 3, 4, BYTE_FF, 0, 0, 7, 8, 9, 10, 11, 12, 13, BYTE_FF, 0, 0};
		byte[] unsynchronised = BufferTools.unsynchroniseBuffer(buffer);
		byte[] synchronised = BufferTools.synchroniseBuffer(unsynchronised);
		assertTrue(Arrays.equals(expectedBuffer, unsynchronised));
		assertTrue(Arrays.equals(buffer, synchronised));
	}
	
	public void testShouldUnsynchroniseThenSynchroniseBufferFullOfFFsCorrectly() throws Exception {
		byte[] buffer = {BYTE_FF, BYTE_FF, BYTE_FF, BYTE_FF};
		byte[] expectedBuffer = {BYTE_FF, 0, BYTE_FF, 0, BYTE_FF, 0, BYTE_FF, 0};
		byte[] unsynchronised = BufferTools.unsynchroniseBuffer(buffer);
		byte[] synchronised = BufferTools.synchroniseBuffer(unsynchronised);
		assertTrue(Arrays.equals(expectedBuffer, unsynchronised));
		assertTrue(Arrays.equals(buffer, synchronised));
	}
	
	public void testShouldUnsynchroniseThenSynchroniseBufferMinimalBufferCorrectly() throws Exception {
		byte[] buffer = {BYTE_FF};
		byte[] expectedBuffer = {BYTE_FF, 0};
		byte[] unsynchronised = BufferTools.unsynchroniseBuffer(buffer);
		byte[] synchronised = BufferTools.synchroniseBuffer(unsynchronised);
		assertTrue(Arrays.equals(expectedBuffer, unsynchronised));
		assertTrue(Arrays.equals(buffer, synchronised));
	}
	
	public void testShouldReturnOriginalBufferIfNoUnynchronisationOrSynchronisationIsRequired() throws Exception {
		byte[] buffer = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
		byte[] unsynchronised = BufferTools.unsynchroniseBuffer(buffer);
		byte[] synchronised = BufferTools.synchroniseBuffer(buffer);
		assertEquals(buffer, unsynchronised);
		assertEquals(buffer, synchronised);
	}
	
	public void testShouldReplaceTokensWithSpecifiedStrings() throws Exception {
		String source = "%1-%2 something %1-%3";
		assertEquals("ONE-%2 something ONE-%3", BufferTools.substitute(source, "%1", "ONE"));
		assertEquals("%1-TWO something %1-%3", BufferTools.substitute(source, "%2", "TWO"));
		assertEquals("%1-%2 something %1-THREE", BufferTools.substitute(source, "%3", "THREE"));
	}
	
	public void testShouldReturnOriginalStringIfTokenToSubstituteDoesNotExistInString() throws Exception {
		String source = "%1-%2 something %1-%3";
		assertEquals("%1-%2 something %1-%3", BufferTools.substitute(source, "%X", "XXXXX"));
	}
	
	public void testShouldReturnOriginalStringForSubstitutionWithEmptyString() throws Exception {
		String source = "%1-%2 something %1-%3";
		assertEquals("%1-%2 something %1-%3", BufferTools.substitute(source, "", "WHATEVER"));
	}
	
	public void testShouldSubstituteEmptyStringWhenDestinationStringIsNull() throws Exception {
		String source = "%1-%2 something %1-%3";
		assertEquals("-%2 something -%3", BufferTools.substitute(source, "%1", null));
	}
	
	public void testShouldConvertNonAsciiCharactersToQuestionMarksInString() throws Exception {
		assertEquals("?12?34?567???89?", BufferTools.asciiOnly("ü12¬34ü567¬¬¬89ü"));
	}

	private void tryCopyBufferWithInvalidOffsetAndOrLength(byte[] buffer, int offset, int length) {
		try {
			BufferTools.copyBuffer(buffer, offset, length);
			fail("ArrayIndexOutOfBoundsException expected but not thrown");
		} catch (ArrayIndexOutOfBoundsException e) {
			// expected
		}
	}
	
	public void testShouldConvertBufferContainingHighAscii() throws Exception {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G};
		assertEquals("T" + (char)(223) + "G", BufferTools.byteBufferToString(buffer, 0, 3));
	}
	
	// finding terminators
	
	public void testShouldFindSingleTerminator() throws Exception {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, BYTE_G, BYTE_A};
		assertEquals(4, BufferTools.indexOfTerminator(buffer, 0, 1));
	}
	
	public void testShouldFindFirstSingleTerminator() throws Exception {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, BYTE_G, BYTE_A, 0, BYTE_G, BYTE_A};
		assertEquals(4, BufferTools.indexOfTerminator(buffer, 0, 1));
	}
	
	public void testShouldFindFirstSingleTerminatorAfterFromIndex() throws Exception {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, BYTE_G, BYTE_A, 0, BYTE_G, BYTE_A};
		assertEquals(7, BufferTools.indexOfTerminator(buffer, 5, 1));
	}
	
	public void testShouldFindSingleTerminatorWhenFirstElement() throws Exception {
		byte[] buffer = {0, BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T};
		assertEquals(0, BufferTools.indexOfTerminator(buffer, 0, 1));
	}
	
	public void testShouldFindSingleTerminatorWhenLastElement() throws Exception {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0};
		assertEquals(4, BufferTools.indexOfTerminator(buffer, 0, 1));
	}
	
	public void testShouldReturnMinusOneWhenNoSingleTerminator() throws Exception {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T};
		assertEquals(-1, BufferTools.indexOfTerminator(buffer, 0, 1));
	}
	
	public void testShouldFindDoubleTerminator() throws Exception {
		byte[] buffer = {BYTE_T, 0, BYTE_G, BYTE_T, 0, 0, BYTE_G, BYTE_A};
		assertEquals(4, BufferTools.indexOfTerminator(buffer, 0, 2));
	}
	
	public void testShouldFindNotFindDoubleTerminatorIfNotOnEvenByte() throws Exception {
		byte[] buffer = {BYTE_T, 0, BYTE_G, BYTE_T, BYTE_T, 0, 0, BYTE_G, BYTE_A};
		assertEquals(-1, BufferTools.indexOfTerminator(buffer, 0, 2));
	}
	
	public void testShouldFindFirstDoubleTerminator() throws Exception {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, 0, BYTE_G, BYTE_A, 0, 0, BYTE_G, BYTE_A};
		assertEquals(4, BufferTools.indexOfTerminator(buffer, 0, 2));
	}
	
	public void testShouldFindFirstDoubleTerminatorOnAnEvenByte() throws Exception {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, 0, 0, BYTE_T, BYTE_G, BYTE_A, 0, 0, BYTE_G, BYTE_A};
		assertEquals(8, BufferTools.indexOfTerminator(buffer, 0, 2));
	}
	
	public void testShouldFindFirstDoubleTerminatorAfterFromIndex() throws Exception {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, 0, BYTE_G, BYTE_A, 0, 0, BYTE_G, BYTE_A};
		assertEquals(8, BufferTools.indexOfTerminator(buffer, 6, 2));
	}
	
	public void testShouldFindDoubleTerminatorWhenFirstElement() throws Exception {
		byte[] buffer = {0, 0, BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T};
		assertEquals(0, BufferTools.indexOfTerminator(buffer, 0, 2));
	}
	
	public void testShouldFindDoubleTerminatorWhenLastElement() throws Exception {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T, 0, 0};
		assertEquals(4, BufferTools.indexOfTerminator(buffer, 0, 2));
	}
	
	public void testShouldReturnMinusOneWhenNoDoubleTerminator() throws Exception {
		byte[] buffer = {BYTE_T, BYTE_ESZETT, BYTE_G, BYTE_T};
		assertEquals(-1, BufferTools.indexOfTerminator(buffer, 0, 2));
	}
}
