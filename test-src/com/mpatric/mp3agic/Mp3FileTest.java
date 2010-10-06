package com.mpatric.mp3agic;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.MpegFrame;
import com.mpatric.mp3agic.MutableInteger;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import junit.framework.TestCase;

public class Mp3FileTest extends TestCase {

	private static final String MP3_WITH_NO_TAGS = "test-res/notags.mp3";
	private static final String MP3_WITH_ID3V1_AND_ID3V23_TAGS = "test-res/v1andv23tags.mp3";
	private static final String MP3_WITH_DUMMY_START_AND_END_FRAMES = "test-res/dummyframes.mp3";
	private static final String MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS = "test-res/v1andv23andcustomtags.mp3";
	private static final String NOT_AN_MP3 = "test-res/notanmp3.mp3";
	private static final String MP3_WITH_INCOMPLETE_MPEG_FRAME = "test-res/incompletempegframe.mp3";
	
	public void testShouldLoadMp3WithNoTags() throws IOException, UnsupportedTagException, InvalidDataException {
		loadAndCheckTestMp3WithNoTags(MP3_WITH_NO_TAGS, 41);
		loadAndCheckTestMp3WithNoTags(MP3_WITH_NO_TAGS, 256);
		loadAndCheckTestMp3WithNoTags(MP3_WITH_NO_TAGS, 1024);
		loadAndCheckTestMp3WithNoTags(MP3_WITH_NO_TAGS, 5000);
	}
	
	public void testShouldLoadMp3WithId3Tags() throws IOException, UnsupportedTagException, InvalidDataException {
		loadAndCheckTestMp3WithTags(MP3_WITH_ID3V1_AND_ID3V23_TAGS, 41);
		loadAndCheckTestMp3WithTags(MP3_WITH_ID3V1_AND_ID3V23_TAGS, 256);
		loadAndCheckTestMp3WithTags(MP3_WITH_ID3V1_AND_ID3V23_TAGS, 1024);
		loadAndCheckTestMp3WithTags(MP3_WITH_ID3V1_AND_ID3V23_TAGS, 5000);
	}
	
	public void testShouldLoadMp3WithFakeStartAndEndFrames() throws IOException, UnsupportedTagException, InvalidDataException {
		loadAndCheckTestMp3WithTags(MP3_WITH_DUMMY_START_AND_END_FRAMES, 41);
		loadAndCheckTestMp3WithTags(MP3_WITH_DUMMY_START_AND_END_FRAMES, 256);
		loadAndCheckTestMp3WithTags(MP3_WITH_DUMMY_START_AND_END_FRAMES, 1024);
		loadAndCheckTestMp3WithTags(MP3_WITH_DUMMY_START_AND_END_FRAMES, 5000);
	}
	
	public void testShouldLoadMp3WithCustomTag() throws IOException, UnsupportedTagException, InvalidDataException {
		loadAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 41);
		loadAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 256);
		loadAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 1024);
		loadAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 5000);
	}
	
	public void testShouldThrowExceptionForFileThatIsNotAnMp3() throws Exception {
		try {
			new Mp3File(NOT_AN_MP3);
			fail("InvalidDataException expected but not thrown");
		} catch (InvalidDataException e) {
			assertEquals("No mpegs frames found", e.getMessage());
		}
	}
	
	public void testShouldFindProbableStartOfMpegFramesWithPrescan() throws IOException {
		Mp3FileForTesting mp3file = new Mp3FileForTesting(MP3_WITH_ID3V1_AND_ID3V23_TAGS);
		assertEquals(0x44B, mp3file.preScanResult);
	}
	
	public void testShouldThrowExceptionIfSavingMp3WithSameNameAsSourceFile() throws Exception {
		Mp3File mp3file = new Mp3File(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS);
		try {
			mp3file.save(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS);
			fail("IllegalArgumentException expected but not thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("Save filename same as source filename", e.getMessage());
		}
	}
	
	public void testShouldSaveLoadedMp3WhichIsEquivalentToOriginal() throws Exception {
		copyAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 41);
		copyAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 256);
		copyAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 1024);
		copyAndCheckTestMp3WithCustomTag(MP3_WITH_ID3V1_AND_ID3V23_AND_CUSTOM_TAGS, 5000);
	}
	
	public void testShouldIgnoreIncompleteMpegFrame() throws Exception {
		Mp3File mp3File = new Mp3File(MP3_WITH_INCOMPLETE_MPEG_FRAME, 256);
		assertEquals(0x44B, mp3File.getXingOffset());
		assertEquals(0x5EC, mp3File.getStartOffset());
		assertEquals(0xF17, mp3File.getEndOffset());
		assertTrue(mp3File.hasId3v1Tag());
		assertTrue(mp3File.hasId3v2Tag());
		assertEquals(5, mp3File.getFrameCount());
	}
	
	public void testShouldInitialiseProperlyWhenNotScanningFile() throws Exception {
		Mp3File mp3File = new Mp3File(MP3_WITH_INCOMPLETE_MPEG_FRAME, 256, false);
		assertTrue(mp3File.hasId3v1Tag());
		assertTrue(mp3File.hasId3v2Tag());
	}
	
	private Mp3File copyAndCheckTestMp3WithCustomTag(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException, NotSupportedException {
		String saveFilename = null;
		try {
			Mp3File mp3file = loadAndCheckTestMp3WithCustomTag(filename, bufferLength);
			saveFilename = filename + ".copy";
			mp3file.save(saveFilename);
			Mp3File copyMp3file = loadAndCheckTestMp3WithCustomTag(saveFilename, 5000);
			assertEquals(mp3file.getId3v1Tag(), copyMp3file.getId3v1Tag());
			assertEquals(mp3file.getId3v2Tag(), copyMp3file.getId3v2Tag());
			assertTrue(Arrays.equals(mp3file.getCustomTag(), copyMp3file.getCustomTag()));
			return copyMp3file;
		} finally {
			deleteFile(saveFilename);
		}
	}

	private Mp3File loadAndCheckTestMp3WithNoTags(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3File = loadAndCheckTestMp3(filename, bufferLength);
		assertEquals(0x000, mp3File.getXingOffset());
		assertEquals(0x1A1, mp3File.getStartOffset());
		assertEquals(0xB34, mp3File.getEndOffset());
		assertFalse(mp3File.hasId3v1Tag());
		assertFalse(mp3File.hasId3v2Tag());
		assertFalse(mp3File.hasCustomTag());
		return mp3File;
	}
	
	private Mp3File loadAndCheckTestMp3WithTags(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3File = loadAndCheckTestMp3(filename, bufferLength);
		assertEquals(0x44B, mp3File.getXingOffset());
		assertEquals(0x5EC, mp3File.getStartOffset());
		assertEquals(0xF7F, mp3File.getEndOffset());
		assertTrue(mp3File.hasId3v1Tag());
		assertTrue(mp3File.hasId3v2Tag());
		assertFalse(mp3File.hasCustomTag());
		return mp3File;
	}
	
	private Mp3File loadAndCheckTestMp3WithCustomTag(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3File = loadAndCheckTestMp3(filename, bufferLength);
		assertEquals(0x44B, mp3File.getXingOffset());
		assertEquals(0x5EC, mp3File.getStartOffset());
		assertEquals(0xF7F, mp3File.getEndOffset());
		assertTrue(mp3File.hasId3v1Tag());
		assertTrue(mp3File.hasId3v2Tag());
		assertTrue(mp3File.hasCustomTag());
		return mp3File;
	}

	private Mp3File loadAndCheckTestMp3(String filename, int bufferLength) throws IOException, UnsupportedTagException, InvalidDataException {
		Mp3File mp3File = new Mp3File(filename, bufferLength);
		assertTrue(mp3File.hasXingFrame());
		assertEquals(6, mp3File.getFrameCount());
		assertEquals(MpegFrame.MPEG_VERSION_1_0, mp3File.getVersion());
		assertEquals(MpegFrame.MPEG_LAYER_3, mp3File.getLayer());
		assertEquals(44100, mp3File.getSampleRate());
		assertEquals(MpegFrame.CHANNEL_MODE_JOINT_STEREO, mp3File.getChannelMode());
		assertEquals(MpegFrame.EMPHASIS_NONE, mp3File.getEmphasis());
		assertTrue(mp3File.isOriginal());
		assertFalse(mp3File.isCopyright());
		assertEquals(128, mp3File.getXingBitrate());
		assertEquals(125, mp3File.getBitrate());
		assertEquals(1, ((MutableInteger)mp3File.getBitrates().get(new Integer(224))).getValue());
		assertEquals(1, ((MutableInteger)mp3File.getBitrates().get(new Integer(112))).getValue());
		assertEquals(2, ((MutableInteger)mp3File.getBitrates().get(new Integer(96))).getValue());
		assertEquals(1, ((MutableInteger)mp3File.getBitrates().get(new Integer(192))).getValue());
		assertEquals(1, ((MutableInteger)mp3File.getBitrates().get(new Integer(32))).getValue());
		assertEquals(156, mp3File.getLengthInMilliseconds());
		return mp3File;
	}
	
	private void deleteFile(String filename) {
		File file = new File(filename);
		file.delete();
	}
	
	private class Mp3FileForTesting extends Mp3File {

		int preScanResult;

		public Mp3FileForTesting(String filename) throws IOException {
			RandomAccessFile file = new RandomAccessFile(filename, "r");
			preScanResult = preScanFile(file);
		}
	}
}
