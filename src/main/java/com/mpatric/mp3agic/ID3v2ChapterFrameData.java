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
            int endTime, int startOffset, int endOffset) {
        super(unsynchronisation);
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    public ID3v2ChapterFrameData(boolean unsynchronisation, byte[] bytes)
            throws InvalidDataException {
        super(unsynchronisation);
        synchroniseAndUnpackFrameData(bytes);
    }

    protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        
        id = ByteBufferUtils.extractNullTerminatedString(bb);
        
        bb.position(id.length() + 1);
        startTime = bb.getInt();
        endTime = bb.getInt();
        startOffset = bb.getInt();
        endOffset = bb.getInt();

        for (int offset = bb.position(); offset < bytes.length;) {
            ID3v2Frame frame = new ID3v2Frame(bytes, offset);
            offset += frame.getLength();
            subframes.add(frame);
        }

    }

    public void addSubframe(String id, AbstractID3v2FrameData frame) {
        subframes.add(new ID3v2Frame(id, frame.toBytes()));
    }

    protected byte[] packFrameData() {
        ByteBuffer bb = ByteBuffer.allocate(getLength());
        bb.put(id.getBytes());
        bb.put((byte) 0);

        bb.putInt(startTime);
        bb.putInt(endTime);
        bb.putInt(startOffset);
        bb.putInt(endOffset);

        for (ID3v2Frame frame : subframes) {
            try {
                bb.put(frame.toBytes());
            } catch (NotSupportedException e) {
                e.printStackTrace();
            }
        }
        return bb.array();
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
        int length = 1;
        length += 16;
        if (id != null)
            length += id.length();
        if (subframes != null) {
            for (ID3v2Frame frame : subframes) {
                length += frame.getLength();
            }
        }
        return length;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ID3v2ChapterFrameData [id=");
        builder.append(id);
        builder.append(", startTime=");
        builder.append(startTime);
        builder.append(", endTime=");
        builder.append(endTime);
        builder.append(", startOffset=");
        builder.append(startOffset);
        builder.append(", endOffset=");
        builder.append(endOffset);
        builder.append(", subframes=");
        builder.append(subframes);
        builder.append("]");
        return builder.toString();
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		ID3v2ChapterFrameData other = (ID3v2ChapterFrameData) obj;
		if (endOffset != other.endOffset) return false;
		if (endTime != other.endTime) return false;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (startOffset != other.startOffset) return false;
		if (startTime != other.startTime) return false;
		if (subframes == null) {
			if (other.subframes != null) return false;
		} else if (!subframes.equals(other.subframes)) return false;
		return true;
	}
}
