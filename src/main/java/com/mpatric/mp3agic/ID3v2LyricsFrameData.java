package com.mpatric.mp3agic;

import java.io.UnsupportedEncodingException;

public class ID3v2LyricsFrameData extends AbstractID3v2FrameData {

	private static final String DEFAULT_LANGUAGE = "eng";
	
	private String language;
	private EncodedText description;
	private EncodedText lyrics;

	public ID3v2LyricsFrameData(boolean unsynchronisation) {
		super(unsynchronisation);
	}
	
	public ID3v2LyricsFrameData(boolean unsynchronisation, String language, EncodedText description, EncodedText lyrics) {
		super(unsynchronisation);
		this.language = language;
		this.description = description;
		this.lyrics = lyrics;
	}

	public ID3v2LyricsFrameData(boolean unsynchronisation, byte[] bytes) throws InvalidDataException {
		super(unsynchronisation);
		synchroniseAndUnpackFrameData(bytes);
	}
	
	protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
		try {
			language = BufferTools.byteBufferToString(bytes, 1, 3);
		} catch (UnsupportedEncodingException e) {
			language = "";
		}
		int marker = BufferTools.indexOfTerminatorForEncoding(bytes, 4, bytes[0]);
		if (marker >= 4) {
			description = new EncodedText(bytes[0], BufferTools.copyBuffer(bytes, 4, marker - 4));
			marker += description.getTerminator().length;
		} else {
			description = new EncodedText(bytes[0], "");
			marker = 4;
		}
		lyrics = new EncodedText(bytes[0], BufferTools.copyBuffer(bytes, marker, bytes.length - marker));
	}

	protected byte[] packFrameData() {
		byte[] bytes = new byte[getLength()];
		if (lyrics != null) bytes[0] = lyrics.getTextEncoding();
		else bytes[0] = 0;
		String langPadded;
		if (language == null) {
			langPadded = DEFAULT_LANGUAGE;
		} else if (language.length() > 3) {
			langPadded = language.substring(0, 3);
		} else {
			langPadded = BufferTools.padStringRight(language, 3, '\00');
		}
		try {
			BufferTools.stringIntoByteBuffer(langPadded, 0, 3, bytes, 1);
		} catch (UnsupportedEncodingException e) {
		}
		int marker = 4;
		if (description != null) {
			byte[] descriptionBytes = description.toBytes(true, true);
			BufferTools.copyIntoByteBuffer(descriptionBytes, 0, descriptionBytes.length, bytes, marker);
			marker += descriptionBytes.length;
		} else {
			bytes[marker++] = 0;
		}
		if (lyrics != null) {
			byte[] commentBytes = lyrics.toBytes(true, false);
			BufferTools.copyIntoByteBuffer(commentBytes, 0, commentBytes.length, bytes, marker);
		}
		return bytes;
	}

	protected int getLength() {
		int length = 4;
		if (description != null) length += description.toBytes(true, true).length;
		else length++;
		if (lyrics != null) length += lyrics.toBytes(true, false).length;
		return length;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	public EncodedText getLyrics() {
		return lyrics;
	}

	public void setComment(EncodedText comment) {
		this.lyrics = comment;
	}

	public EncodedText getDescription() {
		return description;
	}

	public void setDescription(EncodedText description) {
		this.description = description;
	}
	
	public boolean equals(Object obj) {
		if (! (obj instanceof ID3v2LyricsFrameData)) return false;
		if (! super.equals(obj)) return false;
		ID3v2LyricsFrameData other = (ID3v2LyricsFrameData) obj;
		if (language == null) {
			if (other.language != null) return false;
		} else if (other.language == null) return false;
		else if (! language.equals(other.language)) return false;
		if (description == null) {
			if (other.description != null) return false;
		} else if (other.description == null) return false;
		else if (! description.equals(other.description)) return false;
		if (lyrics == null) {
			if (other.lyrics != null) return false;
		} else if (other.lyrics == null) return false;
		else if (! lyrics.equals(other.lyrics)) return false;
		return true;
	}
}
