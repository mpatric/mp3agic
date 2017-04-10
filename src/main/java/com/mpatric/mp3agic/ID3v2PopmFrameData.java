package com.mpatric.mp3agic;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ID3v2PopmFrameData extends AbstractID3v2FrameData {

	protected static final String WMP9_ADDRESS = "Windows Media Player 9 Series";

	protected String address = "";
	protected int rating = -1;

	private static final Map<Byte, Integer> byteToRating = new HashMap<>(5);
	private static final byte[] wmp9encodedRatings = {(byte) 0x00, (byte) 0x01, (byte) 0x40, (byte) 0x80, (byte) 0xC4,
			(byte) 0xFF};

	static {
		for (int i = 0; i < 6; i++) {
			byteToRating.put(wmp9encodedRatings[i], i);
		}
	}

	public ID3v2PopmFrameData(boolean unsynchronisation, byte[] bytes) throws InvalidDataException {
		super(unsynchronisation);
		synchroniseAndUnpackFrameData(bytes);
	}

	public ID3v2PopmFrameData(boolean unsynchronisation, int rating) {
		super(unsynchronisation);
		this.address = WMP9_ADDRESS;
		this.rating = rating;
	}

	@Override
	protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
		try {
			address = BufferTools.byteBufferToString(bytes, 0, bytes.length - 2);
		} catch (UnsupportedEncodingException e) {
			address = "";
		}
		final byte ratingByte = bytes[bytes.length - 1];
		if (byteToRating.containsKey(ratingByte)) {
			rating = byteToRating.get(ratingByte);
		} else {
			rating = -1;
		}

	}

	@Override
	protected byte[] packFrameData() {
		byte[] bytes = address.getBytes();
		bytes = Arrays.copyOf(bytes, address.length() + 2);
		bytes[bytes.length - 2] = 0;
		bytes[bytes.length - 1] = wmp9encodedRatings[rating];
		return bytes;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	protected int getLength() {
		return address.length() + 2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + rating;
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
		ID3v2PopmFrameData other = (ID3v2PopmFrameData) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (rating != other.rating)
			return false;
		return true;
	}


}
