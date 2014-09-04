package com.mpatric.mp3agic;

import java.io.FileNotFoundException;

public interface MediaSource {
    RandomAccessMediaSource open() throws FileNotFoundException;

    long getLength();

    String getFilename();
}
