package com.mpatric.mp3agic;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;

public class EncodedText {
	
	public static final byte TEXT_ENCODING_ISO_8859_1 = 0;
	public static final byte TEXT_ENCODING_UTF_16 = 1;
	public static final byte TEXT_ENCODING_UTF_16BE = 2;
	public static final byte TEXT_ENCODING_UTF_8 = 3;
	
	public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
	public static final String CHARSET_UTF_16 = "UTF-16";
	public static final String CHARSET_UTF_16BE = "UTF-16BE";
	public static final String CHARSET_UTF_8 = "UTF-8";
	
	private static final String[] characterSets = {
		CHARSET_ISO_8859_1,
		CHARSET_UTF_16,
		CHARSET_UTF_16BE,
		CHARSET_UTF_8
	};
	
	private byte[] value;
	private byte textEncoding;
	
	public EncodedText(byte textEncoding, byte[] value) {
		this.textEncoding = textEncoding;
		this.value = value;
	}
	
	public EncodedText(byte textEncoding, String ascii) {
		this.textEncoding = textEncoding;
		value = stringToUnicodeBytes(ascii, characterSetForTextEncoding(textEncoding));
	}
	
	private String characterSetForTextEncoding(byte textEncoding) {
		try {
			return characterSets[textEncoding];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Invalid text encoding " + textEncoding);
		}
	}

	public byte getTextEncoding() {
		return textEncoding;
	}

	public void setTextEncoding(byte textEncoding) {
		this.textEncoding = textEncoding;
	}

	public byte[] toBytes() {
		return value;
	}
	
	public String toString() {
		return unicodeBytesToString(value, characterSetForTextEncoding(textEncoding));
	}

	public String getCharacterSet() {
		return characterSetForTextEncoding(textEncoding);
	}
	
	public boolean equals(Object obj) {
		if (! (obj instanceof EncodedText)) return false;
		if (super.equals(obj)) return true;
		EncodedText other = (EncodedText) obj;
		if (textEncoding != other.textEncoding) return false;
		if (! Arrays.equals(value, other.value)) return false;
		return true;
	}
	
	public static String unicodeBytesToString(byte[] bytes, String characterSet) {
		Charset charset = Charset.forName(characterSet);
	    CharsetDecoder decoder = charset.newDecoder();
	    try {
	        CharBuffer cbuf = decoder.decode(ByteBuffer.wrap(bytes));
	        String s = cbuf.toString();
	        int length = s.indexOf(0);
			if (length == -1) return s;
			return s.substring(0, length);
	    } catch (CharacterCodingException e) {
	    	return null;
	    }
	}
	
	public static byte[] stringToUnicodeBytes(String s, String characterSet) {
		Charset charset = Charset.forName(characterSet);
	    CharsetEncoder encoder = charset.newEncoder();
    	ByteBuffer byteBuffer;
		try {
			byteBuffer = encoder.encode(CharBuffer.wrap(s));
			return BufferTools.copyBuffer(byteBuffer.array(), 0, byteBuffer.limit());
		} catch (CharacterCodingException e) {
			return null;
		}
	}
}
