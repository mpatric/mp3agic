package com.mpatric.mp3agic;

public interface ID3v1 {

	String getVersion();

	String getTrack();

	void setTrack(String track);

	String getArtist();

	void setArtist(String artist);

	String getTitle();

	void setTitle(String title);

	String getAlbum();

	void setAlbum(String album);

	String getYear();

	void setYear(String year);

	int getGenre();

	void setGenre(int genre);

	String getGenreDescription();

	String getComment();

	void setComment(String comment);

	byte[] toBytes() throws NotSupportedException;
}
