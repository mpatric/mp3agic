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
	private static final byte BYTE_SPECIAL_CHARACTER = -0x18;

	public void testShouldExtractStringFromStartOfBuffer() {
		byte[] buffer = {BYTE_T, BYTE_A, BYTE_G, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH};
		assertEquals("TAG", BufferTools.byteBufferToString(buffer, 0, 3));
	}
	
	public void testShouldExtractStringFromEndOfBuffer() {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		assertEquals("TAG", BufferTools.byteBufferToString(buffer, 5, 3));
	}
	
	public void testShouldExtractStringFromMiddleOfBuffer() {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		assertEquals("TAG", BufferTools.byteBufferToString(buffer, 5, 3));
	}
	
	public void testShouldThrowExceptionForOffsetBeforeStartOfArray() {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		try {
			BufferTools.byteBufferToString(buffer, -1, 4);
			fail("ArrayIndexOutOfBoundsException expected but not thrown");
		} catch (ArrayIndexOutOfBoundsException e) {
			// expected
		}
	}
	
	public void testShouldThrowExceptionForOffsetAfterEndOfArray() {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		try {
			BufferTools.byteBufferToString(buffer, buffer.length, 1);
			fail("ArrayIndexOutOfBoundsException expected but not thrown");
		} catch (ArrayIndexOutOfBoundsException e) {
			// expected
		}
	}
	
	public void testShouldThrowExceptionForLengthExtendingBeyondEndOfArray() {
		byte[] buffer = {BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_DASH, BYTE_T, BYTE_A, BYTE_G};
		try {
			BufferTools.byteBufferToString(buffer, buffer.length - 2, 3);
			fail("ArrayIndexOutOfBoundsException expected but not thrown");
		} catch (ArrayIndexOutOfBoundsException e) {
			// expected
		}
	}
	
	public void testShouldConvertStringToBufferAndBack() {
		String original = "1234567890QWERTYUIOP";
		byte buffer[] = BufferTools.stringToByteBuffer(original, 0, original.length());
		String converted = BufferTools.byteBufferToString(buffer, 0, buffer.length);
		assertEquals(original, converted);
	}
	
	public void testShouldCopyStringToStartOfByteBuffer() {
		byte buffer[] = new byte[10];
		Arrays.fill(buffer, (byte)0);
		String s = "TAG-";
		BufferTools.stringIntoByteBuffer(s, 0, s.length(), buffer, 0);
		byte[] expectedBuffer = {BYTE_T, BYTE_A, BYTE_G, BYTE_DASH, 0, 0, 0, 0, 0, 0};
		assertTrue(Arrays.equals(expectedBuffer, buffer));
	}
	
	public void testShouldCopyStringToEndOfByteBuffer() {
		byte buffer[] = new byte[10];
		Arrays.fill(buffer, (byte)0);
		String s = "TAG-";
		BufferTools.stringIntoByteBuffer(s, 0, s.length(), buffer, 6);
		byte[] expectedBuffer = {0, 0, 0, 0, 0, 0, BYTE_T, BYTE_A, BYTE_G, BYTE_DASH};
		assertTrue(Arrays.equals(expectedBuffer, buffer));
	}
	
	public void testShouldCopySubstringToStartOfByteBuffer() {
		byte buffer[] = new byte[10];
		Arrays.fill(buffer, (byte)0);
		String s = "TAG-";
		BufferTools.stringIntoByteBuffer(s, 1, 2, buffer, 0);
		byte[] expectedBuffer = {BYTE_A, BYTE_G, 0, 0, 0, 0, 0, 0, 0, 0};
		assertTrue(Arrays.equals(expectedBuffer, buffer));
	}
	
	public void testShouldCopySubstringToMiddleOfByteBuffer() {
		byte buffer[] = new byte[10];
		Arrays.fill(buffer, (byte)0);
		String s = "TAG-";
		BufferTools.stringIntoByteBuffer(s, 1, 2, buffer, 4);
		byte[] expectedBuffer = {0, 0, 0, 0, BYTE_A, BYTE_G, 0, 0, 0, 0};
		assertTrue(Arrays.equals(expectedBuffer, buffer));
	}
	
	public void testShouldRightTrimStringsCorrectly() {
		assertEquals("", BufferTools.trimStringRight(""));
		assertEquals("", BufferTools.trimStringRight(" "));
		assertEquals("TEST", BufferTools.trimStringRight("TEST"));
		assertEquals("TEST", BufferTools.trimStringRight("TEST   "));
		assertEquals("   TEST", BufferTools.trimStringRight("   TEST"));
		assertEquals("   TEST", BufferTools.trimStringRight("   TEST   "));
		assertEquals("TEST", BufferTools.trimStringRight("TEST\t\r\n"));
		assertEquals("TEST", BufferTools.trimStringRight("TEST" + BufferTools.byteBufferToString(new byte[] {0, 0}, 0, 2)));
	}
	
	public void testShouldRightPadStringsCorrectly() throws Exception {
		assertEquals("1234", BufferTools.padStringRight("1234", 3, ' '));
		assertEquals("123", BufferTools.padStringRight("123", 3, ' '));
		assertEquals("12 ", BufferTools.padStringRight("12", 3, ' '));
		assertEquals("1  ", BufferTools.padStringRight("1", 3, ' '));
		assertEquals("   ", BufferTools.padStringRight("", 3, ' '));
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
		assertEquals("?12?34?567???89?", BufferTools.asciiOnly("¸12¨34¸567¨¨¨89¸"));
	}

	private void tryCopyBufferWithInvalidOffsetAndOrLength(byte[] buffer, int offset, int length) {
		try {
			BufferTools.copyBuffer(buffer, offset, length);
			fail("ArrayIndexOutOfBoundsException expected but not thrown");
		} catch (ArrayIndexOutOfBoundsException e) {
			// expected
		}
	}
	
	public void testShouldConvertBufferContainingHighAscii() {
		byte[] buffer = {BYTE_T, BYTE_SPECIAL_CHARACTER, BYTE_G};
		assertEquals("TèG", BufferTools.byteBufferToString(buffer, 0, 3));
	}
}
