package com.mpatric.mp3agic;

import java.io.ByteArrayInputStream;

public class ID3v2ObseletePictureFrameData extends ID3v2PictureFrameData {

	public ID3v2ObseletePictureFrameData(boolean unsynchronisation) {
		super(unsynchronisation);
	}
	
	public ID3v2ObseletePictureFrameData(boolean unsynchronisation, String mimeType, byte pictureType, EncodedText description, byte[] imageData) {
		super(unsynchronisation, mimeType, pictureType, description, imageData);
	}

	public ID3v2ObseletePictureFrameData(boolean unsynchronisation, byte[] bytes) throws InvalidDataException {
		super(unsynchronisation, bytes);
	}

	@Override
	protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
		ByteArrayInputStream data = new ByteArrayInputStream(bytes);
		Encoding e = Encoding.getEncoding(data.read());

		mimeType = "image/" + BufferTools.byteBufferToString(data, 3).toLowerCase();
		pictureType = (byte) data.read();
		description = new EncodedText(e, data);
		imageData = BufferTools.streamIntoByteBuffer(data);
	}
}
