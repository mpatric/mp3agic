package com.mpatric.mp3agic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MpegFrameTest {

	private static final byte BYTE_FF = -0x01;
	private static final byte BYTE_FB = -0x05;
	private static final byte BYTE_F9 = -0x07;
	private static final byte BYTE_F3 = -0x0D;
	private static final byte BYTE_F2 = -0x0E;
	private static final byte BYTE_A2 = -0x5E;
	private static final byte BYTE_AE = -0x52;
	private static final byte BYTE_DB = -0x25;
	private static final byte BYTE_EB = -0x15;
	private static final byte BYTE_40 = 0x40;
	private static final byte BYTE_02 = 0x02;

	@Test
	public void testBitwiseLeftShiftOperationsOnLong() {
		long original = 0xFFFFFFFE; // 1111 1111 1111 1111 1111 1111 1111 1110
		long expectedShl1 = 0xFFFFFFFC; // 1111 1111 1111 1111 1111 1111 1111 1100
		long expectedShl28 = 0xE0000000; // 1110 0000 0000 0000 0000 0000 0000 0000
		long expectedShl30 = 0x80000000; // 1000 0000 0000 0000 0000 0000 0000 0000
		assertEquals(expectedShl1, original << 1);
		assertEquals(expectedShl28, original << 28);
		assertEquals(expectedShl30, original << 30);
	}

	@Test
	public void testBitwiseRightShiftOperationsOnLong() {
		long original = 0x80000000; // 1000 0000 0000 0000 0000 0000 0000 0000
		long expectedShr1 = 0xC0000000; // 1100 0000 0000 0000 0000 0000 0000 0000
		long expectedShr28 = 0xFFFFFFF8; // 1111 1111 1111 1111 1111 1111 1111 1000
		long expectedShr30 = 0xFFFFFFFE; // 1111 1111 1111 1111 1111 1111 1111 1110
		assertEquals(expectedShr1, original >> 1);
		assertEquals(expectedShr28, original >> 28);
		assertEquals(expectedShr30, original >> 30);
	}

	@Test
	public void testShiftingByteIntoBiggerNumber() {
		byte original = -0x02; // 1111 1110
		long originalAsLong = (original & 0xFF);
		byte expectedShl1 = -0x04; // 1111 1100
		long expectedShl8 = 0x0000FE00; // 0000 0000 0000 0000 1111 1110 0000 0000
		long expectedShl16 = 0x00FE0000; // 0000 0000 1111 1110 0000 0000 0000 0000
		long expectedShl23 = 0x7F000000; // 0111 1111 00000 0000 0000 0000 0000 0000
		assertEquals(expectedShl1, original << 1);
		assertEquals(254, originalAsLong);
		assertEquals(expectedShl8, originalAsLong << 8);
		assertEquals(expectedShl16, originalAsLong << 16);
		assertEquals(expectedShl23, originalAsLong << 23);
	}

	@Test
	public void shouldExtractValidFields() {
		MpegFrameForTesting mpegFrame = new MpegFrameForTesting();
		assertEquals(0x000007FF, mpegFrame.extractField(0xFFE00000, 0xFFE00000L));
		assertEquals(0x000007FF, mpegFrame.extractField(0xFFEFFFFF, 0xFFE00000L));
		assertEquals(0x00000055, mpegFrame.extractField(0x11111155, 0x000000FFL));
		assertEquals(0x00000055, mpegFrame.extractField(0xFFEFFF55, 0x000000FFL));
	}

	@Test
	public void shouldExtractValidMpegVersion1Header() throws InvalidDataException {
		byte[] frameData = {BYTE_FF, BYTE_FB, BYTE_A2, BYTE_40};
		MpegFrameForTesting mpegFrame = new MpegFrameForTesting(frameData);
		assertEquals(MpegFrame.MPEG_VERSION_1_0, mpegFrame.getVersion());
		assertEquals(MpegFrame.MPEG_LAYER_3, mpegFrame.getLayer());
		assertEquals(160, mpegFrame.getBitrate());
		assertEquals(44100, mpegFrame.getSampleRate());
		assertEquals(MpegFrame.CHANNEL_MODE_JOINT_STEREO, mpegFrame.getChannelMode());
		assertEquals("None", mpegFrame.getModeExtension());
		assertEquals("None", mpegFrame.getEmphasis());
		assertEquals(true, mpegFrame.isProtection());
		assertEquals(true, mpegFrame.hasPadding());
		assertEquals(false, mpegFrame.isPrivate());
		assertEquals(false, mpegFrame.isCopyright());
		assertEquals(false, mpegFrame.isOriginal());
		assertEquals(523, mpegFrame.getLengthInBytes());
	}

	@Test
	public void shouldProcessValidMpegVersion2Header() throws InvalidDataException {
		byte[] frameData = {BYTE_FF, BYTE_F3, BYTE_A2, BYTE_40};
		MpegFrameForTesting mpegFrame = new MpegFrameForTesting(frameData);
		assertEquals(MpegFrame.MPEG_VERSION_2_0, mpegFrame.getVersion());
		assertEquals(MpegFrame.MPEG_LAYER_3, mpegFrame.getLayer());
		assertEquals(96, mpegFrame.getBitrate());
		assertEquals(22050, mpegFrame.getSampleRate());
		assertEquals(MpegFrame.CHANNEL_MODE_JOINT_STEREO, mpegFrame.getChannelMode());
		assertEquals("None", mpegFrame.getModeExtension());
		assertEquals("None", mpegFrame.getEmphasis());
		assertEquals(true, mpegFrame.isProtection());
		assertEquals(true, mpegFrame.hasPadding());
		assertEquals(false, mpegFrame.isPrivate());
		assertEquals(false, mpegFrame.isCopyright());
		assertEquals(false, mpegFrame.isOriginal());
		assertEquals(627, mpegFrame.getLengthInBytes());
	}

	@Test
	public void shouldThrowExceptionForInvalidFrameSync() {
		byte[] frameData = {BYTE_FF, BYTE_DB, BYTE_A2, BYTE_40};
		try {
			new MpegFrameForTesting(frameData);
			fail("InvalidDataException expected but not thrown");
		} catch (InvalidDataException e) {
			assertEquals("Frame sync missing", e.getMessage());
		}
	}

	@Test
	public void shouldThrowExceptionForInvalidMpegVersion() {
		byte[] frameData = {BYTE_FF, BYTE_EB, BYTE_A2, BYTE_40};
		try {
			new MpegFrameForTesting(frameData);
			fail("InvalidDataException expected but not thrown");
		} catch (InvalidDataException e) {
			assertEquals("Invalid mpeg audio version in frame header", e.getMessage());
		}
	}

	@Test
	public void shouldThrowExceptionForInvalidMpegLayer() {
		byte[] frameData = {BYTE_FF, BYTE_F9, BYTE_A2, BYTE_40};
		try {
			new MpegFrameForTesting(frameData);
			fail("InvalidDataException expected but not thrown");
		} catch (InvalidDataException e) {
			assertEquals("Invalid mpeg layer description in frame header", e.getMessage());
		}
	}

	@Test
	public void shouldThrowExceptionForFreeBitrate() {
		byte[] frameData = {BYTE_FF, BYTE_FB, BYTE_02, BYTE_40};
		try {
			new MpegFrameForTesting(frameData);
			fail("InvalidDataException expected but not thrown");
		} catch (InvalidDataException e) {
			assertEquals("Invalid bitrate in frame header", e.getMessage());
		}
	}

	@Test
	public void shouldThrowExceptionForInvalidBitrate() {
		byte[] frameData = {BYTE_FF, BYTE_FB, BYTE_F2, BYTE_40};
		try {
			new MpegFrameForTesting(frameData);
			fail("InvalidDataException expected but not thrown");
		} catch (InvalidDataException e) {
			assertEquals("Invalid bitrate in frame header", e.getMessage());
		}
	}

	@Test
	public void shouldThrowExceptionForInvalidSampleRate() {
		byte[] frameData = {BYTE_FF, BYTE_FB, BYTE_AE, BYTE_40};
		try {
			new MpegFrameForTesting(frameData);
			fail("InvalidDataException expected but not thrown");
		} catch (InvalidDataException e) {
			assertEquals("Invalid sample rate in frame header", e.getMessage());
		}
	}

	class MpegFrameForTesting extends MpegFrame {

		public MpegFrameForTesting() {
			super();
		}

		public MpegFrameForTesting(byte[] frameData) throws InvalidDataException {
			super(frameData);
		}

	}
}
