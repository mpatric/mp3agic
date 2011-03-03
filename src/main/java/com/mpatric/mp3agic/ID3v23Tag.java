package com.mpatric.mp3agic;

public class ID3v23Tag extends AbstractID3v2Tag {
	
	public static final String VERSION = "3.0";

	public ID3v23Tag() {
		super();
		version = VERSION;
	}

	public ID3v23Tag(byte[] buffer) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		super(buffer);
	}
	
	protected void unpackFlags(byte[] buffer) {
		unsynchronisation = BufferTools.checkBit(buffer[FLAGS_OFFSET], UNSYNCHRONISATION_BIT);
		extendedHeader = BufferTools.checkBit(buffer[FLAGS_OFFSET], EXTENDED_HEADER_BIT);
		experimental = BufferTools.checkBit(buffer[FLAGS_OFFSET], EXPERIMENTAL_BIT);
	}

	protected void packFlags(byte[] bytes, int offset) {
		bytes[offset + FLAGS_OFFSET] = BufferTools.setBit(bytes[offset + FLAGS_OFFSET], UNSYNCHRONISATION_BIT, unsynchronisation);
		bytes[offset + FLAGS_OFFSET] = BufferTools.setBit(bytes[offset + FLAGS_OFFSET], EXTENDED_HEADER_BIT, extendedHeader);
		bytes[offset + FLAGS_OFFSET] = BufferTools.setBit(bytes[offset + FLAGS_OFFSET], EXPERIMENTAL_BIT, experimental);
	}
}
