package com.mpatric.mp3agic;

public class ID3v2TextFrameData extends AbstractID3v2FrameData {

	protected EncodedText text;

	public ID3v2TextFrameData(boolean unsynchronisation) {
		super(unsynchronisation);
	}

	public ID3v2TextFrameData(boolean unsynchronisation, EncodedText text) {
		super(unsynchronisation);
		this.text = text;
	}

	public ID3v2TextFrameData(boolean unsynchronisation, byte[] bytes) throws InvalidDataException {
		super(unsynchronisation);
		synchroniseAndUnpackFrameData(bytes);
	}

	@Override
	protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
		text = new EncodedText(bytes[0], BufferTools.copyBuffer(bytes, 1, bytes.length - 1));
	}

	@Override
	protected byte[] packFrameData() {
		byte[] bytes = new byte[getLength()];
		if (text != null) {
			bytes[0] = text.getTextEncoding();
			byte[] textBytes = text.toBytes(true, false);
			if (textBytes.length > 0) {
				BufferTools.copyIntoByteBuffer(textBytes, 0, textBytes.length, bytes, 1);
			}
		}
		return bytes;
	}

	@Override
	protected int getLength() {
		int length = 1;
		if (text != null) length += text.toBytes(true, false).length;
		return length;
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
		ID3v2TextFrameData other = (ID3v2TextFrameData) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
}
