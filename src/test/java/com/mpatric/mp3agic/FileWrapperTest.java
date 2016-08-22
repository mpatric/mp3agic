package com.mpatric.mp3agic;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

public class FileWrapperTest {
    private static final String fs = File.separator;

    private static final String VALID_FILENAME = "src" + fs + "test" + fs + "resources" + fs + "notags.mp3";

    private static final long VALID_FILE_LENGTH = 2869;

    private static final String NON_EXISTENT_FILENAME = "just-not.there";

    private static final String MALFORMED_FILENAME = "malformed.?";

    @Test
    public void shouldReadValidFile() throws IOException {
        FileMediaSource fileWrapper = new FileMediaSource(VALID_FILENAME);
        System.out.println(fileWrapper.getFilename());
        System.out.println(VALID_FILENAME);
        assertEquals(fileWrapper.getFilename(), VALID_FILENAME);
        assertTrue(fileWrapper.getLastModified() > 0);
        assertEquals(fileWrapper.getLength(), VALID_FILE_LENGTH);
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldFailForNonExistentFile() throws IOException {
        new FileMediaSource(NON_EXISTENT_FILENAME);
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldFailForMalformedFilename() throws IOException {
        new FileMediaSource(MALFORMED_FILENAME);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailForNullFilename() throws IOException {
        new FileMediaSource((String) null);
    }

}
