package com.mpatric.mp3agic;

public class BufferTools {

	public static String byteBufferToString(byte[] bytes, int offset, int length) {
		if (length < 1) return "";
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			char ch;
			if (bytes[offset + i] >= 0) ch = (char)bytes[offset + i];
			else ch = (char)(bytes[offset + i] + 256);			
			stringBuffer.append(ch);
		}
		return stringBuffer.toString();
	}

	public static byte[] stringToByteBuffer(String s, int offset, int length) {
		String stringToCopy = s.substring(offset, offset + length);
		byte[] bytes = stringToCopy.getBytes();
		return bytes;
	}
	
	public static void stringIntoByteBuffer(String s, int offset, int length, byte[] bytes, int destOffset) {
		for (int i = 0; i < length; i++) {
			char ch = s.charAt(offset + i);
			byte by;
			if (ch < 128) by = (byte)ch;
			else by = (byte)(ch - 256);
			bytes[destOffset + i] = by;
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
		StringBuffer stringBuffer = new StringBuffer(s);
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
			newByte = (byte) (b | ((byte)0x01 << bitPosition));
		} else {
			newByte = (byte) (b & (~ ((byte)0x01 << bitPosition)));
		}
		return newByte;
	}
	
	public static int shiftByte(byte c, int places) {
		int i = c & 0xFF;
		if (places < 0) {
			return i << -places;
		} else if (places > 0) {
			return i >> places;
		}
		return i;
	}
	
	public static int unpackInteger(byte b1, byte b2, byte b3, byte b4) {
		int value = b4 & 0xFF;
		value += BufferTools.shiftByte(b3, -8);
		value += BufferTools.shiftByte(b2, -16);
		value += BufferTools.shiftByte(b1, -24);
		return value;
	}
	
	public static byte[] packInteger(int i) {
		byte[] bytes = new byte[4];
		bytes[3] = (byte) (i & 0xFF);
		bytes[2] = (byte) ((i >> 8) & 0xFF);
		bytes[1] = (byte) ((i >> 16) & 0xFF);
		bytes[0] = (byte) ((i >> 24) & 0xFF);
		return bytes;
	}
	
	public static int unpackSynchsafeInteger(byte b1, byte b2, byte b3, byte b4) {
		int value = ((byte)(b4 & 0x7F));
		value += shiftByte((byte)(b3 & 0x7F), -7);
		value += shiftByte((byte)(b2 & 0x7F), -14);
		value += shiftByte((byte)(b1 & 0x7F), -21);
		return value;
	}
	
	public static byte[] packSynchsafeInteger(int i) {
		byte[] bytes = new byte[4];
		packSynchsafeInteger(i, bytes, 0);
		return bytes;
	}
	
	public static void packSynchsafeInteger(int i, byte[] bytes, int offset) {
		bytes[offset + 3] = (byte) (i & 0x7F);
		bytes[offset + 2] = (byte) ((i >> 7) & 0x7F);
		bytes[offset + 1] = (byte) ((i >> 14) & 0x7F);
		bytes[offset + 0] = (byte) ((i >> 21) & 0x7F);
	}

	public static byte[] copyBuffer(byte[] bytes, int offset, int length) {
		byte[] copy = new byte[length];
		for (int i = 0; i < length; i++) {
			copy[i] = bytes[offset + i];
		}
		return copy;
	}
	
	public static void copyIntoByteBuffer(byte[] bytes, int offset, int length, byte[] destBuffer, int destOffset) {
		for (int i = offset; i < length; i++) {
			destBuffer[destOffset + i] = bytes[i];
		}
	}
	
	public static int sizeUnsynchronisationWouldAdd(byte[] bytes) {
		int count = 0; 
		for (int i = 0; i < bytes.length - 1; i++) {
			if (bytes[i] == -0x01 && ((bytes[i + 1] & -0x20) == -0x20 || bytes[i + 1] == 0)) {
				count++;
			}
		}
		if (bytes.length > 0 && bytes[bytes.length - 1] == -0x01) count++;
		return count;
	}

	public static byte[] unsynchroniseBuffer(byte[] bytes) {
		int count = sizeUnsynchronisationWouldAdd(bytes);
		if (count == 0) return bytes;
		byte[] newBuffer = new byte[bytes.length + count];
		int j = 0;
		for (int i = 0; i < bytes.length - 1; i++) {
			newBuffer[j++] = bytes[i];
			if (bytes[i] == -0x01 && ((bytes[i + 1] & -0x20) == -0x20 || bytes[i + 1] == 0)) {
				newBuffer[j++] = 0;
			}
		}
		newBuffer[j++] = bytes[bytes.length - 1];
		if (bytes[bytes.length - 1] == -0x01) {
			newBuffer[j++] = 0;
		}
		return newBuffer;
	}
	
	public static int sizeSynchronisationWouldSubtract(byte[] bytes) {
		int count = 0; 
		for (int i = 0; i < bytes.length - 2; i++) {
			if (bytes[i] == -0x01 && bytes[i + 1] == 0 && ((bytes[i + 2] & -0x20) == -0x20 || bytes[i + 2] == 0)) {
				count++;
			}
		}
		if (bytes.length > 1 && bytes[bytes.length - 2] == -0x01 && bytes[bytes.length - 1] == 0) count++;
		return count;
	}

	public static byte[] synchroniseBuffer(byte[] bytes) {
		int count = sizeSynchronisationWouldSubtract(bytes);
		if (count == 0) return bytes;
		byte[] newBuffer = new byte[bytes.length - count];
		int i = 0;
		for (int j = 0; j < newBuffer.length - 1; j++) {
			newBuffer[j] = bytes[i];
			if (bytes[i] == -0x01 && bytes[i + 1] == 0 && ((bytes[i + 2] & -0x20) == -0x20 || bytes[i + 2] == 0)) {
				i++;
			}
			i++;
		}
		newBuffer[newBuffer.length - 1] = bytes[i];
		return newBuffer;
	}

	public static String substitute(String s, String replaceThis, String withThis) {
		if (replaceThis.length() < 1 || s.indexOf(replaceThis) < 0) {
			return s;
		}
		StringBuffer newString = new StringBuffer();
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
		StringBuffer newString = new StringBuffer();
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
}
