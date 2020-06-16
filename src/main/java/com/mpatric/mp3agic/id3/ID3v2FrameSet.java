package com.mpatric.mp3agic.id3;

import java.util.ArrayList;
import java.util.List;

public class ID3v2FrameSet {

	private final String id;
	private final ArrayList<ID3v2Frame> frames= new ArrayList<>();

	public ID3v2FrameSet(String id) {
		this.id = id;
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
		result = prime * result + frames.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}

		if (obj == null || getClass() != obj.getClass()){
			return false;
		}
		ID3v2FrameSet other = (ID3v2FrameSet) obj;
		if (!frames.equals(other.frames))
			return false;
		if (id == null) {
			return other.id == null;
		} else {
			return id.equals(other.id);
		}
	}
}
