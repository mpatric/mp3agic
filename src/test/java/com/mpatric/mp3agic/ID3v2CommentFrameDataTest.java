package com.mpatric.mp3agic;

import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Assert;


public class ID3v2CommentFrameDataTest extends TestCase {
	
	private static final String TEST_LANGUAGE = "eng";
	private static final String TEST_DESCRIPTION = "DESCRIPTION";
	private static final String TEST_VALUE = "ABCDEFGHIJKLMNOPQ";
	private static final String TEST_DESCRIPTION_UNICODE = "\u03B3\u03B5\u03B9\u03AC";
	private static final String TEST_VALUE_UNICODE = "\u03C3\u03BF\u03C5";

	public void testShouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false,
				Encoding.getDefault(),
				TEST_LANGUAGE, 
				TEST_DESCRIPTION, 
				TEST_VALUE);
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false,  
				Encoding.getDefault(),
				TEST_LANGUAGE,
				TEST_DESCRIPTION,
				TEST_VALUE);
		assertEquals(frameData1, frameData2);		
	}

	public void testShouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, Encoding.ENCODING_UTF_8, TEST_LANGUAGE, TEST_DESCRIPTION, TEST_VALUE);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {3, 'e', 'n', 'g', 'D', 'E', 'S', 'C', 'R', 'I', 'P', 'T', 'I', 'O', 'N', 0, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'};
		assertTrue(Arrays.equals(expectedBytes, bytes));
		ID3v2CommentFrameData frameDataCopy = new ID3v2CommentFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}
	
	public void testShouldConvertFrameDataWithBlankDescriptionAndLanguageToBytesAndBackToEquivalentObject() throws Exception {
		byte[] bytes = {0, 0, 0, 0, 0, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'};
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, bytes);
		assertEquals("\00\00\00", frameData.getLanguage());
		assertEquals("", frameData.getDescription());
		assertEquals(TEST_VALUE, frameData.getComment());
		Assert.assertArrayEquals(bytes, frameData.toBytes());
	}
	
	public void testShouldConvertFrameDataWithUnicodeToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, Encoding.ENCODING_UTF_16BE, TEST_LANGUAGE, TEST_DESCRIPTION_UNICODE, TEST_VALUE_UNICODE);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {2, 'e', 'n', 'g', 0x03, (byte)0xb3, 0x03, (byte)0xb5, 0x03, (byte)0xb9, 0x03, (byte)0xac, 0, 0, 0x03, (byte)0xc3, 0x03, (byte)0xbf, 0x03, (byte)0xc5};
		Assert.assertArrayEquals(expectedBytes, bytes);
		ID3v2CommentFrameData frameDataCopy = new ID3v2CommentFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}
}
