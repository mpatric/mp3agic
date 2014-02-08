package com.mpatric.mp3agic;

import java.io.*;
import java.util.Vector;


public final class RandomAccessStream extends InputStream {
    public static final int BLOCK_BASE = 8;
    private final int BLOCK_SIZE;
    private final int BLOCK_MASK;
    private final int BLOCK_SHIFT;

    private InputStream src;

    private long pointer;
    private Vector data;
    private long length;
    private boolean foundEOS;

    public RandomAccessStream(InputStream inputstream) {
        this(inputstream, 8192);
    }

    /**
     * Constructs a RandomAccessStream from an InputStream. Seeking
     * backwards is supported using a memory cache.
     *
     * @param inputstream
     * @param blockFactor Mostly 8192 for block size of 65536
     */
    public RandomAccessStream(InputStream inputstream, int blockFactor) {

        BLOCK_SIZE = BLOCK_BASE * blockFactor;
        BLOCK_MASK = BLOCK_SIZE - 1;
        BLOCK_SHIFT = BLOCK_BASE * 2;

        pointer = 0L;
        data = new Vector();
        length = 0L;
        foundEOS = false;
        src = inputstream;
    }

    public int getFilePointer() throws IOException {
        return (int) pointer;
    }

    public long getLongFilePointer() throws IOException {
        return pointer;
    }

    public int read() throws IOException {
        long l = pointer + 1L;
        long l1 = readUntil(l);
        if (l1 >= l) {
            byte abyte0[] = (byte[]) data.elementAt((int) (pointer >> BLOCK_SHIFT));
            return abyte0[(int) (pointer++ & BLOCK_MASK)] & 0xff;
        } else
            return -1;
    }

    public int read(byte[] bytes, int off, int len) throws IOException {
        if (bytes == null)
            throw new NullPointerException();
        if (off < 0 || len < 0 || off + len > bytes.length)
            throw new IndexOutOfBoundsException();
        if (len == 0)
            return 0;
        long l = readUntil(pointer + len);
        if (l <= pointer)
            return -1;
        else {
            byte abyte1[] = (byte[]) data.elementAt((int) (pointer >> BLOCK_SHIFT));
            int k = Math.min(len, BLOCK_SIZE - (int) (pointer & BLOCK_MASK));
            System.arraycopy(abyte1, (int) (pointer & BLOCK_MASK), bytes, off, k);
            pointer += k;
            return k;
        }
    }

    public final void readFully(byte[] bytes) throws IOException {
        readFully(bytes, bytes.length);
    }

    public final void readFully(byte[] bytes, int len) throws IOException {
        int read = 0;
        do {
            int l = read(bytes, read, len - read);
            if (l < 0) break;
            read += l;
        } while (read < len);
    }

    private long readUntil(long l) throws IOException {
        if (l < length)
            return l;
        if (foundEOS)
            return length;
        int i = (int) (l >> BLOCK_SHIFT);
        int j = (int) (length >> BLOCK_SHIFT);
        for (int k = j; k <= i; k++) {
            byte abyte0[] = new byte[BLOCK_SIZE];
            data.addElement(abyte0);
            int i1 = BLOCK_SIZE;
            int j1 = 0;
            while (i1 > 0) {
                int k1 = src.read(abyte0, j1, i1);
                if (k1 == -1) {
                    foundEOS = true;
                    return length;
                }
                j1 += k1;
                i1 -= k1;
                length += k1;
            }
        }
        return length;
    }

    public void seek(long loc) throws IOException {
        if (loc < 0L)
            pointer = 0L;
        else
            pointer = loc;
    }

    public void seek(int loc) throws IOException {
        long lloc = ((long) loc) & 0xffffffffL;
        if (lloc < 0L)
            pointer = 0L;
        else
            pointer = lloc;
    }

    public final int readInt() throws IOException {
        int i = read();
        int j = read();
        int k = read();
        int l = read();
        if ((i | j | k | l) < 0)
            throw new EOFException();
        else
            return (i << 24) + (j << 16) + (k << 8) + l;
    }

    public final long readLong() throws IOException {
        return ((long) readInt() << 32) + ((long) readInt() & 0xffffffffL);
    }

    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    public final short readShort() throws IOException {
        int i = read();
        int j = read();
        if ((i | j) < 0)
            throw new EOFException();
        else
            return (short) ((i << 8) + j);
    }

    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public void close() throws IOException {

        data.removeAllElements();
        src.close();

    }


}