
package com.mpatric.mp3agic;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class ID3v2ChapterTOCFrameData extends AbstractID3v2FrameData {

	protected boolean isRoot;
	protected boolean isOrdered;
	protected String id;
	protected String[] children;
	protected ArrayList<ID3v2Frame> subframes = new ArrayList<>();

	public ID3v2ChapterTOCFrameData(boolean unsynchronisation) {
		super(unsynchronisation);
	}

	public ID3v2ChapterTOCFrameData(boolean unsynchronisation, boolean isRoot, boolean isOrdered,
									String id, String[] children) {
		super(unsynchronisation);
		this.isRoot = isRoot;
		this.isOrdered = isOrdered;
		this.id = id;
		this.children = children;
	}

	public ID3v2ChapterTOCFrameData(boolean unsynchronisation, byte[] bytes)
			throws InvalidDataException {
		super(unsynchronisation);
		synchroniseAndUnpackFrameData(bytes);
	}

	@Override
	protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
		ByteBuffer bb = ByteBuffer.wrap(bytes);

		id = ByteBufferUtils.extractNullTerminatedString(bb);

		byte flags = bb.get();
		if ((flags & 0x01) == 0x01) {
			isRoot = true;
		}
		if ((flags & 0x02) == 0x02) {
			isOrdered = true;
		}

		int childCount = bb.get(); // TODO: 0xFF -> int = 255; byte = -128;

		children = new String[childCount];

		for (int i = 0; i < childCount; i++) {
			children[i] = ByteBufferUtils.extractNullTerminatedString(bb);
		}

		for (int offset = bb.position(); offset < bytes.length; ) {
			ID3v2Frame frame = new ID3v2Frame(bytes, offset);
			offset += frame.getLength();
			subframes.add(frame);
		}

	}

	public void addSubframe(String id, AbstractID3v2FrameData frame) {
		subframes.add(new ID3v2Frame(id, frame.toBytes()));
	}

	@Override
	protected byte[] packFrameData() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());
		bb.put(id.getBytes());
		bb.put((byte) 0);
		bb.put(getFlags());
		bb.put((byte) children.length);

		for (String child : children) {
			bb.put(child.getBytes());
			bb.put((byte) 0);
		}

		for (ID3v2Frame frame : subframes) {
			try {
				bb.put(frame.toBytes());
			} catch (NotSupportedException e) {
				e.printStackTrace();
			}
		}
		return bb.array();
	}

	private byte getFlags() {
		byte b = 0;

		if (isRoot) {
			b |= 0x01;
		}

		if (isOrdered) {
			b |= 0x02;
		}
		return b;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public boolean isOrdered() {
		return isOrdered;
	}

	public void setOrdered(boolean isOrdered) {
		this.isOrdered = isOrdered;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getChildren() {
		return children;
	}

	public void setChildren(String[] children) {
		this.children = children;
	}

	@Deprecated
	public String[] getChilds() {
		return children;
	}

	@Deprecated
	public void setChilds(String[] childs) {
		this.children = childs;
	}

	public ArrayList<ID3v2Frame> getSubframes() {
		return subframes;
	}

	public void setSubframes(ArrayList<ID3v2Frame> subframes) {
		this.subframes = subframes;
	}

	@Override
	protected int getLength() {
		int length = 3;
		if (id != null) length += id.length();
		if (children != null) {
			length += children.length;
			for (String child : children) {
				length += child.length();
			}
		}
		if (subframes != null) {
			for (ID3v2Frame frame : subframes) {
				length += frame.getLength();
			}
		}
		return length;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ID3v2ChapterTOCFrameData [isRoot=");
		builder.append(isRoot);
		builder.append(", isOrdered=");
		builder.append(isOrdered);
		builder.append(", id=");
		builder.append(id);
		builder.append(", children=");
		builder.append(Arrays.toString(children));
		builder.append(", subframes=");
		builder.append(subframes);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(children);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (isOrdered ? 1231 : 1237);
		result = prime * result + (isRoot ? 1231 : 1237);
		result = prime * result
				+ ((subframes == null) ? 0 : subframes.hashCode());
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
		ID3v2ChapterTOCFrameData other = (ID3v2ChapterTOCFrameData) obj;
		if (!Arrays.equals(children, other.children))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isOrdered != other.isOrdered)
			return false;
		if (isRoot != other.isRoot)
			return false;
		if (subframes == null) {
			if (other.subframes != null)
				return false;
		} else if (!subframes.equals(other.subframes))
			return false;
		return true;
	}
}
