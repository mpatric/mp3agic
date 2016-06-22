package com.mpatric.mp3agic;

public interface ID3v1 extends ID3v1Source {
	
	void setTrack(String track);
	
	void setArtist(String artist);
	
	void setTitle(String title);
	
	void setAlbum(String album);
	
	void setYear(String year);
	
	void setGenre(int genre);

        void setComment(String comment);
	
	byte[] toBytes() throws NotSupportedException;
}
