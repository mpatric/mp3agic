package com.mpatric.mp3agic;

import java.util.Arrays;

public class ID3v2PictureFrameData extends AbstractID3v2FrameData {

	protected String mimeType;
	protected byte pictureType;
	protected EncodedText description;
	protected byte[] imageData;

	public ID3v2PictureFrameData(boolean unsynchronisation) {
		super(unsynchronisation);
	}
	
	public ID3v2PictureFrameData(boolean unsynchronisation, String mimeType, byte pictureType, EncodedText description, byte[] imageData) {
		super(unsynchronisation);
		this.mimeType = mimeType;
		this.pictureType = pictureType;
		this.description = description;
		this.imageData = imageData;
	}

	public ID3v2PictureFrameData(boolean unsynchronisation, byte[] bytes) throws InvalidDataException {
		super(unsynchronisation);
		synchroniseAndUnpackFrameData(bytes);
	}
	
	protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
		int marker;
		for (marker = 1; marker < bytes.length; marker++) {
			if (bytes[marker] == 0) break;
		}
		mimeType = BufferTools.byteBufferToString(bytes, 1, marker - 1);
		pictureType = bytes[marker + 1];
		marker += 2;
		int marker2;
		for (marker2 = marker; marker2 < bytes.length; marker2++) {
			if (bytes[marker2] == 0) break;
		}
		description = new EncodedText(bytes[0], BufferTools.copyBuffer(bytes, marker, marker2 - marker));
		imageData = BufferTools.copyBuffer(bytes, marker2 + 1, bytes.length - marker2 - 1);
	}
	
	protected byte[] packFrameData() {
		byte[] bytes = new byte[getLength()];
		if (description != null) bytes[0] = description.getTextEncoding();
		else bytes[0] = 0;
		int mimeTypeLength = 0;
		if (mimeType != null && mimeType.length() > 0) {
			mimeTypeLength = mimeType.length();
			BufferTools.stringIntoByteBuffer(mimeType, 0, mimeTypeLength, bytes, 1);
		}
		bytes[mimeTypeLength + 1] = 0;
		bytes[mimeTypeLength + 2] = pictureType;
		int descriptionLength = 0; 
		if (description != null && description.toBytes().length > 0) {
			descriptionLength = description.toBytes().length;
			BufferTools.copyIntoByteBuffer(description.toBytes(), 0, descriptionLength, bytes, mimeTypeLength + 3);
		}
		bytes[mimeTypeLength + descriptionLength + 3] = 0;
		if (imageData != null && imageData.length > 0) {
			BufferTools.copyIntoByteBuffer(imageData, 0, imageData.length, bytes, mimeTypeLength + descriptionLength + 4);
		}
		return bytes;
	}

	protected int getLength() {
		int length = 4;
		if (mimeType != null) length += mimeType.length();
		if (description != null) length += description.toBytes().length;
		if (imageData != null) length += imageData.length;
		return length;
	}
	
	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public byte getPictureType() {
		return pictureType;
	}

	public void setPictureType(byte pictureType) {
		this.pictureType = pictureType;
	}

	public EncodedText getDescription() {
		return description;
	}
	
	public void setDescription(EncodedText description) {
		this.description = description;
	}
	
	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
	
	public boolean equals(Object obj) {
		if (! (obj instanceof ID3v2PictureFrameData)) return false;
		if (! super.equals(obj)) return false;
		ID3v2PictureFrameData other = (ID3v2PictureFrameData) obj;
		if (pictureType != other.pictureType) return false;
		if (mimeType == null) {
			if (other.mimeType != null) return false;
		} else if (other.mimeType == null) return false;
		else if (! mimeType.equals(other.mimeType)) return false;
		if (description == null) {
			if (other.description != null) return false;
		} else if (other.description == null) return false;
		else if (! description.equals(other.description)) return false;
		if (imageData == null) {
			if (other.imageData != null) return false;
		} else if (other.imageData == null) return false;
		else if (! Arrays.equals(imageData, other.imageData)) return false;
		return true;
	}
}
