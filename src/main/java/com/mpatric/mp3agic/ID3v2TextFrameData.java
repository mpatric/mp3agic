package com.mpatric.mp3agic;

import com.mpatric.mp3agic.annotations.FrameMember;


public class ID3v2TextFrameData extends AbstractID3v2FrameData {
	
	@FrameMember(ordinal = 0)
	protected Encoding encoding;

	@FrameMember(ordinal = 1, encoded = true)
	protected String text;
	
	public ID3v2TextFrameData(boolean unsynchronisation) {
		super(unsynchronisation);
	}
	
	public ID3v2TextFrameData(boolean unsynchronisation, String text) {
		this(unsynchronisation, Encoding.getDefault(), text);
	}
	
	public ID3v2TextFrameData(boolean unsynchronisation, Encoding encoding, String text) {
		super(unsynchronisation);
		this.text = text;
		this.encoding = encoding;
	}
	
	public ID3v2TextFrameData(boolean unsynchronisation, byte[] bytes) throws InvalidDataException {
		super(unsynchronisation);
		synchroniseAndUnpackFrameData(bytes);
	}

//	protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
//		text = new EncodedText(bytes);
//	}
//	
//	protected byte[] packFrameData() {
//		ByteArrayDataOutput out = ByteStreams.newDataOutput();
//		if (null != text) {
//			out.write(text.getTextEncoding());
//			out.write(text.toBytes());
//		} else {
//			out.write(0);
//		}
//		return out.toByteArray();
//	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public boolean equals(Object obj) {
		if (! (obj instanceof ID3v2TextFrameData)) return false;
		if (! super.equals(obj)) return false;
		ID3v2TextFrameData other = (ID3v2TextFrameData) obj;
		if (text == null) {
			if (other.text != null) return false;
		} else if (other.text == null) return false;
		else if (! text.equals(other.text)) return false;
		return true;
	}
}
