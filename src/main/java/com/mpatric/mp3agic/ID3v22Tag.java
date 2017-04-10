package com.mpatric.mp3agic;

public class ID3v22Tag extends AbstractID3v2Tag {

	public static final String VERSION = "2.0";

	public ID3v22Tag() {
		super();
		version = VERSION;
	}

	public ID3v22Tag(byte[] buffer) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		super(buffer);
	}

	public ID3v22Tag(byte[] buffer, boolean obseleteFormat) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		super(buffer, obseleteFormat);
	}

	@Override
	protected void unpackFlags(byte[] bytes) {
		unsynchronisation = BufferTools.checkBit(bytes[FLAGS_OFFSET], UNSYNCHRONISATION_BIT);
		compression = BufferTools.checkBit(bytes[FLAGS_OFFSET], COMPRESSION_BIT);
	}

	@Override
	protected void packFlags(byte[] bytes, int offset) {
		bytes[offset + FLAGS_OFFSET] = BufferTools.setBit(bytes[offset + FLAGS_OFFSET], UNSYNCHRONISATION_BIT, unsynchronisation);
		bytes[offset + FLAGS_OFFSET] = BufferTools.setBit(bytes[offset + FLAGS_OFFSET], COMPRESSION_BIT, compression);
	}
}
