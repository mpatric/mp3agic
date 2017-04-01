package com.mpatric.mp3agic;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ID3v2Frame {

	private static final int HEADER_LENGTH = 10;
	private static final int ID_OFFSET = 0;
	private static final int ID_LENGTH = 4;
	protected static final int DATA_LENGTH_OFFSET = 4;
	private static final int FLAGS1_OFFSET = 8;
	private static final int FLAGS2_OFFSET = 9;
	private static final int PRESERVE_TAG_BIT = 6;
	private static final int PRESERVE_FILE_BIT = 5;
	private static final int READ_ONLY_BIT = 4;
	private static final int GROUP_BIT = 6;
	private static final int COMPRESSION_BIT = 3;
	private static final int ENCRYPTION_BIT = 2;
	private static final int UNSYNCHRONISATION_BIT = 1;
	private static final int DATA_LENGTH_INDICATOR_BIT = 0;

	protected String id;
	protected int dataLength = 0;
	protected byte[] data = null;
	private boolean preserveTag = false;
	private boolean preserveFile = false;
	private boolean readOnly = false;
	private boolean group = false;
	private boolean compression = false;
	private boolean encryption = false;
	private boolean unsynchronisation = false;
	private boolean dataLengthIndicator = false;

	public ID3v2Frame(byte[] buffer, int offset) throws InvalidDataException {
		unpackFrame(buffer, offset);
	}

	public ID3v2Frame(String id, byte[] data) {
		this.id = id;
		this.data = data;
		dataLength = data.length;
	}

	protected final void unpackFrame(byte[] buffer, int offset) throws InvalidDataException {
		int dataOffset = unpackHeader(buffer, offset);
		sanityCheckUnpackedHeader();
		data = BufferTools.copyBuffer(buffer, dataOffset, dataLength);
	}

	protected int unpackHeader(byte[] buffer, int offset) {
		id = BufferTools.byteBufferToStringIgnoringEncodingIssues(buffer, offset + ID_OFFSET, ID_LENGTH);
		unpackDataLength(buffer, offset);
		unpackFlags(buffer, offset);
		return offset + HEADER_LENGTH;
	}

	protected void unpackDataLength(byte[] buffer, int offset) {
		dataLength = BufferTools.unpackInteger(buffer[offset + DATA_LENGTH_OFFSET], buffer[offset + DATA_LENGTH_OFFSET + 1], buffer[offset + DATA_LENGTH_OFFSET + 2], buffer[offset + DATA_LENGTH_OFFSET + 3]);
	}

	private void unpackFlags(byte[] buffer, int offset) {
		preserveTag = BufferTools.checkBit(buffer[offset + FLAGS1_OFFSET], PRESERVE_TAG_BIT);
		preserveFile = BufferTools.checkBit(buffer[offset + FLAGS1_OFFSET], PRESERVE_FILE_BIT);
		readOnly = BufferTools.checkBit(buffer[offset + FLAGS1_OFFSET], READ_ONLY_BIT);
		group = BufferTools.checkBit(buffer[offset + FLAGS2_OFFSET], GROUP_BIT);
		compression = BufferTools.checkBit(buffer[offset + FLAGS2_OFFSET], COMPRESSION_BIT);
		encryption = BufferTools.checkBit(buffer[offset + FLAGS2_OFFSET], ENCRYPTION_BIT);
		unsynchronisation = BufferTools.checkBit(buffer[offset + FLAGS2_OFFSET], UNSYNCHRONISATION_BIT);
		dataLengthIndicator = BufferTools.checkBit(buffer[offset + FLAGS2_OFFSET], DATA_LENGTH_INDICATOR_BIT);
	}

	protected void sanityCheckUnpackedHeader() throws InvalidDataException {
		for (int i = 0; i < id.length(); i++) {
			if (!((id.charAt(i) >= 'A' && id.charAt(i) <= 'Z') || (id.charAt(i) >= '0' && id.charAt(i) <= '9'))) {
				throw new InvalidDataException("Not a valid frame - invalid tag " + id);
			}
		}
	}

	public byte[] toBytes() throws NotSupportedException {
		byte[] bytes = new byte[getLength()];
		packFrame(bytes, 0);
		return bytes;
	}

	public void toBytes(byte[] bytes, int offset) throws NotSupportedException {
		packFrame(bytes, offset);
	}

	public void packFrame(byte[] bytes, int offset) throws NotSupportedException {
		packHeader(bytes, offset);
		BufferTools.copyIntoByteBuffer(data, 0, data.length, bytes, offset + HEADER_LENGTH);
	}

	private void packHeader(byte[] bytes, int i) {
		try {
			BufferTools.stringIntoByteBuffer(id, 0, id.length(), bytes, 0);
		} catch (UnsupportedEncodingException e) {
		}
		BufferTools.copyIntoByteBuffer(packDataLength(), 0, 4, bytes, 4);
		BufferTools.copyIntoByteBuffer(packFlags(), 0, 2, bytes, 8);
	}

	protected byte[] packDataLength() {
		return BufferTools.packInteger(dataLength);
	}

	private byte[] packFlags() {
		byte[] bytes = new byte[2];
		bytes[0] = BufferTools.setBit(bytes[0], PRESERVE_TAG_BIT, preserveTag);
		bytes[0] = BufferTools.setBit(bytes[0], PRESERVE_FILE_BIT, preserveFile);
		bytes[0] = BufferTools.setBit(bytes[0], READ_ONLY_BIT, readOnly);
		bytes[1] = BufferTools.setBit(bytes[1], GROUP_BIT, group);
		bytes[1] = BufferTools.setBit(bytes[1], COMPRESSION_BIT, compression);
		bytes[1] = BufferTools.setBit(bytes[1], ENCRYPTION_BIT, encryption);
		bytes[1] = BufferTools.setBit(bytes[1], UNSYNCHRONISATION_BIT, unsynchronisation);
		bytes[1] = BufferTools.setBit(bytes[1], DATA_LENGTH_INDICATOR_BIT, dataLengthIndicator);
		return bytes;
	}

	public String getId() {
		return id;
	}

	public int getDataLength() {
		return dataLength;
	}

	public int getLength() {
		return dataLength + HEADER_LENGTH;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
		if (data == null) dataLength = 0;
		else dataLength = data.length;
	}

	public boolean hasDataLengthIndicator() {
		return dataLengthIndicator;
	}

	public boolean hasCompression() {
		return compression;
	}

	public boolean hasEncryption() {
		return encryption;
	}

	public boolean hasGroup() {
		return group;
	}

	public boolean hasPreserveFile() {
		return preserveFile;
	}

	public boolean hasPreserveTag() {
		return preserveTag;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public boolean hasUnsynchronisation() {
		return unsynchronisation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (compression ? 1231 : 1237);
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + dataLength;
		result = prime * result + (dataLengthIndicator ? 1231 : 1237);
		result = prime * result + (encryption ? 1231 : 1237);
		result = prime * result + (group ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (preserveFile ? 1231 : 1237);
		result = prime * result + (preserveTag ? 1231 : 1237);
		result = prime * result + (readOnly ? 1231 : 1237);
		result = prime * result + (unsynchronisation ? 1231 : 1237);
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
		ID3v2Frame other = (ID3v2Frame) obj;
		if (compression != other.compression)
			return false;
		if (!Arrays.equals(data, other.data))
			return false;
		if (dataLength != other.dataLength)
			return false;
		if (dataLengthIndicator != other.dataLengthIndicator)
			return false;
		if (encryption != other.encryption)
			return false;
		if (group != other.group)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (preserveFile != other.preserveFile)
			return false;
		if (preserveTag != other.preserveTag)
			return false;
		if (readOnly != other.readOnly)
			return false;
		if (unsynchronisation != other.unsynchronisation)
			return false;
		return true;
	}
}
