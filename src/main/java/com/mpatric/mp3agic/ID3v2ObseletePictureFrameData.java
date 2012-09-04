package com.mpatric.mp3agic;

import java.io.ByteArrayInputStream;


public class ID3v2ObseletePictureFrameData extends ID3v2PictureFrameData {

	public ID3v2ObseletePictureFrameData(boolean unsynchronisation) {
		super(unsynchronisation);
	}
	
	public ID3v2ObseletePictureFrameData(boolean unsynchronisation, Encoding encoding, String mimeType, byte pictureType, String description, byte[] imageData) {
		super(unsynchronisation, encoding, mimeType, pictureType, description, imageData);
	}

	public ID3v2ObseletePictureFrameData(boolean unsynchronisation, byte[] bytes) throws InvalidDataException {
		super(unsynchronisation, bytes);
	}

	@Override
	protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
		ByteArrayInputStream data = new ByteArrayInputStream(bytes);
		encoding = Encoding.getEncoding(data.read());
		mimeType = "image/" + BufferTools.byteBufferToString(data, 3).toLowerCase();
		pictureType = (byte) data.read();
		description = encoding.parse(data, true);
		imageData = BufferTools.streamIntoByteBuffer(data);
	}
}
