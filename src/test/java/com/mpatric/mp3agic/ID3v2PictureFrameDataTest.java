package com.mpatric.mp3agic;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class ID3v2PictureFrameDataTest {

	private static final byte BYTE_FF = -0x01;
	private static final byte BYTE_FB = -0x05;

	private static final String TEST_MIME_TYPE = "mime/type";
	private static final String TEST_DESCRIPTION = "DESCRIPTION";
	private static final String TEST_DESCRIPTION_UNICODE = "\u03B3\u03B5\u03B9\u03AC";
	private static final byte[] DUMMY_IMAGE_DATA = {1, 2, 3, 4, 5};

	@Test
	public void equalsItself() throws Exception {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertEquals(frameData, frameData);
	}

	@Test
	public void notEqualToNull() throws Exception {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertFalse(frameData.equals(null));
	}

	@Test
	public void notEqualToDifferentClass() {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertFalse(frameData.equals("8"));
	}

	@Test
	public void shouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfUnsynchronizationNotEqual() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(true, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfMimeTypeNotEqual() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, "other mime type", (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfMimeTypeIsNullOnOne() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, null, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		new ID3v2ChapterFrameData(false, "ch2", 1, 380, 3, 400);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void equalIfMimeTypeIsNullOnBoth() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, null, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, null, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfPictureTypeNotEqual() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 4, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfDescriptionNotEqual() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, "other description"), DUMMY_IMAGE_DATA);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfDescriptionIsNullOnOne() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, null, DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void equalIfDescriptionIsNullOnBoth() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, null, DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, null, DUMMY_IMAGE_DATA);
		assertEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfImageDataNotEqual() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), new byte[]{});
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void notEqualIfImageDataNullOnOne() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertNotEquals(frameData1, frameData2);
	}

	@Test
	public void equalIfImageDataIsNullOnBoth() {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), null);
		assertEquals(frameData1, frameData2);
	}

	@Test
	public void hashCodeIsConsistent() {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertEquals(frameData.hashCode(), frameData.hashCode());
	}

	@Test
	public void equalObjectsHaveSameHashCode() {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameDataAgain = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertEquals(frameData.hashCode(), frameDataAgain.hashCode());
	}

	@Test
	public void shouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText((byte) 0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {0x00, 'm', 'i', 'm', 'e', '/', 't', 'y', 'p', 'e', 0, 0x03, 'D', 'E', 'S', 'C', 'R', 'I', 'P', 'T', 'I', 'O', 'N', 0, 1, 2, 3, 4, 5};
		assertArrayEquals(expectedBytes, bytes);
		ID3v2PictureFrameData frameDataCopy = new ID3v2PictureFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}

	@Test
	public void shouldConvertFrameDataWithUnicodeDescriptionToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte) 3, new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_DESCRIPTION_UNICODE), DUMMY_IMAGE_DATA);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {0x01, 'm', 'i', 'm', 'e', '/', 't', 'y', 'p', 'e', 0, 0x03, (byte) 0xff, (byte) 0xfe, (byte) 0xb3, 0x03, (byte) 0xb5, 0x03, (byte) 0xb9, 0x03, (byte) 0xac, 0x03, 0, 0, 1, 2, 3, 4, 5};
		assertArrayEquals(expectedBytes, bytes);
		ID3v2PictureFrameData frameDataCopy = new ID3v2PictureFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}

	@Test
	public void shouldUnsynchroniseAndSynchroniseDataWhenPackingAndUnpacking() throws Exception {
		byte[] data = {0x00, 'm', 'i', 'm', 'e', '/', 't', 'y', 'p', 'e', 0, 0x03, 'D', 'E', 'S', 'C', 'R', 'I', 'P', 'T', 'I', 'O', 'N', 0, 1, 2, 3, BYTE_FF, 0x00, BYTE_FB, BYTE_FF, 0x00, BYTE_FB, BYTE_FF, 0, 0, 4, 5};
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(true, data);
		byte[] expectedImageData = {1, 2, 3, BYTE_FF, BYTE_FB, BYTE_FF, BYTE_FB, BYTE_FF, 0, 4, 5};
		assertArrayEquals(expectedImageData, frameData.getImageData());
		byte[] bytes = frameData.toBytes();
		assertArrayEquals(data, bytes);
	}

	@Test
	public void getsAndSetsMimeType() {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false);
		frameData.setMimeType("Mime Type 1");
		assertEquals("Mime Type 1", frameData.getMimeType());
	}

	@Test
	public void getsAndSetsPictureType() {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false);
		frameData.setPictureType((byte) 4);
		assertEquals((byte) 4, frameData.getPictureType());
	}

	@Test
	public void getsAndSetsDescription() {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false);
		EncodedText description = new EncodedText("my description");
		frameData.setDescription(description);
		assertEquals(description, frameData.getDescription());
	}

	@Test
	public void getsAndSetsImageData() {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false);
		frameData.setImageData(new byte[]{1, 2});
		assertArrayEquals(new byte[]{1, 2}, frameData.getImageData());
	}
}
