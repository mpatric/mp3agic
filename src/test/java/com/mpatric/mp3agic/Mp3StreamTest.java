package com.mpatric.mp3agic;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

/**
 * mp3agic
 * User: Bahadir
 * Date: 08.02.2014
 * Time: 14:36
 */
public class Mp3StreamTest {


    File[] allSampleTagFiles;

    @Before
    public void setUp() throws Exception {
        File dir = new File("target/test-classes/");
        allSampleTagFiles = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return
                        name.toLowerCase().endsWith(".mp3") &&
                        name.toLowerCase().startsWith("v");
            }
        });

    }

    @Test
    public void testStreamFromFile() throws Exception {

        for(File file : allSampleTagFiles) {
            System.out.println("Stream testing " + file.getName());
            Mp3Stream mp3Stream = new Mp3Stream(new FileInputStream(file), file.length());
            Assert.assertNotNull(mp3Stream);
            assert(fileAndStreamEquals(file.getAbsolutePath(), mp3Stream));
        }

    }

    @Test
    public void testStreamFromByteArray() throws Exception {
        File file = new File("target/test-classes/v23tag.mp3");
        byte[] barr = readFile(file);

        Mp3Stream mp3Stream = new Mp3Stream(
                new ByteArrayInputStream(barr),
                barr.length);


    }

    public boolean fileAndStreamEquals(String absolutePath, Mp3Stream stream) throws InvalidDataException, IOException, UnsupportedTagException {

        Mp3File file = new Mp3File(absolutePath);
        if(stream.getId3v2Tag() != null) {
            if(file.getId3v2Tag() != null) {
                return file.getId3v2Tag().equals(stream.getId3v2Tag());
            } else {
                return false;
            }
        } else if (stream.getId3v1Tag() != null) {
            if(file.getId3v1Tag() != null) {
                return file.getId3v1Tag().equals(stream.getId3v1Tag());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }
}
