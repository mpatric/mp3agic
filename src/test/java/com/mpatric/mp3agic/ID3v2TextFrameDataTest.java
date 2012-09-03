package com.mpatric.mp3agic;

import java.util.Arrays;

import junit.framework.TestCase;


public class ID3v2TextFrameDataTest extends TestCase {

	private static final String TEST_TEXT = "ABCDEFGHIJKLMNOPQ";
	private static final String TEST_TEXT_UNICODE = "\u03B3\u03B5\u03B9\u03AC";

	public void testShouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2TextFrameData frameData1 = new ID3v2TextFrameData(false, TEST_TEXT);
		ID3v2TextFrameData frameData2 = new ID3v2TextFrameData(false, TEST_TEXT);
		assertEquals(frameData1, frameData2);		
	}
	
	public void testShouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2TextFrameData frameData = new ID3v2TextFrameData(false, TEST_TEXT);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {3, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'};
		assertTrue(Arrays.equals(expectedBytes, bytes));
		ID3v2TextFrameData frameDataCopy = new ID3v2TextFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}
	
	public void testShouldConvertFrameDataWithUnicodeToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2TextFrameData frameData = new ID3v2TextFrameData(false, Encoding.ENCODING_UTF_16BE, TEST_TEXT_UNICODE);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {2, 0x03, (byte)0xb3, 0x03, (byte)0xb5, 0x03, (byte)0xb9, 0x03, (byte)0xac};
		assertTrue(Arrays.equals(expectedBytes, bytes));
		ID3v2TextFrameData frameDataCopy = new ID3v2TextFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}
}
