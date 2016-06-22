package com.mpatric.mp3agic;

import java.util.ArrayList;

public interface ID3v2 extends ID3v1, ID3v2Source {
	
	void setPadding(boolean padding);
	
	void setFooter(boolean footer);
	
	void setUnsynchronisation(boolean unsynchronisation);
	
	void setBPM(int bpm);
	
	void setGrouping(String grouping);
	
	void setKey(String key);
	
	void setDate(String date);
	
	void setComposer(String composer);
	
	void setPublisher(String publisher);
	
	void setOriginalArtist(String originalArtist);
	
	void setAlbumArtist(String albumArtist);
	
	void setCopyright(String copyright);
	
	void setArtistUrl(String url);

	void setCommercialUrl(String url);

	void setCopyrightUrl(String url);

	void setAudiofileUrl(String url);

	void setAudioSourceUrl(String url);

	void setRadiostationUrl(String url);

	void setPaymentUrl(String url);

	void setPublisherUrl(String url);

	void setUrl(String url);

	void setPartOfSet(String partOfSet);

	void setCompilation(boolean compilation);
	
	void setChapters(ArrayList<ID3v2ChapterFrameData> chapters);
	
	void setChapterTOC(ArrayList<ID3v2ChapterTOCFrameData> ctoc);
	
	void setEncoder(String encoder);
	
	void setAlbumImage(byte[] albumImage, String mimeType);
	void clearAlbumImage();
	void setWmpRating(int rating);
	
	void setItunesComment(String itunesComment);
	
	/**
	 * Set genre from text.
	 * This method behaves different depending on the ID3 version.
	 * Prior to ID3v2.4, the provided text must match a id3v1 genre description.
	 * With ID3v2.4, the genre is written as free text.
	 * @param text genre string
	 */
	public void setGenreDescription(String text);
	
	void clearFrameSet(String id);
}
