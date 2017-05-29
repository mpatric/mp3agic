package com.mpatric.mp3agic;

import java.io.UnsupportedEncodingException;

public class ID3v2CommentFrameData extends AbstractID3v2FrameData {

	private static final String DEFAULT_LANGUAGE = "eng";

	private String language;
	private EncodedText description;
	private EncodedText comment;

	public ID3v2CommentFrameData(boolean unsynchronisation) {
		super(unsynchronisation);
	}

	public ID3v2CommentFrameData(boolean unsynchronisation, String language, EncodedText description, EncodedText comment) {
		super(unsynchronisation);
		if (description != null && comment != null && description.getTextEncoding() != comment.getTextEncoding()) {
			throw new IllegalArgumentException("description and comment must have same text encoding");
		}
		this.language = language;
		this.description = description;
		this.comment = comment;
	}

	public ID3v2CommentFrameData(boolean unsynchronisation, byte[] bytes) throws InvalidDataException {
		super(unsynchronisation);
		synchroniseAndUnpackFrameData(bytes);
	}

	@Override
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
		comment = new EncodedText(bytes[0], BufferTools.copyBuffer(bytes, marker, bytes.length - marker));
	}

	@Override
	protected byte[] packFrameData() {
		byte[] bytes = new byte[getLength()];
		if (comment != null) bytes[0] = comment.getTextEncoding();
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
			byte[] terminatorBytes = comment != null ? comment.getTerminator() : new byte[]{0};
			BufferTools.copyIntoByteBuffer(terminatorBytes, 0, terminatorBytes.length, bytes, marker);
			marker += terminatorBytes.length;
		}
		if (comment != null) {
			byte[] commentBytes = comment.toBytes(true, false);
			BufferTools.copyIntoByteBuffer(commentBytes, 0, commentBytes.length, bytes, marker);
		}
		return bytes;
	}

	@Override
	protected int getLength() {
		int length = 4;
		if (description != null) length += description.toBytes(true, true).length;
		else length += comment != null ? comment.getTerminator().length : 1;
		if (comment != null) length += comment.toBytes(true, false).length;
		return length;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public EncodedText getComment() {
		return comment;
	}

	public void setComment(EncodedText comment) {
		this.comment = comment;
	}

	public EncodedText getDescription() {
		return description;
	}

	public void setDescription(EncodedText description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ID3v2CommentFrameData other = (ID3v2CommentFrameData) obj;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		return true;
	}
}
