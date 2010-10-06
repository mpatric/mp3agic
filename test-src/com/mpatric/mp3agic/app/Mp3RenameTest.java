package com.mpatric.mp3agic.app;

import java.io.IOException;

import com.mpatric.mp3agic.ID3Wrapper;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v23Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.mpatric.mp3agic.app.Mp3Rename;

import junit.framework.TestCase;

public class Mp3RenameTest extends TestCase {
	
	private static final String TRACK = "4";
	private static final String ARTIST = "ARTIST";
	private static final String TITLE = "TITLE";
	private static final String ALBUM = "SOME ALBUM";
	private static final String YEAR = "2002";
	private static final int GENRE = 0x0d;
	private static final String GENRE_DESCRIPTION = "Pop";

	public void testShouldCreateValidNewFilenameWithAllParameters() throws Exception {
		Mp3RenameForTesting mp3Rename = new Mp3RenameForTesting(TRACK, ARTIST, TITLE, ALBUM, YEAR, GENRE);
		String newFilename = mp3Rename.composeNewFilename("WHATEVER", "@N - @A - @T - @L - @Y - @G");
		assertEquals("0" + TRACK + " - " + ARTIST + " - " + TITLE + " - " + ALBUM + " - " + YEAR + " - " + GENRE_DESCRIPTION, newFilename);
	}
	
	public void testShouldLeaveOutNullId3Fields() throws Exception {
		Mp3RenameForTesting mp3Rename = new Mp3RenameForTesting(TRACK, null, TITLE, ALBUM, null, GENRE);
		String newFilename = mp3Rename.composeNewFilename("WHATEVER", "@N - @A - @T - @L - @Y - @G");
		assertEquals("0" + TRACK + " - - " + TITLE + " - " + ALBUM + " - - " + GENRE_DESCRIPTION, newFilename);
	}
	
	public void testShouldNotPadTrackContainingMoreThanOneCharacter() throws Exception {
		Mp3RenameForTesting mp3Rename = new Mp3RenameForTesting("10", ARTIST, TITLE, ALBUM, YEAR, GENRE);
		String newFilename = mp3Rename.composeNewFilename("WHATEVER", "@N");
		assertEquals("10", newFilename);
		mp3Rename = new Mp3RenameForTesting("108", ARTIST, TITLE, ALBUM, YEAR, GENRE);
		newFilename = mp3Rename.composeNewFilename("WHATEVER", "@N");
		assertEquals("108", newFilename);
		mp3Rename = new Mp3RenameForTesting("5-21", ARTIST, TITLE, ALBUM, YEAR, GENRE);
		newFilename = mp3Rename.composeNewFilename("WHATEVER", "@N");
		assertEquals("5-21", newFilename);
	}
	
	public void testShouldRemoveOffendingCharactersFromFilename() throws Exception {
		Mp3RenameForTesting mp3Rename = new Mp3RenameForTesting(TRACK, "AC/DC", "WHAT'S UP?", "***ALBUM***", YEAR, GENRE);
		String newFilename = mp3Rename.composeNewFilename("WHATEVER", "@N - @A - @T - @L - @Y - @G");
		assertEquals("0" + TRACK + " - ACDC - WHAT'S UP - ALBUM - " + YEAR + " - " + GENRE_DESCRIPTION, newFilename);
	}
	
	public void testShouldRemoveDoubleSpacesFromFilename() throws Exception {
		Mp3RenameForTesting mp3Rename = new Mp3RenameForTesting(TRACK, ARTIST, "Girl / Boy", "One  Two Three", YEAR, GENRE);
		String newFilename = mp3Rename.composeNewFilename("WHATEVER", "@N - @A - @T - @L - @Y - @G");
		assertEquals("0" + TRACK + " - " + ARTIST + " - Girl Boy - One Two Three - " + YEAR + " - " + GENRE_DESCRIPTION, newFilename);
	}
	
	class Mp3RenameForTesting extends Mp3Rename {

		ID3Wrapper id3Wrapper = new ID3Wrapper(new ID3v1Tag(), new ID3v23Tag());
		
		public Mp3RenameForTesting(String filename, String rename) {
			super(filename, rename);
		}
		
		public Mp3RenameForTesting(String track, String artist, String title, String album, String year, int genre) {
			id3Wrapper.setTrack(track);
			id3Wrapper.setArtist(artist);
			id3Wrapper.setTitle(title);
			id3Wrapper.setAlbum(album);
			id3Wrapper.setYear(year);
			id3Wrapper.setGenre(genre);
		}

		protected ID3Wrapper createId3Wrapper(String filename) throws IOException, UnsupportedTagException, InvalidDataException {
			return id3Wrapper;
		}
	}
}
