package com.mpatric.mp3agic;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ID3v2PictureFrameDataTest {
	
	private static final byte BYTE_FF = -0x01;
	private static final byte BYTE_FB = -0x05;
	
	private static final String TEST_MIME_TYPE = "mime/type";
	private static final String TEST_DESCRIPTION = "DESCRIPTION";
	private static final String TEST_DESCRIPTION_UNICODE = "\u03B3\u03B5\u03B9\u03AC";
	private static final byte[] DUMMY_IMAGE_DATA = {1, 2, 3, 4, 5};

    @Test
	public void shouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte)3, new EncodedText((byte)0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte)3, new EncodedText((byte)0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		assertEquals(frameData1, frameData2);		
	}

    @Test
	public void shouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte)3, new EncodedText((byte)0, TEST_DESCRIPTION), DUMMY_IMAGE_DATA);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {0x00, 'm', 'i', 'm', 'e', '/', 't', 'y', 'p', 'e', 0, 0x03, 'D', 'E', 'S', 'C', 'R', 'I', 'P', 'T', 'I', 'O', 'N', 0, 1, 2, 3, 4, 5};
        assertArrayEquals(expectedBytes, bytes);
		ID3v2PictureFrameData frameDataCopy = new ID3v2PictureFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}

    @Test
	public void shouldConvertFrameDataWithUnicodeDescriptionToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte)3, new EncodedText(EncodedText.TEXT_ENCODING_UTF_16, TEST_DESCRIPTION_UNICODE), DUMMY_IMAGE_DATA);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {0x01, 'm', 'i', 'm', 'e', '/', 't', 'y', 'p', 'e', 0, 0x03, (byte)0xff, (byte)0xfe, (byte)0xb3, 0x03, (byte)0xb5, 0x03, (byte)0xb9, 0x03, (byte)0xac, 0x03, 0, 0, 1, 2, 3, 4, 5};
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
}
