package com.mpatric.mp3agic;

import java.util.Arrays;

import junit.framework.TestCase;


public class ID3v2ObseletePictureFrameDataTest extends TestCase {
	
	private static final String TEST_MIME_TYPE = "image/png";
	private static final String TEST_DESCRIPTION = "DESCRIPTION";
	private static final String TEST_DESCRIPTION_UNICODE = "\u03B3\u03B5\u03B9\u03AC";
	private static final byte[] DUMMY_IMAGE_DATA = {1, 2, 3, 4, 5};

	public void testShouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2ObseletePictureFrameData frameData1 = new ID3v2ObseletePictureFrameData(false, 
				Encoding.ENCODING_UTF_16, TEST_MIME_TYPE, (byte)0, TEST_DESCRIPTION, DUMMY_IMAGE_DATA);
		ID3v2ObseletePictureFrameData frameData2 = new ID3v2ObseletePictureFrameData(false, 
				Encoding.ENCODING_UTF_16, TEST_MIME_TYPE, (byte)0, TEST_DESCRIPTION, DUMMY_IMAGE_DATA);
		assertEquals(frameData1, frameData2);		
	}

	public void testShouldReadFrameData() throws Exception {
		byte[] bytes = {0x00, 'P', 'N', 'G', 0x01, 'D', 'E', 'S', 'C', 'R', 'I', 'P', 'T', 'I', 'O', 'N', 0x00, 1, 2, 3, 4, 5};
		ID3v2ObseletePictureFrameData frameData = new ID3v2ObseletePictureFrameData(false, bytes);
		assertEquals(frameData.getEncoding(), Encoding.ENCODING_ISO_8859_1);
		assertEquals(TEST_MIME_TYPE, frameData.getMimeType());
		assertEquals((byte)1, frameData.getPictureType());
		assertEquals(TEST_DESCRIPTION, frameData.getDescription());
		assertTrue(Arrays.equals(DUMMY_IMAGE_DATA, frameData.getImageData()));
	}
	
	public void testShouldReadFrameDataWithUnicodeDescription() throws Exception {
		byte[] bytes = {0x01, 'P', 'N', 'G', 0x01, (byte)0xff, (byte)0xfe, (byte)0xb3, 0x03, (byte)0xb5, 0x03, (byte)0xb9, 0x03, (byte)0xac, 0x03, 0, 0, 1, 2, 3, 4, 5};
		ID3v2ObseletePictureFrameData frameData = new ID3v2ObseletePictureFrameData(false, bytes);
		assertEquals(frameData.getEncoding(), Encoding.ENCODING_UTF_16);
		assertEquals(TEST_MIME_TYPE, frameData.getMimeType());
		assertEquals((byte)1, frameData.getPictureType());
		assertEquals(TEST_DESCRIPTION_UNICODE, frameData.getDescription());
		assertTrue(Arrays.equals(DUMMY_IMAGE_DATA, frameData.getImageData()));
	}
}
