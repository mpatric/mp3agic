package com.mpatric.mp3agic;

public class ID3v2ObseleteFrame extends ID3v2Frame {

	private static final int HEADER_LENGTH = 6;
	private static final int ID_OFFSET = 0;
	private static final int ID_LENGTH = 3;
	protected static final int DATA_LENGTH_OFFSET = 3;
	
	public ID3v2ObseleteFrame(byte[] buffer, int offset) throws InvalidDataException {
		super(buffer, offset);
	}

	public ID3v2ObseleteFrame(String id, byte[] data) {
		super(id, data);
	}
	
	protected int unpackHeader(byte[] buffer, int offset) {
		id = BufferTools.byteBufferToStringIgnoringEncodingIssues(buffer, offset + ID_OFFSET, ID_LENGTH);
		unpackDataLength(buffer, offset);
		return offset + HEADER_LENGTH;
	}

	protected void unpackDataLength(byte[] buffer, int offset) {
		dataLength = BufferTools.unpackInteger((byte)0, buffer[offset + DATA_LENGTH_OFFSET], buffer[offset + DATA_LENGTH_OFFSET + 1], buffer[offset + DATA_LENGTH_OFFSET + 2]);
	}
		
	public void packFrame(byte[] bytes, int offset) throws NotSupportedException {
		throw(new NotSupportedException("Packing Obselete frames is not supported"));
	}
	
	public int getLength() {
		return dataLength + HEADER_LENGTH;
	}
}
