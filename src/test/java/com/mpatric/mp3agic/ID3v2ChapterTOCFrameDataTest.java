package com.mpatric.mp3agic;

import java.util.Arrays;

import junit.framework.TestCase;

public class ID3v2ChapterTOCFrameDataTest extends TestCase {
	
	public void testShouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		String children[] = {"ch1", "ch2"};
		ID3v2ChapterTOCFrameData frameData1 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", children);
		ID3v2TextFrameData subFrameData1 = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData1.addSubframe("TIT2", subFrameData1);
		ID3v2ChapterTOCFrameData frameData2 = new ID3v2ChapterTOCFrameData(false, true, false, "toc1", children);;
		ID3v2TextFrameData subFrameData2 = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData2.addSubframe("TIT2", subFrameData2);
		assertEquals(frameData1, frameData2);		
	}
	
	public void testShouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		String children[] = {"ch1", "ch2"};
		ID3v2ChapterTOCFrameData frameData = new ID3v2ChapterTOCFrameData(false, true, true, "toc1", children);
		ID3v2TextFrameData subFrameData = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData.addSubframe("TIT2", subFrameData);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {
				't', 'o', 'c', '1', 0,
				3, 2,
				'c', 'h', '1', 0,
				'c', 'h', '2', 0,
				'T', 'I', 'T', '2',
				0, 0, 0, (byte)0xc,
				0, 0,
				0,
				'H', 'e', 'l', 'l', 'o', ' ', 't', 'h', 'e', 'r', 'e'
		};
		assertTrue(Arrays.equals(expectedBytes, bytes));
		ID3v2ChapterTOCFrameData frameDataCopy = new ID3v2ChapterTOCFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}
}
