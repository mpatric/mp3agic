package com.mpatric.mp3agic;

import java.io.IOException;
import java.io.InputStream;

/**
 * This is an input stream backed by an array that (unlike the stock ByteArrayInputStream)
 * allows the user to seek the stream at leisure.
 * <br /> <br />
 * WARNING: Not tested thoroughly, only created for the purposes of this project
 *
 * @author Germán Valencia (machinarius92@gmail.com)
 */
public class SeekerByteArrayInputStream extends InputStream {
    protected byte[] bytesStore;
    protected int cursorPosition;
    protected int lastMark;
    protected int lastReadLimit;
    protected int bytesReadSinceMark;

    public SeekerByteArrayInputStream(byte[] bytesStore) {
        super();
        this.bytesStore = bytesStore;
    }

    @Override
    public int read(byte[] destination) throws IOException {
        return read(destination, 0, destination.length);
    }

    @Override
    public int read(byte[] destination, int offset, int length) throws IOException {
        if(length == 0) {
            return 0;
        }

        if(cursorPosition >= bytesStore.length) {
            return -1;
        }

        int read;
        if(cursorPosition + length > bytesStore.length) {
            read = bytesStore.length - cursorPosition;
        } else {
            read = length;
        }

        System.arraycopy(bytesStore, cursorPosition, destination, offset, read);
        cursorPosition += read;
        updateMark(read);

        return read;
    }

    @Override
    public long skip(long length) throws IOException {
        if(cursorPosition + length > bytesStore.length) {
            throw new IllegalArgumentException("Skipping " + length + " bytes would skip past the limit");
        }

        cursorPosition += length;
        return length;
    }

    @Override
    public int available() throws IOException {
        return bytesStore.length - cursorPosition;
    }

    @Override
    public synchronized void mark(int readLimit) {
        lastMark = cursorPosition;
        lastReadLimit = readLimit;
    }

    @Override
    public synchronized void reset() throws IOException {
        cursorPosition = lastMark;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    public void rewindToZero() {
        cursorPosition = 0;
    }

    public int getContentLength() {
        return bytesStore.length;
    }

    public void seek(int targetLocation) {
        if(targetLocation < 0 || targetLocation > bytesStore.length) {
            throw new IllegalArgumentException("Seeking to " + targetLocation + " would seek behind or past the stream");
        }

        resetMark();
        cursorPosition = targetLocation;
    }

    private void updateMark(int readBytes) {
        bytesReadSinceMark += readBytes;

        if(bytesReadSinceMark > lastReadLimit) {
            resetMark();
        }
    }

    private void resetMark() {
        lastMark = 0;
        lastReadLimit = 0;
        bytesReadSinceMark = 0;
    }
}
