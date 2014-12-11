package com.mpatric.mp3agic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.mpatric.mp3agic.FileWrapper;

import junit.framework.TestCase;

public class FileWrapperTest extends TestCase {
	private static final String fs = File.separator;
	private static final String VALID_FILENAME = "src" + fs + "test" + fs + "resources" + fs + "notags.mp3";
	private static final long VALID_FILE_LENGTH = 2869;
	private static final String NON_EXISTANT_FILENAME = "just-not.there";	
	private static final String MALFORMED_FILENAME = "malformed.?";

	public void testShouldReadValidFile() throws IOException {
		FileWrapper fileWrapper = new FileWrapper(VALID_FILENAME);
		System.out.println(fileWrapper.getFilename());
		System.out.println(VALID_FILENAME);
		assertEquals(fileWrapper.getFilename(), VALID_FILENAME);
		assertTrue(fileWrapper.getLastModified() > 0);
		assertEquals(fileWrapper.getLength(), VALID_FILE_LENGTH);
	}
	
	public void testShouldFailForNonExistentFile() throws IOException {
		try {
			new FileWrapper(NON_EXISTANT_FILENAME);
			fail("FileNotFoundException expected but not thrown");
		} catch (FileNotFoundException e) {
			// expected
		}
	}
	
	public void testShouldFailForMalformedFilename() throws IOException {
		try {
			new FileWrapper(MALFORMED_FILENAME);
			fail("FileNotFoundException expected but not thrown");
		} catch (FileNotFoundException e) {
			// expected
		}
	}
	
	public void testShouldFailForNullFilename() throws IOException {
		try {
			new FileWrapper((String)null);
			fail("NullPointerException expected but not thrown");
		} catch (NullPointerException e) {
			// expected
		}
	}
	
	public void testShouldFailForNullFilenameFile() throws IOException {
		try {
			new FileWrapper((java.io.File)null);
			fail("NullPointerException expected but not thrown");
		} catch (NullPointerException e) {
			// expected
		}
	}
}
