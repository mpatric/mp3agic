package com.mpatric.mp3agic;

import java.util.ArrayList;

public class ID3v2TXXXFrameData extends AbstractID3v2FrameData {

    protected static final String TAG = "ID3::TXXX";

    public static final String ID_FIELD = "TXXX";

    private EncodedText description;
    private EncodedText value;

    public ID3v2TXXXFrameData(boolean unsynchronisation) {
        super(unsynchronisation);
    }

    public ID3v2TXXXFrameData(boolean unsynchronisation, EncodedText description, EncodedText value) {
        super(unsynchronisation);
        this.description = description;
        this.value = value;
    }

    public ID3v2TXXXFrameData(boolean unsynchronisation, byte[] bytes) throws InvalidDataException {
        super(unsynchronisation);
        synchroniseAndUnpackFrameData(bytes);
    }

    @Override
    protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
        int marker = BufferTools.indexOfTerminatorForEncoding(bytes, 1, bytes[0]);

        description = new EncodedText(bytes[0], BufferTools.copyBuffer(bytes, 1, marker - 1));

        marker += description.getTerminator().length;

        value = new EncodedText(bytes[0], BufferTools.copyBuffer(bytes, marker, bytes.length - marker));
    }

    @Override
    protected byte[] packFrameData() {
        byte[] bytes = new byte[getLength()];
        if (value != null) bytes[0] = value.getTextEncoding();
        else bytes[0] = 0;
        int marker = 1;
        if (description != null) {
            byte[] descriptionBytes = description.toBytes(true, true);
            BufferTools.copyIntoByteBuffer(descriptionBytes, 0, descriptionBytes.length, bytes, marker);
            marker += descriptionBytes.length;
        } else {
            bytes[marker++] = 0;
        }
        if (value != null) {
            byte[] commentBytes = value.toBytes(true, false);
            BufferTools.copyIntoByteBuffer(commentBytes, 0, commentBytes.length, bytes, marker);
        }
        return bytes;
    }

    @Override
    protected int getLength() {
        int length = 1;
        if (description != null) length += description.toBytes(true, true).length;
        else length++;
        if (value != null) length += value.toBytes(true, false).length;
        return length;
    }

    public EncodedText getValue() {
        return value;
    }

    public void setValue(EncodedText value) {
        this.value = value;
    }

    public EncodedText getDescription() {
        return description;
    }

    public void setDescription(EncodedText description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        return false;
    }

    public static ArrayList<ID3v2TXXXFrameData> extractAll(ID3v2FrameSet txxx, boolean useFrameUnsynchronisation, String d) {
        if (txxx != null) {

            ArrayList<ID3v2TXXXFrameData> fields = new ArrayList<>();

            for (ID3v2Frame frame : txxx.getFrames())
                try {
                    ID3v2TXXXFrameData field = new ID3v2TXXXFrameData(useFrameUnsynchronisation, frame.getData());

                    if (d == null || field.getDescription().toString().contains(d))
                        fields.add(field);
                } catch (InvalidDataException e) {
                    // do nothing
                }

            return fields;
        }

        return null;
    }

    public static ArrayList<ID3v2TXXXFrameData> extractAll(ID3v2FrameSet txxx, boolean useFrameUnsynchronisation) {
        return extractAll(txxx, useFrameUnsynchronisation, null);
    }

    public static ID3v2TXXXFrameData extract(ID3v2FrameSet txxx, boolean useFrameUnsynchronisation, String d) {
        ArrayList<ID3v2TXXXFrameData> items = extractAll(txxx, useFrameUnsynchronisation, d);
        if (items.size() > 0)
            return items.get(0);

        return null;
    }
}
