package com.mpatric.mp3agic;

import com.mpatric.mp3agic.EncodedText;
import com.mpatric.mp3agic.ID3v2CommentFrameData;

import junit.framework.TestCase;

public class ID3v2CommentFrameDataTest extends TestCase {
	
	private static final String TEST_LANGUAGE = "eng";
	private static final String TEST_DESCRIPTION = "DESCRIPTION";
	private static final String TEST_VALUE = "ABCDEFGHIJKLMNOPQ";

	public void testShouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2CommentFrameData frameData1 = createTestCommentFrameData();
		ID3v2CommentFrameData frameData2 = createTestCommentFrameData();
		assertEquals(frameData1, frameData2);		
	}

	public void testShouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2CommentFrameData frameData = createTestCommentFrameData();
		byte[] bytes = frameData.toBytes();
		ID3v2CommentFrameData frameDataCopy = new ID3v2CommentFrameData(false, bytes);
		assertEquals(1 + TEST_LANGUAGE.length() + TEST_DESCRIPTION.length() + 1 + TEST_VALUE.length(), bytes.length);
		assertEquals(frameData, frameDataCopy);
	}

	private ID3v2CommentFrameData createTestCommentFrameData() {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte)0, TEST_DESCRIPTION), new EncodedText((byte)0, TEST_VALUE));
		return frameData;
	}
}
