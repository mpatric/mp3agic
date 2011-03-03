package com.mpatric.mp3agic.app;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Iterator;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.mpatric.mp3agic.app.Mp3Retag;

import junit.framework.TestCase;

public class Mp3RetagTest extends TestCase {

	private static final String TEST_FILE_WITH_ALL_TAGS_SET = "src/test/resources/v1andv23tags.mp3";
	private static final String TEST_FILE_WITH_ID3V1_ONLY = "src/test/resources/v1tag.mp3";
	private static final String TEST_FILE_WITH_ID3V2_ONLY = "src/test/resources/v23tag.mp3";
	private static final String TEST_FILE_WITH_IMAGE = "src/test/resources/formatchingimage.mp3";
	private static final String TEST_FILE_WITH_IMAGE_WITH_ARTIST_AND = "src/test/resources/formatchingimage2.mp3";
	private static final String TEST_FILE_WITH_SOME_TAGS_SET = "src/test/resources/mixedfields1.mp3";
	private static final String ANOTHER_TEST_FILE_WITH_SOME_TAGS_SET = "src/test/resources/mixedfields2.mp3";
	private static final String TEST_FILE_WITH_NON_NUMERIC_ID3V2_TRACK = "src/test/resources/nonnumeric23track.mp3";
	
	public void setUp() {
		Mp3Retag.attachImage = false;
		Mp3Retag.keepCustomTag = true;
		Mp3Retag.customTag = null;
	}

	public void testShouldReturnFalseForInvalidOptions() throws Exception {
		assertFalse(Mp3Retag.parseArgs(new String[] {}));
		assertFalse(Mp3Retag.parseArgs(new String[] {"-i"}));
		assertFalse(Mp3Retag.parseArgs(new String[] {"-p"}));
		assertFalse(Mp3Retag.parseArgs(new String[] {"-p", "filename"}));
		assertFalse(Mp3Retag.parseArgs(new String[] {"-z", "filename"}));
		assertFalse(Mp3Retag.parseArgs(new String[] {"filename", "filename"}));
	}
	
	public void testShouldRetagFileToEquivalentFile() throws Exception {
		try {
			Mp3RetagForTesting mp3Retag = new Mp3RetagForTesting(TEST_FILE_WITH_ALL_TAGS_SET);
			assertNull(mp3Retag.lastError);
			assertEquals("Retagged \"v1andv23tags.mp3\"", mp3Retag.lastOut);
			validateRetaggedFile(TEST_FILE_WITH_ALL_TAGS_SET + ".retag", true);
		} finally {
			deleteFile(TEST_FILE_WITH_ALL_TAGS_SET + ".retag");
		}
	}

	public void testShouldRetagFileAndAddId3v2Tag() throws Exception {
		try {
			Mp3RetagForTesting mp3Retag = new Mp3RetagForTesting(TEST_FILE_WITH_ID3V1_ONLY);
			assertNull(mp3Retag.lastError);
			assertEquals("Retagged \"v1tag.mp3\", added ID3v2 tag", mp3Retag.lastOut);
			validateRetaggedFile(TEST_FILE_WITH_ID3V1_ONLY + ".retag", false);
		} finally {
			deleteFile(TEST_FILE_WITH_ID3V1_ONLY + ".retag");
		}
	}
	
	public void testShouldRetagFileAndAddId3v1Tag() throws Exception {
		try {
			Mp3RetagForTesting mp3Retag = new Mp3RetagForTesting(TEST_FILE_WITH_ID3V2_ONLY);
			assertNull(mp3Retag.lastError);
			assertEquals("Retagged \"v23tag.mp3\", added ID3v1 tag", mp3Retag.lastOut);
			validateRetaggedFile(TEST_FILE_WITH_ID3V2_ONLY + ".retag", true);
		} finally {
			deleteFile(TEST_FILE_WITH_ID3V2_ONLY + ".retag");
		}
	}
	
	public void testShouldRetagFileWithSomeFieldsSetInEachTag() throws Exception {
		try {
			Mp3RetagForTesting mp3Retag = new Mp3RetagForTesting(TEST_FILE_WITH_SOME_TAGS_SET);
			assertNull(mp3Retag.lastError);
			assertEquals("Retagged \"mixedfields1.mp3\"", mp3Retag.lastOut);
			validateRetaggedFile(TEST_FILE_WITH_SOME_TAGS_SET + ".retag", true);
		} finally {
			deleteFile(TEST_FILE_WITH_SOME_TAGS_SET + ".retag");
		}
	}
	
	public void testShouldRetagFileWithOtherFieldsSetInEachTag() throws Exception {
		try {
			Mp3RetagForTesting mp3Retag = new Mp3RetagForTesting(ANOTHER_TEST_FILE_WITH_SOME_TAGS_SET);
			assertNull(mp3Retag.lastError);
			assertEquals("Retagged \"mixedfields2.mp3\"", mp3Retag.lastOut);
			validateRetaggedFile(ANOTHER_TEST_FILE_WITH_SOME_TAGS_SET + ".retag", true);
		} finally {
			deleteFile(ANOTHER_TEST_FILE_WITH_SOME_TAGS_SET + ".retag");
		}
	}
	
	public void testShouldFailToAddNonExistantAlbumImage() throws Exception {
		try {
			Mp3Retag.attachImage = true;
			Mp3RetagForTesting mp3Retag = new Mp3RetagForTesting(TEST_FILE_WITH_ALL_TAGS_SET);
			assertEquals("WARNING processing \"v1andv23tags.mp3\" - no album image found", mp3Retag.lastError);
			assertEquals("Retagged \"v1andv23tags.mp3\"", mp3Retag.lastOut);
			validateRetaggedFile(TEST_FILE_WITH_ALL_TAGS_SET + ".retag", true);
		} finally {
			deleteFile(TEST_FILE_WITH_ALL_TAGS_SET + ".retag");
		}
	}
	
	public void testShouldAddAlbumImageForFullImageFilename() throws Exception {
		String imageFilename = "src/test/resources/Some Artist - Some Album.png";
		tryAddImage(TEST_FILE_WITH_IMAGE, imageFilename);
	}
	
	public void testShouldAddAlbumImageForCompressedImageFilename() throws Exception {
		String imageFilename = "src/test/resources/SomeArtist-SomeAlbum.png";
		tryAddImage(TEST_FILE_WITH_IMAGE, imageFilename);
	}
	
	public void testShouldAddAlbumImageForJustAlbumImageFilename() throws Exception {
		String imageFilename = "src/test/resources/Some Album.png";
		tryAddImage(TEST_FILE_WITH_IMAGE, imageFilename);
	}
	
	public void testShouldAddAlbumImageForJustCompressedAlbumImageFilename() throws Exception {
		String imageFilename = "src/test/resources/SomeAlbum.png";
		tryAddImage(TEST_FILE_WITH_IMAGE, imageFilename);
	}
	
	public void testShouldAddAlbumImageForFolderFilename() throws Exception {
		String imageFilename = "src/test/resources/folder.png";
		tryAddImage(TEST_FILE_WITH_IMAGE, imageFilename);
	}
	
	public void testShouldAddAlbumImageForArtistWithoutAndBitAndAlbumFilename() throws Exception {
		String imageFilename = "src/test/resources/Some Artist - Some Album.png";
		tryAddImage(TEST_FILE_WITH_IMAGE_WITH_ARTIST_AND, imageFilename);
	}
	
	public void testShouldAddAlbumImageForCompressedArtistWithoutAndBitAndAlbumFilename() throws Exception {
		String imageFilename = "src/test/resources/SomeArtist-SomeAlbum.png";
		tryAddImage(TEST_FILE_WITH_IMAGE_WITH_ARTIST_AND, imageFilename);
	}
	
	public void testShouldDealWithNonNumericId3v2TrackField() throws Exception {
		try {
			Mp3RetagForTesting mp3Retag = new Mp3RetagForTesting(TEST_FILE_WITH_NON_NUMERIC_ID3V2_TRACK);
			assertNull(mp3Retag.lastError);
			assertEquals("Retagged \"nonnumeric23track.mp3\"", mp3Retag.lastOut);
			Mp3File mp3file = new Mp3File(TEST_FILE_WITH_NON_NUMERIC_ID3V2_TRACK + ".retag");
			assertEquals("1", mp3file.getId3v1Tag().getTrack());
		} finally {
			deleteFile(TEST_FILE_WITH_NON_NUMERIC_ID3V2_TRACK + ".retag");
		}
	}
	
	public void testShouldRenameOneFileToBackupAndOtherFileToOriginalName() throws Exception {
		String filename = "src/test/resources/forrenaming.mp3";
		try {
			RandomAccessFile raFile = null;
			try {
				raFile = new RandomAccessFile(filename, "rw");
				raFile.write(new byte[] {0, 1, 2, 3});
			} finally {
				if (raFile != null) raFile.close();
				raFile = null;
			}
			try {
				raFile = new RandomAccessFile(filename + ".retag", "rw");
				raFile.write(new byte[] {0, 1, 2, 3, 4, 5});
			} finally {
				if (raFile != null) raFile.close();
			}
			Mp3RetagForTesting mp3Retag = new Mp3RetagForTesting();
			mp3Retag.tryRenameFiles(filename);
			File file = new File(filename);
			assertEquals(6, file.length());
			file = new File(filename + ".bak");
			assertEquals(4, file.length());
		} finally {
			deleteFile(filename);
			deleteFile(filename + ".bak");
		}
	}
	
	public void testShouldIterateFiletypesInOrder() throws Exception {
		Iterator<String> iterator = Mp3Retag.imageFileTypes.keySet().iterator();
		assertEquals(iterator.next(), "jpg");		
		assertEquals(iterator.next(), "jpeg");		
		assertEquals(iterator.next(), "png");		
	}
	
	private void validateRetaggedFile(String filename, boolean checkV2Fields) throws UnsupportedTagException, InvalidDataException, IOException {
		Mp3File mp3file = new Mp3File(filename);
		assertEquals(6, mp3file.getFrameCount());
		assertEquals(156, mp3file.getLengthInMilliseconds());
		assertEquals("1", mp3file.getId3v1Tag().getTrack());
		assertEquals("ARTIST123456789012345678901234", mp3file.getId3v1Tag().getArtist());
		assertEquals("TITLE1234567890123456789012345", mp3file.getId3v1Tag().getTitle());
		assertEquals("ALBUM1234567890123456789012345", mp3file.getId3v1Tag().getAlbum());
		assertEquals("2001", mp3file.getId3v1Tag().getYear());
		assertEquals(0x0d, mp3file.getId3v1Tag().getGenre());
		assertEquals("Pop", mp3file.getId3v1Tag().getGenreDescription());
		assertEquals("COMMENT123456789012345678901", mp3file.getId3v1Tag().getComment());
		assertEquals("1", mp3file.getId3v2Tag().getTrack());
		assertEquals("ARTIST123456789012345678901234", mp3file.getId3v2Tag().getArtist());
		assertEquals("TITLE1234567890123456789012345", mp3file.getId3v2Tag().getTitle());
		assertEquals("ALBUM1234567890123456789012345", mp3file.getId3v2Tag().getAlbum());
		assertEquals("2001", mp3file.getId3v2Tag().getYear());
		assertEquals(0x0d, mp3file.getId3v2Tag().getGenre());
		assertEquals("Pop", mp3file.getId3v2Tag().getGenreDescription());
		assertEquals("COMMENT123456789012345678901", mp3file.getId3v2Tag().getComment());
		if (checkV2Fields) {
			assertEquals("COMPOSER23456789012345678901234", mp3file.getId3v2Tag().getComposer());
			assertEquals("ORIGARTIST234567890123456789012", mp3file.getId3v2Tag().getOriginalArtist());
			assertEquals("COPYRIGHT2345678901234567890123", mp3file.getId3v2Tag().getCopyright());
			assertEquals("URL2345678901234567890123456789", mp3file.getId3v2Tag().getUrl());
			assertEquals("ENCODER234567890123456789012345", mp3file.getId3v2Tag().getEncoder());
		}
	}
	
	private void validateAlbumImage(String filename) throws UnsupportedTagException, InvalidDataException, IOException {
		Mp3File mp3file = new Mp3File(filename);
		assertEquals("image/png", mp3file.getId3v2Tag().getAlbumImageMimeType());
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile("src/test/resources/image.png", "r");
			byte[] bytes = new byte[(int) file.length()];
			file.read(bytes);
			assertTrue(Arrays.equals(bytes, mp3file.getId3v2Tag().getAlbumImage()));
		} finally {
			if (file != null) file.close();
		}
	}
	
	private void tryAddImage(String mp3Filename, String imageFilename) throws IOException, UnsupportedTagException, InvalidDataException {
		try {
			copyFile("src/test/resources/image.png", imageFilename);
			Mp3Retag.attachImage = true;
			Mp3RetagForTesting mp3Retag = new Mp3RetagForTesting(mp3Filename);
			assertNull(mp3Retag.lastError);
			assertTrue(mp3Retag.lastOut.matches("Retagged .*, added album image"));
			validateAlbumImage(mp3Filename + ".retag");
		} finally {
			deleteFile(mp3Filename + ".retag");
			deleteFile(imageFilename);
		}
	}
	
	private void deleteFile(String filename) {
		File file = new File(filename);
		file.delete();
	}
	
	private void copyFile(String srcFilename, String destFilename) throws IOException {
		RandomAccessFile srcFile = null;
		RandomAccessFile destFile = null;
		try {
			srcFile = new RandomAccessFile(srcFilename, "r");
			destFile = new RandomAccessFile(destFilename, "rw");
			int bufferSize = 8192;
			byte[] bytes = new byte[bufferSize];
			while (true) {
				int bytesRead = srcFile.read(bytes);
				destFile.write(bytes, 0, bytesRead);
				if (bytesRead < bufferSize) break;
			}
		} finally {
			if (srcFile != null) srcFile.close();
			if (destFile != null) destFile.close();
		}
	}
	
	private class Mp3RetagForTesting extends Mp3Retag {
		
		String lastError;
		String lastOut;
		
		Mp3RetagForTesting() {
			super();
		}

		Mp3RetagForTesting(String filename) {
			super(filename);
		}

		protected void renameFiles() {
			// do nothing
		}
		
		protected void tryRenameFiles(String filename) {
			Mp3Retag.filename = filename;
			super.renameFiles();
		}
		
		protected void printError(String message) {
			lastError = message;
		}

		protected void printOut(String message) {
			lastOut = message;
		}
	}
}
