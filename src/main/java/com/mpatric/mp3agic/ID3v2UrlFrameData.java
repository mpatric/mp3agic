package com.mpatric.mp3agic;

import java.io.UnsupportedEncodingException;

public class ID3v2UrlFrameData extends AbstractID3v2FrameData {

	protected String url;
	protected EncodedText description;
	
	public ID3v2UrlFrameData(boolean unsynchronisation) {
		super(unsynchronisation);
	}
	
	public ID3v2UrlFrameData(boolean unsynchronisation, EncodedText description, String url) {
		super(unsynchronisation);
		this.description = description;
		this.url = url;
	}

	public ID3v2UrlFrameData(boolean unsynchronisation, byte[] bytes) throws InvalidDataException {
		super(unsynchronisation);
		synchroniseAndUnpackFrameData(bytes);
	}
	
	@Override
	protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
		int marker = BufferTools.indexOfTerminatorForEncoding(bytes, 1, bytes[0]);
		if (marker >= 0) {
			description = new EncodedText(bytes[0], BufferTools.copyBuffer(bytes, 1, marker - 1));
			marker += description.getTerminator().length;
		} else {
			description = new EncodedText(bytes[0], "");
			marker = 1;
		}
		try {
			url = BufferTools.byteBufferToString(bytes, marker, bytes.length - marker);
		} catch (UnsupportedEncodingException e) {
			url = "";
		}
	}
	
	@Override
	protected byte[] packFrameData() {
		byte[] bytes = new byte[getLength()];
		if (description != null) bytes[0] = description.getTextEncoding();
		else bytes[0] = 0;
		int marker = 1;
		if (description != null) {
			byte[] descriptionBytes = description.toBytes(true, true);
			BufferTools.copyIntoByteBuffer(descriptionBytes, 0, descriptionBytes.length, bytes, marker);
			marker += descriptionBytes.length;
		} else {
			bytes[marker++] = 0;
		}
		if (url != null && url.length() > 0) {
			try {
				BufferTools.stringIntoByteBuffer(url, 0, url.length(), bytes, marker);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return bytes;
	}
	
	@Override
	protected int getLength() {
		int length = 1;
		if (description != null) length += description.toBytes(true, true).length;
		else length++;
		if (url != null) length += url.length();
		return length;
	}

	public EncodedText getDescription() {
		return description;
	}

	public void setDescription(EncodedText description) {
		this.description = description;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		ID3v2UrlFrameData other = (ID3v2UrlFrameData) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
}
