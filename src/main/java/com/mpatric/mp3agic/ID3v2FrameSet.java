package com.mpatric.mp3agic;

import java.util.ArrayList;
import java.util.List;

public class ID3v2FrameSet {

	private String id;
	private ArrayList<ID3v2Frame> frames;
	
	public ID3v2FrameSet(String id) {
		this.id = id;
		frames = new ArrayList<ID3v2Frame>();
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
	
	public boolean equals(Object obj) {
		if (! (obj instanceof ID3v2FrameSet)) return false;
		if (super.equals(obj)) return true;
		ID3v2FrameSet other = (ID3v2FrameSet) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (other.id == null) return false;
		else if (! id.equals(other.id)) return false;
		if (frames == null) {
			if (other.frames != null) return false;
		} else if (other.frames == null) return false;
		else if (! frames.equals(other.frames)) return false;
		return true;
	}
}
