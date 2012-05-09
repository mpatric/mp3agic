package com.mpatric.mp3agic;

import java.util.Arrays;

import com.mpatric.mp3agic.EncodedText;
import com.mpatric.mp3agic.ID3v2TextFrameData;

import junit.framework.TestCase;

public class ID3v2TextFrameDataTest extends TestCase {

	private static final String TEST_TEXT = "ABCDEFGHIJKLMNOPQ";
	private static final String TEST_TEXT_UNICODE = "\u03B3\u03B5\u03B9\u03AC";

	public void testShouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2TextFrameData frameData1 = new ID3v2TextFrameData(false, new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, TEST_TEXT));
		ID3v2TextFrameData frameData2 = new ID3v2TextFrameData(false, new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, TEST_TEXT));
		assertEquals(frameData1, frameData2);		
	}
	
	public void testShouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2TextFrameData frameData = new ID3v2TextFrameData(false, new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, TEST_TEXT));
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {0, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'};
		assertTrue(Arrays.equals(expectedBytes, bytes));
		ID3v2TextFrameData frameDataCopy = new ID3v2TextFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}
	
	public void testShouldConvertFrameDataWithUnicodeToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2TextFrameData frameData = new ID3v2TextFrameData(false, new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_TEXT_UNICODE));
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {1, (byte)0xff, (byte)0xfe, (byte)0xb3, 0x03, (byte)0xb5, 0x03, (byte)0xb9, 0x03, (byte)0xac, 0x03};
		assertTrue(Arrays.equals(expectedBytes, bytes));
		ID3v2TextFrameData frameDataCopy = new ID3v2TextFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}
}
