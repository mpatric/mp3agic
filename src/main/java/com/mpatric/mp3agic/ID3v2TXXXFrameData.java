package com.mpatric.mp3agic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ID3v2TXXXFrameData extends AbstractID3v2FrameData {

	protected static final String TAG = "ID3::TXXX";

	public static final String ID_FIELD = "TXXX";

	private EncodedText description;
	private EncodedText value;

	public ID3v2TXXXFrameData(boolean unsynchronisation) {
		super(unsynchronisation);
	}

	public ID3v2TXXXFrameData(boolean unsynchronisation, EncodedText description, EncodedText value) {
		super(unsynchronisation);
		this.description = description;
		this.value = value;
	}

	public ID3v2TXXXFrameData(boolean unsynchronisation, byte[] bytes) throws InvalidDataException {
		super(unsynchronisation);
		synchroniseAndUnpackFrameData(bytes);
	}

	@Override
	protected void unpackFrameData(byte[] bytes) throws InvalidDataException {
		int marker = BufferTools.indexOfTerminatorForEncoding(bytes, 1, bytes[0]);

		description = new EncodedText(bytes[0], BufferTools.copyBuffer(bytes, 1, marker - 1));

		marker += description.getTerminator().length;

		value = new EncodedText(bytes[0], BufferTools.copyBuffer(bytes, marker, bytes.length - marker));
	}

	@Override
	protected byte[] packFrameData() {
		byte[] bytes = new byte[getLength()];
		if (value != null) {
			bytes[0] = value.getTextEncoding();
		} else {
			bytes[0] = 0;
		}
		int marker = 1;
		if (description != null) {
			byte[] descriptionBytes = description.toBytes(true, true);
			BufferTools.copyIntoByteBuffer(descriptionBytes, 0, descriptionBytes.length, bytes, marker);
			marker += descriptionBytes.length;
		} else {
			bytes[marker++] = 0;
		}
		if (value != null) {
			byte[] commentBytes = value.toBytes(true, false);
			BufferTools.copyIntoByteBuffer(commentBytes, 0, commentBytes.length, bytes, marker);
		}
		return bytes;
	}

	@Override
	protected int getLength() {
		int length = 1;
		if (description != null) {
			length += description.toBytes(true, true).length;
		} else {
			length++;
		}
		if (value != null) {
			length += value.toBytes(true, false).length;
		}
		return length;
	}

	public EncodedText getValue() {
		return value;
	}

	public void setValue(EncodedText value) {
		this.value = value;
	}

	public EncodedText getDescription() {
		return description;
	}

	public void setDescription(EncodedText description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		return false;
	}

	public static Map<ID3v2Frame, ID3v2TXXXFrameData> extractAllFrames(
			Map<String, ID3v2FrameSet> frameSets,
			boolean useFrameUnsynchronisation,
			String description) {
		ID3v2FrameSet frameSet = frameSets.get(ID_FIELD);

		HashMap<ID3v2Frame, ID3v2TXXXFrameData> frames = new HashMap<>();

		if (frameSet == null)
			return frames;

		for (ID3v2Frame frame : frameSet.getFrames()) {
			try {
				ID3v2TXXXFrameData field = new ID3v2TXXXFrameData(
						useFrameUnsynchronisation,
						frame.getData());

				if (description == null || field.getDescription().toString().contains(description)) {
					frames.put(frame, field);
				}
			} catch (InvalidDataException e) {
				e.printStackTrace();
			}
		}

		return frames;
	}

	public static ArrayList<ID3v2TXXXFrameData> extractAll(
			Map<String, ID3v2FrameSet> frameSets,
			boolean useFrameUnsynchronisation,
			String description) {

		Map<ID3v2Frame, ID3v2TXXXFrameData> frames = extractAllFrames(
				frameSets,
				useFrameUnsynchronisation,
				description);

		ArrayList<ID3v2TXXXFrameData> fields = new ArrayList<>();

		for (Map.Entry<ID3v2Frame, ID3v2TXXXFrameData> item : frames.entrySet()) {
			fields.add(item.getValue());
		}

		return fields;
	}

	public static ArrayList<ID3v2TXXXFrameData> extractAll(
			Map<String, ID3v2FrameSet> frameSets,
			boolean useFrameUnsynchronisation) {

		return extractAll(frameSets, useFrameUnsynchronisation, null);
	}

	public static ID3v2TXXXFrameData extract(
			Map<String, ID3v2FrameSet> frameSets,
			boolean useFrameUnsynchronisation,
			String description) {

		ArrayList<ID3v2TXXXFrameData> items = extractAll(
				frameSets,
				useFrameUnsynchronisation,
				description);

		if (items.size() > 0) {
			return items.get(0);
		}

		return null;
	}

	public static void createOrAddField(
			Map<String, ID3v2FrameSet> frameSets,
			boolean useFrameUnsynchronisation,
			String description,
			String value,
			boolean useDescriptionToMatch
	) {

		ID3v2TXXXFrameData field = new ID3v2TXXXFrameData(
				useFrameUnsynchronisation,
				new EncodedText(description),
				new EncodedText(value));

		ID3v2Frame frame = new ID3v2Frame(
				ID_FIELD,
				field.toBytes());

		ID3v2FrameSet frameSet = frameSets.get(frame.getId());
		if (frameSet == null) {
			frameSet = new ID3v2FrameSet(frame.getId());
			frameSet.addFrame(frame);
			frameSets.put(frame.getId(), frameSet);
		} else {
			if (useDescriptionToMatch) {
				Map<ID3v2Frame, ID3v2TXXXFrameData> frames = extractAllFrames(
						frameSets,
						useFrameUnsynchronisation,
						description);

				if (frames != null && !frames.isEmpty()) {
					frameSet.getFrames().removeAll(frames.keySet());
				}
			}

			frameSet.addFrame(frame);
		}

	}

}
