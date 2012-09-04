package com.mpatric.mp3agic;

import com.mpatric.mp3agic.annotations.FrameMember;

public class AbstractID3v2EncodedFrameData extends AbstractID3v2FrameData {
	
	@FrameMember(ordinal = 0)
	private Encoding encoding;
	
	public AbstractID3v2EncodedFrameData(boolean unsynchronisation) {
		this(unsynchronisation, Encoding.getDefault());
	}
	
	public AbstractID3v2EncodedFrameData(boolean unsynchronisation, Encoding encoding) {
		super(unsynchronisation);
		this.encoding = encoding;
	}

	public Encoding getEncoding() {
		return encoding;
	}

	public void setEncoding(Encoding encoding) {
		this.encoding = encoding;
	}

	public boolean equals(Object obj) {
		if (! (obj instanceof AbstractID3v2EncodedFrameData)) return false;
		if (! super.equals(obj)) return false;
		AbstractID3v2EncodedFrameData other = (AbstractID3v2EncodedFrameData) obj;
		if ((null == encoding) ^ (null == other.encoding)) {
			return false;
		}
		// They're either both null, or they're the same object
		if ((null == encoding) || (encoding == other.encoding)) {
			return true;
		}
		// Different non-null objects
		return false;
	}

}
