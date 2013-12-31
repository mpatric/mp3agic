package com.mpatric.mp3agic;

import java.util.ArrayList;

public interface ID3v24 extends ID3v2 {
	String[] getTitles();
	void setTitles(String[] titles);

	String[] getArtists();
	void setArtists(String[] artists);

	String[] getAlbums();
	void setAlbums(String[] albums);

	String[] getTracks();
	void setTracks(String[] tracks);

	String[] getYears();
	void setYears(String[] years);

	String[] getGenreDescriptions();
	void setGenreDescriptions(String[] genreDescriptions);

	String[] getComments();
	void setComments(String[] comments);

	String[] getComposers();
	void setComposers(String[] composers);

	String[] getPublishers();
	void setPublishers(String[] publishers);

	String[] getOriginalArtists();
	void setOriginalArtists(String[] originalArtists);

	String[] getAlbumArtists();
	void setAlbumArtists(String[] albumArtists);

	String[] getCopyrights();
	void setCopyrights(String[] copyrights);

	String[] getUrls();
	void setUrls(String[] urls);

	String[] getPartOfSets();
	void setPartOfSets(String[] partOfSets);

	String[] getGroupings();
	void setGroupings(String[] groupings);

	String[] getEncoders();
	void setEncoders(String[] encoders);
}
