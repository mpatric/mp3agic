package com.mpatric.mp3agic;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class ID3v2CommentFrameDataTest {

	private static final String TEST_LANGUAGE = "eng";
	private static final String TEST_DESCRIPTION = "DESCRIPTION";
	private static final String TEST_VALUE = "ABCDEFGHIJKLMNOPQ";
	private static final String TEST_DESCRIPTION_UNICODE = "\u03B3\u03B5\u03B9\u03AC";
	private static final String TEST_VALUE_UNICODE = "\u03C3\u03BF\u03C5";

	@Test
	public void equalsItself() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertEquals(frameData, frameData);
	}

	@Test
	public void notEqualToNull() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertFalse(frameData.equals(null));
	}

	@Test
	public void notEqualToDifferentClass() {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertFalse(frameData.equals("8"));
	}

	@Test
	public void shouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfUnsynchronizationNotEqual() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(true, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfLanguageNotEqual() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, "jap", new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfLanguageIsNullOnOne() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, null, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void equalIfLanguageIsNullOnBoth() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, null, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, null, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfDescriptionNotEqual() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, "other description"), new EncodedText((byte) 0, TEST_VALUE));
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfDescriptionIsNullOnOne() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, null, new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void equalIfDescriptionIsNullOnBoth() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, null, new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, null, new EncodedText((byte) 0, TEST_VALUE));
		assertEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfCommentNotEqual() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, "other comment"));
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfCommentIsNullOnOne() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), new EncodedText((byte) 0, TEST_VALUE));
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void equalIfCommentIsNullOnBoth() {
		ID3v2CommentFrameData frameData1 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		ID3v2CommentFrameData frameData2 = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		assertEquals(frameData1, frameData2);
	}

	@Test
	public void hashCodeIsConsistent() {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		assertEquals(frameData.hashCode(), frameData.hashCode());
	}

	@Test
	public void equalObjectsHaveSameHashCode() {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		ID3v2CommentFrameData frameDataAgain = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		assertEquals(frameData.hashCode(), frameDataAgain.hashCode());
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

	@Test(expected = IllegalArgumentException.class)
	public void constructorThrowsErrorWhenEncodingsDoNotMatch() {
		new ID3v2CommentFrameData(false, TEST_LANGUAGE,
				new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, "description"),
				new EncodedText(EncodedText.TEXT_ENCODING_UTF_8, "comment"));
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

	@Test
	public void convertsEmptyFrameDataToBytesAndBack() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, null, null, null);
		byte[] bytes = frameData.toBytes();
		ID3v2CommentFrameData frameDataCopy = new ID3v2CommentFrameData(false, bytes);
		assertEquals("eng", frameDataCopy.getLanguage());
		assertEquals(new EncodedText(""), frameDataCopy.getDescription());
		assertEquals(new EncodedText(""), frameDataCopy.getComment());
	}

	@Test
	public void convertsFrameDataWithNoLanguageToBytesAndBack() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, null, new EncodedText(TEST_DESCRIPTION), new EncodedText(TEST_VALUE));
		byte[] bytes = frameData.toBytes();
		ID3v2CommentFrameData frameDataCopy = new ID3v2CommentFrameData(false, bytes);
		assertEquals("eng", frameDataCopy.getLanguage());
		assertEquals(new EncodedText(TEST_DESCRIPTION), frameDataCopy.getDescription());
		assertEquals(new EncodedText(TEST_VALUE), frameDataCopy.getComment());
	}

	@Test
	public void convertsFrameDataWithNoDescriptionToBytesAndBack() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, null, new EncodedText(TEST_VALUE));
		byte[] bytes = frameData.toBytes();
		ID3v2CommentFrameData frameDataCopy = new ID3v2CommentFrameData(false, bytes);
		assertEquals("eng", frameDataCopy.getLanguage());
		assertEquals(new EncodedText(""), frameDataCopy.getDescription());
		assertEquals(new EncodedText(TEST_VALUE), frameDataCopy.getComment());
	}

	@Test
	public void convertsFrameDataWithNoDescriptionAndCommentIsUnicodeToBytesAndBack() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, null,
				new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_VALUE_UNICODE));
		byte[] bytes = frameData.toBytes();
		ID3v2CommentFrameData frameDataCopy = new ID3v2CommentFrameData(false, bytes);
		assertEquals("eng", frameDataCopy.getLanguage());
		assertEquals(new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, ""), frameDataCopy.getDescription());
		assertEquals(new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_VALUE_UNICODE), frameDataCopy.getComment());
	}

	@Test
	public void convertsFrameDataWithNoCommentToBytesAndBack() throws Exception {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false, TEST_LANGUAGE, new EncodedText(TEST_DESCRIPTION), null);
		byte[] bytes = frameData.toBytes();
		ID3v2CommentFrameData frameDataCopy = new ID3v2CommentFrameData(false, bytes);
		assertEquals("eng", frameDataCopy.getLanguage());
		assertEquals(new EncodedText(TEST_DESCRIPTION), frameDataCopy.getDescription());
		assertEquals(new EncodedText(""), frameDataCopy.getComment());
	}

	@Test
	public void getsAndSetsLanguage() {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false);
		frameData.setLanguage("my language");
		assertEquals("my language", frameData.getLanguage());
	}

	@Test
	public void getsAndSetsComment() {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false);
		EncodedText comment = new EncodedText("my comment");
		frameData.setComment(comment);
		assertEquals(comment, frameData.getComment());
	}

	@Test
	public void getsAndSetsDescription() {
		ID3v2CommentFrameData frameData = new ID3v2CommentFrameData(false);
		EncodedText description = new EncodedText("my description");
		frameData.setDescription(description);
		assertEquals(description, frameData.getDescription());
	}
}
