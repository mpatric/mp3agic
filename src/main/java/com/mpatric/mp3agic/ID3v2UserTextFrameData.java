package com.mpatric.mp3agic;


/**
 * User defined text information frame.
 * 
 * This frame is intended for one-string text information concerning the 
 * audio file in a similar way to the other "T"-frames. The frame body consists
 * of a description of the string, represented as a terminated string, followed
 * by the actual string. There may be more than one "TXXX" frame in each tag,
 * but only one with the same description.
 * 
 * <pre>
 * <Header for 'User defined text information frame', ID: "TXXX">
 * Text encoding    $xx
 * Description    <text string according to encoding> $00 (00)
 * Value    <text string according to encoding>
 * </pre>
 * 
 * @author Paul Gregoire (mondain@gmail.com)
 */
public class ID3v2UserTextFrameData extends AbstractID3v2FrameData {

    protected EncodedText text;

    protected EncodedText description;
    
    public ID3v2UserTextFrameData(boolean unsynchronisation) {
        super(unsynchronisation);
    }
    
    public ID3v2UserTextFrameData(boolean unsynchronisation, EncodedText description, EncodedText text) {
        super(unsynchronisation);
        this.description = description;
        this.text = text;
    }

    public ID3v2UserTextFrameData(boolean unsynchronisation, byte[] bytes) throws InvalidDataException {
        super(unsynchronisation);
        synchroniseAndUnpackFrameData(bytes);
    }
    
    protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
        int marker = BufferTools.indexOfTerminatorForEncoding(bytes, 1, bytes[0]);
        if (marker >= 0) {
            description = new EncodedText(bytes[0], BufferTools.copyBuffer(bytes, 1, marker - 1));
            marker += description.getTerminator().length;
        } else {
            description = new EncodedText(bytes[0], "");
            marker = 1;
        }
        text = new EncodedText(bytes[0], BufferTools.copyBuffer(bytes, 1, marker - 1));
        marker += text.getTerminator().length;
    }
    
    protected byte[] packFrameData() {
        byte[] bytes = new byte[getLength()];
        if (description != null) {
            bytes[0] = description.getTextEncoding();
        } else {
            bytes[0] = 0;
        }
        int marker = 1;
        if (description != null) {
            byte[] descriptionBytes = description.toBytes(true, true);
            BufferTools.copyIntoByteBuffer(descriptionBytes, 0, descriptionBytes.length, bytes, marker);
            marker += descriptionBytes.length;
        } else {
            bytes[marker++] = 0;
        }
        if (text != null) {
            byte[] textBytes = text.toBytes(true, true);
            BufferTools.copyIntoByteBuffer(textBytes, 0, textBytes.length, bytes, marker);
            marker += textBytes.length;
        } else {
            bytes[marker++] = 0;
        }
        return bytes;
    }
    
    protected int getLength() {
        int length = 1;
        if (description != null) length += description.toBytes(true, true).length;
        else length++;
        if (text != null) length += text.toBytes(true, true).length;
        return length;
    }

    public EncodedText getDescription() {
        return description;
    }

    public void setDescription(EncodedText description) {
        this.description = description;
    }
    
    public EncodedText getText() {
        return text;
    }

    public void setText(EncodedText text) {
        this.text = text;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
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
        ID3v2UserTextFrameData other = (ID3v2UserTextFrameData) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }
}
