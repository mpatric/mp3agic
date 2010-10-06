package com.mpatric.mp3agic;

import com.mpatric.mp3agic.EncodedText;
import com.mpatric.mp3agic.ID3v2TextFrameData;

import junit.framework.TestCase;

public class ID3v2TextFrameDataTest extends TestCase {

	private static final String TEST_TEXT = "ABCDEFGHIJKLMNOPQ";

	public void testShouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2TextFrameData frameData1 = createTestTextFrameData();
		ID3v2TextFrameData frameData2 = createTestTextFrameData();
		assertEquals(frameData1, frameData2);		
	}
	
	public void testShouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2TextFrameData frameData = createTestTextFrameData();
		byte[] bytes = frameData.toBytes();
		ID3v2TextFrameData frameDataCopy = new ID3v2TextFrameData(false, bytes);
		assertEquals(1 + TEST_TEXT.length(), bytes.length);
		assertEquals(frameData, frameDataCopy);
	}

	private ID3v2TextFrameData createTestTextFrameData() {
		ID3v2TextFrameData frameData = new ID3v2TextFrameData(false, new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, TEST_TEXT));
		return frameData;
	}
}
