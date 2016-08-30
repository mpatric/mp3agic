package com.mpatric.mp3agic;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ID3v2UserTextFrameDataTest {

    private static final String TEST_DESCRIPTION = "DESCRIPTION";

    private static final String TEST_DESCRIPTION_UNICODE = "\u03B3\u03B5\u03B9\u03AC";
    
    private static final String TEST_TEXT_UNICODE = "\u03C3\u03BF\u03C5";
    
    private static final String TEST_TEXT = "ABCDEFGHIJKLMNOPQ";
    
    private static final String TEST_DECODED_TEXT = " DESCRIPTION ABCDEFGHIJKLMNOPQ";

    @Test
    public void shouldConsiderTwoEquivalentObjectsEqual() throws Exception {
        ID3v2UserTextFrameData frameData1 = new ID3v2UserTextFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_TEXT));
        ID3v2UserTextFrameData frameData2 = new ID3v2UserTextFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_TEXT));
        assertEquals(frameData1, frameData2);
    }

    @Test
    public void shouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
        ID3v2UserTextFrameData frameData = new ID3v2UserTextFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_TEXT));
        byte[] bytes = frameData.toBytes();
        byte[] dest = new byte[bytes.length];
        System.arraycopy(bytes, 0, dest, 0, dest.length);
        TestHelper.replaceNullsWithSpaces(dest);
        String testStr = new String(dest);
        System.out.printf("shouldConvertFrameDataToBytesAndBackToEquivalentObject%n%s%n%s%n", testStr, TestHelper.bytesToHexString(bytes));
        assertEquals(TEST_DECODED_TEXT, testStr);
        byte[] expectedBytes = { 0, 'D', 'E', 'S', 'C', 'R', 'I', 'P', 'T', 'I', 'O', 'N', 0, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q' };
        assertArrayEquals(expectedBytes, bytes);
        ID3v2UserTextFrameData frameDataCopy = new ID3v2UserTextFrameData(false, bytes);
        assertEquals(frameData, frameDataCopy);
    }

    @Test
    public void shouldConvertFrameDataWithNoDescriptionToBytesAndBackToEquivalentObject() throws Exception {
        ID3v2UserTextFrameData frameData = new ID3v2UserTextFrameData(false, new EncodedText(""), new EncodedText(TEST_TEXT));
        byte[] bytes = frameData.toBytes();
        System.out.printf("shouldConvertFrameDataWithNoDescriptionToBytesAndBackToEquivalentObject%n%s%n%s%n", new String(bytes), TestHelper.bytesToHexString(bytes));
        byte[] expectedBytes = { 0, 0, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q' };
        assertArrayEquals(expectedBytes, bytes);
        ID3v2UserTextFrameData frameDataCopy = new ID3v2UserTextFrameData(false, bytes);
        assertEquals(frameData, frameDataCopy);
    }

    @Test
    public void shouldConvertFrameDataWithUnicodeDescriptionToBytesAndBackToEquivalentObject() throws Exception {
        ID3v2UserTextFrameData frameData = new ID3v2UserTextFrameData(false, new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_DESCRIPTION_UNICODE), new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_TEXT_UNICODE));
        byte[] bytes = frameData.toBytes();
        System.out.printf("shouldConvertFrameDataWithUnicodeDescriptionToBytesAndBackToEquivalentObject%n%s%n%s%n", new String(bytes), TestHelper.bytesToHexString(bytes));
        // ff fe == Little endian / ff fd == Big endian
        byte[] expectedBytes = { 1, (byte) 0xff, (byte) 0xfe, (byte) 0xb3, 0x03, (byte) 0xb5, 0x03, (byte) 0xb9, 0x03, (byte) 0xac, 0x03, 0, 0, (byte) 0xff, (byte) 0xfe, (byte) 0xc3, 0x03, (byte) 0xbf, 0x03, (byte) 0xc5, 0x03 };
        assertArrayEquals(expectedBytes, bytes);
        ID3v2UserTextFrameData frameDataCopy = new ID3v2UserTextFrameData(false, bytes);
        assertEquals(frameData, frameDataCopy);
    }
    
}
