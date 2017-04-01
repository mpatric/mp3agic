package com.mpatric.mp3agic;

import java.util.ArrayList;
import java.util.Map;

public interface ID3v2 extends ID3v1 {

	boolean getPadding();

	void setPadding(boolean padding);

	boolean hasFooter();

	void setFooter(boolean footer);

	boolean hasUnsynchronisation();

	void setUnsynchronisation(boolean unsynchronisation);

	int getBPM();

	void setBPM(int bpm);

	String getGrouping();

	void setGrouping(String grouping);

	String getKey();

	void setKey(String key);

	String getDate();

	void setDate(String date);

	String getComposer();

	void setComposer(String composer);

	String getPublisher();

	void setPublisher(String publisher);

	String getOriginalArtist();

	void setOriginalArtist(String originalArtist);

	String getAlbumArtist();

	void setAlbumArtist(String albumArtist);

	String getCopyright();

	void setCopyright(String copyright);

	String getArtistUrl();

	void setArtistUrl(String url);

	String getCommercialUrl();

	void setCommercialUrl(String url);

	String getCopyrightUrl();

	void setCopyrightUrl(String url);

	String getAudiofileUrl();

	void setAudiofileUrl(String url);

	String getAudioSourceUrl();

	void setAudioSourceUrl(String url);

	String getRadiostationUrl();

	void setRadiostationUrl(String url);

	String getPaymentUrl();

	void setPaymentUrl(String url);

	String getPublisherUrl();

	void setPublisherUrl(String url);

	String getUrl();

	void setUrl(String url);

	String getPartOfSet();

	void setPartOfSet(String partOfSet);

	boolean isCompilation();

	void setCompilation(boolean compilation);

	ArrayList<ID3v2ChapterFrameData> getChapters();

	void setChapters(ArrayList<ID3v2ChapterFrameData> chapters);

	ArrayList<ID3v2ChapterTOCFrameData> getChapterTOC();

	void setChapterTOC(ArrayList<ID3v2ChapterTOCFrameData> ctoc);

	String getEncoder();

	void setEncoder(String encoder);

	byte[] getAlbumImage();

	void setAlbumImage(byte[] albumImage, String mimeType);

	void setAlbumImage(byte[] albumImage, String mimeType, byte imageType, String imageDescription);

	void clearAlbumImage();

	String getAlbumImageMimeType();

	int getWmpRating();

	void setWmpRating(int rating);

	String getItunesComment();

	void setItunesComment(String itunesComment);

	String getLyrics();

	void setLyrics(String lyrics);

	/**
	 * Set genre from text.
	 * This method behaves different depending on the ID3 version.
	 * Prior to ID3v2.4, the provided text must match a id3v1 genre description.
	 * With ID3v2.4, the genre is written as free text.
	 *
	 * @param text genre string
	 */
	public void setGenreDescription(String text);

	int getDataLength();

	int getLength();

	boolean getObseleteFormat();

	Map<String, ID3v2FrameSet> getFrameSets();

	void clearFrameSet(String id);
}
