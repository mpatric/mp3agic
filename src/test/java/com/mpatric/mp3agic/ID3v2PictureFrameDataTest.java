package com.mpatric.mp3agic;

import java.util.Arrays;

import com.mpatric.mp3agic.EncodedText;
import com.mpatric.mp3agic.ID3v2PictureFrameData;

import junit.framework.TestCase;

public class ID3v2PictureFrameDataTest extends TestCase {
	
	private static final byte BYTE_FF = -0x01;
	private static final byte BYTE_FB = -0x05;
	
	private static final int TEST_IMAGE_DATA_LENGTH = 20;
	private static final String TEST_MIME_TYPE = "mime/type";
	private static final String TEST_DESCRIPTION = "DESCRIPTION";

	public void testShouldConsiderTwoEquivalentObjectsEqual() throws Exception {
		ID3v2PictureFrameData frameData1 = createTestPictureFrameData();
		ID3v2PictureFrameData frameData2 = createTestPictureFrameData();
		assertEquals(frameData1, frameData2);		
	}

	public void testShouldConvertFrameDataToBytesAndBackToEquivalentObject() throws Exception {
		ID3v2PictureFrameData frameData = createTestPictureFrameData();
		byte[] bytes = frameData.toBytes();
		ID3v2PictureFrameData frameDataCopy = new ID3v2PictureFrameData(false, bytes);
		assertEquals(1 + TEST_MIME_TYPE.length() + 1 + 1 + TEST_DESCRIPTION.length() + 1 + TEST_IMAGE_DATA_LENGTH, bytes.length);
		assertEquals(frameData, frameDataCopy);
	}
	
	public void testShouldInitialiseFrameDataCorrectlyWithUnsynchronisedData() throws Exception {
		byte[] bytes = createTestPictureFrameDataRequiringSynchronisation();
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(true, bytes);
		int headerLength = 4;
		assertEquals(TEST_IMAGE_DATA_LENGTH - headerLength - 3, frameData.getImageData().length);
	}
	
	public void testShouldGenerateUnsynchronisedDataFromFrameData() throws Exception {
		byte[] bytes = createTestPictureFrameDataRequiringSynchronisation();
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(true, bytes);
		byte[] unsynchronised = frameData.toBytes();
		assertTrue(Arrays.equals(bytes, unsynchronised));
	}
	
	private ID3v2PictureFrameData createTestPictureFrameData() {
		byte[] imageData = new byte[TEST_IMAGE_DATA_LENGTH];
		for (int i = 0; i < imageData.length; i++) {
			imageData[i] = (byte)i;
		}
		ID3v2PictureFrameData frameData = new ID3v2PictureFrameData(false, TEST_MIME_TYPE, (byte)3, new EncodedText((byte)0, TEST_DESCRIPTION), imageData);
		return frameData;
	}
	
	private byte[] createTestPictureFrameDataRequiringSynchronisation() {
		byte[] bytes = new byte[TEST_IMAGE_DATA_LENGTH];
		bytes[0] = 0;
		bytes[1] = 0;
		bytes[2] = 0;
		bytes[3] = 0;
		for (int i = 0; i < TEST_IMAGE_DATA_LENGTH - 4; i++) {
			bytes[4 + i] = (byte)(i + 1);
		}
		bytes[4] = BYTE_FF;
		bytes[5] = 0;
		bytes[6] = BYTE_FB;
		bytes[8] = BYTE_FF;
		bytes[9] = 0;
		bytes[10] = BYTE_FB;
		bytes[bytes.length - 2] = BYTE_FF;
		bytes[bytes.length - 1] = 0;
		return bytes;
	}
}
