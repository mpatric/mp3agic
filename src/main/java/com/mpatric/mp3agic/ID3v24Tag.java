package com.mpatric.mp3agic;

public class ID3v24Tag extends AbstractID3v2Tag {

	public static final String VERSION = "4.0";

	public ID3v24Tag() {
		super();
		version = VERSION;
	}

	public ID3v24Tag(byte[] buffer) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		super(buffer);
	}
	
	protected void unpackFlags(byte[] buffer) {
		unsynchronisation = BufferTools.checkBit(buffer[FLAGS_OFFSET], UNSYNCHRONISATION_BIT);
		extendedHeader = BufferTools.checkBit(buffer[FLAGS_OFFSET], EXTENDED_HEADER_BIT);
		experimental = BufferTools.checkBit(buffer[FLAGS_OFFSET], EXPERIMENTAL_BIT);
		footer = BufferTools.checkBit(buffer[FLAGS_OFFSET], FOOTER_BIT);
	}
	
	protected void packFlags(byte[] bytes, int offset) {
		bytes[offset + FLAGS_OFFSET] = BufferTools.setBit(bytes[offset + FLAGS_OFFSET], UNSYNCHRONISATION_BIT, unsynchronisation);
		bytes[offset + FLAGS_OFFSET] = BufferTools.setBit(bytes[offset + FLAGS_OFFSET], EXTENDED_HEADER_BIT, extendedHeader);
		bytes[offset + FLAGS_OFFSET] = BufferTools.setBit(bytes[offset + FLAGS_OFFSET], EXPERIMENTAL_BIT, experimental);		
		bytes[offset + FLAGS_OFFSET] = BufferTools.setBit(bytes[offset + FLAGS_OFFSET], FOOTER_BIT, footer);
	}
	
	protected boolean useFrameUnsynchronisation() {
		return unsynchronisation;
	}
	
	protected ID3v2Frame createFrame(byte[] buffer, int currentOffset) throws InvalidDataException {
		return new ID3v24Frame(buffer, currentOffset);
	}
	
	protected ID3v2Frame createFrame(String id, byte[] data) {
		return new ID3v24Frame(id, data);
	}
}
