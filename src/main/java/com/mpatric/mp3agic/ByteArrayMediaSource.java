package com.mpatric.mp3agic;

import java.io.FileNotFoundException;

public class ByteArrayMediaSource extends AbstractMediaSource {

    private final byte[] mediaContent;

    public ByteArrayMediaSource(byte[] mediaContent) {
        this.mediaContent = mediaContent;
    }

    @Override
    public RandomAccessMediaSource open() throws FileNotFoundException {
        return new RandomAccessMediaByteArray(mediaContent);
    }

    @Override
    public long getLength() {
        return mediaContent.length;
    }

    @Override
    public String getFilename() {
        return null;
    }
}
