
package com.mpatric.mp3agic;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class ID3v2ChapterTOCFrameData extends AbstractID3v2FrameData {

    protected boolean isRoot;
    protected boolean isOrdered; 
    protected String id;
    protected String[] childs;
    protected ArrayList<ID3v2Frame> subframes = new ArrayList<ID3v2Frame>();

    public ID3v2ChapterTOCFrameData(boolean unsynchronisation) {
        super(unsynchronisation);
    }

    public ID3v2ChapterTOCFrameData(boolean unsynchronisation, boolean isRoot, boolean isOrdered,
            String id, String[] childs, ArrayList<ID3v2Frame> subframes) {
        super(unsynchronisation);
        this.isRoot = isRoot;
        this.isOrdered = isOrdered;
        this.id = id;
        this.childs = childs;
        this.subframes = subframes;
    }

    public ID3v2ChapterTOCFrameData(boolean unsynchronisation, byte[] bytes)
            throws InvalidDataException {
        super(unsynchronisation);
        synchroniseAndUnpackFrameData(bytes);
    }

    protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        
        id = ByteBufferUtils.extractNullTerminatedString(bb);
        
        byte flags = bb.get();
        if((flags & 0x01) == 0x01) {
            isRoot = true;
        }
        if((flags & 0x02) == 0x02) {
            isOrdered = true;
        }
        
        int childCount = bb.get(); //TODO: 0xFF -> int = 255; byte = -128;

        childs = new String[childCount];
        
        for (int i = 0; i < childCount; i++) {
            childs[i] = ByteBufferUtils.extractNullTerminatedString(bb);
        }
        
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
    
    

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public boolean isOrdered() {
        return isOrdered;
    }

    public void setOrdered(boolean isOrdered) {
        this.isOrdered = isOrdered;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getChilds() {
        return childs;
    }

    public void setChilds(String[] childs) {
        this.childs = childs;
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
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ID3v2ChapterTOCFrameData [isRoot=");
        builder.append(isRoot);
        builder.append(", isOrdered=");
        builder.append(isOrdered);
        builder.append(", id=");
        builder.append(id);
        builder.append(", childs=");
        builder.append(Arrays.toString(childs));
        builder.append(", subframes=");
        builder.append(subframes);
        builder.append("]");
        return builder.toString();
    }
}
