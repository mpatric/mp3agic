package com.mpatric.mp3agic;

import java.io.UnsupportedEncodingException;

public class BufferTools {

	protected static final String defaultCharsetName = "ISO-8859-1";

	public static String byteBufferToStringIgnoringEncodingIssues(byte[] bytes, int offset, int length) {
		try {
			return byteBufferToString(bytes, offset, length, defaultCharsetName);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static String byteBufferToString(byte[] bytes, int offset, int length) throws UnsupportedEncodingException {
		return byteBufferToString(bytes, offset, length, defaultCharsetName);
	}

	public static String byteBufferToString(byte[] bytes, int offset, int length, String charsetName) throws UnsupportedEncodingException {
		if (length < 1) return "";
		return new String(bytes, offset, length, charsetName);
	}

	public static byte[] stringToByteBufferIgnoringEncodingIssues(String s, int offset, int length) {
		try {
			return stringToByteBuffer(s, offset, length);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static byte[] stringToByteBuffer(String s, int offset, int length) throws UnsupportedEncodingException {
		return stringToByteBuffer(s, offset, length, defaultCharsetName);
	}

	public static byte[] stringToByteBuffer(String s, int offset, int length, String charsetName) throws UnsupportedEncodingException {
		String stringToCopy = s.substring(offset, offset + length);
		byte[] bytes = stringToCopy.getBytes(charsetName);
		return bytes;
	}

	public static void stringIntoByteBuffer(String s, int offset, int length, byte[] bytes, int destOffset) throws UnsupportedEncodingException {
		stringIntoByteBuffer(s, offset, length, bytes, destOffset, defaultCharsetName);
	}

	public static void stringIntoByteBuffer(String s, int offset, int length, byte[] bytes, int destOffset, String charsetName) throws UnsupportedEncodingException {
		String stringToCopy = s.substring(offset, offset + length);
		byte[] srcBytes = stringToCopy.getBytes(charsetName);
		if (srcBytes.length > 0) {
			System.arraycopy(srcBytes, 0, bytes, destOffset, srcBytes.length);
		}
	}

	public static String trimStringRight(String s) {
		int endPosition = s.length() - 1;
		char endChar;
		while (endPosition >= 0) {
			endChar = s.charAt(endPosition);
			if (endChar > 32) {
				break;
			}
			endPosition--;
		}
		if (endPosition == s.length() - 1) return s;
		else if (endPosition < 0) return "";
		return s.substring(0, endPosition + 1);
	}

	public static String padStringRight(String s, int length, char padWith) {
		if (s.length() >= length) return s;
		StringBuilder stringBuffer = new StringBuilder(s);
		while (stringBuffer.length() < length) {
			stringBuffer.append(padWith);
		}
		return stringBuffer.toString();
	}

	public static boolean checkBit(byte b, int bitPosition) {
		return ((b & (0x01 << bitPosition)) != 0);
	}

	public static byte setBit(byte b, int bitPosition, boolean value) {
		byte newByte;
		if (value) {
			newByte = (byte) (b | ((byte) 0x01 << bitPosition));
		} else {
			newByte = (byte) (b & (~((byte) 0x01 << bitPosition)));
		}
		return newByte;
	}

	public static int shiftByte(byte c, int places) {
		int i = c & 0xff;
		if (places < 0) {
			return i << -places;
		} else if (places > 0) {
			return i >> places;
		}
		return i;
	}

	public static int unpackInteger(byte b1, byte b2, byte b3, byte b4) {
		int value = b4 & 0xff;
		value += BufferTools.shiftByte(b3, -8);
		value += BufferTools.shiftByte(b2, -16);
		value += BufferTools.shiftByte(b1, -24);
		return value;
	}

	public static byte[] packInteger(int i) {
		byte[] bytes = new byte[4];
		bytes[3] = (byte) (i & 0xff);
		bytes[2] = (byte) ((i >> 8) & 0xff);
		bytes[1] = (byte) ((i >> 16) & 0xff);
		bytes[0] = (byte) ((i >> 24) & 0xff);
		return bytes;
	}

	public static int unpackSynchsafeInteger(byte b1, byte b2, byte b3, byte b4) {
		int value = ((byte) (b4 & 0x7f));
		value += shiftByte((byte) (b3 & 0x7f), -7);
		value += shiftByte((byte) (b2 & 0x7f), -14);
		value += shiftByte((byte) (b1 & 0x7f), -21);
		return value;
	}

	public static byte[] packSynchsafeInteger(int i) {
		byte[] bytes = new byte[4];
		packSynchsafeInteger(i, bytes, 0);
		return bytes;
	}

	public static void packSynchsafeInteger(int i, byte[] bytes, int offset) {
		bytes[offset + 3] = (byte) (i & 0x7f);
		bytes[offset + 2] = (byte) ((i >> 7) & 0x7f);
		bytes[offset + 1] = (byte) ((i >> 14) & 0x7f);
		bytes[offset + 0] = (byte) ((i >> 21) & 0x7f);
	}

	public static byte[] copyBuffer(byte[] bytes, int offset, int length) {
		byte[] copy = new byte[length];
		if (length > 0) {
			System.arraycopy(bytes, offset, copy, 0, length);
		}
		return copy;
	}

	public static void copyIntoByteBuffer(byte[] bytes, int offset, int length, byte[] destBuffer, int destOffset) {
		if (length > 0) {
			System.arraycopy(bytes, offset, destBuffer, destOffset, length);
		}
	}

	public static int sizeUnsynchronisationWouldAdd(byte[] bytes) {
		int count = 0;
		for (int i = 0; i < bytes.length - 1; i++) {
			if (bytes[i] == (byte) 0xff && ((bytes[i + 1] & (byte) 0xe0) == (byte) 0xe0 || bytes[i + 1] == 0)) {
				count++;
			}
		}
		if (bytes.length > 0 && bytes[bytes.length - 1] == (byte) 0xff) count++;
		return count;
	}

	public static byte[] unsynchroniseBuffer(byte[] bytes) {
		// unsynchronisation is replacing instances of:
		// 11111111 111xxxxx with 11111111 00000000 111xxxxx and
		// 11111111 00000000 with 11111111 00000000 00000000
		int count = sizeUnsynchronisationWouldAdd(bytes);
		if (count == 0) return bytes;
		byte[] newBuffer = new byte[bytes.length + count];
		int j = 0;
		for (int i = 0; i < bytes.length - 1; i++) {
			newBuffer[j++] = bytes[i];
			if (bytes[i] == (byte) 0xff && ((bytes[i + 1] & (byte) 0xe0) == (byte) 0xe0 || bytes[i + 1] == 0)) {
				newBuffer[j++] = 0;
			}
		}
		newBuffer[j++] = bytes[bytes.length - 1];
		if (bytes[bytes.length - 1] == (byte) 0xff) {
			newBuffer[j++] = 0;
		}
		return newBuffer;
	}

	public static int sizeSynchronisationWouldSubtract(byte[] bytes) {
		int count = 0;
		for (int i = 0; i < bytes.length - 2; i++) {
			if (bytes[i] == (byte) 0xff && bytes[i + 1] == 0 && ((bytes[i + 2] & (byte) 0xe0) == (byte) 0xe0 || bytes[i + 2] == 0)) {
				count++;
			}
		}
		if (bytes.length > 1 && bytes[bytes.length - 2] == (byte) 0xff && bytes[bytes.length - 1] == 0) count++;
		return count;
	}

	public static byte[] synchroniseBuffer(byte[] bytes) {
		// synchronisation is replacing instances of:
		// 11111111 00000000 111xxxxx with 11111111 111xxxxx and
		// 11111111 00000000 00000000 with 11111111 00000000
		int count = sizeSynchronisationWouldSubtract(bytes);
		if (count == 0) return bytes;
		byte[] newBuffer = new byte[bytes.length - count];
		int i = 0;
		for (int j = 0; j < newBuffer.length - 1; j++) {
			newBuffer[j] = bytes[i];
			if (bytes[i] == (byte) 0xff && bytes[i + 1] == 0 && ((bytes[i + 2] & (byte) 0xe0) == (byte) 0xe0 || bytes[i + 2] == 0)) {
				i++;
			}
			i++;
		}
		newBuffer[newBuffer.length - 1] = bytes[i];
		return newBuffer;
	}

	public static String substitute(String s, String replaceThis, String withThis) {
		if (replaceThis.length() < 1 || !s.contains(replaceThis)) {
			return s;
		}
		StringBuilder newString = new StringBuilder();
		int lastPosition = 0;
		int position = 0;
		while ((position = s.indexOf(replaceThis, position)) >= 0) {
			if (position > lastPosition) {
				newString.append(s.substring(lastPosition, position));
			}
			if (withThis != null) {
				newString.append(withThis);
			}
			lastPosition = position + replaceThis.length();
			position++;
		}
		if (lastPosition < s.length()) {
			newString.append(s.substring(lastPosition));
		}
		return newString.toString();
	}

	public static String asciiOnly(String s) {
		StringBuilder newString = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if (ch < 32 || ch > 126) {
				newString.append('?');
			} else {
				newString.append(ch);
			}
		}
		return newString.toString();
	}

	public static int indexOfTerminator(byte[] bytes) {
		return indexOfTerminator(bytes, 0);
	}

	public static int indexOfTerminator(byte[] bytes, int fromIndex) {
		return indexOfTerminator(bytes, 0, 1);
	}

	public static int indexOfTerminator(byte[] bytes, int fromIndex, int terminatorLength) {
		int marker = -1;
		for (int i = fromIndex; i <= bytes.length - terminatorLength; i++) {
			if ((i - fromIndex) % terminatorLength == 0) {
				int matched;
				for (matched = 0; matched < terminatorLength; matched++) {
					if (bytes[i + matched] != 0) break;
				}
				if (matched == terminatorLength) {
					marker = i;
					break;
				}
			}
		}
		return marker;
	}

	public static int indexOfTerminatorForEncoding(byte[] bytes, int fromIndex, int encoding) {
		int terminatorLength = (encoding == EncodedText.TEXT_ENCODING_UTF_16 || encoding == EncodedText.TEXT_ENCODING_UTF_16BE) ? 2 : 1;
		return indexOfTerminator(bytes, fromIndex, terminatorLength);
	}
}
