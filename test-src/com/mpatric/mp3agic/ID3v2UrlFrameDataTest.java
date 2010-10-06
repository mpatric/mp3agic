package com.mpatric.mp3agic;

import com.mpatric.mp3agic.EncodedText;
import com.mpatric.mp3agic.ID3v2UrlFrameData;

import junit.framework.TestCase;

public class ID3v2UrlFrameDataTest extends TestCase {
	
	private static final String TEST_DESCRIPTION = "DESCRIPTION";
	private static final String TEST_URL = "http://ABCDEFGHIJKLMNOPQ";

	public void testShouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2UrlFrameData frameData1 = createTestUrlFrameData();
		ID3v2UrlFrameData frameData2 = createTestUrlFrameData();
		assertEquals(frameData1, frameData2);		
	}
	
	public void testShouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2UrlFrameData frameData = createTestUrlFrameData();
		byte[] bytes = frameData.toBytes();
		ID3v2UrlFrameData frameDataCopy = new ID3v2UrlFrameData(false, bytes);
		assertEquals(1 + TEST_DESCRIPTION.length() + 1 + TEST_URL.length(), bytes.length);
		assertEquals(frameData, frameDataCopy);
	}

	private ID3v2UrlFrameData createTestUrlFrameData() {
		ID3v2UrlFrameData frameData = new ID3v2UrlFrameData(false, new EncodedText((byte)0, TEST_DESCRIPTION), TEST_URL);
		return frameData;
	}
}
