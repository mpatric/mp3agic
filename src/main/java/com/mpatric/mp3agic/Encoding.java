package com.mpatric.mp3agic;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import com.google.common.base.Charsets;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public enum Encoding {
	ENCODING_ISO_8859_1(Charsets.ISO_8859_1, 1, new byte[] { 0x0 }),
	ENCODING_UTF_16(Charsets.UTF_16, 2, new byte[] { 0x0, 0x0 }),
	ENCODING_UTF_16BE(Charsets.UTF_16BE, 2, new byte[] { 0x0, 0x0 }),
	ENCODING_UTF_8(Charsets.UTF_8, 1, new byte[] { 0x0 });

	public final Charset charset;
	public final int characterSize;
	public final byte[] terminator;
	
	private static Encoding DEFAULT_ENCODING = Encoding.ENCODING_UTF_8;

	public static void setDefault(Encoding encoding) {
		DEFAULT_ENCODING = encoding;
	}
	
	public static Encoding getDefault() {
		return DEFAULT_ENCODING;
	}
	
	
	private Encoding(Charset charset, int characterSize, byte[] terminator) {
		this.charset = charset; 
		this.characterSize = characterSize;
		this.terminator = terminator;
	}
	

	
	private static final byte[] BOM_STRING = "a".getBytes(Charsets.UTF_16);

	public static Encoding getEncoding(int encoding) {
		if (encoding >= Encoding.values().length) {
			throw new IllegalArgumentException("Unexpected encoding " + encoding);
		}
		return Encoding.values()[encoding];
	}
	
	public byte[] encode(String text) {
		ByteArrayDataOutput outputBuffer = ByteStreams.newDataOutput();
		// Write the data.  Note, that Java won't output the BOM for an empty string
		// so we have to special case that.
		if (charset == Charsets.UTF_16 && text.isEmpty() ) {
			outputBuffer.write(BOM_STRING, 0, 2);
		} else {
			outputBuffer.write(text.getBytes(charset));
		}
		// Write the terminator
		outputBuffer.write(0);
		if (characterSize == 2) {
			outputBuffer.write(0);
		}
		return outputBuffer.toByteArray();
	}
	
	public String parse(ByteArrayInputStream data, boolean terminated) {
		ByteArrayDataOutput outputBuffer = ByteStreams.newDataOutput();
		
		int token, byte1, byte2 = 0;
		while (data.available() > 0) {
			token = byte1 = data.read();
			if (characterSize == 2) {
				if (data.available() == 0) {
					throw new IllegalStateException();
				}
				byte2 = data.read();
				token <<= 8;
				token = token | byte2;
			}

			if (token == 0) {
				break;
			} 
				
			outputBuffer.write(byte1);
			if (characterSize == 2) {
				outputBuffer.write(byte2);
			}
		}

		return new String(outputBuffer.toByteArray(), charset);
	}
};

		
