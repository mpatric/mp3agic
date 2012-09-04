package com.mpatric.mp3agic.app;

import com.mpatric.mp3agic.app.Mp3Catalog;

import junit.framework.TestCase;

public class Mp3CatalogTest extends TestCase {

	private static final String INVALID_FILENAME = "InvalidFile.mp3";
	private static final String UNSUPPORTED_VERSION_FILENAME = "src/test/resources/v1andunsupportedv2tags.mp3";
	private static final String VALID_MP3_FILENAME_WITH_ALBUM_IMAGE = "src/test/resources/v1andv23tagswithalbumimage.mp3";
	private static final String VALID_MP3_FILENAME_WITH_CUSTOM_TAG = "src/test/resources/v1andv23andcustomtags.mp3";
	
	public void testShouldPrintErrorForFileNotFound() throws Exception {
		Mp3CatalogForTesting mp3Catalog = new Mp3CatalogForTesting(INVALID_FILENAME);
		assertEquals("ERROR processing InvalidFile.mp3 - [java.io.FileNotFoundException: File not found " + INVALID_FILENAME + "]", mp3Catalog.lastError);
	}
	
	public void testShouldPrintErrorForMp3WithUnsupportedTagVersion() throws Exception {
		Mp3CatalogForTesting mp3Catalog = new Mp3CatalogForTesting(UNSUPPORTED_VERSION_FILENAME);
		assertEquals("ERROR processing v1andunsupportedv2tags.mp3 - [com.mpatric.mp3agic.UnsupportedTagException: Unsupported version 2.5.0]", mp3Catalog.lastError);
	}
	
	public void testShouldPrintCatalogLineForValidMp3WithAlbumImage() throws Exception {
		Mp3CatalogForTesting mp3Catalog = new Mp3CatalogForTesting(VALID_MP3_FILENAME_WITH_ALBUM_IMAGE);
		assertEquals("\"v1andv23tagswithalbumimage.mp3\",\"6144\",\"0\",\"1.0\",\"III\",\"44100\",\"125\",\"VBR\",\"Joint stereo\",\"1.1\",\"2.3.0\",\"1\",\"ARTIST123456789012345678901234\",\"ALBUM1234567890123456789012345\",\"TITLE1234567890123456789012345\",\"2001\",\"Pop\",\"COMMENT123456789012345678901\",\"COMPOSER23456789012345678901234\",\"ORIGARTIST234567890123456789012\",\"COPYRIGHT2345678901234567890123\",\"URL2345678901234567890123456789\",\"ENCODER234567890123456789012345\",\"image/png\",\"\"", mp3Catalog.lastOut);
	}
	
	public void testShouldPrintCatalogLineForValidMp3WithCustomTag() throws Exception {
		Mp3CatalogForTesting mp3Catalog = new Mp3CatalogForTesting(VALID_MP3_FILENAME_WITH_CUSTOM_TAG);
		assertEquals("\"v1andv23andcustomtags.mp3\",\"4129\",\"0\",\"1.0\",\"III\",\"44100\",\"125\",\"VBR\",\"Joint stereo\",\"1.1\",\"2.3.0\",\"1\",\"ARTIST123456789012345678901234\",\"ALBUM1234567890123456789012345\",\"TITLE1234567890123456789012345\",\"2001\",\"Pop\",\"COMMENT123456789012345678901\",\"COMPOSER23456789012345678901234\",\"ORIGARTIST234567890123456789012\",\"COPYRIGHT2345678901234567890123\",\"URL2345678901234567890123456789\",\"ENCODER234567890123456789012345\",\"\",\"CUSTOMTAG123456789012345678901234\"", mp3Catalog.lastOut);
	}
	
	public void testShouldEscapeQuotes() throws Exception {
		Mp3CatalogForTesting mp3Catalog = new Mp3CatalogForTesting();
		assertEquals("Hello there!", mp3Catalog.escapeQuotes("Hello there!"));
		assertEquals("Hello \"\" there!", mp3Catalog.escapeQuotes("Hello \" there!"));
		assertEquals("Hello \"\"there!\"\"", mp3Catalog.escapeQuotes("Hello \"there!\""));
		assertEquals("\"\"Hello\"\"\"\"there!\"\"", mp3Catalog.escapeQuotes("\"Hello\"\"there!\""));
	}
	
	class Mp3CatalogForTesting extends Mp3Catalog {
		
		String lastError;
		String lastOut;
		
		public Mp3CatalogForTesting() {
			super();
		}

		public Mp3CatalogForTesting(String filename) {
			super(filename);
		}

		protected void printError(String message) {
			lastError = message;
		}

		protected void printOut(String message) {
			lastOut = message;
		}
	}
}
