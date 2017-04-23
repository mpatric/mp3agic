package com.mpatric.mp3agic;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class ID3v2UrlFrameDataTest {

	private static final String TEST_DESCRIPTION = "DESCRIPTION";
	private static final String TEST_DESCRIPTION_UNICODE = "\u03B3\u03B5\u03B9\u03AC";
	private static final String TEST_URL = "http://ABCDEFGHIJKLMNOPQ";

	@Test
	public void equalsItself() throws Exception {
		ID3v2UrlFrameData frameData = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), TEST_URL);
		assertEquals(frameData, frameData);
	}

	@Test
	public void notEqualToNull() throws Exception {
		ID3v2UrlFrameData frameData = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), TEST_URL);
		assertFalse(frameData.equals(null));
	}

	@Test
	public void notEqualToDifferentClass() {
		ID3v2UrlFrameData frameData = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), TEST_URL);
		assertFalse(frameData.equals("8"));
	}

	@Test
	public void shouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2UrlFrameData frameData1 = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), TEST_URL);
		ID3v2UrlFrameData frameData2 = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), TEST_URL);
		assertEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfUnsynchronizationNotEqual() {
		ID3v2UrlFrameData frameData1 = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), TEST_URL);
		ID3v2UrlFrameData frameData2 = new ID3v2UrlFrameData(true, new EncodedText((byte) 0, TEST_DESCRIPTION), TEST_URL);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfDescriptionNotEqual() {
		ID3v2UrlFrameData frameData1 = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), TEST_URL);
		ID3v2UrlFrameData frameData2 = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, "other description"), TEST_URL);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfDescriptionIsNullOnOne() {
		ID3v2UrlFrameData frameData1 = new ID3v2UrlFrameData(false, null, TEST_URL);
		ID3v2UrlFrameData frameData2 = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), TEST_URL);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void equalIfDescriptionIsNullOnBoth() {
		ID3v2UrlFrameData frameData1 = new ID3v2UrlFrameData(false, null, TEST_URL);
		ID3v2UrlFrameData frameData2 = new ID3v2UrlFrameData(false, null, TEST_URL);
		assertEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfUrlNotEqual() {
		ID3v2UrlFrameData frameData1 = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), TEST_URL);
		ID3v2UrlFrameData frameData2 = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), "other url");
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfIdUrlNullOnOne() {
		ID3v2UrlFrameData frameData1 = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		ID3v2UrlFrameData frameData2 = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), TEST_URL);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void equalIfUrlIsNullOnBoth() {
		ID3v2UrlFrameData frameData1 = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		ID3v2UrlFrameData frameData2 = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		assertEquals(frameData1, frameData2);
	}

	@Test
	public void hashCodeIsConsistent() {
		ID3v2UrlFrameData frameData = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), TEST_URL);
		assertEquals(frameData.hashCode(), frameData.hashCode());
	}

	@Test
	public void equalObjectsHaveSameHashCode() {
		ID3v2UrlFrameData frameData = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), TEST_URL);
		ID3v2UrlFrameData frameDataAgain = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), TEST_URL);
		assertEquals(frameData.hashCode(), frameDataAgain.hashCode());
	}

	@Test
	public void shouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2UrlFrameData frameData = new ID3v2UrlFrameData(false, new EncodedText((byte) 0, TEST_DESCRIPTION), TEST_URL);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {0, 'D', 'E', 'S', 'C', 'R', 'I', 'P', 'T', 'I', 'O', 'N', 0, 'h', 't', 't', 'p', ':', '/', '/', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'};
		assertArrayEquals(expectedBytes, bytes);
		ID3v2UrlFrameData frameDataCopy = new ID3v2UrlFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}

	@Test
	public void shouldConvertFrameDataWithNoDescriptionToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2UrlFrameData frameData = new ID3v2UrlFrameData(false, new EncodedText(""), TEST_URL);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {0, 0, 'h', 't', 't', 'p', ':', '/', '/', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'};
		assertArrayEquals(expectedBytes, bytes);
		ID3v2UrlFrameData frameDataCopy = new ID3v2UrlFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}

	@Test
	public void shouldConvertFrameDataWithUnicodeDescriptionToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2UrlFrameData frameData = new ID3v2UrlFrameData(false, new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_DESCRIPTION_UNICODE), TEST_URL);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {1, (byte) 0xff, (byte) 0xfe, (byte) 0xb3, 0x03, (byte) 0xb5, 0x03, (byte) 0xb9, 0x03, (byte) 0xac, 0x03, 0, 0, 'h', 't', 't', 'p', ':', '/', '/', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'};
		assertArrayEquals(expectedBytes, bytes);
		ID3v2UrlFrameData frameDataCopy = new ID3v2UrlFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}

	@Test
	public void getsAndSetsDescription() {
		ID3v2UrlFrameData frameData = new ID3v2UrlFrameData(false);
		EncodedText description = new EncodedText("my description");
		frameData.setDescription(description);
		assertEquals(description, frameData.getDescription());
	}

	@Test
	public void getsAndSetsUrl() {
		ID3v2UrlFrameData frameData = new ID3v2UrlFrameData(false);
		frameData.setUrl("My URL");
		assertEquals("My URL", frameData.getUrl());
	}
}
