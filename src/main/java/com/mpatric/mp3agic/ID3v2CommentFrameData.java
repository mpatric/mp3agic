package com.mpatric.mp3agic;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import com.google.common.base.Charsets;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

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
		this.language = language;
		this.description = description;
		this.comment = comment;
	}

	public ID3v2CommentFrameData(boolean unsynchronisation, byte[] bytes) throws InvalidDataException {
		super(unsynchronisation);
		synchroniseAndUnpackFrameData(bytes);
	}

	protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
		language = BufferTools.byteBufferToString(bytes, 1, 3);

		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		Encoding enc = Encoding.getEncoding(is.read());
		is.read(); is.read(); is.read();

		description = new EncodedText(enc, is, true);
		comment = new EncodedText(enc, is);
	}

	protected byte[] packFrameData() {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		Encoding encoding = Encoding.getDefault();
		if (comment != null) {
			encoding = comment.getEncoding(); 
		} else if (description != null) {
			encoding = description.getEncoding();
		}

		output.write(encoding.ordinal());
		
		if (language == null) {
			language = DEFAULT_LANGUAGE;
		}
		
		byte[] langOutput = new byte[] { 0x0, 0x0, 0x0 };
		byte[] langEncoded = language.getBytes(Charsets.US_ASCII);
		
		for (int i = 0; i < Math.min(langOutput.length, 3); ++i) {
			langOutput[i] = langEncoded[i];
		}
		output.write(langOutput);
		
		if (description != null) {
			output.write(description.toBytes());
		} else {
			output.write(encoding.terminator);
		}

		if (comment != null) {
			output.write(comment.toBytes());
		}
		
		return output.toByteArray();
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
