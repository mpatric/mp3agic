package com.mpatric.mp3agic;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import junit.framework.TestCase;


public class ID3v2PictureFrameDataTest extends TestCase {
	
	private static final byte BYTE_FF = -0x01;
	private static final byte BYTE_FB = -0x05;
	
	private static final String TEST_MIME_TYPE = "mime/type";
	private static final String TEST_DESCRIPTION = "DESCRIPTION";
	private static final String TEST_DESCRIPTION_UNICODE = "\u03B3\u03B5\u03B9\u03AC";
	private static final byte[] DUMMY_IMAGE_DATA = {1, 2, 3, 4, 5};

	public void testShouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2PictureFrameData frameData1 = new ID3v2PictureFrameData(false, 
				Encoding.getDefault(), TEST_MIME_TYPE, (byte)3, TEST_DESCRIPTION, DUMMY_IMAGE_DATA);
		ID3v2PictureFrameData frameData2 = new ID3v2PictureFrameData(false, 
				Encoding.getDefault(), TEST_MIME_TYPE, (byte)3, TEST_DESCRIPTION, DUMMY_IMAGE_DATA);
		assertEquals(frameData1, frameData2);		
	}

	public void testShouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, 
				Encoding.ENCODING_ISO_8859_1,  TEST_MIME_TYPE, (byte)3, TEST_DESCRIPTION, DUMMY_IMAGE_DATA);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {0x00, 'm', 'i', 'm', 'e', '/', 't', 'y', 'p', 'e', 0, 0x03, 'D', 'E', 'S', 'C', 'R', 'I', 'P', 'T', 'I', 'O', 'N', 0, 1, 2, 3, 4, 5};
		assertTrue(Arrays.equals(expectedBytes, bytes));
		ID3v2PictureFrameData frameDataCopy = new ID3v2PictureFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}
	
	public void testShouldConvertFrameDataWithUnicodeDescriptionToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, 
				Encoding.ENCODING_UTF_16BE, TEST_MIME_TYPE, (byte)3, TEST_DESCRIPTION_UNICODE, DUMMY_IMAGE_DATA);
		byte[] bytes = frameData.toBytes();
		byte[] expectedBytes = {0x02, 'm', 'i', 'm', 'e', '/', 't', 'y', 'p', 'e', 0, 0x03, 0x03, (byte)0xb3, 0x03, (byte)0xb5, 0x03, (byte)0xb9, 0x03, (byte)0xac, 0, 0, 1, 2, 3, 4, 5};
		assertArrayEquals(expectedBytes, bytes);
		ID3v2PictureFrameData frameDataCopy = new ID3v2PictureFrameData(false, bytes);
		assertEquals(frameData, frameDataCopy);
	}
	
	public void testShouldUnsynchroniseAndSynchroniseDataWhenPackingAndUnpacking() throws Exception {
		byte[] data = {0x00, 'm', 'i', 'm', 'e', '/', 't', 'y', 'p', 'e', 0, 0x03, 'D', 'E', 'S', 'C', 'R', 'I', 'P', 'T', 'I', 'O', 'N', 0, 1, 2, 3, BYTE_FF, 0x00, BYTE_FB, BYTE_FF, 0x00, BYTE_FB, BYTE_FF, 0, 0, 4, 5};
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(true, data);
		byte[] expectedImageData = {1, 2, 3, BYTE_FF, BYTE_FB, BYTE_FF, BYTE_FB, BYTE_FF, 0, 4, 5};
		assertArrayEquals(expectedImageData, frameData.getImageData());
		byte[] bytes = frameData.toBytes();
		assertArrayEquals(data, bytes);
	}
}
