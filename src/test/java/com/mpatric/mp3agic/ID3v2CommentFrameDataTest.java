package com.mpatric.mp3agic;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ID3v2CommentFrameDataTest {

	private static final String TEST_LANGUAGE = "eng";
	private static final String TEST_DESCRIPTION = "DESCRIPTION";
	private static final String TEST_VALUE = "ABCDEFGHIJKLMNOPQ";
	private static final String TEST_DESCRIPTION_UNICODE = "\u03B3\u03B5\u03B9\u03AC";
	private static final String TEST_VALUE_UNICODE = "\u03C3\u03BF\u03C5";

	@Test
	public void shouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertEquals(frameData1, frameData2);
	}

	@Test
	public void shouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {0, 'e', 'n', 'g', 'D', 'E', 'S', 'C', 'R', 'I', 'P', 'T', 'I', 'O', 'N', 0, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'};
		assertArrayEquals(expectedBytes, bytes);
		ID3v2CommentFrameData frameDataCopy = new ID3v2CommentFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}

	@Test
	public void shouldConvertFrameDataWithBlankDescriptionAndLanguageToBytesAndBackToEquivalentObject() throws Exception {
		byte[] bytes = {0, 0, 0, 0, 0, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'};
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, bytes);
		assertEquals("\00\00\00", frameData.getLanguage());
		assertEquals(new EncodedText(""), frameData.getDescription());
		assertEquals(new EncodedText(TEST_VALUE), frameData.getComment());
		assertArrayEquals(bytes, frameData.toBytes());
	}

	@Test
	public void shouldConvertFrameDataWithUnicodeToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_DESCRIPTION_UNICODE), new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_VALUE_UNICODE));
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {1, 'e', 'n', 'g', (byte) 0xff, (byte) 0xfe, (byte) 0xb3, 0x03, (byte) 0xb5, 0x03, (byte) 0xb9, 0x03, (byte) 0xac, 0x03, 0, 0, (byte) 0xff, (byte) 0xfe, (byte) 0xc3, 0x03, (byte) 0xbf, 0x03, (byte) 0xc5, 0x03};
		assertArrayEquals(expectedBytes, bytes);
		ID3v2CommentFrameData frameDataCopy = new ID3v2CommentFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}
}
