package com.mpatric.mp3agic;

import com.mpatric.mp3agic.annotations.FrameMember;

public class ID3v2CommentFrameData extends AbstractID3v2EncodedFrameData {
	private static final String DEFAULT_LANGUAGE = "eng";
	
	@FrameMember(width = 3, ordinal = 1)
	private String language = DEFAULT_LANGUAGE;

	@FrameMember(ordinal = 2, encoded = true, terminated = true)
	private String description;

	@FrameMember(ordinal = 3, encoded = true)
	private String comment;

	public ID3v2CommentFrameData(boolean unsynchronisation) {
		super(unsynchronisation);
	}
	
	public ID3v2CommentFrameData(boolean unsynchronisation, String language, String description, String comment) {
		this(unsynchronisation, Encoding.getDefault(), language, description, comment);
	}

	public ID3v2CommentFrameData(boolean unsynchronisation, Encoding encoding, String language, String description, String comment) {
		super(unsynchronisation, encoding);
		this.language = language;
		this.description = description;
		this.comment = comment;
	}

	public ID3v2CommentFrameData(boolean unsynchronisation, byte[] bytes) throws InvalidDataException {
		super(unsynchronisation);
		synchroniseAndUnpackFrameData(bytes);
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean equals(Object obj) {
		if (! (obj instanceof ID3v2CommentFrameData)) return false;
		if (! super.equals(obj)) return false;
		ID3v2CommentFrameData other = (ID3v2CommentFrameData) obj;
		if (language == null) {
			if (other.language != null) return false;
		} else if (other.language == null) return false;
		else if (! language.equals(other.language)) return false;
		if (description == null) {
			if (other.description != null) return false;
		} else if (other.description == null) return false;
		else if (! description.equals(other.description)) return false;
		if (comment == null) {
			if (other.comment != null) return false;
		} else if (other.comment == null) return false;
		else if (! comment.equals(other.comment)) return false;
		return true;
	}

}
