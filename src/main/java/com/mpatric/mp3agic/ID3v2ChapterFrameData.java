
package com.mpatric.mp3agic;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ID3v2ChapterFrameData extends AbstractID3v2FrameData {

    protected String id;
    protected int startTime;
    protected int endTime;
    protected int startOffset;
    protected int endOffset;
    protected ArrayList<ID3v2Frame> subframes = new ArrayList<ID3v2Frame>();

    public ID3v2ChapterFrameData(boolean unsynchronisation) {
        super(unsynchronisation);
    }

    public ID3v2ChapterFrameData(boolean unsynchronisation, String id, int startTime,
            int endTime, int startOffset, int endOffset, ArrayList<ID3v2Frame> subframes) {
        super(unsynchronisation);
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.subframes = subframes;
    }

    public ID3v2ChapterFrameData(boolean unsynchronisation, byte[] bytes)
            throws InvalidDataException {
        super(unsynchronisation);
        synchroniseAndUnpackFrameData(bytes);
    }

    protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        String fromBytes = new String(bytes);
        int i = fromBytes.indexOf(0);
        if (i > 0) {
            id = fromBytes.substring(0, i);
        }

        bb.position(id.length() + 1);
        startTime = bb.getInt();
        endTime = bb.getInt();
        startOffset = bb.getInt();
        endOffset = bb.getInt();

        for (int offset = bb.position(); offset < bytes.length;) {
            ID3v2Frame frame = new ID3v2Frame(bytes, bb.position());
            offset += frame.getLength();
            subframes.add(frame);
        }

    }

    protected byte[] packFrameData() {
        // byte[] bytes = new byte[getLength()];
        // if (text != null)
        // bytes[0] = text.getTextEncoding();
        // else
        // bytes[0] = 0;
        // byte[] textBytes = text.toBytes(true, false);
        // if (textBytes.length > 0) {
        // BufferTools.copyIntoByteBuffer(textBytes, 0, textBytes.length, bytes,
        // 1);
        // }
        // return bytes;
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }

    public ArrayList<ID3v2Frame> getSubframes() {
        return subframes;
    }

    public void setSubframes(ArrayList<ID3v2Frame> subframes) {
        this.subframes = subframes;
    }

    @Override
    protected int getLength() {
        return 0;
    }

    // public boolean equals(Object obj) {
    // if (! (obj instanceof ID3v2ChapterFrameData)) return false;
    // if (! super.equals(obj)) return false;
    // ID3v2ChapterFrameData other = (ID3v2ChapterFrameData) obj;
    // if (text == null) {
    // if (other.text != null) return false;
    // } else if (other.text == null) return false;
    // else if (! text.equals(other.text)) return false;
    // return true;
    // }
}
