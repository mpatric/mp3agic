package com.mpatric.mp3agic;

import java.util.Arrays;

import junit.framework.TestCase;


public class ID3v2UrlFrameDataTest extends TestCase {
	
	private static final String TEST_DESCRIPTION = "DESCRIPTION";
	private static final String TEST_DESCRIPTION_UNICODE = "\u03B3\u03B5\u03B9\u03AC";
	private static final String TEST_URL = "http://ABCDEFGHIJKLMNOPQ";

	public void testShouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2UrlFrameData frameData1 = new ID3v2UrlFrameData(false, Encoding.ENCODING_ISO_8859_1, TEST_DESCRIPTION, TEST_URL);
		ID3v2UrlFrameData frameData2 = new ID3v2UrlFrameData(false, Encoding.ENCODING_ISO_8859_1, TEST_DESCRIPTION, TEST_URL);
		assertEquals(frameData1, frameData2);		
	}
	
	public void testShouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2UrlFrameData frameData = new ID3v2UrlFrameData(false, Encoding.ENCODING_ISO_8859_1, TEST_DESCRIPTION, TEST_URL);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {0, 'D', 'E', 'S', 'C', 'R', 'I', 'P', 'T', 'I', 'O', 'N', 0, 'h', 't', 't', 'p', ':', '/', '/', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'};
		assertTrue(Arrays.equals(expectedBytes, bytes));
		ID3v2UrlFrameData frameDataCopy = new ID3v2UrlFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}
	
	public void testShouldConvertFrameDataWithNoDescriptionToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2UrlFrameData frameData = new ID3v2UrlFrameData(false, Encoding.ENCODING_ISO_8859_1, "", TEST_URL);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {0, 0, 'h', 't', 't', 'p', ':', '/', '/', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'};
		assertTrue(Arrays.equals(expectedBytes, bytes));
		ID3v2UrlFrameData frameDataCopy = new ID3v2UrlFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}
	
	public void testShouldConvertFrameDataWithUnicodeDescriptionToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2UrlFrameData frameData = new ID3v2UrlFrameData(false, Encoding.ENCODING_UTF_16BE, TEST_DESCRIPTION_UNICODE, TEST_URL);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {2, 0x03, (byte)0xb3, 0x03, (byte)0xb5, 0x03, (byte)0xb9, 0x03, (byte)0xac, 0, 0, 'h', 't', 't', 'p', ':', '/', '/', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'};
		assertTrue(Arrays.equals(expectedBytes, bytes));
		ID3v2UrlFrameData frameDataCopy = new ID3v2UrlFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}
}
