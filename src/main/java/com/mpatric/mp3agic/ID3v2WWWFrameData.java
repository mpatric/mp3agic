package com.mpatric.mp3agic;

import java.io.UnsupportedEncodingException;

public class ID3v2WWWFrameData extends AbstractID3v2FrameData {

	protected String url;

	public ID3v2WWWFrameData(boolean unsynchronisation) {
		super(unsynchronisation);
	}

	public ID3v2WWWFrameData(boolean unsynchronisation, String url) {
		super(unsynchronisation);
		this.url = url;
	}

	public ID3v2WWWFrameData(boolean unsynchronisation, byte[] bytes) throws InvalidDataException {
		super(unsynchronisation);
		synchroniseAndUnpackFrameData(bytes);
	}

	@Override
	protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
		try {
			url = BufferTools.byteBufferToString(bytes, 0, bytes.length);
		} catch (UnsupportedEncodingException e) {
			url = "";
		}
	}

	@Override
	protected byte[] packFrameData() {
		byte[] bytes = new byte[getLength()];
		if (url != null && url.length() > 0) {
			try {
				BufferTools.stringIntoByteBuffer(url, 0, url.length(), bytes, 0);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return bytes;
	}

	@Override
	protected int getLength() {
		int length = 0;
		if (url != null) length = url.length();
		return length;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
