package com.mpatric.mp3agic;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import com.google.common.base.Charsets;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

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
		ByteArrayInputStream data = new ByteArrayInputStream(bytes);
		Encoding enc = Encoding.getEncoding(data.read());
		mimeType = BufferTools.streamIntoTerminatedString(data);
		pictureType = (byte) data.read();
		description = new EncodedText(enc, data, true);
		imageData = BufferTools.streamIntoByteBuffer(data);
	}

	protected byte[] packFrameData() {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		Encoding encoding = Encoding.getDefault();
		if (description != null) {
			encoding = description.getEncoding();
		}
		output.write(encoding.ordinal());
		output.write(mimeType.getBytes(Charsets.ISO_8859_1));
		output.write(0);
		output.write(pictureType);
		if (description != null && description.toBytes().length > 0) {
			output.write(description.toBytes());
		} else {
			output.write(0);
		}
		if (imageData != null && imageData.length > 0) {
			output.write(imageData);
		}
		return output.toByteArray();
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
