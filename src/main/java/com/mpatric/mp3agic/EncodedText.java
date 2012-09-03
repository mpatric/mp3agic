package com.mpatric.mp3agic;

import java.io.ByteArrayInputStream;

public class EncodedText {
	public final String text;
	public final Encoding encoding;
	public final boolean terminated; 

	// The prime ctor
	public EncodedText(Encoding textEncoding, String string, boolean terminated) {
		this.text = string;
		this.encoding = textEncoding;
		this.terminated = terminated;
	}

	// The prime ctor, with default of no termination 
	public EncodedText(Encoding encoding, String string) {
		this(Encoding.getDefault(), string, false);
	}
	
	// No text default ctors
	public EncodedText() {
		this(Encoding.getDefault(), "", false);
	}

	public EncodedText(boolean terminated) {
		this(Encoding.getDefault(), "", terminated);
	}
	
	// String ctors, default encoding
	public EncodedText(String string) {
		this(Encoding.getDefault(), string, false);
	}
	
	public EncodedText(String string, boolean terminated) {
		this(Encoding.getDefault(), string, terminated);
	}

	// Stream ctors, implicit encoding
	public EncodedText(byte[] data) {
		this(data, false);
	}

	public EncodedText(byte[] data, boolean terminated) {
		this(new ByteArrayInputStream(data), terminated);
	}

	// Stream ctors, implicit encoding
	public EncodedText(ByteArrayInputStream data) {
		this(data, false);
	}

	public EncodedText(ByteArrayInputStream data, boolean terminated) {
		this(Encoding.getEncoding(data.read()), data, terminated);
	}

	// Stream ctors, explicit encoding
	public EncodedText(Encoding textEncoding, ByteArrayInputStream data) {
		this(textEncoding, data, false);
	}

	public EncodedText(Encoding textEncoding, ByteArrayInputStream data, boolean terminated) {
		this(textEncoding, textEncoding.parse(data, terminated), terminated);
	}

	public String getText() {
		return text;
	}

	public Encoding getEncoding() {
		return encoding;
	}

	public boolean isEncoding() {
		return terminated;
	}

	public int getTextEncoding() {
		return encoding.ordinal();
	}

	public byte[] toBytes() {
		return encoding.encode(text);
	}

	public String toString() {
		return text;
	}

//	public boolean equals(Object obj) {
//		if (!(obj instanceof EncodedText)) return false;
//		EncodedText other = (EncodedText) obj;
//		if (other == this) return true;
//		return ((other.encoding == this.encoding) && (this.text.equals(other.text)) && ());
//	}
}	
