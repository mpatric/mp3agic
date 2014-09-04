package com.mpatric.mp3agic;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class RandomAccessMediaByteArray implements RandomAccessMediaSource {
    private final ByteArrayInputStream bais;

    public RandomAccessMediaByteArray(byte[] mediaContent) {
        this.bais = new ByteArrayInputStream(mediaContent);
    }

    @Override
    public void close() throws IOException {
        bais.close();
    }

    @Override
    public void seek(long pos) throws IOException {
        bais.reset();
        bais.skip(pos);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return bais.read(b, off, len);
    }
}
