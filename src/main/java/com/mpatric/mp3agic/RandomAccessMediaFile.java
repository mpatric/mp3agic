package com.mpatric.mp3agic;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class RandomAccessMediaFile extends RandomAccessFile implements RandomAccessMediaSource {
    public RandomAccessMediaFile(String filename, String mode) throws FileNotFoundException {
        super(filename, mode);
    }
}
