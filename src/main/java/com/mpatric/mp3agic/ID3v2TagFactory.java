package com.mpatric.mp3agic;

public class ID3v2TagFactory {

	public static AbstractID3v2Tag createTag(byte[] bytes) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		sanityCheckTag(bytes);
		int majorVersion = bytes[AbstractID3v2Tag.MAJOR_VERSION_OFFSET];
		switch (majorVersion) {
			case 2:
				return createID3v22Tag(bytes);
			case 3:
				return new ID3v23Tag(bytes);
			case 4:
				return new ID3v24Tag(bytes);
		}
		throw new UnsupportedTagException("Tag version not supported");
	}

	private static AbstractID3v2Tag createID3v22Tag(byte[] bytes) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
		ID3v22Tag tag = new ID3v22Tag(bytes);
		if (tag.getFrameSets().isEmpty()) {
			tag = new ID3v22Tag(bytes, true);
		}
		return tag;
	}

	public static void sanityCheckTag(byte[] bytes) throws NoSuchTagException, UnsupportedTagException {
		if (bytes.length < AbstractID3v2Tag.HEADER_LENGTH) {
			throw new NoSuchTagException("Buffer too short");
		}
		if (!AbstractID3v2Tag.TAG.equals(BufferTools.byteBufferToStringIgnoringEncodingIssues(bytes, 0, AbstractID3v2Tag.TAG.length()))) {
			throw new NoSuchTagException();
		}
		int majorVersion = bytes[AbstractID3v2Tag.MAJOR_VERSION_OFFSET];
		if (majorVersion != 2 && majorVersion != 3 && majorVersion != 4) {
			int minorVersion = bytes[AbstractID3v2Tag.MINOR_VERSION_OFFSET];
			throw new UnsupportedTagException("Unsupported version 2." + majorVersion + "." + minorVersion);
		}
	}
}
