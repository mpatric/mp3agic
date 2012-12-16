package com.mpatric.mp3agic;

import com.mpatric.mp3agic.annotations.FrameMember;


public class ID3v2UrlFrameData extends AbstractID3v2EncodedFrameData {

	@FrameMember(ordinal = 1, encoded = true, terminated = true)
	protected String description;
	@FrameMember(ordinal = 2)
	protected String url;
	
	public ID3v2UrlFrameData(boolean unsynchronisation) {
		super(unsynchronisation);
	}
	
	public ID3v2UrlFrameData(boolean unsynchronisation, String description, String url) {
		this(unsynchronisation, Encoding.getDefault(), description, url);
	}

	public ID3v2UrlFrameData(boolean unsynchronisation, Encoding encoding, String description, String url) {
		super(unsynchronisation, encoding);
		this.description = description;
		this.url = url;
	}

	public ID3v2UrlFrameData(boolean unsynchronisation, byte[] bytes) throws InvalidDataException {
		super(unsynchronisation);
		synchroniseAndUnpackFrameData(bytes);
	}
		
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public boolean equals(Object obj) {
		if (! (obj instanceof ID3v2UrlFrameData)) return false;
		if (! super.equals(obj)) return false;
		ID3v2UrlFrameData other = (ID3v2UrlFrameData) obj;
		if (url == null) {
			if (other.url != null) return false;
		} else if (other.url == null) return false;
		else if (! url.equals(other.url)) return false;
		if (description == null) {
			if (other.description != null) return false;
		} else if (other.description == null) return false;
		else if (! description.equals(other.description)) return false;
		return true;
	}
}
