package com.mpatric.mp3agic;

import java.util.ArrayList;
import java.util.List;

public class ID3v2FrameSet {

	private String id;
	private ArrayList<ID3v2Frame> frames;

	public ID3v2FrameSet(String id) {
		this.id = id;
		frames = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void clear() {
		frames.clear();
	}

	public void addFrame(ID3v2Frame frame) {
		frames.add(frame);
	}

	public List<ID3v2Frame> getFrames() {
		return frames;
	}

	@Override
	public String toString() {
		return this.id + ": " + frames.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((frames == null) ? 0 : frames.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ID3v2FrameSet other = (ID3v2FrameSet) obj;
		if (frames == null) {
			if (other.frames != null)
				return false;
		} else if (!frames.equals(other.frames))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
