package com.mpatric.mp3agic;

import java.util.Map;

public interface ID3v2 extends ID3v1 {
	
	boolean getPadding();
	void setPadding(boolean padding);
	
	boolean hasFooter();
	void setFooter(boolean footer);
	
	boolean hasUnsynchronisation();
	void setUnsynchronisation(boolean unsynchronisation);
	
	String getComposer();
	void setComposer(String composer);
	
	String getOriginalArtist();
	void setOriginalArtist(String originalArtist);
	
	String getCopyright();
	void setCopyright(String copyright);
	
	String getUrl();
	void setUrl(String url);
	
	String getEncoder();
	void setEncoder(String encoder);
	
	byte[] getAlbumImage();
	void setAlbumImage(byte[] albumImage, String mimeType);
	String getAlbumImageMimeType();
	
	String getItunesComment();
	void setItunesComment(String itunesComment);
	
	int getDataLength();
	int getLength();
	boolean getObseleteFormat();
	
	Map<String, ID3v2FrameSet> getFrameSets();
	void clearFrameSet(String id);
}
