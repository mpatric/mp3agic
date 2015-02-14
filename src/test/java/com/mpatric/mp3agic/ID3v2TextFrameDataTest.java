package com.mpatric.mp3agic;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ID3v2TextFrameDataTest {

	private static final String TEST_TEXT = "ABCDEFGHIJKLMNOPQ";
	private static final String TEST_TEXT_UNICODE = "\u03B3\u03B5\u03B9\u03AC";

    @Test
	public void shouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2TextFrameData frameData1 = new ID3v2TextFrameData(false, new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, TEST_TEXT));
		ID3v2TextFrameData frameData2 = new ID3v2TextFrameData(false, new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, TEST_TEXT));
		assertEquals(frameData1, frameData2);		
	}

    @Test
	public void shouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2TextFrameData frameData = new ID3v2TextFrameData(false, new EncodedText(EncodedText.TEXT_ENCODING_ISO_8859_1, TEST_TEXT));
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {0, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'};
		assertArrayEquals(expectedBytes, bytes);
		ID3v2TextFrameData frameDataCopy = new ID3v2TextFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}

    @Test
	public void shouldConvertFrameDataWithUnicodeToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2TextFrameData frameData = new ID3v2TextFrameData(false, new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_TEXT_UNICODE));
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {1, (byte)0xff, (byte)0xfe, (byte)0xb3, 0x03, (byte)0xb5, 0x03, (byte)0xb9, 0x03, (byte)0xac, 0x03};
        assertArrayEquals(expectedBytes, bytes);
		ID3v2TextFrameData frameDataCopy = new ID3v2TextFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}
}
