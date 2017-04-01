package com.mpatric.mp3agic;

import java.io.UnsupportedEncodingException;
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

	@Override
	protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
		int marker = BufferTools.indexOfTerminator(bytes, 1, 1);
		if (marker >= 0) {
			try {
				mimeType = BufferTools.byteBufferToString(bytes, 1, marker - 1);
			} catch (UnsupportedEncodingException e) {
				mimeType = "image/unknown";
			}
		} else {
			mimeType = "image/unknown";
		}
		pictureType = bytes[marker + 1];
		marker += 2;
		int marker2 = BufferTools.indexOfTerminatorForEncoding(bytes, marker, bytes[0]);
		if (marker2 >= 0) {
			description = new EncodedText(bytes[0], BufferTools.copyBuffer(bytes, marker, marker2 - marker));
			marker2 += description.getTerminator().length;
		} else {
			description = new EncodedText(bytes[0], "");
			marker2 = marker;
		}
		imageData = BufferTools.copyBuffer(bytes, marker2, bytes.length - marker2);
	}

	@Override
	protected byte[] packFrameData() {
		byte[] bytes = new byte[getLength()];
		if (description != null) bytes[0] = description.getTextEncoding();
		else bytes[0] = 0;
		int mimeTypeLength = 0;
		if (mimeType != null && mimeType.length() > 0) {
			mimeTypeLength = mimeType.length();
			try {
				BufferTools.stringIntoByteBuffer(mimeType, 0, mimeTypeLength, bytes, 1);
			} catch (UnsupportedEncodingException e) {
			}
		}
		int marker = mimeTypeLength + 1;
		bytes[marker++] = 0;
		bytes[marker++] = pictureType;
		if (description != null && description.toBytes().length > 0) {
			byte[] descriptionBytes = description.toBytes(true, true);
			BufferTools.copyIntoByteBuffer(descriptionBytes, 0, descriptionBytes.length, bytes, marker);
			marker += descriptionBytes.length;
		} else {
			bytes[marker++] = 0;
		}
		if (imageData != null && imageData.length > 0) {
			BufferTools.copyIntoByteBuffer(imageData, 0, imageData.length, bytes, marker);
		}
		return bytes;
	}

	@Override
	protected int getLength() {
		int length = 3;
		if (mimeType != null) length += mimeType.length();
		if (description != null) length += description.toBytes(true, true).length;
		else length++;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + Arrays.hashCode(imageData);
		result = prime * result
				+ ((mimeType == null) ? 0 : mimeType.hashCode());
		result = prime * result + pictureType;
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
		ID3v2PictureFrameData other = (ID3v2PictureFrameData) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (!Arrays.equals(imageData, other.imageData))
			return false;
		if (mimeType == null) {
			if (other.mimeType != null)
				return false;
		} else if (!mimeType.equals(other.mimeType))
			return false;
		if (pictureType != other.pictureType)
			return false;
		return true;
	}
}
