package com.mpatric.mp3agic;

public class ID3v24Frame extends ID3v2Frame {

	public ID3v24Frame(byte[] buffer, int offset) throws InvalidDataException {
		super(buffer, offset);
	}
	
	public ID3v24Frame(String id, byte[] data) {
		super(id, data);
	}

	protected void unpackDataLength(byte[] buffer, int offset) {
		dataLength = BufferTools.unpackSynchsafeInteger(buffer[offset + DATA_LENGTH_OFFSET], buffer[offset + DATA_LENGTH_OFFSET + 1], buffer[offset + DATA_LENGTH_OFFSET + 2], buffer[offset + DATA_LENGTH_OFFSET + 3]);
	}
	
	protected byte[] packDataLength() {
		return BufferTools.packSynchsafeInteger(dataLength);
	}
	
	public boolean equals(Object obj) {
		if (! (obj instanceof ID3v24Frame)) return false;
		return super.equals(obj);
	}
}
