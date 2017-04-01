package com.mpatric.mp3agic;

public abstract class AbstractID3v2FrameData {

	boolean unsynchronisation;

	public AbstractID3v2FrameData(boolean unsynchronisation) {
		this.unsynchronisation = unsynchronisation;
	}

	protected final void synchroniseAndUnpackFrameData(byte[] bytes) throws InvalidDataException {
		if (unsynchronisation && BufferTools.sizeSynchronisationWouldSubtract(bytes) > 0) {
			byte[] synchronisedBytes = BufferTools.synchroniseBuffer(bytes);
			unpackFrameData(synchronisedBytes);
		} else {
			unpackFrameData(bytes);
		}
	}

	protected byte[] packAndUnsynchroniseFrameData() {
		byte[] bytes = packFrameData();
		if (unsynchronisation && BufferTools.sizeUnsynchronisationWouldAdd(bytes) > 0) {
			return BufferTools.unsynchroniseBuffer(bytes);
		}
		return bytes;
	}

	protected byte[] toBytes() {
		return packAndUnsynchroniseFrameData();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		AbstractID3v2FrameData other = (AbstractID3v2FrameData) obj;
		if (unsynchronisation != other.unsynchronisation)
			return false;
		return true;
	}

	protected abstract void unpackFrameData(byte[] bytes) throws InvalidDataException;

	protected abstract byte[] packFrameData();

	protected abstract int getLength();
}
