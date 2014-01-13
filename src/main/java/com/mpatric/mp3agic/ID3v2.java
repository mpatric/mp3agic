package com.mpatric.mp3agic;

import java.util.List;
import java.util.Map;

public interface ID3v2 extends ID3v1 {
	
	boolean getPadding();
	void setPadding(boolean padding);
	
	boolean hasFooter();
	void setFooter(boolean footer);
	
	boolean hasUnsynchronisation();
	void setUnsynchronisation(boolean unsynchronisation);
	
	List<String> getTitles();
	List<String> getArtists();
	List<String> getAlbums();
	List<String> getTracks();
	List<String> getYears();
	List<String> getComments();
	
	String getComposer();
	List<String> getComposers();
	void setComposer(String composer);
	
	String getPublisher();
	List<String> getPublishers();
	void setPublisher(String publisher);
	
	String getOriginalArtist();
	List<String> getOriginalArtists();
	void setOriginalArtist(String originalArtist);
	
	String getAlbumArtist();
	List<String> getAlbumArtists();
	void setAlbumArtist(String albumArtist);
	
	String getCopyright();
	List<String> getCopyrights();
	void setCopyright(String copyright);
	
	String getUrl();
	List<String> getUrls();
	void setUrl(String url);

	String getPartOfSet();
	List<String> getPartOfSets();
	void setPartOfSet(String partOfSet);

	boolean isCompilation();
	void setCompilation(boolean compilation);

	String getGrouping();
	List<String> getGroupings();
	void setGrouping(String grouping);

	List<ID3v2ChapterFrameData> getChapters();
	void setChapters(Iterable<ID3v2ChapterFrameData> chapters);
	
	List<ID3v2ChapterTOCFrameData> getChapterTOC();
	void setChapterTOC(Iterable<ID3v2ChapterTOCFrameData> ctoc);
	
	String getEncoder();
	List<String> getEncoders();
	void setEncoder(String encoder);
	
	byte[] getAlbumImage();
	void setAlbumImage(byte[] albumImage, String mimeType);
	String getAlbumImageMimeType();
	
	String getItunesComment();
	void setItunesComment(String itunesComment);
	
	/**
	 * Set genre from text.
	 * This method behaves different depending on the ID3 version.
	 * Prior to ID3v2.4, the provided text must match a id3v1 genre description.
	 * With ID3v2.4, the genre is written as free text.
	 * @param text genre string
	 */
	public void setGenreDescription(String text);
	List<String> getGenreDescriptions();
	
	int getDataLength();
	int getLength();
	boolean getObseleteFormat();
	
	Map<String, ID3v2FrameSet> getFrameSets();
	void clearFrameSet(String id);
}
