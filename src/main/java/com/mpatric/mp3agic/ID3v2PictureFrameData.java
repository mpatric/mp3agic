package com.mpatric.mp3agic;

import java.util.Arrays;

import com.mpatric.mp3agic.annotations.FrameMember;

public class ID3v2PictureFrameData extends AbstractID3v2FrameData {

	@FrameMember(ordinal = 0)
	protected Encoding encoding;
	@FrameMember(ordinal = 1, terminated = true)
	protected String mimeType;
	@FrameMember(ordinal = 2)
	protected byte pictureType;
	@FrameMember(ordinal = 3, encoded = true, terminated = true)
	protected String description;
	@FrameMember(ordinal = 4)
	protected byte[] imageData;

	public ID3v2PictureFrameData(boolean unsynchronisation) {
		super(unsynchronisation);
	}
	
	public ID3v2PictureFrameData(boolean unsynchronisation, Encoding encoding, String mimeType, byte pictureType, String description, byte[] imageData) {
		super(unsynchronisation);
		this.mimeType = mimeType;
		this.encoding = encoding;
		this.pictureType = pictureType;
		this.description = description;
		this.imageData = imageData;
	}

	public ID3v2PictureFrameData(boolean unsynchronisation, byte[] bytes) throws InvalidDataException {
		super(unsynchronisation);
		synchroniseAndUnpackFrameData(bytes);
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

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
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

	public Encoding getEncoding() {
		return encoding;
	}

	public void setEncoding(Encoding encoding) {
		this.encoding = encoding;
	}
}
