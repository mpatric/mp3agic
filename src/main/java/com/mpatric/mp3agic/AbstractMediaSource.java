package com.mpatric.mp3agic;

import java.io.FileNotFoundException;

public abstract class AbstractMediaSource implements MediaSource {

    protected String filename = "undefined";

    @Override
    public abstract RandomAccessMediaSource open() throws FileNotFoundException;

    @Override
    public abstract long getLength();

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public void setFilename(String filename) {
        this.filename = filename;
    }

}
