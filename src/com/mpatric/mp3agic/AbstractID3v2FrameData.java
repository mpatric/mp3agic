package com.mpatric.mp3agic;

public abstract class AbstractID3v2FrameData {
	
	boolean unsynchronisation;
	
	public AbstractID3v2FrameData(boolean unsynchronisation) {
		this.unsynchronisation = unsynchronisation;
	}
	
	protected void synchroniseAndUnpackFrameData(byte[] bytes) throws InvalidDataException {
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
	
	public boolean equals(Object obj) {
		if (! (obj instanceof AbstractID3v2FrameData)) return false;
		AbstractID3v2FrameData other = (AbstractID3v2FrameData) obj;
		if (unsynchronisation != other.unsynchronisation) return false;
		return true;
	}

	protected abstract void unpackFrameData(byte[] bytes) throws InvalidDataException;
	protected abstract byte[] packFrameData();
	protected abstract int getLength();
}
