package com.mpatric.mp3agic;

import static org.junit.Assert.*;

import org.junit.Test;

public class ID3v2ChapterFrameDataTest {

    @Test
	public void shouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2ChapterFrameData frameData1 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2TextFrameData subFrameData1 = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData1.addSubframe("TIT2", subFrameData1);
		ID3v2ChapterFrameData frameData2 = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2TextFrameData subFrameData2 = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData2.addSubframe("TIT2", subFrameData2);
		assertEquals(frameData1, frameData2);		
	}

    @Test
	public void shouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2ChapterFrameData frameData = new ID3v2ChapterFrameData(false, "ch1", 1, 380, 3, 400);
		ID3v2TextFrameData subFrameData = new ID3v2TextFrameData(false, new EncodedText("Hello there"));
		frameData.addSubframe("TIT2", subFrameData);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {
				'c', 'h', '1', 0, 
				0, 0, 0, 1,
				0, 0, 1, (byte)0x7c,
				0, 0, 0, 3,
				0, 0, 1, (byte)0x90,
				'T', 'I', 'T', '2',
				0, 0, 0, (byte)0xc,
				0, 0,
				0,
				'H', 'e', 'l', 'l', 'o', ' ', 't', 'h', 'e', 'r', 'e'
		};
		assertArrayEquals(expectedBytes, bytes);
		ID3v2ChapterFrameData frameDataCopy = new ID3v2ChapterFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}
}
