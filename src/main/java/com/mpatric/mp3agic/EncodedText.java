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
	public static final String CHARSET_UTF_16 = "UTF-16LE";
	public static final String CHARSET_UTF_16BE = "UTF-16BE";
	public static final String CHARSET_UTF_8 = "UTF-8";

	private static final String[] characterSets = {
			CHARSET_ISO_8859_1,
			CHARSET_UTF_16,
			CHARSET_UTF_16BE,
			CHARSET_UTF_8
	};

	private static final byte[] textEncodingFallback = {0, 2, 1, 3};

	private static final byte[][] boms = {
			{},
			{(byte) 0xff, (byte) 0xfe},
			{(byte) 0xfe, (byte) 0xff},
			{}
	};

	private static final byte[][] terminators = {
			{0},
			{0, 0},
			{0, 0},
			{0}
	};

	private byte[] value;
	private byte textEncoding;

	public EncodedText(byte textEncoding, byte[] value) {
		// if encoding type 1 and big endian BOM is present, switch to big endian
		if ((textEncoding == TEXT_ENCODING_UTF_16) &&
				(textEncodingForBytesFromBOM(value) == TEXT_ENCODING_UTF_16BE)) {
			this.textEncoding = TEXT_ENCODING_UTF_16BE;
		} else {
			this.textEncoding = textEncoding;
		}
		this.value = value;
		this.stripBomAndTerminator();
	}

	public EncodedText(String string) throws IllegalArgumentException {
		for (byte textEncoding : textEncodingFallback) {
			this.textEncoding = textEncoding;
			value = stringToBytes(string, characterSetForTextEncoding(textEncoding));
			if (value != null && this.toString() != null) {
				this.stripBomAndTerminator();
				return;
			}
		}
		throw new IllegalArgumentException("Invalid string, could not find appropriate encoding");
	}

	public EncodedText(String string, byte transcodeToTextEncoding) throws IllegalArgumentException, CharacterCodingException {
		this(string);
		setTextEncoding(transcodeToTextEncoding, true);
	}

	public EncodedText(byte textEncoding, String string) {
		this.textEncoding = textEncoding;
		value = stringToBytes(string, characterSetForTextEncoding(textEncoding));
		this.stripBomAndTerminator();
	}

	public EncodedText(byte[] value) {
		this(textEncodingForBytesFromBOM(value), value);
	}

	private static byte textEncodingForBytesFromBOM(byte[] value) {
		if (value.length >= 2 && value[0] == (byte) 0xff && value[1] == (byte) 0xfe) {
			return TEXT_ENCODING_UTF_16;
		} else if (value.length >= 2 && value[0] == (byte) 0xfe && value[1] == (byte) 0xff) {
			return TEXT_ENCODING_UTF_16BE;
		} else if (value.length >= 3 && (value[0] == (byte) 0xef && value[1] == (byte) 0xbb && value[2] == (byte) 0xbf)) {
			return TEXT_ENCODING_UTF_8;
		} else {
			return TEXT_ENCODING_ISO_8859_1;
		}
	}

	private static String characterSetForTextEncoding(byte textEncoding) {
		try {
			return characterSets[textEncoding];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Invalid text encoding " + textEncoding);
		}
	}

	private void stripBomAndTerminator() {
		int leadingCharsToRemove = 0;
		if (value.length >= 2 && ((value[0] == (byte) 0xfe && value[1] == (byte) 0xff) || (value[0] == (byte) 0xff && value[1] == (byte) 0xfe))) {
			leadingCharsToRemove = 2;
		} else if (value.length >= 3 && (value[0] == (byte) 0xef && value[1] == (byte) 0xbb && value[2] == (byte) 0xbf)) {
			leadingCharsToRemove = 3;
		}
		int trailingCharsToRemove = 0;
		byte[] terminator = terminators[textEncoding];
		if (value.length - leadingCharsToRemove >= terminator.length) {
			boolean haveTerminator = true;
			for (int i = 0; i < terminator.length; i++) {
				if (value[value.length - terminator.length + i] != terminator[i]) {
					haveTerminator = false;
					break;
				}
			}
			if (haveTerminator) trailingCharsToRemove = terminator.length;
		}
		if (leadingCharsToRemove + trailingCharsToRemove > 0) {
			int newLength = value.length - leadingCharsToRemove - trailingCharsToRemove;
			byte[] newValue = new byte[newLength];
			if (newLength > 0) {
				System.arraycopy(value, leadingCharsToRemove, newValue, 0, newValue.length);
			}
			value = newValue;
		}
	}

	public byte getTextEncoding() {
		return textEncoding;
	}

	public void setTextEncoding(byte textEncoding) throws CharacterCodingException {
		setTextEncoding(textEncoding, true);
	}

	public void setTextEncoding(byte textEncoding, boolean transcode) throws CharacterCodingException {
		if (this.textEncoding != textEncoding) {
			CharBuffer charBuffer = bytesToCharBuffer(this.value, characterSetForTextEncoding(this.textEncoding));
			byte[] transcodedBytes = charBufferToBytes(charBuffer, characterSetForTextEncoding(textEncoding));
			this.textEncoding = textEncoding;
			this.value = transcodedBytes;
		}
	}

	public byte[] getTerminator() {
		return terminators[textEncoding];
	}

	public byte[] toBytes() {
		return toBytes(false, false);
	}

	public byte[] toBytes(boolean includeBom) {
		return toBytes(includeBom, false);
	}

	public byte[] toBytes(boolean includeBom, boolean includeTerminator) {
		characterSetForTextEncoding(textEncoding); // ensured textEncoding is valid
		int newLength = value.length + (includeBom ? boms[textEncoding].length : 0) + (includeTerminator ? getTerminator().length : 0);
		if (newLength == value.length) {
			return value;
		} else {
			byte bytes[] = new byte[newLength];
			int i = 0;
			if (includeBom) {
				byte[] bom = boms[textEncoding];
				if (bom.length > 0) {
					System.arraycopy(boms[textEncoding], 0, bytes, i, boms[textEncoding].length);
					i += boms[textEncoding].length;
				}
			}
			if (value.length > 0) {
				System.arraycopy(value, 0, bytes, i, value.length);
				i += value.length;
			}
			if (includeTerminator) {
				byte[] terminator = getTerminator();
				if (terminator.length > 0) {
					System.arraycopy(terminator, 0, bytes, i, terminator.length);
				}
			}
			return bytes;
		}
	}

	@Override
	public String toString() {
		try {
			return bytesToString(value, characterSetForTextEncoding(textEncoding));
		} catch (CharacterCodingException e) {
			return null;
		}
	}

	public String getCharacterSet() {
		return characterSetForTextEncoding(textEncoding);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + textEncoding;
		result = prime * result + Arrays.hashCode(value);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EncodedText other = (EncodedText) obj;
		if (textEncoding != other.textEncoding)
			return false;
		if (!Arrays.equals(value, other.value))
			return false;
		return true;
	}

	private static String bytesToString(byte[] bytes, String characterSet) throws CharacterCodingException {
		CharBuffer cbuf = bytesToCharBuffer(bytes, characterSet);
		String s = cbuf.toString();
		int length = s.indexOf(0);
		if (length == -1) return s;
		return s.substring(0, length);
	}

	protected static CharBuffer bytesToCharBuffer(byte[] bytes, String characterSet) throws CharacterCodingException {
		Charset charset = Charset.forName(characterSet);
		CharsetDecoder decoder = charset.newDecoder();
		return decoder.decode(ByteBuffer.wrap(bytes));
	}

	private static byte[] stringToBytes(String s, String characterSet) {
		try {
			return charBufferToBytes(CharBuffer.wrap(s), characterSet);
		} catch (CharacterCodingException e) {
			return null;
		}
	}

	protected static byte[] charBufferToBytes(CharBuffer charBuffer, String characterSet) throws CharacterCodingException {
		Charset charset = Charset.forName(characterSet);
		CharsetEncoder encoder = charset.newEncoder();
		ByteBuffer byteBuffer = encoder.encode(charBuffer);
		return BufferTools.copyBuffer(byteBuffer.array(), 0, byteBuffer.limit());
	}
}
